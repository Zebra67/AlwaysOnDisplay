package com.yong.aod;

import android.app.*;
import android.content.*;
import android.hardware.*;
import android.os.*;
import android.util.*;
import android.provider.*;

public class AODProximityService extends Service //implements SensorEventListener
{
	int originalCapacitiveButtonsState;
	int originalTimeoutState;
	
	static final String SYSTEM_DIALOG_REASON_KEY = "reason";
    static final String SYSTEM_DIALOG_REASON_GLOBAL_ACTIONS = "globalactions";
    static final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
    static final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
	
	//private SensorManager m_clsSensorManager;
	//private Sensor m_clsSensor;
	
	private final BroadcastReceiver closeReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			context.sendBroadcast(new Intent("exit"));
		}
	};
	
	private BroadcastReceiver homeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
                if (reason != null) {
					if (reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {
						Log.d("home", "pressed");
						context.sendBroadcast(new Intent("exit"));
					}
                }
            }

        }
    };
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		IntentFilter filter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        registerReceiver(homeReceiver,filter);
		registerReceiver(closeReceiver, new IntentFilter("close_aod"));
		//Initialize Proximity Sensor
		//m_clsSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
		//m_clsSensor = m_clsSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
		//m_clsSensorManager.registerListener(this, m_clsSensor, SensorManager.SENSOR_DELAY_NORMAL);
		try {
			originalCapacitiveButtonsState = Settings.System.getInt(getContentResolver(), "button_key_light", 1500);
		} catch (Exception e) {}
		try {
			originalTimeoutState = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, 0);
		} catch (Exception e) {}
		try{
			Settings.System.putInt(getContentResolver(), "button_key_light", 0);
		}catch(Exception e){}
		try {
			Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, 5000);
		} catch (Exception e) {}
		WakeUpScreen.acquireCpuLock(getApplicationContext());
		startForeground(1379, new Notification());
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		try{
			unregisterReceiver(closeReceiver);
		}catch(IllegalArgumentException e){}
		try{
			unregisterReceiver(homeReceiver);
		}catch(IllegalArgumentException e){}
		//m_clsSensorManager.unregisterListener(this);
		try {
			Settings.System.putInt(getContentResolver(), "button_key_light", originalCapacitiveButtonsState);
		}catch(RuntimeException e) {}
		try{
			Settings.System.putLong(getContentResolver(), "button_key_light", originalCapacitiveButtonsState);
		}catch(Exception e) {}
		try{
			Settings.Secure.putInt(getContentResolver(), "button_key_light", originalCapacitiveButtonsState);
		}catch(Exception e) {}
		try{
			Settings.System.putInt(getContentResolver(), "button_key_light", originalCapacitiveButtonsState);
		}catch(Exception e){}
		try {
			Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, originalTimeoutState);
		} catch (Exception e) {}
		WakeUpScreen.releaseCpuLock();
		stopForeground(true);
	}
	/*
	@Override
	public void onAccuracyChanged( Sensor sensor, int accuracy )
	{
	}

	@Override
	public void onSensorChanged( SensorEvent event )
	{
		float dbDistance = event.values[0];
		if(dbDistance<=1.0){
			Intent intent = new Intent("PROXIMITY_NEAR");
			sendBroadcast(intent);
			WakeUpScreen.releaseCpuLock();
			Log.i("Proximity", "Release");
		}else{
			Intent intent = new Intent("PROXIMITY_FAR");
			sendBroadcast(intent);
			WakeUpScreen.acquireCpuLock(getApplicationContext());
			Log.i("Proximity", "Acquire");
		}
	}*/

	@Override
	public IBinder onBind(Intent p1)
	{
		// TODO: Implement this method
		return null;
	}

}
