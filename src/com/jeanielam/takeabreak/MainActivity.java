package com.jeanielam.takeabreak;

import java.util.Random;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
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
	static SharedPreferences sp;
	static boolean haptic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		PreferenceManager.setDefaultValues(this, R.layout.settings, false);

		setContentView(R.layout.activity_main);
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		sp = PreferenceManager.getDefaultSharedPreferences(this);

		loadPreferences();

	}

	public void loadPreferences() {

		String actionBarColourHex = sp.getString("action_bar_colour",
				"#ff33B5E5");
		System.out.println(actionBarColourHex);
		// Action bar
		final ActionBar actionBar = getActionBar();

		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor(actionBarColourHex)));
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowTitleEnabled(true);

		String bgColourHex = sp.getString("bg_colour", "#ffFFFFFF");
		System.out.println(bgColourHex);
		getWindow().getDecorView().setBackgroundColor(
				Color.parseColor(bgColourHex));

		haptic = sp.getBoolean("haptic_pref", false);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		loadPreferences();
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
		if (id == R.id.settings) {
			startActivity(new Intent(this, AppSettingsActivity.class));
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
	static String breakLength;
	public boolean isBreak;
	static boolean scheduledRestart;

	public static class PlaceholderFragment extends Fragment {

		protected CountDownTimer countDownTimer;
		protected CountDownTimer countDownTimer1;
		protected long workBreak;
		NotificationManager notifManager;

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

			final TextView minuteText = (TextView) rootView
					.findViewById(R.id.textView2);
			final TextView minuteTextBreak = (TextView) rootView
					.findViewById(R.id.TextView01);

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
					if (test.equals("1")) {
						minuteText.setText("minute");
					} else {
						minuteText.setText("minutes");
					}
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

			// Spinner to choose length of break
			final Spinner breakTime = (Spinner) rootView
					.findViewById(R.id.Spinner01);
			final TextView breakCountdown = (TextView) rootView
					.findViewById(R.id.breakCountdown);
			breakCountdown.setTextColor(Color.parseColor("#0099CC"));
			// per Android Dev tutorial:
			ArrayAdapter<CharSequence> adapter1 = ArrayAdapter
					.createFromResource(this.getActivity(),
							R.array.breakTimeChoices,
							android.R.layout.simple_spinner_item);
			adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
			breakTime.setAdapter(adapter1);

			breakTime.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {

					breakLength = parent.getItemAtPosition(position).toString();
					if (breakLength.equals("1")) {
						minuteTextBreak.setText("minute");
					} else {
						minuteTextBreak.setText("minutes");
					}
					if (breakLength.length() == 1) {
						breakCountdown.setText("+ 0" + breakLength + ":00");
					} else {
						breakCountdown.setText("+ " + breakLength + ":00");
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
					.getBroadcast(getActivity(), 1, intent,
							PendingIntent.FLAG_UPDATE_CURRENT);

			final PendingIntent pintent2 = PendingIntent
					.getBroadcast(getActivity(), 2, intent,
							PendingIntent.FLAG_UPDATE_CURRENT);

			final PendingIntent pintent3 = PendingIntent
					.getBroadcast(getActivity(), 3, intent,
							PendingIntent.FLAG_UPDATE_CURRENT);

			// set initial colour
			stop.setTextColor(Color.RED);

			// disable stop button on create
			stop.setEnabled(false);

			// animation for when countdown ends
			final Animation anim = new AlphaAnimation(0.0f, 1.0f);
			anim.setDuration(100);
			anim.setStartOffset(20);
			anim.setRepeatMode(Animation.REVERSE);
			anim.setRepeatCount(10);

			oneTime.setOnClickListener(new OnClickListener() {
				long length;
				long interval;
				long breakLeng;

				public void onClick(View v) {
					setOneTimeColours();

					// values
					length = (long) (Integer.parseInt(test) * 60 * 1000);
					interval = (long) 1000;
					breakLeng = (long) (Integer.parseInt(breakLength) * 60 * 1000);
					workBreak = length + breakLeng;

					SharedPreferences pref = getActivity()
							.getSharedPreferences("pref", 0);
					SharedPreferences.Editor edit = pref.edit();

					edit.putLong("length", length);
					edit.putLong("breakLeng", breakLeng);
					edit.commit();

					alarmOneTimeWork();
					alarmOneTimeBreak();

					System.out.println("one time alarm is set");
					Toast.makeText(getActivity().getApplicationContext(),
							"Notification set", Toast.LENGTH_LONG).show();

					setOneTimeCountdown();

					// disable button
					reoccuring.setEnabled(false);
					stop.setEnabled(true);

					// haptic feedback
					vibrate();

					// change colour of button text back when time is up
					oneTime.postDelayed(new Runnable() {
						@Override
						public void run() {
							oneTime.setTextColor(Color.BLACK);
							stop.setTextColor(Color.RED);
							reoccuring.setEnabled(true);
							notifManager.cancel(2);

							AlarmManager am = (AlarmManager) getActivity()
									.getSystemService(ALARM_SERVICE);
							am.cancel(pintent2);
							am.cancel(pintent);
							
							pintent.cancel();
							pintent2.cancel();
						}
					}, workBreak);

				}

				private void setOneTimeCountdown() {

					// main countdown
					countDownTimer = new CountDownTimer(length, interval) {
						public void onTick(long millisLeft) {
							countdown.setTextColor(Color.BLACK);
							String minute = String
									.valueOf(millisLeft / 60000 % 60);

							String sec = String.valueOf(millisLeft / 1000 % 60);

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
							countdown.setTextColor(Color.parseColor("#CC0000"));
							countdown.startAnimation(anim);
							countDownTimer1.start();
						}
					};
					countDownTimer.start();

					// break countdown
					countDownTimer1 = new CountDownTimer(breakLeng, interval) {
						public void onTick(long millis) {

							String minute = String.valueOf(millis / 60000 % 60);

							String sec = String.valueOf(millis / 1000 % 60);

							if (millis / 1000 <= 60) {
								breakCountdown.setText("+ 00:"
										+ String.valueOf(millis / 1000));
								if (millis / 1000 % 60 < 10) {
									breakCountdown.setText("+ " + minute + ":0"
											+ sec);
								} else {
									breakCountdown.setText("+ " + minute + ":"
											+ sec);
								}
							}
						}

						@Override
						public void onFinish() {
							breakCountdown.setText("+ 00:00");
							breakCountdown.setTextColor(Color
									.parseColor("#FF4444"));
							breakCountdown.startAnimation(anim);
						}

					};
				}

				private void setOneTimeColours() {

					// default start colours
					oneTime.setTextColor(Color.RED);
					reoccuring.setTextColor(Color.BLACK);
					stop.setTextColor(Color.BLACK);
					breakCountdown.setTextColor(Color.parseColor("#0099CC"));
					countdown.setTextColor(Color.BLACK);
					System.out.println("Length of alarm " + test + " minutes");
					System.out.println("Alarm set at "
							+ System.currentTimeMillis());
				}

				SharedPreferences pref = getActivity().getSharedPreferences(
						"pref", 0);
				SharedPreferences.Editor edit = pref.edit();

				private void alarmOneTimeWork() {

					long trigger = System.currentTimeMillis()
							+ (Integer.parseInt(test) * 60 * 1000);
					alarm.set(AlarmManager.RTC_WAKEUP, trigger, pintent);

					edit.putString("takeBreak", "true");
					edit.commit();
					System.out.println("takeBreak = true");
				}

				private void alarmOneTimeBreak() {

					long trigger = System.currentTimeMillis()
							+ (Integer.parseInt(test) * 60 * 1000);
					final long breakLeng = (long) (Integer
							.parseInt(breakLength) * 60 * 1000);

					alarm.set(AlarmManager.RTC_WAKEUP, trigger + breakLeng,
							pintent2);

				}

			});

			reoccuring.setOnClickListener(new OnClickListener() {
				long workLength;
				long breakLeng;

				public void onClick(View v) {

					// button colours
					oneTime.setTextColor(Color.BLACK);
					reoccuring.setTextColor(Color.RED);
					stop.setTextColor(Color.BLACK);
					breakCountdown.setTextColor(Color.parseColor("#0099CC"));
					// set alarm
					Toast.makeText(getActivity().getApplicationContext(),
							"Notification set", Toast.LENGTH_LONG).show();

					workLength = (long) (Integer.parseInt(test) * 60 * 1000);

					breakLeng = (long) (Integer.parseInt(breakLength) * 60 * 1000);
					workBreak = workLength + breakLeng;

					recurringWorkAlarm();
					recurringBreakAlarm();

					// haptic feedback
					vibrate();

					// button disable
					oneTime.setEnabled(false);
					stop.setEnabled(true);

					recurringCountdown();

				}

				private void recurringWorkAlarm() {
					// work recurring
					SharedPreferences pref = getActivity()
							.getSharedPreferences("pref", 0);
					SharedPreferences.Editor edit = pref.edit();
					edit.putString("takeBreak", "true");
					edit.commit();
					System.out.println("takeBreak = true");
					alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP,
							System.currentTimeMillis() + workLength, workLength
									+ breakLeng, pintent3);
					System.out.println("work recurring"
							+ ((2 * workLength) + breakLeng));

				}

				private void recurringBreakAlarm() {

					// break

					alarm.setInexactRepeating(
							AlarmManager.RTC_WAKEUP,
							System.currentTimeMillis() + workLength + breakLeng,
							workLength + breakLeng, pintent2);
					System.out.println("break times" + workLength + breakLeng);

				}

				private void recurringCountdown() {
					// countdown Timer
					long length = (long) (Integer.parseInt(test) * 60 * 1000);
					long interval = (long) 1000;

					countDownTimer = new CountDownTimer(length, interval) {
						public void onTick(long millisLeft) {
							countdown.setTextColor(Color.BLACK);
							String minute = String
									.valueOf(millisLeft / 60000 % 60);

							String sec = String.valueOf(millisLeft / 1000 % 60);

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
							countdown.setTextColor(Color.parseColor("#CC0000"));
							countdown.startAnimation(anim);
							countDownTimer1.start();
						}
					}.start();

					// break countdown

					countDownTimer1 = new CountDownTimer(breakLeng, interval) {

						public void onTick(long millis) {
							breakCountdown.setTextColor(Color
									.parseColor("#0099CC"));
							String minute = String.valueOf(millis / 60000 % 60);

							String sec = String.valueOf(millis / 1000 % 60);

							if (millis / 1000 <= 60) {
								breakCountdown.setText("+ 00:"
										+ String.valueOf(millis / 1000));
								if (millis / 1000 % 60 < 10) {
									breakCountdown.setText("+ " + minute + ":0"
											+ sec);
								}
							} else {
								breakCountdown.setText("+ " + minute + ":"
										+ sec);
							}
						}

						@Override
						public void onFinish() {
							breakCountdown.setText("+ 00:00");
							breakCountdown.setTextColor(Color
									.parseColor("#FF4444"));
							breakCountdown.startAnimation(anim);
							countDownTimer.start();
						}

					};

				}
			});
			stop.setOnClickListener(new OnClickListener() {

				AlarmManager am;

				public void onClick(View v) {

					am = (AlarmManager) getActivity().getSystemService(
							ALARM_SERVICE);

					setTextStop();

					Toast.makeText(getActivity().getApplicationContext(),
							"Notification cancelled", Toast.LENGTH_LONG).show();

					cancelAll();

					SharedPreferences pref = getActivity()
							.getSharedPreferences("pref", 0);
					SharedPreferences.Editor edit = pref.edit();
					edit.putString("takeBreak", "true");
					edit.commit();
					System.out.println("takeBreak = true");

					// haptic feedback
					vibrate();

				}

				private void setTextStop() {

					// button text colours
					oneTime.setTextColor(Color.BLACK);
					reoccuring.setTextColor(Color.BLACK);
					stop.setTextColor(Color.RED);
				}

				private void cancelAll() {

					// cancel Countdown Timer
					countDownTimer.cancel();
					countdown.startAnimation(anim);
					countdown.setTextColor(Color.RED);

					countDownTimer1.cancel();
					breakCountdown.startAnimation(anim);
					breakCountdown.setTextColor(Color.RED);
					// re-enable buttons
					reoccuring.setEnabled(true);
					oneTime.setEnabled(true);

					// cancel AlarmManager

					am.cancel(pintent3);
					am.cancel(pintent2);
					am.cancel(pintent);
					pintent.cancel();
					pintent3.cancel();
					pintent2.cancel();
					

				}
			});

			// return View
			return rootView;
		}

		protected void vibrate() {

			// set vibration

			myVib = (Vibrator) getActivity().getSystemService(VIBRATOR_SERVICE);
			if (haptic) {
				myVib.vibrate(50);
			} else {
				myVib.vibrate(0);
			}
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

	@Override
	public void onDestroy() {

		super.onDestroy();
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	// Apply theme
	@Override
	public void onRestart() {
		super.onRestart();
		restartOnSwitch(this);
	}

	static void restartOnSwitch(Activity act) {
		if (AppSettingsActivity.hasChanged == true) {
			Intent it = act.getIntent();
			it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			AppSettingsActivity.hasChanged = false;
			act.startActivity(it);
		}
	}

	@Override
	public void onBackPressed() {

		moveTaskToBack(true);
	}

	@Override
	public void onPause() {
		super.onPause();

	}

}