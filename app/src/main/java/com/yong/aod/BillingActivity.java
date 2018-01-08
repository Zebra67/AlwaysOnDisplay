package com.yong.aod;

import android.app.*;
import android.content.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import com.anjlab.android.iab.v3.*;
import android.support.v7.app.*;

public class BillingActivity extends AppCompatActivity
{
	private static final String PRODUCT_ID1 = "donate_1000";
	private static final String PRODUCT_ID2 = "donate_5000";
	private static final String PRODUCT_ID3 = "donate_10000";
	private static final String PRODUCT_ID4 = "donate_50000";
	private static final String PRODUCT_ID5 = "ad_remove";
    private static final String LICENSE_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjr0/OwBm0O89qNZnwQEH2J/2Aey52u/a3v8L9jVglvRW1ncbxzWODcLavNH8qXX8K1Ty73GAokIOyLdGAU4vpRnOpFZUPjvwjxt5X60Jb6IQeepfHDhAbwW/z9+Y2HPeZEapZN9uCM2PTQgG+JpuZhhQnBSH7AfA1VHdgVEn8Qkm4jOck3cC6tkZCsRcFypw5QX9CpEQmyXim7mb+ystU3InbuBSG7KB0Mq9hI0mEeU7Pnrw//WJTwMOqDROiRl8c/uGoQ43fzMQDl8sVtAkyygYxU3ToHSS5+N1rQ7ZNs8+Qzmpm41wZyoyo1RKZ+3afbl5MGITVpB4PzR/42sHvwIDAQAB";
	private static final String MERCHANT_ID= "8501-6440-5276";
	private static final String LOG_TAG = "yong_aod";
	private BillingProcessor bp;
	private boolean readyToPurchase = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_billing);
		final Button donateButton1 = (Button)findViewById(R.id.donate1);
		final Button donateButton2 = (Button)findViewById(R.id.donate2);
		final Button donateButton3 = (Button)findViewById(R.id.donate3);
		final Button donateButton4 = (Button)findViewById(R.id.donate4);
		final Button ad_removeButton = (Button)findViewById(R.id.ad_remove);
		donateButton1.setEnabled(false);
		donateButton1.setText(getString(R.string.buy_wait));
		donateButton2.setEnabled(false);
		donateButton2.setText(getString(R.string.buy_wait));
		donateButton3.setEnabled(false);
		donateButton3.setText(getString(R.string.buy_wait));
		donateButton4.setEnabled(false);
		donateButton4.setText(getString(R.string.buy_wait));
		ad_removeButton.setEnabled(false);
		ad_removeButton.setText(getString(R.string.buy_wait));
		if(!BillingProcessor.isIabServiceAvailable(this)) {
            showToast("In-app billing service is unavailable, please upgrade Android Market/Play to version >= 3.9.16");
        }

        bp = new BillingProcessor(this, LICENSE_KEY, MERCHANT_ID, new BillingProcessor.IBillingHandler() {
				@Override
				public void onProductPurchased(String productId, TransactionDetails details) {
					switch(productId){
						case "ad_remove":
							ad_removeButton.setEnabled(false);
							ad_removeButton.setText(getString(R.string.already_purchased));
							SharedPreferences prefs = getApplicationContext().getSharedPreferences("androesPrefName", MODE_PRIVATE);
							SharedPreferences.Editor ed = prefs.edit();
							ed.remove("ad_removed");
							ed.putBoolean("ad_removed", true);
							ed.commit();
							break;
						case "donate_1000":
							donateButton1.setEnabled(false);
							donateButton1.setText(getString(R.string.already_purchased));
							break;
						case "donate_5000":
							donateButton2.setEnabled(false);
							donateButton2.setText(getString(R.string.already_purchased));
							break;
						case "donate_10000":
							donateButton3.setEnabled(false);
							donateButton3.setText(getString(R.string.already_purchased));
							break;
						case "donate_50000":
							donateButton4.setEnabled(false);
							donateButton4.setText(getString(R.string.already_purchased));
							break;
					}
				}
				@Override
				public void onBillingError(int errorCode, Throwable error) {
					showToast("Error: " + Integer.toString(errorCode));
				}
				@Override
				public void onBillingInitialized() {
					readyToPurchase = true;
					donateButton1.setEnabled(true);
					donateButton1.setText(getString(R.string.buy_ready));
					donateButton2.setEnabled(true);
					donateButton2.setText(getString(R.string.buy_ready));
					donateButton3.setEnabled(true);
					donateButton3.setText(getString(R.string.buy_ready));
					donateButton4.setEnabled(true);
					donateButton4.setText(getString(R.string.buy_ready));
					ad_removeButton.setEnabled(true);
					ad_removeButton.setText(getString(R.string.buy_ready));
				}
				@Override
				public void onPurchaseHistoryRestored() {
					for(String sku : bp.listOwnedProducts()){
						Log.d(LOG_TAG, "Owned Managed Product: " + sku);
						if(sku=="ad_remove"){
							
							SharedPreferences prefs = getApplicationContext().getSharedPreferences("androesPrefName", MODE_PRIVATE);
							SharedPreferences.Editor ed = prefs.edit();
							ed.remove("ad_removed");
							ed.putBoolean("ad_removed", true);
							ed.commit();
						}
						}
					for(String sku : bp.listOwnedSubscriptions())
						Log.d(LOG_TAG, "Owned Subscription: " + sku);
				}
			});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
    public void onDestroy() {
        if (bp != null)
            bp.release();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
	
	public void donate1(View v){
		if (!readyToPurchase) {
            showToast("Billing not initialized.");
            return;
        }
		bp.purchase(this,PRODUCT_ID1);
	}
	
	public void donate2(View v){
		if (!readyToPurchase) {
            showToast("Billing not initialized.");
            return;
        }
		bp.purchase(this,PRODUCT_ID2);
	}
	
	public void donate3(View v){
		if (!readyToPurchase) {
            showToast("Billing not initialized.");
            return;
        }
		bp.purchase(this,PRODUCT_ID3);
	}
	
	public void donate4(View v){
		if (!readyToPurchase) {
            showToast("Billing not initialized.");
            return;
        }
		bp.purchase(this,PRODUCT_ID4);
	}
	
	public void ad_remove(View v){
		if (!readyToPurchase) {
            showToast("Billing not initialized.");
            return;
        }
		bp.purchase(this,PRODUCT_ID5);
	}
}	
