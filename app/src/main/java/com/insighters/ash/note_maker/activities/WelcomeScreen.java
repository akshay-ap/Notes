package com.insighters.ash.note_maker.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.insighters.ash.note_maker.fragments.MainMenu;
import com.insighters.ash.note_maker.R;
import com.insighters.ash.note_maker.fragments.SplashScreen;

public class WelcomeScreen extends AppCompatActivity{

    CountDownTimer countDownTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences=getSharedPreferences("Settings", Context.MODE_PRIVATE);

if(sharedPreferences.contains("show_splash_screen")==true)
{
int i=sharedPreferences.getInt("show_splash_screen",8);
    if(i==1)
    {    android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame_layout,new SplashScreen());
        ft.commit();
        countDownTimer=new CountDownTimer(1500, 1000) {
            @Override
            public void onTick(long l) {
            }

            @Override
            public void onFinish() {

                android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.fade_in,R.anim.fade_out);
                ft.replace(R.id.frame_layout, new MainMenu());

                ft.commit();
                transaction.commit();

            }
        };

        countDownTimer.start();
        setContentView(R.layout.activity_welcome_screen);

    }
    else
    { android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        ft.replace(R.id.frame_layout, new MainMenu());

        ft.commit();
        transaction.commit();
        setContentView(R.layout.activity_welcome_screen);

    }
}
else
{
    SharedPreferences.Editor editor=sharedPreferences.edit();
    editor.putInt("show_splash_screen",1);
    editor.commit();
    android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

    ft.replace(R.id.frame_layout, new MainMenu());

    ft.commit();
    transaction.commit();
    setContentView(R.layout.activity_welcome_screen);

}




    }


}
