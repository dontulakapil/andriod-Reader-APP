package org.example.assistive_reader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;

import com.google.tts.TTS;

import android.widget.Toast;
import android.app.Activity; //import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

public class reader extends Activity implements OnClickListener {
	/** Called when the activity is first created. */

	private int imageview_to_use = 0;
	private ImageView[] image;
	private String[] image_text;
	private TextView prev = null;
	private TextView textview;
	private CharSequence text_to_show;
	SpannableString text_to_span;
	CountDownTimer timer;
	private ForegroundColorSpan prev_highlight;
	ScrollView scrollview;
	private TTS mytts;

	private TTS.InitListener ttsInitListener = new TTS.InitListener() {
		public void onInit(int version) {

			try {
				mytts.speak("I want a cool", 0, null);
			} catch (Exception e) {
				e.toString();
			}

			// tv.setText(exception);
			// setContentView(tv);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);
		image = new ImageView[4];
		image_text = new String[4];
		image[0] = (ImageView) findViewById(R.id.imageview1);
		image[1] = (ImageView) findViewById(R.id.imageview2);
		image[2] = (ImageView) findViewById(R.id.imageview3);
		image[3] = (ImageView) findViewById(R.id.imageview4);
		textview = (TextView) findViewById(R.id.text_view1);
		mytts = new TTS(this, null, true);
		ttsInitListener.onInit(0);
		mytts.speak("welcome to assisitive reader", 0, null);
		textview.setMovementMethod(new LinkMovementMethod());
		Button temp = (Button) findViewById(R.id.close_program);
		temp.setOnClickListener(this);
		Button temp2 = (Button) findViewById(R.id.load);
		temp2.setOnClickListener(this);
		Button temp3 = (Button) findViewById(R.id.input);
		temp3.setOnClickListener(this);
		temp3 = (Button) findViewById(R.id.edit);
		temp3.setOnClickListener(this);
		temp3.setVisibility(View.INVISIBLE);
		temp3 = (Button) findViewById(R.id.save);
		temp3.setVisibility(View.INVISIBLE);
		temp3.setOnClickListener(this);
		temp3 = (Button) findViewById(R.id.read);
		temp3.setOnClickListener(this);
		temp3.setVisibility(View.INVISIBLE);
		scrollview = (ScrollView) findViewById(R.id.scrollview);
		/*
		 * Display display = getWindowManager().getDefaultDisplay(); int
		 * orientation=display.getOrientation(); int width=display.getWidth();
		 * int height=display.getHeight();
		 * //Log.i("screen height,width",""+height+" "+width );
		 * if(width>height){ LayoutParams scroll=new
		 * LayoutParams(width/2,height-60); scrollview.setLayoutParams(scroll);
		 * (
		 * (TableLayout)findViewById(R.id.table_images)).setLayoutParams(scroll)
		 * ; } else{ LayoutParams scroll=new LayoutParams(width,(height-60)/2);
		 * scrollview.setLayoutParams(scroll);
		 * ((TableLayout)findViewById(R.id.table_images
		 * )).setLayoutParams(scroll); }
		 */

		// Toast t=Toast.makeText(this,""+height+" "+width+" "+orientation
		// ,Toast.LENGTH_LONG);
		// t.show();
		scrollview
				.setBackgroundResource(R.drawable.panel_picture_frame_bg_pressed_blue);

		try {
			text_to_show = savedInstanceState.getCharSequence("text_to_show");
			if (text_to_show != null) {
				set_textview(text_to_show);
			}
		} catch (NullPointerException e) {
			text_to_show = "";
		}
		((TextView) findViewById(R.id.image_text1)).setTextColor(Color.RED);
		((TextView) findViewById(R.id.image_text2)).setTextColor(Color.RED);
		((TextView) findViewById(R.id.image_text3)).setTextColor(Color.RED);
		((TextView) findViewById(R.id.image_text4)).setTextColor(Color.RED);

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.input:
			startActivityForResult(new Intent(this, Take_input.class), 1);
			return;
		case R.id.close_program:
			// Intent intent2=new Intent(Intent.ACTION_PICK);
			// startActivityForResult(intent2,2);
			System.exit(0);
			return;
			// return true;
		case R.id.edit:
			Intent intent = new Intent(this, Take_input.class);
			try {
				intent.putExtra("edit_value", text_to_show.toString());
			} catch (NullPointerException e) {

			}
			startActivityForResult(intent, 1);
			return;
		case R.id.load:
			startActivityForResult(new Intent(this, List_view.class), 2);
			return;
		case R.id.save:
			startActivityForResult(new Intent(this, Take_file.class), 3);
			return;
		case R.id.read:

			StringTokenizer st = new StringTokenizer(text_to_show.toString());
			int count = 0;
			while (st.hasMoreTokens()) {
				count++;
				st.nextToken();
			}
			start_highlight(count);
			return;
			// return true;
		}
		TextView text = (TextView) v;
		// v.setHighlightColor(0xFF001225);
		if (prev != null) {
			prev.setBackgroundColor(0xffffff);
			/*
			 * int len=prev.toString().length(); if(len>7)
			 * prev.setTextSize(TypedValue.COMPLEX_UNIT_DIP,10); else if (len<=7
			 * && len >6) prev.setTextSize(TypedValue.COMPLEX_UNIT_DIP,12); else
			 * prev.setTextSize(TypedValue.COMPLEX_UNIT_DIP,13);
			 */
		}
		prev = (TextView) v;
		text.setBackgroundColor(0xff6cbbf7);
		// text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
		// int text_id =text.getId();
		// Log.i("text_view_highlighted_id",Integer.toString(text_id));
	}

	public void set_image(String s, int start, int end) {
		if (timer != null) {
			timer.cancel();
		}
		highlight_string(start, end);
		String ini;
		String name;
		String ext;
		String path;
		int i;
		int arrlen;
		String[] punc = { "!", "(", ")", "-", "_", "{", "}", ":", ";", "\"",
				"'", "?", ",", "." };
		String[] rep = { "s", "es", "ed", "d", "er", "ing", "ly" };
		String temp;
		String tname = "";
		int rlength;
		ini = new String("/sdcard/.assistive_reader/");
		// name = text.getText().toString();
		name = s;
		// Log.i("picture",name);
		name = name.toLowerCase();
		arrlen = punc.length;
		for (i = 0; i < arrlen; i++) {
			name = name.replace(punc[i], "");
		}

		ext = new String(".png");
		path = ini + name + ext;
		Bitmap bMap = BitmapFactory.decodeFile(path);
		int flag = 0;
		Toast t = Toast.makeText(this, name, Toast.LENGTH_SHORT);
		t.show();
		for (i = 0; i < 4; i++) {
			if (image_text[i] != null && image_text[i].equalsIgnoreCase(name)) {
				flag = 1;
			}
		}
		if (flag == 0) {
			image_text[imageview_to_use] = name;
			try {
				for (i = 0; bMap == null && i < rep.length; i++)
				// if(bMap == null)
				{
					rlength = rep[i].length();
					temp = name.substring(name.length() - rlength);
					if (temp.equals(rep[i])) {
						tname = name.substring(0, name.length() - rlength);
					}
					path = "";
					// tname=name.substring(0, i);
					path = ini + tname + ext;
					bMap = BitmapFactory.decodeFile(path);
				}
			}// ending try
			catch (Exception e) {
			}
			if (bMap != null) {
				image[imageview_to_use].setImageBitmap(bMap);
			} else {
				image[imageview_to_use]
						.setImageBitmap(BitmapFactory
								.decodeFile("/sdcard/.assistive_reader/no_image_to_display.png"));
			}
			prev_highlight = new ForegroundColorSpan(Color.RED);
			// text_to_span.setSpan(prev_highlight, start, end,
			// Spannable.SPAN_INTERMEDIATE);

			switch (imageview_to_use) {
			case 0:
				((TextView) findViewById(R.id.image_text1))
						.setText(image_text[imageview_to_use]);
				break;
			case 1:
				((TextView) findViewById(R.id.image_text2))
						.setText(image_text[imageview_to_use]);
				break;
			case 2:
				((TextView) findViewById(R.id.image_text3))
						.setText(image_text[imageview_to_use]);
				break;
			case 3:
				((TextView) findViewById(R.id.image_text4))
						.setText(image_text[imageview_to_use]);
				break;

			}

			imageview_to_use = (imageview_to_use + 1) % 4;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// textshow.setText("abcdef");
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		// Log.i("mesg","menu entered......");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.input_t:
			startActivityForResult(new Intent(this, Take_input.class), 1);
			return true;

		case R.id.close:
			System.exit(0);
			return true;

		}
		return false;
	}

	@Override
	protected void onActivityResult(int requestcode, int resultcode,
			Intent intent) {
		// Log.i("mesg2","on activity result entered......");
		if (resultcode == RESULT_OK && (requestcode == 1 || requestcode == 2)) {
			// return;
			// Log.i("mesg3","on activity result entered...... modifying text_show");

			CharSequence str = intent.getCharSequenceExtra("abc");
			set_textview(str);
		}
		if (resultcode == RESULT_OK && requestcode == 3) {
			CharSequence input = intent.getCharSequenceExtra("abc");
			String str = "";
			StringTokenizer trim = new StringTokenizer(input.toString());
			int cnt = 0;
			while (trim.hasMoreTokens()) {
				if (cnt >= 1) {
					str += " ";
				}
				str += trim.nextToken();
				cnt++;
			}
			if (str != null) {
				String path = "/sdcard/.assistive_reader/" + str.toString();
				File out_file = new File(path);
				try {
					out_file.createNewFile();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					return;
				}
				FileOutputStream output = null;
				try {
					output = new FileOutputStream(out_file);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					return;
				}
				try {
					output.write(text_to_show.toString().getBytes());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					return;
				}
				try {
					output.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					return;
				}
				FileWriter file_list = null;
				try {
					file_list = new FileWriter(
							"/sdcard/.assistive_reader/saved_files", true);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					return;
				}
				BufferedWriter file_list_writer = null;
				file_list_writer = new BufferedWriter(file_list);
				try {
					file_list_writer.write("\n" + str.toString());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					return;
				}
				try {
					file_list_writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					return;
				}
				try {
					file_list.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					return;
				}
			}
		}
	}

	protected void set_textview(CharSequence str) {
		int i = 0;
		try {
			if (str != null && str.length() > 0) {
				text_to_span = new SpannableString(str);
				// Log.i("str","str read from intent");
				// Log.i("str2",(String)str.toString());
				// textadapter.set_text_to_show(str.toString());

				StringTokenizer st = new StringTokenizer(str.toString());
				// text_to_show=new String[st.countTokens()];
				i = 0;
				int len = 0;
				String temp;

				while (st.hasMoreTokens()) {
					temp = st.nextToken();
					len = (str.toString()).indexOf(temp, len);
					text_to_span.setSpan(new clickable(temp, this, len, len
							+ temp.length()), len, len + temp.length(),
							Spannable.SPAN_INTERMEDIATE);
					len = len + temp.length();
					// text_to_show[i]=st.nextToken();
					i++;
				}
				text_to_span.setSpan(new ForegroundColorSpan(Color.BLACK), 0,
						str.length(), Spannable.SPAN_INTERMEDIATE);
				textview.setText(text_to_span);
				// gridview.setAdapter(text_adapter=new
				// TextAdapter(this,text_to_show,this));

				text_to_show = str;
				Button text_edit = (Button) findViewById(R.id.edit);
				text_edit.setVisibility(View.VISIBLE);
				text_edit = (Button) findViewById(R.id.save);
				text_edit.setVisibility(View.VISIBLE);
				text_edit = (Button) findViewById(R.id.read);
				text_edit.setVisibility(View.VISIBLE);
				for (i = 0; i < 4; i++) {
					image[i].setImageBitmap(null);
					image_text[i] = null;
				}
				((TextView) findViewById(R.id.image_text1)).setText("");
				((TextView) findViewById(R.id.image_text2)).setText("");
				((TextView) findViewById(R.id.image_text3)).setText("");
				((TextView) findViewById(R.id.image_text4)).setText("");
			}
		} catch (NullPointerException e) {
			;
		}
		// start_highlight(i);
	}

	public void start_highlight(int no_of_items) {
		if (no_of_items > 0) {
			timer = new CountDownTimer((no_of_items + 1) * 1000, 1000) {
				public int iter = 0;

				public void onTick(long time) {
					iter++;
					int i = 0;
					StringTokenizer st = new StringTokenizer(text_to_show
							.toString());
					int len = 0;
					String str;
					while (st.hasMoreTokens()) {
						str = st.nextToken();
						len = (text_to_show.toString()).indexOf(str, len);
						i++;
						if (i == iter) {
							highlight_string(len, len + str.length());
							break;
						}
						len = len + str.length();
					}
					return;
				}

				public void onFinish() {
					stop_highlight();
					return;
				}
			}.start();
		}

	}

	public void highlight_string(int span_st, int span_end) {
		String st = text_to_show.subSequence(span_st, span_end).toString();
		// Log.i("tts_speak",st);

		mytts.speak(st, 0, null);
		int line = textview.getLayout().getLineForOffset(span_st);

		// Log.i("line no",""+line);
		if (line > 4) {
			int offset = textview.getLayout().getLineTop(line - 4);
			scrollview.smoothScrollTo(0, offset);
		} else
			scrollview.smoothScrollTo(0, 0);
		if (prev_highlight != null)
			text_to_span.removeSpan(prev_highlight);
		ForegroundColorSpan[] highlight = text_to_span.getSpans(0, text_to_span
				.length(), ForegroundColorSpan.class);
		int i;
		for (i = 0; i < highlight.length; i++) {
			text_to_span.removeSpan(highlight[i]);
		}
		prev_highlight = new ForegroundColorSpan(Color.RED);
		text_to_span.setSpan(prev_highlight, span_st, span_end, 0);
		textview.setText(text_to_span);
		// Log.i("highlight",""+span_st+" "+span_end);
	}

	public void stop_highlight() {
		// text_to_span.setSpan(new
		// ForegroundColorSpan(Color.BLACK),prev_st,prev_end,Spannable.SPAN_INTERMEDIATE);
		timer.cancel();
		if (prev_highlight != null)
			text_to_span.removeSpan(prev_highlight);
		textview.setText(text_to_span);
	}

	@Override
	public void onSaveInstanceState(Bundle bundle) {

		bundle.putCharSequence("text_to_show", text_to_show);

	}

	@Override
	public void onRestoreInstanceState(Bundle bundle) {
		text_to_show = bundle.getCharSequence("text_to_show");
	}

}

class clickable extends ClickableSpan {
	String word;
	View.OnClickListener parent;
	int start, end;

	clickable(String str, View.OnClickListener p, int s, int e) {
		word = str;
		parent = p;
		start = s;
		end = e;
	}

	@Override
	public void onClick(View v) {
		((reader) parent).set_image(word, start, end);
	}

	@Override
	public void updateDrawState(TextPaint ds) {
		ds.setColor(Color.BLACK);
		ds.setUnderlineText(false);
	}
}
