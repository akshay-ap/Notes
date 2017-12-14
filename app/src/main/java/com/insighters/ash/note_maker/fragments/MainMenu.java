package com.insighters.ash.note_maker.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.insighters.ash.note_maker.NoteMaker.Notes;
import com.insighters.ash.note_maker.activities.AddNew;
import com.insighters.ash.note_maker.activities.Settings;
import com.insighters.ash.note_maker.activities.ViewNotesCard;
import com.insighters.ash.note_maker.activities.MoreInfo;
import com.insighters.ash.note_maker.R;


public class MainMenu extends Fragment implements View.OnClickListener{
    Context ctx;
    View V;
    Notes notes;
    public AdView mAdView;

    Button b1,b2,b3,b4;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notes= Notes.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        V=inflater.inflate(R.layout.fragment_main_menu,container,false);
        b1=(Button)V.findViewById(R.id.button_view_notes);
        b1.setOnClickListener(this);
        b2=(Button)V.findViewById(R.id.button_settings);
        b2.setOnClickListener(this);
        b3=(Button)V.findViewById(R.id.button_add_note);
        b3.setOnClickListener(this);
        b4=(Button)V.findViewById(R.id.button_information);
        b4.setOnClickListener(this);


        String androidId = android.provider.Settings.Secure.getString(getActivity().getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        String deviceId = Notes.md5(androidId).toUpperCase();
        mAdView = (AdView)V.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(deviceId).build();
        mAdView.loadAd(adRequest);
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor settingsEditor=sharedPreferences.edit();
        if(!sharedPreferences.contains("text_background_color"))
        {
        settingsEditor.putInt("text_background_color",Color.WHITE);
        }
        if(sharedPreferences.contains("background_color"))
        {
            settingsEditor.putInt("background_color",Color.WHITE);

        }
        if(sharedPreferences.contains("text_color"))
        {
            settingsEditor.putInt("text_color",Color.BLACK);

        }

        settingsEditor.commit();
        return V;
       }

    @Override
    public void onResume() {

        if (mAdView != null) {
            mAdView.resume();
        }
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_settings:
                Intent i=new Intent(getActivity(),Settings.class);
                i.putExtra("which_event",1);

                startActivity(i);
                break;
            case R.id.button_add_note:
                Intent i2=new Intent(getActivity(),AddNew.class);
                i2.putExtra("which_event",2);
                startActivity(i2);
                break;
            case R.id.button_information:
                Intent i3=new Intent(getActivity(),MoreInfo.class);
                i3.putExtra("which_event",3);
                startActivity(i3);
                break;
            case R.id.button_view_notes:
                Intent i4=new Intent(getActivity(),ViewNotesCard.class);
                i4.putExtra("which_event",4);
                startActivity(i4);
                break;


        }
    }//on click
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




}