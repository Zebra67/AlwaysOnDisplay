package com.yong.aod;

import android.app.*;
import android.content.*;
import android.os.*;
import android.support.v7.app.*;
import android.view.*;
import android.widget.*;
import com.fsn.cauly.*;
import java.util.*;

public class MainActivity extends AppCompatActivity implements CaulyCloseAdListener
{
	private CaulyAdView adView;
	private static final String APP_CODE = "TOeplGZT";
 	CaulyCloseAd mCloseAd ;
	String DDayString = "";
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		final SharedPreferences prefs = getApplicationContext().getSharedPreferences("androesPrefName", MODE_PRIVATE);
		if(prefs.getBoolean("isPermissionGranted", false)==false){
			startActivity(new Intent(this, PermissionActivity.class));
		}
		final Switch serviceToggle = (Switch)findViewById(R.id.serviceSwitch);
		serviceToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton switchView, boolean isChecked)
			{
				if(isChecked==true){
					serviceToggle.setText(R.string.aodOn);
					SharedPreferences.Editor ed = prefs.edit();
					ed.remove("service");
					ed.commit();
					// WRITE
					ed.putInt("service", 1);
					ed.commit();
					switch(prefs.getInt("useTimer", 0)){
						case 0:
							startService(new Intent(MainActivity.this, service.class));
							break;
						case 1:
							startService(new Intent(MainActivity.this, TimerAODservice.class));
							break;
					}
				}else{
					serviceToggle.setText(R.string.aodOff);
					SharedPreferences.Editor ed = prefs.edit();
					ed.remove("service");
					ed.commit();
					// WRITE
					ed.putInt("service", 2);
					ed.commit();
					switch(prefs.getInt("useTimer", 0)){
						case 0:
							stopService(new Intent(MainActivity.this, service.class));
							break;
						case 1:
							stopService(new Intent(MainActivity.this, TimerAODservice.class));
							break;
					}
				}
			}
		});
		if(isServiceRunning()==true){
			serviceToggle.setChecked(true);
			serviceToggle.setText(R.string.aodOn);
		}else{
			serviceToggle.setChecked(false);
			serviceToggle.setText(R.string.aodOff);
		}
		if((prefs.getBoolean("ad_removed",false)==false)){
			showBanner();
		}else{
			LinearLayout layout=(LinearLayout)findViewById(R.id.mainLayout);
			layout.setVisibility(View.GONE);
		}
		DDayString = Integer.toString(calDate(2018, 11, 15));
		TextView DDayTV = (TextView)findViewById(R.id.ddaytv);
		DDayTV.setText(getResources().getString(R.string.sat_dday) + DDayString + "\n" + getResources().getString(R.string.detail));
		CaulyAdInfo closeAdInfo = new CaulyAdInfoBuilder(APP_CODE).build();
		mCloseAd = new CaulyCloseAd();
		mCloseAd.setButtonText("취소", "종료");
		mCloseAd.setDescriptionText("종료하시겠습니까?");
		mCloseAd.setAdInfo(closeAdInfo);
		mCloseAd.setCloseAdListener(this); 
		mCloseAd.disableBackKey();
	}
	
	@Override
	protected void onResume() {
		super.onResume(); 
		if (mCloseAd != null)
 			mCloseAd.resume(this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			SharedPreferences prefs = getApplicationContext().getSharedPreferences("androesPrefName", MODE_PRIVATE);
			if((prefs.getBoolean("ad_removed",false)==false)){
				if (mCloseAd.isModuleLoaded())
				{
					mCloseAd.show(this);
				} 
				else
				{
					showDefaultClosePopup();
				}
			}else{
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void showDefaultClosePopup()
	{
		new android.support.v7.app.AlertDialog.Builder(this).setTitle("").setMessage("종료 하시겠습니까?")
			.setPositiveButton("예", new DialogInterface.OnClickListener() {
 			    @Override
 			    public void onClick(DialogInterface dialog, int which) {
					finish();
 			    }
			})
			.setNegativeButton("아니요",null)
			.show();
 	}

	@Override
 	public void onFailedToReceiveCloseAd(CaulyCloseAd ad, int errCode,String errMsg) {
 	}
 	@Override
 	public void onLeaveCloseAd(CaulyCloseAd ad) {
 	}
 	@Override
 	public void onReceiveCloseAd(CaulyCloseAd ad, boolean isChargable) {

 	}	
 	@Override
 	public void onLeftClicked(CaulyCloseAd ad) {

 	}	
 	@Override
 	public void onRightClicked(CaulyCloseAd ad) {
 		finish();
 	}
 	@Override
 	public void onShowedCloseAd(CaulyCloseAd ad, boolean isChargable) {		
 	}
	
	private void showBanner(){
		LinearLayout layout=(LinearLayout)findViewById(R.id.mainLayout);
		CaulyAdInfo adInfo= new CaulyAdInfoBuilder("TOeplGZT").effect("Circle").reloadInterval(1).enableDefaultBannerAd(true).build();
		adView=new CaulyAdView(this);
		adView.setAdInfo(adInfo);
		layout.addView(adView,0);
	}
	
	public int calDate(int mYear, int mMonth, int mDay){
		try{
			Calendar today = Calendar.getInstance();
			Calendar dday = Calendar.getInstance();
			dday.set(mYear, mMonth-1, mDay);
			
			long day = dday.getTimeInMillis()/86400000;
			long tday = today.getTimeInMillis()/86400000;
			long count = tday - day + 1;
			
			return (int)count;
		}catch(Exception e){
			e.printStackTrace();
			return -1;
		}
	}
	
	public void info(View v){
		startActivity(new Intent(this, InfoActivity.class));
	}
	
	public void setting(View v){
		startActivity(new Intent(this, SettingsActivity.class));
	}
	
	public void buyActivity(View v){
		Intent intent = new Intent(this, BillingActivity.class);
		startActivity(intent);
	}
	
	public void sat(View v){
		android.app.AlertDialog.Builder alt_bld = new android.app.AlertDialog.Builder(this);
		alt_bld.setMessage(getResources().getString(R.string.noticeDialog));
		alt_bld.setCancelable(true);
		alt_bld.setNegativeButton(getResources().getString(R.string.noticeDialogClose),
			new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.dismiss();
				}
			});
		alt_bld.show();
	}
	
	public boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) this.getSystemService(Activity.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("com.yong.aod.service".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
	}
}
