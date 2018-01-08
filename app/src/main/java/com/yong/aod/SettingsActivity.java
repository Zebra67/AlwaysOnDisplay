package com.yong.aod;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.widget.SeekBar.*;
import java.util.*;
import android.support.v7.app.*;

public class SettingsActivity extends AppCompatActivity
{
	SeekBar seekbar;
	TextView status;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		SharedPreferences prefs = getApplicationContext().getSharedPreferences("androesPrefName", MODE_PRIVATE);
		CheckBox checkBox1 = (CheckBox) findViewById(R.id.burninprotection);
		CheckBox checkBox2 = (CheckBox) findViewById(R.id.dt2w);
		CheckBox checkBox3 = (CheckBox) findViewById(R.id.timeFormat);
		CheckBox checkBox5 = (CheckBox) findViewById(R.id.autoBrightness);
		CheckBox checkBox6 = (CheckBox) findViewById(R.id.homeButton);
		CheckBox checkBox7 = (CheckBox) findViewById(R.id.volumeButton);
		CheckBox checkBox8 = (CheckBox) findViewById(R.id.timer);
		seekbar = (SeekBar)findViewById(R.id.brightnessBar);
		switch(prefs.getInt("useTimer",0)){
			case 0:
				checkBox8.setChecked(false);
				Button applyTimer = (Button)findViewById(R.id.applyTimer);
				applyTimer.setEnabled(false);
				break;
			case 1:
				checkBox8.setChecked(true);
				Button applyTimer2 = (Button)findViewById(R.id.applyTimer);
				applyTimer2.setEnabled(true);
				break;
		}
		checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub

					if(isChecked==false){
					SharedPreferences prefs = getApplicationContext().getSharedPreferences("androesPrefName", MODE_PRIVATE);
					SharedPreferences.Editor ed = prefs.edit();
					ed.remove("burnin");
					ed.commit();
					// WRITE
					ed.putInt("burnin", 0);
					ed.commit();
					}else{
						SharedPreferences prefs = getApplicationContext().getSharedPreferences("androesPrefName", MODE_PRIVATE);
						SharedPreferences.Editor ed = prefs.edit();
						ed.remove("burnin");
						ed.commit();
						// WRITE
						ed.putInt("burnin", 1);
						ed.commit();
				}}
			});
		checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub

					if(isChecked==false){
						SharedPreferences prefs = getApplicationContext().getSharedPreferences("androesPrefName", MODE_PRIVATE);
						SharedPreferences.Editor ed = prefs.edit();
						ed.remove("dt2w");
						ed.commit();
						// WRITE
						ed.putInt("dt2w", 0);
						ed.commit();
					}else{
						SharedPreferences prefs = getApplicationContext().getSharedPreferences("androesPrefName", MODE_PRIVATE);
						SharedPreferences.Editor ed = prefs.edit();
						ed.remove("dt2w");
						ed.commit();
						// WRITE
						ed.putInt("dt2w", 1);
						ed.commit();
					}}
			});
		checkBox3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub

					if(isChecked==false){
						SharedPreferences prefs = getApplicationContext().getSharedPreferences("androesPrefName", MODE_PRIVATE);
						SharedPreferences.Editor ed = prefs.edit();
						ed.remove("timeFormat");
						ed.commit();
						// WRITE
						ed.putInt("timeFormat", 0);
						ed.commit();
					}else{
						SharedPreferences prefs = getApplicationContext().getSharedPreferences("androesPrefName", MODE_PRIVATE);
						SharedPreferences.Editor ed = prefs.edit();
						ed.remove("timeFormat");
						ed.commit();
						// WRITE
						ed.putInt("timeFormat", 1);
						ed.commit();
					}}
			});
		checkBox5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					if(isChecked==false){
						SharedPreferences prefs = getApplicationContext().getSharedPreferences("androesPrefName", MODE_PRIVATE);
						SharedPreferences.Editor ed = prefs.edit();
						ed.remove("autoBrightness");
						ed.commit();
						// WRITE
						ed.putInt("autoBrightness", 0);
						ed.commit();
						seekbar.setEnabled(true);
					}else{
						SharedPreferences prefs = getApplicationContext().getSharedPreferences("androesPrefName", MODE_PRIVATE);
						SharedPreferences.Editor ed = prefs.edit();
						ed.remove("autoBrightness");
						ed.commit();
						// WRITE
						ed.putInt("autoBrightness", 1);
						ed.commit();
						seekbar.setEnabled(false);
					}}
			});
		checkBox6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub

					if(isChecked==false){
						SharedPreferences prefs = getApplicationContext().getSharedPreferences("androesPrefName", MODE_PRIVATE);
						SharedPreferences.Editor ed = prefs.edit();
						ed.remove("home_button");
						ed.commit();
						// WRITE
						ed.putInt("home_button", 0);
						ed.commit();
					}else{
						SharedPreferences prefs = getApplicationContext().getSharedPreferences("androesPrefName", MODE_PRIVATE);
						SharedPreferences.Editor ed = prefs.edit();
						ed.remove("home_button");
						ed.commit();
						// WRITE
						ed.putInt("home_button", 1);
						ed.commit();
					}}
			});
		checkBox7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub

					if(isChecked==false){
						SharedPreferences prefs = getApplicationContext().getSharedPreferences("androesPrefName", MODE_PRIVATE);
						SharedPreferences.Editor ed = prefs.edit();
						ed.remove("volume_button");
						ed.commit();
						// WRITE
						ed.putInt("volume_button", 0);
						ed.commit();
					}else{
						SharedPreferences prefs = getApplicationContext().getSharedPreferences("androesPrefName", MODE_PRIVATE);
						SharedPreferences.Editor ed = prefs.edit();
						ed.remove("volume_button");
						ed.commit();
						// WRITE
						ed.putInt("volume_button", 1);
						ed.commit();
					}}
			});
		checkBox8.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					if(isChecked==false){
						SharedPreferences prefs = getApplicationContext().getSharedPreferences("androesPrefName", MODE_PRIVATE);
						SharedPreferences.Editor ed = prefs.edit();
						ed.remove("useTimer");
						ed.commit();
						// WRITE
						ed.putInt("useTimer", 0);
						ed.commit();
						Button applyTimer = (Button)findViewById(R.id.applyTimer);
						applyTimer.setEnabled(false);
					}else{
						SharedPreferences prefs = getApplicationContext().getSharedPreferences("androesPrefName", MODE_PRIVATE);
						SharedPreferences.Editor ed = prefs.edit();
						ed.remove("useTimer");
						ed.commit();
						// WRITE
						ed.putInt("useTimer", 1);
						ed.commit();
						Button applyTimer = (Button)findViewById(R.id.applyTimer);
						applyTimer.setEnabled(true);
					}}
			});
		TextView currentValue = (TextView)findViewById(R.id.currentTimerSetting);
		currentValue.setText(getResources().getString(R.string.startTime)+prefs.getString("startTime", getResources().getString(R.string.notSet))+"\n"+getResources().getString(R.string.stopTime)+prefs.getString("stopTime", getResources().getString(R.string.notSet)));
		seekbar = (SeekBar)findViewById(R.id.brightnessBar);

		switch(prefs.getInt("burnin",0)){
			case 0:
				checkBox1.setChecked(false);
				break;
			case 1:
				checkBox1.setChecked(true);
				break;
		}
		switch(prefs.getInt("dt2w",1)){
			case 0:
				checkBox2.setChecked(false);
				break;
			case 1:
				checkBox2.setChecked(true);
				break;
		}
		switch(prefs.getInt("home_button",0)){
			case 0:
				checkBox6.setChecked(false);
				break;
			case 1:
				checkBox6.setChecked(true);
				break;
		}
		switch(prefs.getInt("timeFormat",0)){
			case 0:
				checkBox3.setChecked(false);
				break;
			case 1:
				checkBox3.setChecked(true);
				break;
		}
		switch(prefs.getInt("volume_button",0)){
			case 0:
				checkBox7.setChecked(false);
				break;
			case 1:
				checkBox7.setChecked(true);
				break;
		}
		switch(prefs.getInt("autoBrightness",0)){
			case 0:
				checkBox5.setChecked(false);
				seekbar.setEnabled(true);
				seekbar.setOnSeekBarChangeListener(new SeekBar_Listener());
				break;
			case 1:
				checkBox5.setChecked(true);
				seekbar.setEnabled(false);
				break;
		}
		status = (TextView) findViewById(R.id.brightnessValue);
		status.setText(String.valueOf((prefs.getInt("brightness", 20))));
		seekbar.setProgress(prefs.getInt("brightness", 20));
	}

	@Override
	protected void onDestroy()
	{
		// TODO: Implement this method
		super.onDestroy();
		if(isServiceRunning()==true){
			SharedPreferences prefs = getApplicationContext().getSharedPreferences("androesPrefName", MODE_PRIVATE);
			switch(prefs.getInt("useTimer", 0)){
				case 0:
					stopService(new Intent(this, service.class));
					startService(new Intent(this, service.class));
					break;
				case 1:
					stopService(new Intent(this, TimerAODservice.class));
					startService(new Intent(this, TimerAODservice.class));
					break;
			}
		}else{
			
		}
	}
	
	public boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) this.getSystemService(Activity.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("com.yong.aod.StartingService".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
	}
	
	class SeekBar_Listener implements OnSeekBarChangeListener {
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			status.setText(progress + "%");
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
			
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
			int progress = seekbar.getProgress();
			SharedPreferences prefs = getApplicationContext().getSharedPreferences("androesPrefName", MODE_PRIVATE);
			SharedPreferences.Editor ed = prefs.edit();
			ed.remove("brightness");
			ed.commit();
			ed.putInt("brightness", progress);
			ed.commit();
        }
    }
	
	public void themeActivity(View v){
		startActivity(new Intent(this, ThemeActivity.class));
	}
	
	public void applyTimer(View v){
		Toast.makeText(getApplicationContext(), getResources().getString(R.string.timerInfo), Toast.LENGTH_LONG).show();
		new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
				@Override
				public void onTimeSet(TimePicker timePicker, int i, int i1) {
					int startHour = timePicker.getHour();
					int startMinute = timePicker.getMinute();
					SharedPreferences prefs = getApplicationContext().getSharedPreferences("androesPrefName", MODE_PRIVATE);
					SharedPreferences.Editor ed = prefs.edit();
					ed.remove("startTime");
					ed.commit();
					// WRITEt
					ed.putString("startTime", String.format("%02d", startHour) + ":" + String.format("%02d", startMinute));
					ed.commit();
					new TimePickerDialog(SettingsActivity.this, new TimePickerDialog.OnTimeSetListener() {
							@Override
							public void onTimeSet(TimePicker timePicker, int i, int i1) {
								int stopHour = timePicker.getHour();
								int stopMinute = timePicker.getMinute();
								SharedPreferences prefs = getApplicationContext().getSharedPreferences("androesPrefName", MODE_PRIVATE);
								SharedPreferences.Editor ed = prefs.edit();
								ed.remove("stopTime");
								ed.commit();
								// WRITE
								ed.putString("stopTime", String.format("%02d", stopHour) + ":" + String.format("%02d", stopMinute));
								ed.commit();
								TextView currentValue = (TextView)findViewById(R.id.currentTimerSetting);
								currentValue.setText(getResources().getString(R.string.startTime) + prefs.getString("startTime", "00:00") + "\n" + getResources().getString(R.string.stopTime) + prefs.getString("stopTime", "00:00"));
							}
						}, Calendar.HOUR_OF_DAY, Calendar.MINUTE, true).show();
				}
			}, Calendar.HOUR_OF_DAY, Calendar.MINUTE, true).show();
	}
}
