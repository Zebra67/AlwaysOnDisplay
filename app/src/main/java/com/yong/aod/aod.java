package com.yong.aod;

import android.annotation.*;
import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.graphics.*;
import android.media.*;
import android.os.*;
import android.provider.*;
import android.support.v7.widget.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.view.animation.*;
import android.widget.*;
import com.ssomai.android.scalablelayout.*;
import java.text.*;
import java.util.*;

public class aod extends Activity 
{
	String NEW_NOTIFICATION = "new_notification";
	String battery = "";
	String amText = "";
	String pmText = "";
	int unreadMessagesCount = 0;
	int defaultBackLightValue = 0;
	int defaultAutoBrightnessValue = 0;
	int burninMarginMaxheight = 0;
	int heightMargin = 0;
	int widthMargin = 0;
	int randHeight = 0;
	int status = 0;
	int perc = 0;
	boolean isCharging = false;
	static boolean ring=false;
	static boolean callReceived=false;
    static String callerPhoneNumber = "";
	
	private WindowManager.LayoutParams moldLp;
	private WindowManager.LayoutParams mnewLp;
	private Window mWindow;
	private int currentApiVersion;
	private PowerManager pm;
	private KeyguardManager km;
	private KeyguardManager.KeyguardLock keyLock;
	
	private BroadcastReceiver newNotificationBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            View notiLed = findViewById(R.id.icons_wrapper);
			notiLed.setVisibility(View.VISIBLE);
			Animation mAnimation = new AlphaAnimation(1, 0);
			mAnimation.setDuration(500);
			mAnimation.setInterpolator(new LinearInterpolator());
			mAnimation.setRepeatCount(Animation.INFINITE);
			mAnimation.setRepeatMode(Animation.REVERSE);
			notiLed.startAnimation(mAnimation);
			Log.d("NotiReceived", "True");
        }
    };
	
	private final BroadcastReceiver exitReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			unregisterReceiver(exitReceiver);
			stopService(new Intent(aod.this, AODProximityService.class));
			finish();        
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		setContentView(R.layout.aod_black);
		super.onCreate(savedInstanceState);
		//Block Screen Orientation
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		initializeAOD();
		registerReceiver(exitReceiver, new IntentFilter("exit"));
		startService(new Intent(this, AODProximityService.class));
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		exitAOD();
		View notiLed = findViewById(R.id.icons_wrapper);
		notiLed.setVisibility(View.GONE);
		notiLed.clearAnimation();
	}
		
	//Battery Handler
	Handler mBatteryHandler = new Handler() {
		public void handleMessage(Message msg) {
			IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
			Intent batteryStatus = getApplicationContext().registerReceiver(null, ifilter);
			status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
			perc = getBatteryPercentage(getApplicationContext());
			isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING;
			TextView tv  = (TextView)findViewById(R.id.battery);
			ImageView IcBattery = (ImageView)findViewById(R.id.ic_battery);
			if (isCharging == true){
				battery=String.valueOf(perc);
				tv.setText(battery+"%");
				if(perc<=15){
					IcBattery.setImageResource(R.drawable.battery_charge_1);
				}else if(perc<=45){
					IcBattery.setImageResource(R.drawable.battery_charge_2);
				}else if(perc<=60){
					IcBattery.setImageResource(R.drawable.battery_charge_3);
				}else if(perc<=75){
					IcBattery.setImageResource(R.drawable.battery_charge_4);
				}else if(perc<=99){
					IcBattery.setImageResource(R.drawable.battery_charge_5);
				}else if(perc<=100){
					IcBattery.setImageResource(R.drawable.battery_charge_6);
				}
			}else{
				battery=String.valueOf(perc);
				tv.setText(battery+"%");
				if(perc<=15){
					IcBattery.setImageResource(R.drawable.battery_15);
				}else if(perc<=45){
					IcBattery.setImageResource(R.drawable.battery_45);
				}else if(perc<=60){
					IcBattery.setImageResource(R.drawable.battery_60);
				}else if(perc<=75){
					IcBattery.setImageResource(R.drawable.battery_75);
				}else if(perc<=95){
					IcBattery.setImageResource(R.drawable.battery_95);
				}else if(perc<=100){
					IcBattery.setImageResource(R.drawable.battery_100);
				}
			}
			mBatteryHandler.sendEmptyMessageDelayed(0, 3000);
		}
	};
	
	//Burn In Protection Handler
	Handler mBurnInHandler = new Handler() {
		public void handleMessage(Message msg) {
			DisplayMetrics metrics = new DisplayMetrics();
			WindowManager windowManager = (WindowManager) getApplicationContext()
				.getSystemService(Context.WINDOW_SERVICE);
			windowManager.getDefaultDisplay().getMetrics(metrics);
			ScalableLayout clockLayout = (ScalableLayout)findViewById(R.id.clock);
			FrameLayout.LayoutParams plControl = (FrameLayout.LayoutParams) clockLayout.getLayoutParams();
			SharedPreferences prefs = getApplicationContext().getSharedPreferences("androesPrefName", MODE_PRIVATE);
			if(prefs.getInt("burnin",0)==1){
				if(prefs.getInt("setting",1)==3 || prefs.getInt("setting", 1)==6){
					burninMarginMaxheight = (metrics.heightPixels/5);
					randHeight = getRandomMath(burninMarginMaxheight, 1);
					plControl.bottomMargin = randHeight;
					clockLayout.setLayoutParams(plControl);
				}else{
					windowManager.getDefaultDisplay().getMetrics(metrics);
					burninMarginMaxheight = (metrics.heightPixels/4);
					randHeight = getRandomMath(burninMarginMaxheight, 1);
					plControl.bottomMargin = randHeight;
					clockLayout.setLayoutParams(plControl);
				}
				mBurnInHandler.sendEmptyMessageDelayed(0, 15000);
			}
		}
	};
		
	//Clock Handler
	Handler mClockHandler = new Handler() {
		public void handleMessage(Message msg) {
			amText = getString(R.string.amText);
			pmText = getString(R.string.pmText);
			SharedPreferences prefs = getApplicationContext().getSharedPreferences("androesPrefName", MODE_PRIVATE);
			if(prefs.getInt("setting",1)==4){
				try{
					TextView DateText = (TextView) findViewById(R.id.date);
					String DateFormat = new String("MM"+getString(R.string.month) +" dd"+getString(R.string.date) +" E"+getString(R.string.day));
					SimpleDateFormat Date = new SimpleDateFormat(DateFormat, Locale.getDefault());
					DateText.setText(Date.format(new Date()));
				}catch(Exception e){}
			}else if(prefs.getInt("setting",1)==3){
				try{
					TextView ClockText = (TextView) findViewById(R.id.time);
					String ClockHour = new String("HH");
					String ClockMin = new String("mm");
					SimpleDateFormat Clock = new SimpleDateFormat(ClockHour, Locale.getDefault());
					SimpleDateFormat Min = new SimpleDateFormat(ClockMin, Locale.getDefault());
					if(prefs.getInt("timeFormat",1)==0){
						if(Integer.parseInt(Clock.format(new Date()))>12){
							int hour = Integer.parseInt(Clock.format(new Date()));
							hour = hour-12;
						}else{
							int hour = Integer.parseInt(Clock.format(new Date()));
							ClockText.setText(String.valueOf(hour) + ":" + Min.format(new Date()));
						}
					}else{
						ClockText.setText(Clock.format(new Date()) + ":" + Min.format(new Date()));
					}
				}catch(Exception e){}
			}else if(prefs.getInt("setting",1)==6){
				try{
					TextView DateText = (TextView) findViewById(R.id.date);
					TextView ClockText = (TextView) findViewById(R.id.time);
					String DateFormat = new String("MM"+getString(R.string.month) +" dd"+getString(R.string.date) +" E"+getString(R.string.day));
					SimpleDateFormat Date = new SimpleDateFormat(DateFormat, Locale.getDefault());
					DateText.setText(Date.format(new Date()));
					String ClockHour = new String("HH");
					String ClockMin = new String("mm");
					SimpleDateFormat Clock = new SimpleDateFormat(ClockHour, Locale.getDefault());
					SimpleDateFormat Min = new SimpleDateFormat(ClockMin, Locale.getDefault());
					if(prefs.getInt("timeFormat",1)==0){
						if(Integer.parseInt(Clock.format(new Date()))>12){
							int hour = Integer.parseInt(Clock.format(new Date()));
							hour = hour-12;
							ClockText.setText(String.format("%02d", hour) + "\n" + Min.format(new Date()));
						}else{
							int hour = Integer.parseInt(Clock.format(new Date()));
							ClockText.setText(String.format("%02d", hour) + "\n" + Min.format(new Date()));
						}
					}else{
						ClockText.setText(Clock.format(new Date()) + "\n" + Min.format(new Date()));
					}
				}catch(Exception e){}
			}else{
				try{
					TextView DateText = (TextView) findViewById(R.id.date);
					TextView ClockText = (TextView) findViewById(R.id.time);
					String DateFormat = new String("MM"+getString(R.string.month) +" dd"+getString(R.string.date) +" E"+getString(R.string.day));
					SimpleDateFormat Date = new SimpleDateFormat(DateFormat, Locale.getDefault());
					DateText.setText(Date.format(new Date()));
					String ClockHour = new String("HH");
					String ClockMin = new String("mm");
					SimpleDateFormat Clock = new SimpleDateFormat(ClockHour, Locale.getDefault());
					SimpleDateFormat Min = new SimpleDateFormat(ClockMin, Locale.getDefault());
					if(prefs.getInt("timeFormat",1)==0){
						if(Integer.parseInt(Clock.format(new Date()))>12){
							int hour = Integer.parseInt(Clock.format(new Date()));
							hour = hour-12;
							ClockText.setText(String.valueOf(hour) + ":" + Min.format(new Date()));
						}else{
							int hour = Integer.parseInt(Clock.format(new Date()));
							ClockText.setText(String.valueOf(hour) + ":" + Min.format(new Date()));
						}
					}else{
						ClockText.setText(Clock.format(new Date()) + ":" + Min.format(new Date()));
					}
				}catch(Exception e){}
			}
			mClockHandler.sendEmptyMessageDelayed(0, 1000);
		}
	};
	
	//Block HardWare Buttons
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean keyBoolean = false;
    	switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:
				keyBoolean = true;
				break;
			case KeyEvent.KEYCODE_MENU:
				keyBoolean = true;
				break;
			case KeyEvent.KEYCODE_VOLUME_UP:
				SharedPreferences prefs5 = getApplicationContext().getSharedPreferences("androesPrefName", MODE_PRIVATE);
				switch(prefs5.getInt("volume_button",0)){
					case 0:
						keyBoolean = false;
						break;
					case 1:
						keyBoolean = true;
						break;
				}
				break;
			case KeyEvent.KEYCODE_VOLUME_DOWN:
				SharedPreferences prefs4 = getApplicationContext().getSharedPreferences("androesPrefName", MODE_PRIVATE);
				switch(prefs4.getInt("volume_button",0)){
					case 0:
						keyBoolean = false;
						break;
					case 1:
						keyBoolean = true;
						break;
				}
				break;
			case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
				SharedPreferences prefs = getApplicationContext().getSharedPreferences("androesPrefName", MODE_PRIVATE);
				switch(prefs.getInt("volume_button",0)){
					case 0:
						keyBoolean = false;
						boolean reverse = false;
						AudioManager am = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
						if (!am.isMusicActive())
							((AppCompatImageView) findViewById(R.id.play)).setImageResource(reverse ? R.drawable.ic_play : R.drawable.ic_pause);
						else
							((AppCompatImageView) findViewById(R.id.play)).setImageResource(reverse ? R.drawable.ic_pause : R.drawable.ic_play);
						break;
					case 1:
						keyBoolean = true;
						break;
				}
				break;
			case KeyEvent.KEYCODE_MEDIA_NEXT:
				SharedPreferences prefs2 = getApplicationContext().getSharedPreferences("androesPrefName", MODE_PRIVATE);
				switch(prefs2.getInt("volume_button",0)){
					case 0:
						keyBoolean = false;
						break;
					case 1:
						keyBoolean = true;
						break;
				}
				break;
			case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
				SharedPreferences prefs3 = getApplicationContext().getSharedPreferences("androesPrefName", MODE_PRIVATE);
				switch(prefs3.getInt("volume_button",0)){
					case 0:
						keyBoolean = false;
						break;
					case 1:
						keyBoolean = true;
						break;
				}
				break;
		}
		return keyBoolean;
	}
	
	@SuppressLint("NewApi")
	@Override
	public void onWindowFocusChanged(boolean hasFocus)
	{
		super.onWindowFocusChanged(hasFocus);
		if(currentApiVersion >= Build.VERSION_CODES.KITKAT && hasFocus)
		{
			getWindow().getDecorView().setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_LAYOUT_STABLE
				| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
				| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
				| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
		}
	}
	
	public void initializeAOD(){
		registerReceiver(newNotificationBroadcast, new IntentFilter(NEW_NOTIFICATION));
		//Start Battery Handler
		mBatteryHandler.sendEmptyMessage(0);
		//Start Clock Handler
		mClockHandler.sendEmptyMessage(0);
		//Start BurnIn Handler
		mBurnInHandler.sendEmptyMessage(0);
		//Set Clock Font and Clock Theme
		TextView time;
		Typeface font;
		SharedPreferences prefs = getApplicationContext().getSharedPreferences("androesPrefName", MODE_PRIVATE);
		switch(prefs.getInt("setting",1)){
			case 1:
				setContentView(R.layout.aod_g5);
				time = (TextView)findViewById(R.id.time);
				font = Typeface.createFromAsset( getAssets() , "fonts/lg.ttf" );
				time.setTypeface(font);
				break;
			case 2:
				setContentView(R.layout.aod_s7);
				time = (TextView)findViewById(R.id.time);
				font = Typeface.createFromAsset( getAssets() , "fonts/samsung.ttf" );
				time.setTypeface(font);
				break;
			case 3:
				setContentView(R.layout.aod_cal);
				time = (TextView)findViewById(R.id.time);
				font = Typeface.createFromAsset( getAssets() , "fonts/samsung.ttf" );
				time.setTypeface(font);
				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
				calendar.set(Calendar.HOUR_OF_DAY, 23);
				long endOfMonth = calendar.getTimeInMillis();
				calendar = Calendar.getInstance();
				calendar.set(Calendar.DATE, 1);
				calendar.set(Calendar.HOUR_OF_DAY, 0);
				long startOfMonth = calendar.getTimeInMillis();
				CalendarView mCalendarView = (CalendarView)findViewById(R.id.aodCalendar);
				mCalendarView.setMaxDate(endOfMonth);
				mCalendarView.setMinDate(startOfMonth);
				mCalendarView.setEnabled(false);
				break;
			case 4:
				setContentView(R.layout.aod_anal);
				break;
			case 5:
				setContentView(R.layout.aod_s8);
				time = (TextView)findViewById(R.id.time);
				font = Typeface.createFromAsset( getAssets() , "fonts/samsung_s8.ttf" );
				time.setTypeface(font);
				break;
			case 6:
				setContentView(R.layout.aod_s8v);
				time = (TextView)findViewById(R.id.time);
				font = Typeface.createFromAsset( getAssets() , "fonts/samsung_s8.ttf" );
				time.setTypeface(font);
				break;
		}
		if(prefs.getInt("setting", 1)==3 || prefs.getInt("setting", 1)==6){
			DisplayMetrics metrics = new DisplayMetrics();
			WindowManager windowManager = (WindowManager) getApplicationContext()
				.getSystemService(Context.WINDOW_SERVICE);
			windowManager.getDefaultDisplay().getMetrics(metrics);
			heightMargin = (metrics.heightPixels/7);
			widthMargin = (metrics.widthPixels/4);
			ScalableLayout clockLayout = (ScalableLayout)findViewById(R.id.clock);
			FrameLayout.LayoutParams plControl = (FrameLayout.LayoutParams) clockLayout.getLayoutParams();
			plControl.bottomMargin = heightMargin;
			plControl.leftMargin = widthMargin;
			clockLayout.setLayoutParams(plControl);
		}else{
			DisplayMetrics metrics = new DisplayMetrics();
			WindowManager windowManager = (WindowManager) getApplicationContext()
				.getSystemService(Context.WINDOW_SERVICE);
			windowManager.getDefaultDisplay().getMetrics(metrics);
			heightMargin = (metrics.heightPixels/7);
			widthMargin = (metrics.widthPixels/100)*18;
			ScalableLayout clockLayout = (ScalableLayout)findViewById(R.id.clock);
			FrameLayout.LayoutParams plControl = (FrameLayout.LayoutParams) clockLayout.getLayoutParams();
			plControl.bottomMargin = heightMargin;
			plControl.leftMargin = widthMargin;
			clockLayout.setLayoutParams(plControl);
		}
		pm = (PowerManager)getSystemService(POWER_SERVICE);
		km = (KeyguardManager)getSystemService(KEYGUARD_SERVICE);
		keyLock = km.newKeyguardLock(KEYGUARD_SERVICE);
		mWindow = getWindow();
		moldLp = mWindow.getAttributes();
		mnewLp = mWindow.getAttributes();
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		currentApiVersion = android.os.Build.VERSION.SDK_INT;
		//Wallpaper Set
		ImageView wallpaperView = (ImageView)findViewById(R.id.wallpaper);
		switch(prefs.getInt("wallpaper",0)){
			case 0:
				wallpaperView.setImageDrawable(getResources().getDrawable(R.drawable.wallpaper_01));
				wallpaperView.setVisibility(View.INVISIBLE);
				break;
			case 1:
				wallpaperView.setVisibility(View.VISIBLE);
				wallpaperView.setImageDrawable(getResources().getDrawable(R.drawable.wallpaper_01));
				break;
			case 2:
				wallpaperView.setVisibility(View.VISIBLE);
				wallpaperView.setImageDrawable(getResources().getDrawable(R.drawable.wallpaper_02));
				break;
			case 3:
				wallpaperView.setVisibility(View.VISIBLE);
				wallpaperView.setImageDrawable(getResources().getDrawable(R.drawable.wallpaper_03));
				break;
			case 4:
				wallpaperView.setVisibility(View.VISIBLE);
				wallpaperView.setImageDrawable(getResources().getDrawable(R.drawable.wallpaper_04));
				break;
			case 5:
				wallpaperView.setVisibility(View.VISIBLE);
				wallpaperView.setImageDrawable(getResources().getDrawable(R.drawable.wallpaper_05));
				break;
		}
		//Knock On Set
		View knockButton = findViewById(R.id.knock);
		knockButton.setOnTouchListener(new OnTouchListener() {
				private GestureDetector gestureDetector = new GestureDetector(aod.this, new GestureDetector.SimpleOnGestureListener() {
						@Override
						public boolean onDoubleTap(MotionEvent e) {
							SharedPreferences prefs = getApplicationContext().getSharedPreferences("androesPrefName", MODE_PRIVATE);
							switch(prefs.getInt("dt2w",0)){
								case 1:
									sendBroadcast(new Intent("close_aod"));
									break;
								case 2:
							}
							return super.onDoubleTap(e);
						}
					});

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					gestureDetector.onTouchEvent(event);
					return true;
				}
			});
		//S8 Home Button Set
		ImageView homeButton = (ImageView)findViewById(R.id.home_button);
		final Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		switch(prefs.getInt("home_button",0)){
			case 1:
				homeButton.setVisibility(View.VISIBLE);
				homeButton.setOnTouchListener(new OnTouchListener() {
						private GestureDetector gestureDetector = new GestureDetector(aod.this, new GestureDetector.SimpleOnGestureListener() {
								@Override
								public void onLongPress(MotionEvent e) {
									vibrator.vibrate(30); 
									sendBroadcast(new Intent("close_aod"));
									return ;
								}
								@Override
								public boolean onSingleTapUp(MotionEvent e){
									vibrator.vibrate(30);
									return true;
								}
								@Override
								public boolean onDown(MotionEvent e){
									vibrator.vibrate(30);
									return true;
								}
							});
						@Override
						public boolean onTouch(View v, MotionEvent event) {
							gestureDetector.onTouchEvent(event);
							return true;
						}
					});
				break;
			case 2:
				homeButton.setVisibility(View.INVISIBLE);
				break;
		}
		//Hide Soft Key
		final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
			| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
			| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
			| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
		if(currentApiVersion >= Build.VERSION_CODES.KITKAT)
		{
			getWindow().getDecorView().setSystemUiVisibility(flags);
			final View decorView = getWindow().getDecorView();
			decorView
				.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener()
				{
					@Override
					public void onSystemUiVisibilityChange(int visibility)
					{
						if((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0)
						{
							decorView.setSystemUiVisibility(flags);
						}
					}
				});
		}
		//Auto Brightness Set
		if(prefs.getInt("autoBrightness",0)==1){
			try {
				defaultAutoBrightnessValue = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE);
			} catch (Settings.SettingNotFoundException e) {}
			try {
				Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, 1);
			} catch (RuntimeException e2) {}
		}else{
			float value = (prefs.getInt("brightness",20))/100f;
			mnewLp.screenBrightness = value;
			mWindow.setAttributes(mnewLp);
		}
	}
	
	public void exitAOD(){
		//MusicPlayer.destroy();
		//Stop Battery Handler
		mBatteryHandler.removeMessages(0);
		//Stop Clock Handler
		mClockHandler.removeMessages(0);
		//Stop BurnIn Handler
		mBurnInHandler.removeMessages(0);
		SharedPreferences prefs = getApplicationContext().getSharedPreferences("androesPrefName", MODE_PRIVATE);
		if(prefs.getInt("autoBrightness",0)==1){
			if(defaultAutoBrightnessValue == 1){
				try {
					Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, 1);
				} catch (RuntimeException e2) {}
			}else{
				try {
					Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, 0);
				} catch (RuntimeException e2) {}
			}
		}else{
			mWindow.setAttributes(moldLp);
		}
		try{
			unregisterReceiver(newNotificationBroadcast);
		} catch(IllegalArgumentException e){}
	}
	
	public int getRandomMath(int max, int offset) {
		int nResult = (int)(Math.random() * max) + offset;
		return nResult;
	}

	public static int getBatteryPercentage(Context context) {
		Intent batteryStatus = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
		int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
		float batteryPct = level / (float)scale;
		return (int)(batteryPct * 100);
	}
}
