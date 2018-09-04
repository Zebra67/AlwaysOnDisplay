package com.yong.aod;
import android.*;
import android.content.pm.*;
import android.os.*;
import android.support.annotation.*;
import android.support.v4.app.*;
import android.support.v4.content.*;
import android.support.v7.app.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import android.content.*;
import android.net.*;
import android.provider.*;
import java.util.*;
import com.gun0912.tedpermission.*;

public class PermissionActivity extends AppCompatActivity
{
	int no_again = 0;
	boolean notiAllowed = false;
	boolean phonePermissionGranted = false;
	boolean notiError = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_permission);
	}
	
	public void phoneState(View v){
		PermissionListener permissionlistener = new PermissionListener() {
			@Override
			public void onPermissionGranted() {
				
			}

			@Override
			public void onPermissionDenied(ArrayList<String> deniedPermissions) {
				
			}
		};
		TedPermission.with(this)
			.setPermissionListener(permissionlistener)
			.setDeniedMessage(getResources().getString(R.string.permission_dialog))
			.setPermissions(Manifest.permission.READ_PHONE_STATE)
			.check();
		}
		
		public void notiPermission(View v){
			try{
				Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
				startActivity(intent);
			}catch(IllegalStateException e){
				Button notiButton = (Button)findViewById(R.id.btn_noti);
				notiError = true;
				notiButton.setEnabled(false);
				notiAllowed = true;
				notiButton.setText(getResources().getString(R.string.ok));
			}
		}
		
		public void end(View v){
			SharedPreferences prefs = getApplicationContext().getSharedPreferences("androesPrefName", MODE_PRIVATE);
			SharedPreferences.Editor ed = prefs.edit();
			ed.remove("isPermissionGranted");
			ed.commit();
			// WRITE
			ed.putBoolean("isPermissionGranted", true);
			ed.commit();
			finish();
		}

		@Override
		protected void onResume()
		{
			// TODO: Implement this method
			super.onResume();
			Button endButton = (Button)findViewById(R.id.btn_end);
			endButton.setEnabled(false);
			Button notiButton = (Button)findViewById(R.id.btn_noti);
			if(notiError == false){
				if(isNotiPermissionAllowed()==true){
					notiButton.setEnabled(false);
					notiAllowed = true;
					notiButton.setText(getResources().getString(R.string.ok));
				}else{
					notiButton.setEnabled(true);
				}
			}
			int Permission_PhoneState = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
			Button phoneStateButton = (Button)findViewById(R.id.btn_phoneState);
			if(Permission_PhoneState== PackageManager.PERMISSION_DENIED){
				phoneStateButton.setEnabled(true);
			}else{
				phoneStateButton.setEnabled(false);
				phonePermissionGranted = true;
				phoneStateButton.setText(getResources().getString(R.string.ok));
			}
			new Handler().postDelayed(new Runnable()
				{
					@Override
					public void run()
					{
						if((notiAllowed==true)&&(phonePermissionGranted==true)){
							Button endButton = (Button)findViewById(R.id.btn_end);
							endButton.setEnabled(true);
							endButton.setText(getResources().getString(R.string.finish));
						}
					}
				}, 500);
		}

	private boolean isNotiPermissionAllowed() {
		Set<String> notiListenerSet = NotificationManagerCompat.getEnabledListenerPackages(this);
		String myPackageName = getPackageName();

		for(String packageName : notiListenerSet) {
			if(packageName == null) {
				continue;
			}
			if(packageName.equals(myPackageName)) {
				return true;
			}
		}

		return false;
	}
}
