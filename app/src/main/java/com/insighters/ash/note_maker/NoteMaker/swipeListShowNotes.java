package com.insighters.ash.note_maker.NoteMaker;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.insighters.ash.note_maker.R;
import com.insighters.ash.note_maker.activities.add_new;
import com.insighters.ash.note_maker.activities.settings;
import com.insighters.ash.note_maker.activities.view_note;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class swipeListShowNotes extends AppCompatActivity {
    private DBHelper db;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> titleList;
    private TextView totalClassmates;
    private SwipeLayout swipeLayout;
    private Map<String,Long> note_titles_map;
    ArrayList<Long> idList;
    private AdView mAdView;
    LayoutInflater inflater ;
    private final static String TAG = swipeListShowNotes.class.getSimpleName();
    private ArrayList<String> note_titles;//= new ArrayList<String>( note_titles_map.keySet());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_list_show_notes);
        listView = (ListView)findViewById(R.id.list_item);

        mAdView = (AdView) findViewById(R.id.adView5);
        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        String deviceId = Notes.md5(androidId).toUpperCase();
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(deviceId).build();
        mAdView.loadAd(adRequest);

        getDataFromDatabase();
        setListViewHeader();
        setListViewAdapter();
    }


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



    @Override
    protected void onResume() {
        getDataFromDatabase();
        adapter.clear();

        adapter = new ListViewAdapter(this, R.layout.item_listview,note_titles, idList);
        listView.setAdapter(adapter);

        //  listView.setAdapter(adapter);
       // updateAdapter();



        if (mAdView != null) {
            mAdView.resume();
        }
        // Normal case behavior follows
        super.onResume();
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

    private void getDataFromDatabase() {
        db=new DBHelper(this);
        //get the titles
        note_titles_map=new HashMap<>();
        note_titles_map= db.getByTitles();
        note_titles= new ArrayList<String>( note_titles_map.keySet());

        Log.i("dbsize===", String.valueOf(note_titles_map.size()));
        titleList= new ArrayList<String>( note_titles_map.keySet());
        idList=new ArrayList<>(note_titles_map.values());
        db.close();
    }

    private void setListViewHeader() {

       inflater = getLayoutInflater();
        View header = inflater.inflate(R.layout.header_listview, listView, false);
      //  totalClassmates = (TextView) header.findViewById(R.id.total);

        swipeLayout = (SwipeLayout)header.findViewById(R.id.swipe_layout);
        setSwipeViewFeatures();
        //listView.addHeaderView(header);
    }

    private void setSwipeViewFeatures() {
        //set show mode.
        swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

        //add drag edge.(If the BottomView has 'layout_gravity' attribute, this line is unnecessary)
        swipeLayout.addDrag(SwipeLayout.DragEdge.Left, findViewById(R.id.bottom_wrapper));

        swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onClose(SwipeLayout layout) {
                Log.i(TAG, "onClose");
            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                Log.i(TAG, "on swiping");
            }

            @Override
            public void onStartOpen(SwipeLayout layout) {
                Log.i(TAG, "on start open");
            }

            @Override
            public void onOpen(SwipeLayout layout) {
                Log.i(TAG, "the BottomView totally show");
            }

            @Override
            public void onStartClose(SwipeLayout layout) {
                Log.i(TAG, "the BottomView totally close");
            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                //when user's hand released.
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(getApplicationContext(),view_note.class);
                // String sending=(String)listView.getItemAtPosition(i);
                Long sending =note_titles_map.get(titleList.get(i));
                Log.i("===pos===", String.valueOf(note_titles_map.get(titleList.get(i))));
                intent.putExtra("ID_TO_DISPLAY",sending);
                startActivity(intent);
            }
        });
    }

    private void setListViewAdapter() {

        adapter = new ListViewAdapter(this, R.layout.item_listview,note_titles, idList);
        listView.setAdapter(adapter);

     //   totalClassmates.setText("(" + titleList.size() + ")");
    }

    public void updateAdapter() {
        adapter.notifyDataSetChanged(); //update adapter
      //  totalClassmates.setText("(" + titleList.size() + ")"); //update total friends in list
    }




}
