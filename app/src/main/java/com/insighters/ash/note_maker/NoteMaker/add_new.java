package com.insighters.ash.note_maker.NoteMaker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.insighters.ash.note_maker.R;

import java.util.ArrayList;
import java.util.Calendar;
//Need more changes
public class add_new extends AppCompatActivity {
    int ssss;
    Long edit_id;
    DBHelper db;
    //TextView date_setter;
    EditText mainNote;
    EditText title;
    String  actionBarDate;
    SharedPreferences settingsPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.grid);
        mainNote=(EditText)findViewById(R.id.editText_data);
        title=(EditText)findViewById(R.id.editText_title);

        title.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    Toast.makeText(getApplicationContext(),"a",Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });


        settingsPreferences= getSharedPreferences("Settings", Context.MODE_PRIVATE);

        setUI();

        setCharacterCountForNote();
        setCharacterCountForTitle();

        ssss=getIntent().getIntExtra("column_name",1);
        edit_id=getIntent().getLongExtra("colmun_id_to_edit",-1);
        db=new DBHelper(this);

        if(ssss==0&&edit_id!=-1)
        {
            ArrayList<String> setdata=db.getNote(edit_id);
            //editing current note
            mainNote.setText(setdata.get(1));
            title.setText(setdata.get(0));
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_add_note, menu);
        TextView tv = new TextView(this);
        tv.setText(actionBarDate);
        tv.setPadding(5, 0, 5, 0);
        tv.setTypeface(null, Typeface.BOLD);
        tv.setTextSize(8);
       // tv.getId();
        menu.add(0, tv.getId(), 1, "00/00/2016").setActionView(tv).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_save_note:

             function_save(  null);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void setUI()
    {

        GradientDrawable bgMainNote = (GradientDrawable)mainNote.getBackground();
        bgMainNote.setColor(settingsPreferences.getInt("text_background_color", Color.WHITE));

        GradientDrawable bgTitle = (GradientDrawable)title.getBackground();
        bgTitle.setColor(settingsPreferences.getInt("text_background_color", Color.WHITE));
        //date_setter.setBackgroundResource(R.drawable.button_main_menu);

        //GradientDrawable sd = (GradientDrawable) date_setter.getBackground().mutate();
        //sd.setColor(settingsPreferences.getInt("text_background_color", Color.WHITE));
      //  sd.invalidateSelf();

        mainNote.setTextColor(settingsPreferences.getInt("text_color",Color.BLACK));
        title.setTextColor(settingsPreferences.getInt("text_color",Color.BLACK));
        //date_setter.setTextColor(settingsPreferences.getInt("text_color", Color.BLACK));

        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(settingsPreferences.getInt("background_color", Color.WHITE));


    }
    private String getDate()
    {
        Calendar c = Calendar.getInstance();
        String minutes = String.valueOf(c.get(Calendar.MINUTE));
        String hr = String.valueOf(c.get(Calendar.HOUR));
        String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        String month = String.valueOf(c.get(Calendar.MONTH));
        String year = String.valueOf(c.get(Calendar.YEAR));
        String date=day+"/"+month+"/"+year+" "+hr+":"+minutes;//+":"+seconds;
        actionBarDate=day+"/"+month+"/"+year;
        return date;
    }

public void function_save(View v)
{
    db=new DBHelper(this);
    EditText e1=(EditText)findViewById(R.id.editText_title);
    EditText e2=(EditText)findViewById(R.id.editText_data);
    String d1,d2,d3,d4;
    d1=e1.getText().toString();
    d2=e2.getText().toString();
    d3=getDate();

    d4="0";

    if(d1.isEmpty()||d2.isEmpty()||d4.isEmpty())
    {
        Toast.makeText(getBaseContext(),"Please fill all the data",Toast.LENGTH_SHORT).show();
    }//if not valid then do not insert
    else {
        if (ssss == 1) {
            db.insertNote(d1, d2, d3 , Integer.parseInt(d4));
                      finish();
        } else {
            db.edit(edit_id, d1, d2, d3, Integer.parseInt(d4));
            Intent intent = new Intent();
            intent.putExtra("edit_title",d1);
            intent.putExtra("edit_data",d2);
            intent.putExtra("edit_priority",d4);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
    db.close();
}//end function save

    @Override
    protected void onResume() {
        setUI();
        super.onResume();
    }

    private void setCharacterCountForTitle()
    {
       final   TextView mTextView;
       final   EditText mEditText;

        mTextView=(TextView)findViewById(R.id.textView_title_count);
        mEditText=(EditText)findViewById(R.id.editText_title);
        final TextWatcher mTextEditorWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //This sets a textview to the current length
                mTextView.setText(String.valueOf(s.length()));
            }

            public void afterTextChanged(Editable s) {
            }
        };
        mEditText.addTextChangedListener(mTextEditorWatcher);
    }


    private void setCharacterCountForNote()
    {
        final TextView mTextView;
        final EditText mEditText;
        mTextView=(TextView)findViewById(R.id.textView_note_count);
        mEditText=(EditText)findViewById(R.id.editText_data);

        final TextWatcher mTextEditorWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //This sets a textview to the current length
                mTextView.setText(String.valueOf(s.length()));
            }

            public void afterTextChanged(Editable s) {
            }
        };
        mEditText.addTextChangedListener(mTextEditorWatcher);
    }

}//end class
