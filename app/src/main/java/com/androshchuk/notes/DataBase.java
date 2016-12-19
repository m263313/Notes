package com.androshchuk.notes;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import  android.database.sqlite.*;
import android.util.Log;

import java.util.Calendar;


/**
 * Created by Androshchuk on 15-Dec-16.
 */

public class DataBase  {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "NotesDataBase.db";
    public static final String TABLE_NOTES = "notes";

//
    public static final String KEY_NOTES_ID = "id";
    public static final String KEY_NOTES_TITLE="notes_title";
    public static final String KEY_NOTES_TEXT="notes_text";
    public static final String KEY_NOTES_DATE="notes_date";
    public static final String KEY_NOTES_THEME="notes_theme";

    final String LOG_TAG = "myLogs";
    private DBHelper dbHelper;
    private SQLiteDatabase DB;
    private  Context mCtx;
    private static final String CREATE_NOTES_TABLE =
            "CREATE TABLE " + TABLE_NOTES + " (" +
                    KEY_NOTES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_NOTES_TITLE + " TEXT," +
                    KEY_NOTES_TEXT+ " TEXT,"+
                    KEY_NOTES_DATE+ " TEXT,"+
                    KEY_NOTES_THEME+ " TEXT"+
                    ");";
    public DataBase(Context ctx) {
        Log.d(LOG_TAG,"DataBase constructor");
        mCtx = ctx;
    }
    public void open() {
        Log.d(LOG_TAG,"Open connection");
        dbHelper = new DBHelper(mCtx, DATABASE_NAME, null, DATABASE_VERSION);
        DB = dbHelper.getWritableDatabase();
    }
    // закрыть подключение
    public void close() {
        Log.d(LOG_TAG,"Close connection");
        if (dbHelper!=null) dbHelper.close();
    }

    public Cursor getAllData() {
        Log.d(LOG_TAG,"Get data from base");
        return DB.query(TABLE_NOTES, null, null, null, null, null, null);
    }

    // добавить запись в DB_TABLE
    public void addRec(String title, String text) {
        Log.d(LOG_TAG,"Add record to base");
        ContentValues cv = new ContentValues();
        cv.put(KEY_NOTES_TITLE, title);
        cv.put(KEY_NOTES_TEXT, text);
        cv.put(KEY_NOTES_DATE, Calendar.getInstance().toString());
        cv.put(KEY_NOTES_THEME,"Nothing");
        DB.insert(TABLE_NOTES, null, cv);
    }
    public  void updateRec(int id, String title,String text){
        ContentValues cv = new ContentValues();
        //cv.put(KEY_NOTES_ID,id);
        cv.put(KEY_NOTES_TITLE, title);
        cv.put(KEY_NOTES_TEXT, text);
        cv.put(KEY_NOTES_DATE, Calendar.getInstance().toString());
        cv.put(KEY_NOTES_THEME,"Nothing");
        DB.update(TABLE_NOTES,cv,KEY_NOTES_ID+" = "+id,null);
    }
    // удалить запись из DB_TABLE
    public void delRec(long id) {
        DB.delete(TABLE_NOTES, KEY_NOTES_ID + " = " + id, null);
    }
    public Cursor getRec(long id){
        //)true, Table, from, where, null, null, null, null, null)
      return   DB.query(TABLE_NOTES,null,KEY_NOTES_ID+" = "+id,null,null,null,null);
    }
    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                        int version) {
            super(context, name, factory, version);
            Log.d(LOG_TAG,"Constructor DBHelper");
        }

        // создаем и заполняем БД
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_NOTES_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}
