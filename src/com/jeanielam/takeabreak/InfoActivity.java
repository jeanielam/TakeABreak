package com.jeanielam.takeabreak;

import android.app.ActionBar;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

import com.jeanielam.takeabreak.R;

public class InfoActivity extends Activity {
	SharedPreferences sp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info);
		sp = PreferenceManager.getDefaultSharedPreferences(this);

		loadPreferences();
		TextView info = (TextView) findViewById(R.id.info);
		TextView author = (TextView) findViewById(R.id.author);
	}

	private void loadPreferences() {

		String actionBarColourHex = sp.getString("action_bar_colour", "");
		// Action bar
		final ActionBar actionBar = getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor(actionBarColourHex)));

		String bgColourHex = sp.getString("bg_colour", "");
		getWindow().getDecorView().setBackgroundColor(Color.parseColor(bgColourHex));
	}

}
