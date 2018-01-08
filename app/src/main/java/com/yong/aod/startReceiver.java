package com.yong.aod;

import android.content.*;
import android.util.*;
import java.util.*;
import java.text.*;
import android.app.*;
import android.os.*;

public class startReceiver extends BroadcastReceiver {
    public startReceiver() {
	}
	
	Date tomorrow;
	Date tomorrow2;

    @Override
    public void onReceive(final Context context, Intent intent) {
		Log.i("Start Receiver", "Received!");
        context.startService(new Intent(context, service.class));
		Intent receiverIntentw = new Intent("com.yong.aod.ALARM_START");
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
					String startTime = prefs.getString("startTime", "00:00");
					String startDate;
					SimpleDateFormat Date = new SimpleDateFormat("yyyy-MM-", Locale.getDefault());
					startDate = (Date.format(new Date())) + tomorrowDate() + " ";
					Intent receiverIntent = new Intent("com.yong.aod.ALARM_START");
					PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, receiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);
					try
					{
						tomorrow = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(startDate + startTime + ":00");
						Log.i("tomorrow1", tomorrow.toString());
					}
					catch (ParseException e)
					{}
					AlarmManager am = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
					am.setExact(AlarmManager.RTC_WAKEUP, tomorrow.getTime(), pendingIntent);
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
