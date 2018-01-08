package com.yong.aod;

import android.content.*;
import android.util.*;
import java.util.*;
import java.text.*;
import android.app.*;
import android.os.*;

public class stopReceiver extends BroadcastReceiver {
    public stopReceiver() {
	}
	
	Date tomorrow;
	Date tomorrow2;

    @Override
    public void onReceive(final Context context, Intent intent) {
		Log.i("Stop Receiver", "Received!");
		context.stopService(new Intent(context, service.class));
		Intent receiverIntentw = new Intent("com.yong.aod.ALARM_STOP");
		PendingIntent pendingIntentw = PendingIntent.getBroadcast(context, 0, receiverIntentw, PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager amw = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
		if (pendingIntentw != null){
			amw.cancel(pendingIntentw);
			pendingIntentw.cancel();
		}
		new Handler().postDelayed(new Runnable()
			{
				@Override
				public void run()
				{
					SharedPreferences prefs = context.getSharedPreferences("androesPrefName", context.MODE_PRIVATE);
					String stopTime = prefs.getString("stopTime", "00:00");
					String startDate;
					SimpleDateFormat Date = new SimpleDateFormat("yyyy-MM-", Locale.getDefault());
					startDate = (Date.format(new Date())) + tomorrowDate() + " ";
					Intent receiverIntent2 = new Intent("com.yong.aod.ALARM_STOP");
					PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context, 0, receiverIntent2, PendingIntent.FLAG_UPDATE_CURRENT);
					try
					{
						tomorrow2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(startDate + stopTime + ":00");
						Log.i("tomorrow2", tomorrow2.toString());
					}
					catch (ParseException e)
					{}
					AlarmManager am2 = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
					am2.setExact(AlarmManager.RTC_WAKEUP, tomorrow2.getTime(), pendingIntent2);
				}
			}, 60000);
    }
	
	public static String tomorrowDate(){
		Date today = new Date();
		Date seldate = new Date();
		SimpleDateFormat simple = new SimpleDateFormat("dd");
		seldate.setTime(today.getTime() + (1000*60*60*24));
		return simple.format(seldate);
	}
}
