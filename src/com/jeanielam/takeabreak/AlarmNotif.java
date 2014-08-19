package com.jeanielam.takeabreak;

import com.jeanielam.takeabreak.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;

public class AlarmNotif extends Service {

	NotificationManager notifManager;
	SharedPreferences pref;
	String takeBreak;
	SharedPreferences.Editor edit;
	private Notification myNotificationgoWork;
	private Notification myNotificationTakeBreak;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		pref = getSharedPreferences("pref", 0);
		takeBreak = pref.getString("takeBreak", null);
		edit = pref.edit();
		displayNotif();
		changeVal();
	
	}

	

	private void changeVal() {

		if (takeBreak.equals("true")) {
			edit.putString("takeBreak", "false");
			edit.commit();
			System.out.println("takeBreak = false");
		} else {
			edit.putString("takeBreak", "true");
			edit.commit();
			System.out.println("takeBreak = true");
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		
	}

	private void displayNotif() {
		System.out.println("displayNotif method");
		notifManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);

		Intent newIntent = new Intent(this, MainActivity.class);

		PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 1,
				newIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		if (takeBreak != null) {
			if (takeBreak.equals("true")) {
				System.out.println("break notificiation");
				myNotificationTakeBreak = new Notification.Builder(this)
						.setContentTitle("Take a break!")
						.setContentText("You deserve it!")
						.setTicker("Take a break!")
						.setDefaults(Notification.DEFAULT_ALL)
						.setContentIntent(resultPendingIntent)
						.setAutoCancel(true)
						.setSmallIcon(R.drawable.notification_icon).build();
				notifManager.notify(1, myNotificationTakeBreak);
			} else {
				System.out.println("work notification");
				myNotificationgoWork = new Notification.Builder(this)
						.setContentTitle("Let's get back to work!")
						.setContentText("You can do it!")
						.setTicker("Back to work!")
						.setDefaults(Notification.DEFAULT_ALL)
						.setContentIntent(resultPendingIntent)
						.setAutoCancel(true)
						.setSmallIcon(R.drawable.notification_icon).build();
				notifManager.notify(2, myNotificationgoWork);
			}
		}
		System.out.println("notification sent");

	}

}
