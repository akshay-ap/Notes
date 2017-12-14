package com.insighters.ash.note_maker.activities;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.insighters.ash.note_maker.NoteMaker.DBHelper;
import com.insighters.ash.note_maker.NoteMaker.Notes;
import com.insighters.ash.note_maker.R;

import java.util.ArrayList;
import java.util.Map;

public class show_notes extends AppCompatActivity {
    DBHelper db;
    ListView listView;
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_notes);
      listView  = (ListView) findViewById(R.id.listview_showlist);

        mAdView = (AdView) findViewById(R.id.adView4);
        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        String deviceId = Notes.md5(androidId).toUpperCase();
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(deviceId).build();
        mAdView.loadAd(adRequest);

        populate_list();
    }//end of on create

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_show_notes, menu);

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                Intent i=new Intent(getApplicationContext(),settings.class);
                i.putExtra("which_event",1);

                startActivity(i);
                return true;

            case R.id.action_add_note:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Intent i2=new Intent(getApplicationContext(),add_new.class);
                i2.putExtra("which_event",2);
                startActivity(i2);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
public void populate_list()

{
    //open the database
    db=new DBHelper(this);
    //get the titles
    final Map<String,Long> note_titles_map= db.getByTitles();
    final ArrayList<String> note_titles= new ArrayList<String>( note_titles_map.keySet());
    ArrayList<Long> id=new ArrayList<>(note_titles_map.values());
  //  declare the adapter for the listview
    ArrayAdapter arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,note_titles);
    listView.setAdapter(arrayAdapter);
    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent=new Intent(getApplicationContext(),view_note.class);
       // String sending=(String)listView.getItemAtPosition(i);
        Long sending =note_titles_map.get(note_titles.get(i));
        Log.i("===pos===", String.valueOf(note_titles_map.get(note_titles.get(i))));
        intent.putExtra("ID_TO_DISPLAY",sending);
        startActivity(intent);
    }
});


}//populatelist
    @Override
    protected void onResume() {
        super.onResume();
    populate_list();

        if (mAdView != null) {
            mAdView.resume();
        }
        // Normal case behavior follows
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



}//end class
