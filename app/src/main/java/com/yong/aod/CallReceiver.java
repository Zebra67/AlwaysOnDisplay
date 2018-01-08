package com.yong.aod;
import android.content.*;
import android.util.*;
import android.telephony.*;

public class CallReceiver extends BroadcastReceiver
{
	@Override
    public void onReceive(Context arg0, Intent arg1) {
		Intent START_SERVICE = new Intent("START_SERVICE");
		Intent STOP_SERVICE = new Intent("STOP_SEEVICE");
		
        String state = arg1.getStringExtra(TelephonyManager.EXTRA_STATE);
		if((TelephonyManager.EXTRA_STATE_IDLE).equals(state))
		{
			Log.d("CallReceiver", "End");
			arg0.sendBroadcast(START_SERVICE);
		}
		else if((TelephonyManager.EXTRA_STATE_RINGING).equals(state))
		{
			Log.d("CallReceiver", "Ringing");
			arg0.sendBroadcast(STOP_SERVICE);
		}
		else if((TelephonyManager.EXTRA_STATE_OFFHOOK).equals(state))
		{
			Log.d("CallReceiver", "Talking");
			arg0.sendBroadcast(STOP_SERVICE);
		}
		else if(arg1.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL))
		{
			Log.d("CallReceiver", "Out Going Call");
			arg0.sendBroadcast(STOP_SERVICE);
		}
	}
}
