package com.yong.aod;

import android.app.*;
import android.content.*;
import android.os.*;
import java.text.*;
import java.util.*;
import android.util.*;
import android.support.v4.app.*;
import android.graphics.*;

public class TimerAODservice extends Service
{
	Date tomorrow;
	Date tomorrow2;
	
	BroadcastReceiver callBroadcast = new BroadcastReceiver() {    
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("START_SERVICE")) {
				register();
			}else if(intent.getAction().equals("STOP_SERVICE")){
				unregister();
			}
		}
	};
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		IntentFilter filter = new IntentFilter();
		filter.addAction("START_SERVICE");
		filter.addAction("STOP_SERVICE");
		registerReceiver(callBroadcast, filter);
		register();
		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext())
			.setSmallIcon(R.drawable.ic_launcher)
			.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher) )
			.setContentTitle("Always On Display")
			.setContentText("Always On Display" + getResources().getString(R.string.aod_running))
			.setOngoing(true)
			.setPriority(Notification.PRIORITY_MIN)
			.setAutoCancel(false);
		startForeground(2222, notificationBuilder.build());
		return onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy()
	{
		// TODO: Implement this method
		super.onDestroy();
		unregister();
		try{
			unregisterReceiver(callBroadcast);
		}catch(IllegalArgumentException e){}
		stopForeground(true);
	}

	@Override
	public IBinder onBind(Intent p1)
	{
		// TODO: Implement this method
		return null;
	}
	
	void register(){
		SharedPreferences prefs = getApplicationContext().getSharedPreferences("androesPrefName", MODE_PRIVATE);
		String startTime = prefs.getString("startTime", "00:00");
		String stopTime = prefs.getString("stopTime", "00:00");
		String startDate;
		SimpleDateFormat Date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		startDate = (Date.format(new Date())) + " ";
		Intent receiverIntent = new Intent("com.yong.aod.ALARM_START");
		PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, receiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		try
		{
			tomorrow = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(startDate + startTime + ":00");
			Log.i("tomorrow1", tomorrow.toString());
		}
		catch (ParseException e)
		{}
		AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
		am.setExact(AlarmManager.RTC_WAKEUP, tomorrow.getTime(), pendingIntent);

		Intent receiverIntent2 = new Intent("com.yong.aod.ALARM_STOP");
		PendingIntent pendingIntent2 = PendingIntent.getBroadcast(this, 0, receiverIntent2, PendingIntent.FLAG_UPDATE_CURRENT);
		try
		{
			tomorrow2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(startDate + stopTime + ":00");
			Log.i("tomorrow2", tomorrow2.toString());
		}
		catch (ParseException e)
		{}
		AlarmManager am2 = (AlarmManager) getSystemService(ALARM_SERVICE);
		am2.setExact(AlarmManager.RTC_WAKEUP, tomorrow2.getTime(), pendingIntent2);
		if(pendingIntent!=null){
			Log.i("AM1", "Registered!");
		}
		if(pendingIntent2!=null){
			Log.i("AM2", "Registered!");
		}
	}
	
	void unregister(){
		Intent receiverIntent = new Intent("com.yong.aod.ALARM_START");
		PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, receiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
		if (pendingIntent != null){
			am.cancel(pendingIntent);
			pendingIntent.cancel();
		}

		Intent receiverIntent2 = new Intent("com.yong.aod.ALARM_STOP");
		PendingIntent pendingIntent2 = PendingIntent.getBroadcast(this, 0, receiverIntent2, PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager am2 = (AlarmManager) getSystemService(ALARM_SERVICE);
		if (pendingIntent2 != null){
			am2.cancel(pendingIntent2);
			pendingIntent2.cancel();
		}
	}
}
