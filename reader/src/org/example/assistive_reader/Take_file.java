package org.example.assistive_reader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

/**
 * @author kapil
 *
 */
public class Take_file extends Activity implements OnClickListener{

	/**
	 * @param args
	 */
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.takeinput);
        View acceptButton = findViewById(R.id.input_accept);
        acceptButton.setOnClickListener(this);
        View cancelButton = findViewById(R.id.input_reject);
        cancelButton.setOnClickListener(this);
        EditText text=(EditText)findViewById(R.id.input_text_contents);
        Intent intent=getIntent();
        String str=intent.getStringExtra("edit_value");
        if(str!=null){
        	text.setText(str);
        }
	}
	public void onClick(View v) {		
		switch (v.getId()) {
		case R.id.input_accept:
			Intent intent;
			intent=getIntent();
			EditText text=(EditText)findViewById(R.id.input_text_contents);
		    
			CharSequence str=text.getText();	     	
			intent.putExtra("abc",str);
			//TextView textshow=(TextView)findViewById(R.id.text_show2);
			//textshow.setText(str);
			setResult(RESULT_OK,intent);			
			finish();
	        //startActivity(new Intent(this, MainActivity.class));
			break;
		// More buttons go here (if any) ...
		case R.id.input_reject:
			setResult(RESULT_CANCELED);			
			finish();
			//startActivity(new Intent(this, MainActivity.class));
			break;
		}
		
	}
		
}
