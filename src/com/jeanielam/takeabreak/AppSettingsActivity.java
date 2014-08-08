package com.jeanielam.takeabreak;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import com.jeanielam.takeabreak.R;

public class AppSettingsActivity extends PreferenceActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.settings);

		

		setContentView(R.layout.activity_main);
		 
	}
}

