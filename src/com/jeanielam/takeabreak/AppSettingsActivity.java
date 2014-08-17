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
import android.widget.TextView;

import com.jeanielam.takeabreak.R;

public class AppSettingsActivity extends PreferenceActivity implements
		OnSharedPreferenceChangeListener {

	public static SharedPreferences sp;
	MainActivity ma;

	static boolean hasChanged;

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

		String actionBarColourHex = sp.getString("action_bar_colour", "#33B5E5");
		// Action bar
		final ActionBar actionBar = getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor(actionBarColourHex)));

		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowTitleEnabled(true);

		String bgColourHex = sp.getString("bg_colour", "#FFFFFF");
		getWindow().getDecorView().setBackgroundColor(
				Color.parseColor(bgColourHex));
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		hasChanged = true;
		if (key.equals("action_bar_colour") | (key.equals("bg_colour"))) {
			
			restartActivity();

		}
	}

	private void restartActivity() {

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
	public void onPause() {
		super.onPause();
		sp.unregisterOnSharedPreferenceChangeListener(this);
	}

}
