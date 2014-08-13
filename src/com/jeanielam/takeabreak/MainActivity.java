package com.jeanielam.takeabreak;

import java.util.Random;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
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

		// Action bar
		final ActionBar actionBar = getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#0099CC")));

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

		protected CountDownTimer countDownTimer;

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

			final TextView countdown = (TextView) rootView
					.findViewById(R.id.countdownText);

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
					if (test.length() == 1) {
						countdown.setText("0" + test + ":00");
					} else {
						countdown.setText(test + ":00");
					}

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

			// animation for when countdown ends
			final Animation anim = new AlphaAnimation(0.0f, 1.0f);
			anim.setDuration(100);
			anim.setStartOffset(20);
			anim.setRepeatMode(Animation.REVERSE);
			anim.setRepeatCount(10);

			oneTime.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {

					// default start colours
					oneTime.setTextColor(Color.RED);
					reoccuring.setTextColor(Color.BLACK);
					stop.setTextColor(Color.BLACK);

					System.out.println("Length of alarm " + test + " minutes");
					System.out.println("Alarm set at "
							+ System.currentTimeMillis());

					// set AlarmManager
					alarm.set(
							AlarmManager.RTC_WAKEUP,
							System.currentTimeMillis()
									+ (Integer.parseInt(test) * 60 * 1000),
							pintent);
					System.out.println("one time alarm is set");
					Toast.makeText(getActivity().getApplicationContext(),
							"Notification set", Toast.LENGTH_LONG).show();

					// countdown timer
					long length = (long) (Integer.parseInt(test) * 60 * 1000);
					long interval = (long) 1000;

					countDownTimer = new CountDownTimer(length, interval) {
						public void onTick(long millisLeft) {
							countdown.setTextColor(Color.BLACK);
							String minute = String
									.valueOf(millisLeft / 60000 % 60);

							String sec = String.valueOf(millisLeft / 1000 % 60);

							System.out.println("minute" + minute);
							System.out.println("sec" + sec);
							if (millisLeft / 1000 <= 60) {
								countdown.setText("00:"
										+ String.valueOf(millisLeft / 1000));
								if (millisLeft / 1000 % 60 < 10) {
									countdown.setText(minute + ":0" + sec);
								}
							} else {
								countdown.setText(minute + ":" + sec);
							}
						}

						@Override
						public void onFinish() {
							countdown.setText("00:00");
							countdown.setTextColor(Color.RED);
							countdown.startAnimation(anim);
						}
					};
					countDownTimer.start();

					// disable button
					reoccuring.setEnabled(false);

					// haptic feedback
					myVib.vibrate(50);

					// change colour of button text back when time is up
					oneTime.postDelayed(new Runnable() {
						@Override
						public void run() {
							oneTime.setTextColor(Color.BLACK);
							stop.setTextColor(Color.RED);
							reoccuring.setEnabled(true);
						}
					}, Integer.parseInt(test) * 60 * 1000);

				}
			});

			reoccuring.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {

					// button colours
					oneTime.setTextColor(Color.BLACK);
					reoccuring.setTextColor(Color.RED);
					stop.setTextColor(Color.BLACK);

					// set alarm
					Toast.makeText(getActivity().getApplicationContext(),
							"Notification set", Toast.LENGTH_LONG).show();
					alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP,
							System.currentTimeMillis(),
							(Integer.parseInt(test) * 60 * 1000), pintent);

					// haptic feedback
					myVib.vibrate(50);

					// button disable
					oneTime.setEnabled(false);

					// countdown Timer
					long length = (long) (Integer.parseInt(test) * 60 * 1000);
					long interval = (long) 1000;

					countDownTimer = new CountDownTimer(length, interval) {
						public void onTick(long millisLeft) {
							countdown.setTextColor(Color.BLACK);
							String minute = String
									.valueOf(millisLeft / 60000 % 60);

							String sec = String.valueOf(millisLeft / 1000 % 60);

							System.out.println("minute" + minute);
							System.out.println("sec" + sec);
							if (millisLeft / 1000 <= 60) {
								countdown.setText("00:"
										+ String.valueOf(millisLeft / 1000));
								if (millisLeft / 1000 % 60 < 10) {
									countdown.setText(minute + ":0" + sec);
								}
							} else {
								countdown.setText(minute + ":" + sec);
							}
						}

						@Override
						public void onFinish() {
							countdown.setText("00:00");
							countdown.setTextColor(Color.RED);
							countdown.startAnimation(anim);
							countDownTimer.start();
						}
					};
					countDownTimer.start();

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

					// button text colours
					oneTime.setTextColor(Color.BLACK);
					reoccuring.setTextColor(Color.BLACK);
					stop.setTextColor(Color.RED);
					Toast.makeText(getActivity().getApplicationContext(),
							"Notification cancelled", Toast.LENGTH_LONG).show();

					// cancel Countdown Timer
					countDownTimer.cancel();
					countdown.startAnimation(anim);
					countdown.setTextColor(Color.RED);

					// re-enable buttons
					reoccuring.setEnabled(true);
					oneTime.setEnabled(true);

					// cancel AlarmManager
					am.cancel(sendStop);
					sendStop.cancel();

					// haptic feedback
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