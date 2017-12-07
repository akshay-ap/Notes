package com.insighters.ash.note_maker.NoteMaker;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.insighters.ash.note_maker.R;

/**
 * Created by row_hammer on 15/12/16.
 */

public class AlarmReceiver extends BroadcastReceiver {
    String title;
    String note;
    Context context;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context=context;
        Toast.makeText(context, "NoteMaker Notification", Toast.LENGTH_SHORT).show();
       title=  intent.getStringExtra("Title");
      note=intent.getStringExtra("Note");
        SharedPreferences sharedPreferences=context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        if((sharedPreferences.contains("noteMaker_vibrate")==false))
        {
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putInt("noteMaker_vibrate",0);
            editor.commit();
        };
        if(sharedPreferences.getInt("noteMaker_vibrate",-1)>0) {
            Vibrator vibrator = (Vibrator) context
                    .getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(2000);
        }
        notifyme();


        //   vibrator.vibrate(1000);
    }//end of onreceive

    public void notifyme()
    {   NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(note);
        Intent notificationIntent = new Intent(context, swipeListShowNotes.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, mBuilder.build());
    }

}
