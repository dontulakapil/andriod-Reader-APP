package org.example.assistive_reader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class List_view extends ListActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  String [] st=null;
	  BufferedReader buff = null;
	  try {
		buff=new BufferedReader(new FileReader("/sdcard/.assistive_reader/saved_files"));
	  } catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		finish();
	  }
	  String line = null;
	  try {
		line=buff.readLine();
	  }
	  catch (IOException e) {
		// TODO Auto-generated catch block
		;
      }
	  int count=0;
	  while(line!=null){
		  if(line.length()>0){
		  count++;
		  }
		  try {
				line = buff.readLine();
			  }
			  catch (IOException e) {
				// TODO Auto-generated catch block
				line=null;
		      }  
	  }
	  st=new String [count];
	  try {
		buff.close();
	  } catch (IOException e) {
		// TODO Auto-generated catch block
	  }
	  try {
		buff=new BufferedReader(new FileReader("/sdcard/.assistive_reader/saved_files"));
	  } catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		finish();
	  }
	  try {
			line=buff.readLine();
	  }
	  catch (IOException e) {
			// TODO Auto-generated catch block
			;
	  }
	  count=0;
	  while(line!=null){
		if(line.length()>0){
			st[count]=line;
			count++;
		}
	    try {
			line = buff.readLine();
	    }
		catch (IOException e) {
					// TODO Auto-generated catch block
		    line=null;
		}  
	  }
	  try {
			buff.close();
	  } catch (IOException e) {
			// TODO Auto-generated catch block
	  }
	
	  setListAdapter(new ArrayAdapter<String>(this, R.layout.list_view,st ));
	  ListView lv = getListView();
	  lv.setTextFilterEnabled(true);

	  lv.setOnItemClickListener(new OnItemClickListener() {
	    public void onItemClick(AdapterView<?> parent, View view,
	        int position, long id) {
	    	TextView text=(TextView)view;
	    	Intent intent=getIntent();
	    	intent.putExtra("file", text.getText());
	    	CharSequence file=text.getText();
	    	BufferedReader input_file=null;
	    	String line=null;
	    	try {
				input_file=new BufferedReader(new FileReader("/sdcard/.assistive_reader/"+file.toString()));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				intent.putExtra("abc", "");
				setResult(RESULT_CANCELED);	
				finish();
			}
			String new_text="";
			try {
				line=input_file.readLine();
		    }
		    catch (IOException e) {
				// TODO Auto-generated catch block
				;
		    }
		    int count=0;
		    while(line!=null){
		    	new_text+=line;
		    	count++;
		    	try {
		    		line =input_file.readLine();
		    	}
		    	catch (IOException e) {
						// TODO Auto-generated catch block
		    		line=null;
				}  
		  	}
		    try {
				input_file.close();
		  } catch (IOException e) {
				// TODO Auto-generated catch block
		  }
		    intent.putExtra("abc", new_text);
	    	setResult(RESULT_OK,intent);
	    	finish();
	    }
	  });
	}

}
