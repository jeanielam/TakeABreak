package com.example.takeabreak;

import java.util.Random;
import android.app.Activity;
import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.google.analytics.tracking.android.EasyTracker;
import com.jeanielam.takeabreak.R;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			startActivity(new Intent(this, InfoActivity.class));
			return true;
		}
		if (id == R.id.toggleTheme) {

			Toast.makeText(getApplicationContext(), "Coming Soon!",
					Toast.LENGTH_LONG).show();
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */

	// variables
	static Chronometer chron;
	private static String[] myString;
	private static final Random rgenerator = new Random();
	static String chronDisplay;
	static String chronDisplayFormatted;
	static String currentTime;
	static Notification myNotification;
	static NotificationManager notifManager;
	static boolean resume = false;
	static long timeStopped = 0;
	static String chronoText;

	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			// initialization
			chronDisplay = "";
			chronDisplayFormatted = "";
			currentTime = "";
			chronoText = "";

			// inflate XML layout
			final View rootView = inflater.inflate(R.layout.fragment_main,
					container, false);

			// setting random quote based on ones in array
			myString = getResources().getStringArray(R.array.arrayQuotes);

			String q = myString[rgenerator.nextInt(myString.length)];

			TextView tv = (TextView) rootView.findViewById(R.id.quote);

			tv.setText(q);

			// toggle button + listener
			final ToggleButton newButton = (ToggleButton) rootView
					.findViewById(R.id.toggleButton1);
			final Chronometer chron = ((Chronometer) rootView
					.findViewById(R.id.chronometer));

			newButton.setOnClickListener(new OnClickListener() {

				private long lastPause;

				@Override
				public void onClick(View v) {
					if (newButton.isChecked()) {
						if (!resume) {
							// Situation where we are starting from 00:00
							
							chron.setBase(SystemClock.elapsedRealtime());
							chron.start();
						} else {
							// Situation where we are resuming from stopped
							// state

							chron.setBase(chron.getBase()
									+ SystemClock.elapsedRealtime() - lastPause);
							chron.start();
						}
					}
					if (!newButton.isChecked()) {
						// Situation where we are pausing the timer momentarily

						lastPause = SystemClock.elapsedRealtime();
						chron.stop();
						resume = true;

					}
				}

			});
			// Button restart

			Button restart = (Button) rootView.findViewById(R.id.button1);
			restart.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					chron.stop();
					newButton.setChecked(false);
					resume = false;
					chron.setText("00:00");

				}

			});

			// TextView "Remind me in"

			rootView.findViewById(R.id.textView1);

			// TextView "minutes."

			rootView.findViewById(R.id.textView2);

			// notification set-up

			Context context = getActivity().getApplicationContext();

			myNotification = new Notification.Builder(context)
					.setContentTitle("Take a break!")
					.setContentText("You deserve it!")
					.setTicker("Take a break!")
					.setDefaults(Notification.DEFAULT_ALL)
					.setSmallIcon(R.drawable.notif_icon).build();

			notifManager = (NotificationManager) getActivity()
					.getSystemService(Context.NOTIFICATION_SERVICE);

			// Spinner to choose length before break

			final Spinner chooseTime = (Spinner) rootView
					.findViewById(R.id.spinner1);

			// per Android Dev tutorial:
			ArrayAdapter<CharSequence> adapter = ArrayAdapter
					.createFromResource(this.getActivity(),
							R.array.timeChoices,
							android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
			chooseTime.setAdapter(adapter);

			chooseTime.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {

					String test = parent.getItemAtPosition(position).toString();

					if (test.length() == 1) {
						chronDisplayFormatted = "0" + test + ":00";
					} else {
						chronDisplayFormatted = test + ":00";
					}

				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {

				}

			});
			// Chronometer Listener to stop tick when reached
			chron.setOnChronometerTickListener(new OnChronometerTickListener() {

				@Override
				public void onChronometerTick(Chronometer arg0) {

					chronoText = chron.getText().toString();
					if (chronoText.equals(chronDisplayFormatted)) {
						chron.stop();
						resume = true;
						newButton.setChecked(false);

						// send notification
						notifManager.notify(2, myNotification);
					}
				}

			});

			// return View
			return rootView;
		}

	}
	
	// Google Analytics
	@Override
	  public void onStart() {
	    super.onStart();
	   
	    EasyTracker.getInstance(this).activityStart(this);  // Add this method.
	  }

	  @Override
	  public void onStop() {
	    super.onStop();
	  
	    EasyTracker.getInstance(this).activityStop(this);  // Add this method.
	  }

}