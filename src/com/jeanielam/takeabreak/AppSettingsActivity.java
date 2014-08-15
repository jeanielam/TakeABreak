package com.jeanielam.takeabreak;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

import com.jeanielam.takeabreak.R;

public class AppSettingsActivity extends PreferenceActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 final ActionBar actionBar = getActionBar(); 
		 actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0099CC")));
		getFragmentManager().beginTransaction()
				.replace(android.R.id.content, new SettingsFragment()).commit();

	}
}
