package com.insighters.ash.note_maker.activities;

import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.insighters.ash.note_maker.BuildConfig;
import com.insighters.ash.note_maker.NoteMaker.Notes;
import com.insighters.ash.note_maker.R;

public class more_info extends AppCompatActivity {
    private AdView mAdView;

    InterstitialAd mInterstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);

        //======================insterstitial ad
        TextView textView_version=(TextView)findViewById(R.id.textView_version_value);
        //PackageInfo pInfo = BuildConfig.VERSION_NAME;
        String version = BuildConfig.VERSION_NAME;
        textView_version.setText(version);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        String deviceId = Notes.md5(androidId).toUpperCase();
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(deviceId).build();
        mInterstitialAd.loadAd(adRequest);
        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }
        });
    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }

        mAdView = (AdView) findViewById(R.id.adView2);
        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        String deviceId = Notes.md5(androidId).toUpperCase();
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(deviceId).build();
        mAdView.loadAd(adRequest);

    }

    private void loadInterstitialAd()
    {

        mInterstitialAd = new InterstitialAd(this);

        // set the ad unit ID
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));

        AdRequest adRequest = new AdRequest.Builder()
                .build();

        // Load ads into Interstitial Ads
        mInterstitialAd.loadAd(adRequest);

        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
            }
        });
    }//end of load ad


    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }


    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    public void onResume() {

        if (mAdView != null) {
            mAdView.resume();
        }
        super.onResume();
    }

    public void click_share(View v)
    {
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "NoteMaker");
            String sAux = "\nKeeping Notes Simplified\n\n";
            sAux = sAux + "https://play.google.com/store/apps/details?id=com.insighters.ash.note_maker&hl=en \n\n";
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(i, "choose one"));
        } catch(Exception e) {
    }
    }//end of click_share

    public void click_rate(View V)
    { String url_rate="https://play.google.com/store/apps/details?id=com.insighters.ash.note_maker&hl=en";

        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url_rate)));

        } catch(Exception e) {
        }
    }//click rate

}//end of class

