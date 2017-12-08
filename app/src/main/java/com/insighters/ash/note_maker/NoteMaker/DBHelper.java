package com.insighters.ash.note_maker.NoteMaker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.util.Log;
import android.util.LongSparseArray;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ash.note_maker on 10/7/16.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "notes_db";
    private static final String COLUMN_TITLE = "title";
    private static final String TABLE_NAME = "table_notes";
    private static final String COLUMN_DATA = "data";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_PRIORITY = "priority";
    private static final String COLUMN_COLOR = "color";
    private static final String COLUMN_TIME = "created_at";

    private static final String CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
            "( " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + ","
            + COLUMN_TITLE + " TEXT, "
            + COLUMN_DATA + " TEXT , "
            + COLUMN_DATE + " DATETIME NOT NULL DEFAULT(GETDATE()), "
            + COLUMN_PRIORITY + " INT DEFAULT 0, "
            + COLUMN_TIME + "DATETIME DEFAULT CURRENT_TIMESTAMP, "

            +COLUMN_COLOR+" INTEGER DEFAULT "+ Color.WHITE +");"
            ;

    Context ctx;
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        ctx=context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_QUERY);

    }

    public void clearNotes()
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DROP TABLE  IF EXISTS " + TABLE_NAME);
        this.onCreate (sqLiteDatabase);
        sqLiteDatabase.close ();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE " + TABLE_NAME + " IF EXISTS ");
        onCreate(sqLiteDatabase);
    }

    public boolean insertNote(String title, String data, String date, int priority) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TITLE, title);
        contentValues.put(COLUMN_DATA, data);
        contentValues.put(COLUMN_DATE, date);
        contentValues.put(COLUMN_PRIORITY, priority);
        long id= sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        Toast.makeText(ctx,"Note has been added",Toast.LENGTH_SHORT).show();
        Log.i("====id=== ", String.valueOf(id));
        return true;
    }//end insertNote

    public Map<String,Long> getByTitles() {
        Map<String,Long> titles = new HashMap();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor res1 = sqLiteDatabase.rawQuery("SELECT "+ COLUMN_TITLE+ " , "+COLUMN_ID +"  FROM " + TABLE_NAME + " ORDER BY " + COLUMN_TITLE+ " ASC ", null);
        res1.moveToFirst();
        while (res1.isAfterLast()==false)
        {
            Log.i("==ret", String.valueOf(res1.getLong(res1.getColumnIndex(COLUMN_ID))));
            titles.put(res1.getString(res1.getColumnIndex(COLUMN_TITLE)), res1.getLong(res1.getColumnIndex(COLUMN_ID)));
            res1.moveToNext();
        }
        sqLiteDatabase.close();
        return  titles;
    }


    public  Map<String,Long> getById() {
        Map<String,Long> titles = new HashMap();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor res1 = sqLiteDatabase.rawQuery("SELECT "+ COLUMN_TITLE+ " , "+COLUMN_ID +"  FROM " + TABLE_NAME + " ORDER BY " + COLUMN_ID+ " ASC ", null);
        res1.moveToFirst();
        while (res1.isAfterLast()==false)
        {
            titles.put(res1.getString(res1.getColumnIndex(COLUMN_TITLE)),  res1.getLong(res1.getColumnIndex(COLUMN_ID)));
            res1.moveToNext();
        }
        sqLiteDatabase.close();
        return  titles;
    }


    public  Map<String,Long> getByPriority() {
        Map<String,Long> titles = new HashMap();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor res1 = sqLiteDatabase.rawQuery("SELECT "+ COLUMN_TITLE+ " , "+COLUMN_ID +"  FROM " + TABLE_NAME + " ORDER BY " + COLUMN_PRIORITY+ " ASC ", null);
        res1.moveToFirst();
        while (res1.isAfterLast()==false)
        {
            titles.put(res1.getString(res1.getColumnIndex(COLUMN_TITLE)), res1.getLong(res1.getColumnIndex(COLUMN_ID)));
            res1.moveToNext();
        }
        sqLiteDatabase.close();
        return  titles;
    }



    public  Map<String,Long>getByDate() {
        Map<String,Long> titles = new HashMap();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor res1 = sqLiteDatabase.rawQuery("SELECT "+ COLUMN_TITLE+ " , "+COLUMN_ID +"  FROM " + TABLE_NAME + " ORDER BY " + COLUMN_DATE+ " ASC ", null);
        res1.moveToFirst();
        while (res1.isAfterLast()==false)
        {
            titles.put(res1.getString(res1.getColumnIndex(COLUMN_TITLE)),  res1.getLong(res1.getColumnIndex(COLUMN_ID)));
            res1.moveToNext();
        }
        sqLiteDatabase.close();

        return  titles;
    }

    public ArrayList<String> getNote(Long title_to_display)
    {
        ArrayList<String> titles = new ArrayList<String>();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        String query = "select * from " + TABLE_NAME + " where "+ COLUMN_ID + " = " + title_to_display + "";
        Cursor res1 = sqLiteDatabase.rawQuery(query, null);

        res1.moveToFirst();
            titles.add(res1.getString(res1.getColumnIndex(COLUMN_TITLE)));
            titles.add(res1.getString(res1.getColumnIndex(COLUMN_DATA)));
            titles.add(res1.getString(res1.getColumnIndex(COLUMN_PRIORITY)));

            titles.add(res1.getString(res1.getColumnIndex(COLUMN_DATE)));
              titles.add(String.valueOf(res1.getLong(res1.getColumnIndex(COLUMN_ID))));
sqLiteDatabase.close();
        return  titles;


    }//getNote
    public boolean delete_note(Long what_to_delete)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Log.i("===DBHelper/deleteid", String.valueOf(what_to_delete));
     sqLiteDatabase.execSQL("DELETE FROM  "+ TABLE_NAME+ " WHERE "+ COLUMN_ID +" = '"+what_to_delete+"'");
        sqLiteDatabase.close();
return true;
    }

    public int edit(Long id_to_edit, String d1, String d2, String d3, int d4) {

        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_TITLE, d1);
        contentValues.put(COLUMN_DATA, d2);
       // contentValues.put(COLUMN_DATE, d3);
        contentValues.put(COLUMN_PRIORITY, d4);
        Log.i("=====", String.valueOf(id_to_edit));
        return db.update(TABLE_NAME,contentValues, COLUMN_ID + " = '" + id_to_edit + "'", null);

    }

    public  ArrayList<Long> getObjectId()
    {
        ArrayList<Long> list_id = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
       String q= "SELECT _id  from "+ TABLE_NAME;

        Cursor res = sqLiteDatabase.rawQuery(q, null);

        res.moveToFirst();
        while (res.isAfterLast()==false)
        {
            list_id.add( res.getLong(res.getColumnIndex(COLUMN_ID)));
            res.moveToNext();
        }
        sqLiteDatabase.close();
        return  list_id;
    }
    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db,TABLE_NAME);
        return numRows;
    }

}//end of class
