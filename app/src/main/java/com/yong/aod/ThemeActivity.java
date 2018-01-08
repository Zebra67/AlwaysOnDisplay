package com.yong.aod;
import android.app.*;
import android.os.*;
import android.widget.*;
import android.content.*;
import android.view.*;
import android.support.v7.app.*;

public class ThemeActivity extends AppCompatActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_theme);
		SharedPreferences prefs = getApplicationContext().getSharedPreferences("androesPrefName", MODE_PRIVATE);
		RadioGroup radioGroup = (RadioGroup)findViewById(R.id.style);
		RadioGroup wallRadioGroup = (RadioGroup)findViewById(R.id.wallpaper);
		switch(prefs.getInt("setting",1)){
			case 1:
				radioGroup.check(R.id.g5clock);
				break;
			case 2:
				radioGroup.check(R.id.s7clock);
				break;
			case 3:
				radioGroup.check(R.id.s7calendar);
				break;
			case 4:
				radioGroup.check(R.id.analog);
				break;
			case 5:
				radioGroup.check(R.id.s8clock);
				break;
			case 6:
				radioGroup.check(R.id.s8VerticalClock);
		}
		switch(prefs.getInt("wallpaper",0)){
			case 0:
				wallRadioGroup.check(R.id.wallNone);
				break;
			case 1:
				wallRadioGroup.check(R.id.wall1);
				break;
			case 2:
				wallRadioGroup.check(R.id.wall2);
				break;
			case 3:
				wallRadioGroup.check(R.id.wall3);
				break;
			case 4:
				wallRadioGroup.check(R.id.wall4);
				break;
			case 5:
				wallRadioGroup.check(R.id.wall5);
		}
	}
	
	public void wallNone(View v){
		SharedPreferences prefs = getApplicationContext().getSharedPreferences("androesPrefName", MODE_PRIVATE);
		SharedPreferences.Editor ed = prefs.edit();
		ed.remove("wallpaper");
		ed.commit();
		// WRITE
		ed.putInt("wallpaper", 0);
		ed.commit();
	}

	public void wall1(View v){
		SharedPreferences prefs = getApplicationContext().getSharedPreferences("androesPrefName", MODE_PRIVATE);
		SharedPreferences.Editor ed = prefs.edit();
		ed.remove("wallpaper");
		ed.commit();
		// WRITE
		ed.putInt("wallpaper", 1);
		ed.commit();
	}

	public void wall2(View v){
		SharedPreferences prefs = getApplicationContext().getSharedPreferences("androesPrefName", MODE_PRIVATE);
		SharedPreferences.Editor ed = prefs.edit();
		ed.remove("wallpaper");
		ed.commit();
		// WRITE
		ed.putInt("wallpaper", 2);
		ed.commit();
	}

	public void wall3(View v){
		SharedPreferences prefs = getApplicationContext().getSharedPreferences("androesPrefName", MODE_PRIVATE);
		SharedPreferences.Editor ed = prefs.edit();
		ed.remove("wallpaper");
		ed.commit();
		// WRITE
		ed.putInt("wallpaper", 3);
		ed.commit();
	}

	public void wall4(View v){
		SharedPreferences prefs = getApplicationContext().getSharedPreferences("androesPrefName", MODE_PRIVATE);
		SharedPreferences.Editor ed = prefs.edit();
		ed.remove("wallpaper");
		ed.commit();
		// WRITE
		ed.putInt("wallpaper", 4);
		ed.commit();
	}

	public void wall5(View v){
		SharedPreferences prefs = getApplicationContext().getSharedPreferences("androesPrefName", MODE_PRIVATE);
		SharedPreferences.Editor ed = prefs.edit();
		ed.remove("wallpaper");
		ed.commit();
		// WRITE
		ed.putInt("wallpaper", 5);
		ed.commit();
	}

	public void g5clock(View v){
		SharedPreferences prefs = getApplicationContext().getSharedPreferences("androesPrefName", MODE_PRIVATE);
		SharedPreferences.Editor ed = prefs.edit();
		ed.remove("setting");
		ed.commit();
		// WRITE
		ed.putInt("setting", 1);
		ed.commit();
	}

	public void s7clock(View v){
		SharedPreferences prefs = getApplicationContext().getSharedPreferences("androesPrefName", MODE_PRIVATE);
		SharedPreferences.Editor ed = prefs.edit();
		ed.remove("setting");
		ed.commit();
		// WRITE
		ed.putInt("setting", 2);
		ed.commit();
	}

	public void analog(View v){
		SharedPreferences prefs = getApplicationContext().getSharedPreferences("androesPrefName", MODE_PRIVATE);
		SharedPreferences.Editor ed = prefs.edit();
		ed.remove("setting");
		ed.commit();
		// WRITE
		ed.putInt("setting", 4);
		ed.commit();
	}

	public void s7calendar(View v){
		SharedPreferences prefs = getApplicationContext().getSharedPreferences("androesPrefName", MODE_PRIVATE);
		SharedPreferences.Editor ed = prefs.edit();
		ed.remove("setting");
		ed.commit();
		// WRITE
		ed.putInt("setting", 3);
		ed.commit();
	}

	public void s8clock(View v){
		SharedPreferences prefs = getApplicationContext().getSharedPreferences("androesPrefName", MODE_PRIVATE);
		SharedPreferences.Editor ed = prefs.edit();
		ed.remove("setting");
		ed.commit();
		// WRITE
		ed.putInt("setting", 5);
		ed.commit();
	}

	public void s8VerticalClock(View v){
		SharedPreferences prefs = getApplicationContext().getSharedPreferences("androesPrefName", MODE_PRIVATE);
		SharedPreferences.Editor ed = prefs.edit();
		ed.remove("setting");
		ed.commit();
		// WRITE
		ed.putInt("setting", 6);
		ed.commit();
	}
}
