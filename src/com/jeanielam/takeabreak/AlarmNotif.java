package com.jeanielam.takeabreak;

import com.jeanielam.takeabreak.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class AlarmNotif extends Service {

	NotificationManager notifManager;
	Notification myNotification;

	
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
		displayNotif();
	}


	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private void displayNotif() {
		
		notifManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);

		Intent newIntent = new Intent(this, MainActivity.class);

		PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 2,
				newIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		myNotification = new Notification.Builder(this)
				.setContentTitle("Take a break!")
				.setContentText("You deserve it!").setTicker("Take a break!")
				.setDefaults(Notification.DEFAULT_ALL)
				.setContentIntent(resultPendingIntent).setAutoCancel(true)
				.setSmallIcon(R.drawable.icon_teal).build();

		notifManager.notify(2, myNotification);
		System.out.println("notification sent");
		
		
	}

}
