package com.yong.aod;

import android.app.*;
import android.content.*;
import android.hardware.*;
import android.os.*;
import android.util.*;
import android.graphics.*;
import android.telephony.*;
import android.support.v4.app.*;

public class service extends Service
{
	BroadcastReceiver mybroadcast = new BroadcastReceiver() {    
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
				Intent inte = new Intent(getApplicationContext(), aod.class);
				inte.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
				inte.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				inte.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
				startActivity(inte);
				Log.i("[BroadcastReceiver]", "Screen OFF");
			}
		}
	};
	
	BroadcastReceiver callBroadcast = new BroadcastReceiver() {    
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("START_SERVICE")) {
				registerReceiver(mybroadcast, new IntentFilter(Intent.ACTION_SCREEN_OFF));
			}else if(intent.getAction().equals("STOP_SERVICE")){
				try{
					unregisterReceiver(mybroadcast);
				}catch(IllegalArgumentException e){}
			}
		}
	};
	
	BroadcastReceiver proximityBroadcast = new BroadcastReceiver() {    
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("PROXIMITY_FAR")) {
				registerReceiver(mybroadcast, new IntentFilter(Intent.ACTION_SCREEN_OFF));
				Log.i("SensorReceiver", "Far!");
			}else if(intent.getAction().equals("PROXIMITY_NEAR")){
				try{
					unregisterReceiver(mybroadcast);
				}catch(IllegalArgumentException e){}
				Log.i("SensorReceiver", "Near!");
			}
		}
	};
	
	@Override
	public IBinder onBind(Intent p1)
	{
		return null;
	}

	@Override
	public void onCreate()
	{
		super.onCreate();
		registerReceiver(mybroadcast, new IntentFilter(Intent.ACTION_SCREEN_OFF));
		IntentFilter filter = new IntentFilter();
		filter.addAction("START_SERVICE");
		filter.addAction("STOP_SERVICE");
		registerReceiver(callBroadcast, filter);
		IntentFilter filter2 = new IntentFilter();
		filter2.addAction("PROXIMITY_NEAR");
		filter2.addAction("PROXIMITY_FAR");
		registerReceiver(proximityBroadcast, filter2);
		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext())
			.setSmallIcon(R.drawable.ic_launcher)
			.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher) )
			.setContentTitle("Always On Display")
			.setContentText("Always On Display" + getResources().getString(R.string.aod_running))
			.setOngoing(true)
			.setPriority(Notification.PRIORITY_MIN)
			.setAutoCancel(false);
		startForeground(1111, notificationBuilder.build());
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		try{
			unregisterReceiver(mybroadcast);
		}catch(IllegalArgumentException e){}
		try{
			unregisterReceiver(callBroadcast);
		}catch(IllegalArgumentException e){}
		try{
			unregisterReceiver(proximityBroadcast);
		}catch(IllegalArgumentException e){}
		stopForeground(true);
	}
}
