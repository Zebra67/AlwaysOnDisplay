package com.yong.aod;

import android.content.*;
import android.os.*;
import android.util.*;

public class WakeUpScreen {
	private static PowerManager.WakeLock sCpuWakeLock;   
    
    static void acquireCpuLock(Context context) {       
        if (sCpuWakeLock != null) {           
            return;       
        }        
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);        
		sCpuWakeLock = pm.newWakeLock(               
		PowerManager.ACQUIRE_CAUSES_WAKEUP |
		PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, "hello");
        sCpuWakeLock.acquire();       
    }

    static void releaseCpuLock() {       
        if (sCpuWakeLock != null) {           
            sCpuWakeLock.release();           
            sCpuWakeLock = null;       
        }   
    }
}

