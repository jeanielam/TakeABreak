package com.jeanielam.takeabreak;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {


	@Override
	public void onReceive(Context context, Intent intent) {

		Intent newIntent = new Intent(context, AlarmNotif.class);
		context.startService(newIntent);

	}
}
