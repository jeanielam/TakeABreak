package com.jeanielam.takeabreak;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

import android.R.color;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.HapticFeedbackConstants;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

	private static String[] myString;
	private static final Random rgenerator = new Random();
	static Notification myNotification;
	static NotificationManager notifManager;
	static String test;
	static Vibrator myVib;

	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			// inflate XML layout
			final View rootView = inflater.inflate(R.layout.fragment_main,
					container, false);

			// setting random quote based on ones in array
			myString = getResources().getStringArray(R.array.arrayQuotes);

			String q = myString[rgenerator.nextInt(myString.length)];

			TextView tv = (TextView) rootView.findViewById(R.id.quote);

			tv.setText(q);

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

					test = parent.getItemAtPosition(position).toString();

				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {

				}

			});
			// toggle buttons + listener

			final Button oneTime = (Button) rootView.findViewById(R.id.oneTime);
			final Button reoccuring = (Button) rootView
					.findViewById(R.id.reocurring);
			final Button stop = (Button) rootView.findViewById(R.id.stopButton);
			final AlarmManager alarm = (AlarmManager) getActivity()
					.getSystemService(Context.ALARM_SERVICE);
			Intent intent = new Intent(getActivity(), AlarmReceiver.class);
			final PendingIntent pintent = PendingIntent
					.getBroadcast(getActivity(), 0, intent,
							PendingIntent.FLAG_UPDATE_CURRENT);

			// set initial colour
			stop.setTextColor(Color.RED);

			// set vibration
			myVib = (Vibrator) getActivity().getSystemService(VIBRATOR_SERVICE);

			oneTime.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {

					oneTime.setTextColor(Color.RED);
					reoccuring.setTextColor(Color.BLACK);
					stop.setTextColor(Color.BLACK);

					System.out.println("Length of alarm " + test + " minutes");
					System.out.println("Alarm set at "
							+ System.currentTimeMillis());
					alarm.set(
							AlarmManager.RTC_WAKEUP,
							System.currentTimeMillis()
									+ (Integer.parseInt(test) * 60 * 1000),
							pintent);
					System.out.println("one time alarm is set");
					Toast.makeText(getActivity().getApplicationContext(),
							"Notification set", Toast.LENGTH_LONG).show();

					myVib.vibrate(50);
					oneTime.postDelayed(new Runnable() {
						@Override
						public void run() {
							oneTime.setTextColor(Color.BLACK);
							stop.setTextColor(Color.RED);
						}
					}, Integer.parseInt(test)*60*1000);

				}
			});

			reoccuring.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {

					oneTime.setTextColor(Color.BLACK);
					reoccuring.setTextColor(Color.RED);
					stop.setTextColor(Color.BLACK);

					Toast.makeText(getActivity().getApplicationContext(),
							"Notification set", Toast.LENGTH_LONG).show();
					alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP,
							System.currentTimeMillis(),
							(Integer.parseInt(test) * 60 * 1000), pintent);
					myVib.vibrate(50);
				}
			});
			stop.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {

					Intent intent = new Intent(getActivity(),
							AlarmReceiver.class);
					PendingIntent sendStop = PendingIntent.getBroadcast(
							getActivity(), 0, intent,
							PendingIntent.FLAG_UPDATE_CURRENT);
					AlarmManager am = (AlarmManager) getActivity()
							.getSystemService(ALARM_SERVICE);

					oneTime.setTextColor(Color.BLACK);
					reoccuring.setTextColor(Color.BLACK);
					stop.setTextColor(Color.RED);
					Toast.makeText(getActivity().getApplicationContext(),
							"Notification cancelled", Toast.LENGTH_LONG).show();
					am.cancel(sendStop);
					sendStop.cancel();
					myVib.vibrate(50);
				}
			});

			// TextView "Remind me in"

			rootView.findViewById(R.id.textView1);

			// TextView "minutes."

			rootView.findViewById(R.id.textView2);

			// return View
			return rootView;
		}
	}

	// Google Analytics
	@Override
	public void onStart() {
		super.onStart();

		EasyTracker.getInstance(this).activityStart(this); // Add this method.
	}

	@Override
	public void onStop() {
		super.onStop();

		EasyTracker.getInstance(this).activityStop(this); // Add this method.
	}

}