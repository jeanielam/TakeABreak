package com.jeanielam.takeabreak;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.TextView;

import com.jeanielam.takeabreak.R;

public class InfoActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info);
		// Action bar
		 final ActionBar actionBar = getActionBar(); 
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0099CC")));
		TextView info = (TextView)findViewById(R.id.info);
		TextView author = (TextView)findViewById(R.id.author);
	}
}
