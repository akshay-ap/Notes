package com.insighters.ash.note_maker.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.insighters.ash.note_maker.NoteMaker.DBHelper;
import com.insighters.ash.note_maker.NoteMaker.Notes;
import com.insighters.ash.note_maker.R;

import petrov.kristiyan.colorpicker.ColorPicker;


public class Settings extends AppCompatActivity {
    CheckBox checked;
    Context context;
    CheckBox vibrate;
    private AdView mAdView;
    private int type_confirm=0;
    SharedPreferences.Editor settingsEditor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        checked=(CheckBox)findViewById(R.id.checkBox_splash_screen);
        SharedPreferences sharedPreferences=getSharedPreferences("Settings", Context.MODE_PRIVATE);
        settingsEditor=sharedPreferences.edit();
        vibrate=(CheckBox)findViewById(R.id.checkBox_vibrate);
        context=getApplicationContext();

        mAdView = (AdView) findViewById(R.id.adView1);
        String androidId = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        String deviceId = Notes.md5(androidId).toUpperCase();
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(deviceId).build();
        mAdView.loadAd(adRequest);

        if(sharedPreferences.getInt("show_splash_screen",0)==1)
        {
            checked.setChecked(true);
        }
        else
        {
            checked.setChecked(false);
        }

        if(sharedPreferences.getInt("noteMaker_vibrate",0)==1)
        {
            vibrate.setChecked(true);
        }
        else
        {
           vibrate.setChecked(false);
        }
    }//end OnCreate

    public void click_clear_all_notes(View V)
    {    type_confirm=2;
        AlertDialog diaBox = AskOption();
        diaBox.show();

    }
    public void click_text_color(View V)
    {

        ColorPicker colorPicker = new ColorPicker(this);
        colorPicker.show();
        colorPicker.setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
            @Override
            public void onChooseColor(int position,int color) {
                // put code
                settingsEditor.putInt("text_color",color);
                settingsEditor.commit();
            }

            @Override
            public void onCancel(){
                // put code
            }
        });
    }



    public void click_text_background_color(View V)
    {


        ColorPicker colorPicker = new ColorPicker(this);
        colorPicker.show();
        colorPicker.setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
            @Override
            public void onChooseColor(int position,int color) {
                settingsEditor.putInt("text_background_color",color);
                settingsEditor.commit();
            }

            @Override
            public void onCancel(){
                // put code
            }
        });



    }

    public void click_background_color( View V)
    {


        ColorPicker colorPicker = new ColorPicker(this);
        colorPicker.show();
        colorPicker.setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
            @Override
            public void onChooseColor(int position,int color) {
                // put code
                settingsEditor.putInt("background_color",color);
                settingsEditor.commit();
            }

            @Override
            public void onCancel(){
                // put code
            }
        });



    }



   public void click_checkbox(View V)
    {

    if(checked.isChecked())
        {
        settingsEditor.putInt("show_splash_screen",1);
            Toast.makeText(context,"Splash screen will be shown",Toast.LENGTH_SHORT).show();

        }
        else
    {
        settingsEditor.putInt("show_splash_screen",0);
        Toast.makeText(context,"Splash screen will not be shown",Toast.LENGTH_SHORT).show();

    }
        settingsEditor.commit();

    }//end of function

    public void click_checkbox_vibrate(View V)
    {

        if(vibrate.isChecked())
        {
            settingsEditor.putInt("noteMaker_vibrate",1);
            Toast.makeText(context,"Vibration ON",Toast.LENGTH_SHORT).show();

        }
        else
        {
            settingsEditor.putInt("noteMaker_vibrate",0);
            Toast.makeText(context,"Vibration OFF",Toast.LENGTH_SHORT).show();

        }
        settingsEditor.commit();

    }//end of function
    public void click_reset(View V)
    { type_confirm=1;
        AlertDialog diaBox = AskOption();
        diaBox.show();


    }//end click_reset
    private AlertDialog AskOption()
    {
       String type_settings="Do you want to delete all Settings?";
        String type_notes="Do you want to delete all notes?";
String str="";
        if(type_confirm==1)
            str=type_settings;
        else if(type_confirm==2)str=type_notes;

        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(this)
                //set message, title, and icon
                .setTitle("Delete")
                .setMessage(str)
                .setIcon(R.mipmap.ic_delete_forever_black_24dp)

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                       if(type_confirm==1){
                           //reset Settings
                       reset_settings();

                       }
                        else if(type_confirm==2)
                       {//delete all notes
                        clear_notes();

                       }
                    }

                })



                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();
        return myQuittingDialogBox;

    }//end of dailog altert

    private void reset_settings()
    {
        settingsEditor.putInt("noteMaker_vibrate",0);
        settingsEditor.putInt("background_color", Color.WHITE);
        settingsEditor.putInt("text_background_color",Color.WHITE);
        settingsEditor.putInt("text_color",Color.BLACK);
        settingsEditor.putInt("show_splash_screen",1);

        settingsEditor.commit();
        Toast.makeText(context,"Settings have been reset",Toast.LENGTH_SHORT).show();
    }

    private void clear_notes()
    {
        DBHelper db;
        db=new DBHelper(this);
        db.clearNotes();
        db.close();
        Toast.makeText(context,"All notes deleted",Toast.LENGTH_SHORT).show();
    }
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

}//end of class Settings
