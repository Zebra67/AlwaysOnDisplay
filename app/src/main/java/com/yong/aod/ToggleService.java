package com.yong.aod;

import android.service.quicksettings.*;
import android.content.*;
import android.app.*;

public class ToggleService extends TileService
{
	boolean isListening = false;
	
	@Override
	public void onStartListening()
	{
		// TODO: Implement this method
		super.onStartListening();
		isListening = true;
	}

	@Override
	public void onStopListening()
	{
		// TODO: Implement this method
		super.onStopListening();
		isListening = false;
	}
	
	@Override
    public void onClick() {
		Tile tile = getQsTile();
        int tileState = tile.getState();
        if (tileState != Tile.STATE_UNAVAILABLE) {
            tile.setState(tileState == Tile.STATE_ACTIVE? Tile.STATE_INACTIVE : Tile.STATE_ACTIVE);
            tile.updateTile();
			SharedPreferences prefs = getApplicationContext().getSharedPreferences("androesPrefName", MODE_PRIVATE);
            if (tileState == Tile.STATE_ACTIVE) {
                switch(prefs.getInt("useTimer", 0)){
					case 0:
						stopService(new Intent(this, service.class));
						break;
					case 1:
						stopService(new Intent(this, TimerAODservice.class));
						break;
				}
            }else if(tileState == Tile.STATE_INACTIVE){
				switch(prefs.getInt("useTimer", 0)){
					case 0:
						startService(new Intent(this, service.class));
						break;
					case 1:
						startService(new Intent(this, TimerAODservice.class));
						break;
				}
			}
        }
    }
}
