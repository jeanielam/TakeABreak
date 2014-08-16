package com.jeanielam.takeabreak;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.jeanielam.takeabreak.R;

public class AppSettingsActivity extends PreferenceActivity implements
		OnSharedPreferenceChangeListener {

	public static SharedPreferences sp;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp = PreferenceManager.getDefaultSharedPreferences(this);
		sp.registerOnSharedPreferenceChangeListener(this);
		loadPreferences();
		getFragmentManager().beginTransaction()
				.replace(android.R.id.content, new SettingsFragment()).commit();

	}

	private void loadPreferences() {
		// TODO Auto-generated method stub

		String appTheme = sp.getString("theme", "");
		if (appTheme.equals("1")) {
			setTheme(android.R.style.Theme_Holo_Light);
		} else if (appTheme.equals("2")) {
			setTheme(android.R.style.Theme_Holo);
		}

		String actionBarColourHex = sp.getString("action_bar_colour", "");
		// Action bar
		final ActionBar actionBar = getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor(actionBarColourHex)));
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowTitleEnabled(true);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// TODO Auto-generated method stub
		if (key.equals("theme") | key.equals("action_bar_colour")) {
			MainActivity.scheduledRestart = true;
			//MainActivity.reloadTheme();
			
		}
	}

	private void restartActivity() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		finish();
		startActivity(intent);
	}

	@Override
	public void onResume() {
		super.onResume();
		sp.registerOnSharedPreferenceChangeListener(this);
		
	}
	
	@Override
	public void onPause(){
		super.onPause();
		sp.unregisterOnSharedPreferenceChangeListener(this);
	}

}
