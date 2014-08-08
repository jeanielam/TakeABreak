package com.jeanielam.takeabreak;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import com.jeanielam.takeabreak.R;

public class InfoActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info);
		
		TextView info = (TextView)findViewById(R.id.info);
		TextView author = (TextView)findViewById(R.id.author);
	}
}
