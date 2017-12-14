package com.insighters.ash.note_maker.activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.insighters.ash.note_maker.NoteMaker.AlarmReceiver;
import com.insighters.ash.note_maker.NoteMaker.DBHelper;
import com.insighters.ash.note_maker.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class view_note extends AppCompatActivity {
    TextView title;
    TextView mainNote;
    TextView date;
    TextView priority;
    Long id_of_note;
    Activity activity;
    SharedPreferences settingsPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);
        //get references to the text view
        DBHelper dbHelper=new DBHelper(this);
        title=(TextView)findViewById(R.id.textView_title_count);
        mainNote=(TextView)findViewById(R.id.textView_main_text);
        priority=(TextView)findViewById(R.id.textView_priority);
        date=(TextView)findViewById(R.id.textView_view_date);
        //recieve intent

        Intent received=getIntent();
        Long x=received.getLongExtra("ID_TO_DISPLAY",2);
        //open database
        ArrayList<String>a1=dbHelper.getNote(x);


        title.setText(a1.get(0));
        mainNote.setText(a1.get(1));
        priority.setText(a1.get(2));
        date.setText(a1.get(3));
        id_of_note= Long.valueOf(a1.get(4));
        settingsPreferences=getSharedPreferences("Settings", Context.MODE_PRIVATE);

        activity=this;


        set_textview_background_color();
        set_background_color();
        set_text_color();

       ArrayList<Long> list_id=dbHelper.getObjectId();


        for(int i=0;i<list_id.size();i++)
        {
          Log.i("===viewnote/Objectid===", String.valueOf(list_id.get(i)));
        }//end for


    }
    private void set_text_color()
    {
        title.setTextColor(settingsPreferences.getInt("text_color", Color.BLACK));
        mainNote.setTextColor(settingsPreferences.getInt("text_color", Color.BLACK));
        priority.setTextColor(settingsPreferences.getInt("text_color", Color.BLACK));
        date.setTextColor(settingsPreferences.getInt("text_color", Color.BLACK));
    }//set_text_color


    private  void set_textview_background_color()
    {
        GradientDrawable bgDateSetter = (GradientDrawable)date.getBackground().mutate();
        bgDateSetter.setColor(settingsPreferences.getInt("text_background_color", Color.WHITE));
        bgDateSetter.invalidateSelf();

        GradientDrawable bgNoteSetter = (GradientDrawable)mainNote.getBackground().mutate();
        bgNoteSetter.setColor(settingsPreferences.getInt("text_background_color", Color.WHITE));
        bgNoteSetter.invalidateSelf();

        GradientDrawable bgPrioritySetter = (GradientDrawable)priority.getBackground().mutate();
        bgPrioritySetter.setColor(settingsPreferences.getInt("text_background_color", Color.WHITE));
        bgPrioritySetter.invalidateSelf();

        GradientDrawable bgTitle = (GradientDrawable)title.getBackground().mutate();
        bgTitle.setColor(settingsPreferences.getInt("text_background_color", Color.WHITE));
        bgTitle.invalidateSelf();
    }//end set_textview_background_color

    private void set_background_color()
    {
        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(settingsPreferences.getInt("background_color", Color.WHITE));
    }
/*
    public void delete_note_func(View v)
    {
        DBHelper dbHelper=new DBHelper(this);
        dbHelper.delete_note(id_of_note);
        finish();
    }
    public void edit_note_func(View v)
    {
       Intent intent_edit=new Intent(getApplicationContext(),add_new.class);
        intent_edit.putExtra("column_name",0);
        intent_edit.putExtra("colmun_id_to_edit",id_of_note);
        startActivity(intent_edit);

    }//end of edit*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_view_note, menu);

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_edit_note:
                Intent intent_edit=new Intent(this,add_new.class);
                intent_edit.putExtra("column_name",0);
                intent_edit.putExtra("colmun_id_to_edit",id_of_note);
                startActivityForResult(intent_edit, 1);
                return true;

            case R.id.action_delete_note:
                AlertDialog diaBox = AskOption();
                diaBox.show();
                return  true;

            case R.id.action_settings_1:
                Intent i=new Intent(getApplicationContext(),settings.class);
                i.putExtra("which_event",1);
                startActivity(i);
                return true;
            case R.id.action_add_remainder:
                setAlarm();

               // int baseLayout = isKeyguard ? R.layout.keyguard_widget_layout : R.layout.note_maker_widget;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }//end optionItemSelected

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
               title.setText(data.getStringExtra("edit_title"));
               mainNote.setText(data.getStringExtra("edit_data"));
                priority.setText(data.getStringExtra("edit_priority"));


            }
        }
    }//end OnActivityResult

    private AlertDialog AskOption()
    {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(this)
                //set message, title, and icon
                .setTitle("Delete")
                .setMessage("Do you want to Delete")
                .setIcon(R.mipmap.ic_delete_forever_black_24dp)

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code
                        DBHelper dbHelper=new DBHelper(getApplication());
                        dbHelper.delete_note(id_of_note);

                        dialog.dismiss();
                        finish();
                    }

                })



                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();
        return myQuittingDialogBox;

    }

    public void setAlarm()
    {
         final View dialogView = View.inflate(activity, R.layout.date_time_picker, null);
         final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();


       dialogView.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);
                TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.time_picker);

           int hour ;
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                     hour= timePicker.getHour();}
                else  hour=timePicker.getCurrentHour();

                int minute ;
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    minute= timePicker.getMinute();}
                else  minute=timePicker.getCurrentMinute();



             /*   Calendar cal = Calendar.getInstance();
                cal.set(datePicker.getYear(),
                        datePicker.getMonth(),
                       datePicker.getDayOfMonth(),
                       hour,
                       minute);*/


            //    timePicker.setCurrentHour(now.get(Calendar.HOUR_OF_DAY));
             //   timePicker.setCurrentMinute(now.get(Calendar.MINUTE));


                                Calendar cal = new GregorianCalendar(datePicker.getYear(),
                        datePicker.getMonth(),
                        datePicker.getDayOfMonth(),hour,minute);
                Long time = cal.getTimeInMillis();

                Log.i("===ViewNote/currtime", String.valueOf(Calendar.getInstance().getTimeInMillis()));
                Log.i("===ViewNote/time", String.valueOf(time));
                    if(time<=Calendar.getInstance().getTimeInMillis())
                    {
                        Toast.makeText(getApplicationContext(),"Invalid Time",Toast.LENGTH_SHORT).show();

                    }
                else {

                        Intent alarmIntent = new Intent(activity, AlarmReceiver.class);
                        alarmIntent.putExtra("Title",title.getText().toString());
                        alarmIntent.putExtra("Note",mainNote.getText().toString());
                        Toast.makeText(getApplicationContext(),"Notification Set",Toast.LENGTH_SHORT).show();

                        PendingIntent pendingIntent = PendingIntent.getBroadcast(activity, 0, alarmIntent, 0);
                        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
                    }
                alertDialog.dismiss();
            }});//end alarm setter
        alertDialog.setView(dialogView);
       alertDialog.show();


    }//end setAlarm

}//end of class
