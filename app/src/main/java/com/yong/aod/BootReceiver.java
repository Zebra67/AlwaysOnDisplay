package com.yong.aod;

import android.content.*;

public class BootReceiver extends BroadcastReceiver
{
	@Override
    public void onReceive(Context arg0, Intent arg1) {
        SharedPreferences prefs = arg0.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
		switch(prefs.getInt("service",2)){
			case 1:
				switch(prefs.getInt("useTimer", 0)){
					case 0:
						arg0.startService(new Intent(arg0, service.class));
						break;
					case 1:
						arg0.startService(new Intent(arg0, TimerAODservice.class));
						break;
				}
				break;
			case 2:
				break;
			}
		}
}
