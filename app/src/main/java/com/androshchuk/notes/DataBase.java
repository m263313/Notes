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
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "NotesDataBase.db";
    //Tables
    public static final String TABLE_NOTES = "notes";
    public static final String TABLE_THEMES = "themes";
    public static final String TABLE_DATACLASSIFIER = "dataclassifier";
    //Colums in tables
    public static final String KEY_NOTES_ID = "id";
    public static final String KEY_NOTES_TITLE="notes_title";
    public static final String KEY_NOTES_TEXT="notes_text";
    public static final String KEY_NOTES_DATE="notes_date";
    public static final String KEY_NOTES_THEME="notes_theme";

    public static final String KEY_THEMES_ID = "id";
    public static final String KEY_THEMES_THEME= "theme";

    public static final String KEY_DATACLASSIFIER_ID = "id";
    public static final String KEY_DATACLASSIFIER_IDTHEME = "id_theme";
    public static final String KEY_DATACLASSIFIER_THEME = "theme";
    public static final String KEY_DATACLASSIFIER_WORD = "word";
    public static final String KEY_DATACLASSIFIER_COUNT = "count";

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
                   // "FOREIGN KEY("+KEY_NOTES_THEME+") REFERENCES "+TABLE_THEMES+"("+KEY_THEMES_THEME+")"+
                    ");";
    private static final String CREATE_THEMES_TABLE =
                    "CREATE TABLE themes (" +
                    KEY_THEMES_ID+" integer PRIMARY KEY AUTOINCREMENT," +
                    KEY_THEMES_THEME+" text" +
                    ");";
    private static final String CREATE_DATACLASSIFIER_TABLE =
            "CREATE TABLE dataclassifier (" +
                    KEY_DATACLASSIFIER_ID+" integer PRIMARY KEY AUTOINCREMENT," +

                    KEY_DATACLASSIFIER_THEME+" text," +
                    KEY_DATACLASSIFIER_WORD+" text," +
                    KEY_DATACLASSIFIER_COUNT+" integer" +
                //    "FOREIGN KEY("+KEY_DATACLASSIFIER_IDTHEME+") REFERENCES "+TABLE_THEMES+"("+KEY_THEMES_ID+")"+
                //    "FOREIGN KEY("+KEY_DATACLASSIFIER_THEME+") REFERENCES "+TABLE_THEMES+"("+KEY_THEMES_THEME+")"
                    ");";
    public DataBase(Context ctx) {
       // Log.d(LOG_TAG,"DataBase constructor");
        mCtx = ctx;
    }
    public void open() {
     //   Log.d(LOG_TAG,"Open connection");

        dbHelper = new DBHelper(mCtx, DATABASE_NAME, null, DATABASE_VERSION);
        DB = dbHelper.getWritableDatabase();
//        DB.execSQL("DELETE FROM "+TABLE_NOTES);
//        DB.execSQL("VACUUM");
//        DB.execSQL("DELETE FROM "+TABLE_DATACLASSIFIER);
//        DB.execSQL("VACUUM");
    }
    // закрыть подключение
    public void close() {
       // Log.d(LOG_TAG,"Close connection");
        if (dbHelper!=null) dbHelper.close();
    }

    public Cursor getAllData() {
        //Log.d(LOG_TAG,"Get data from base");
       // DB.execSQL("drop table "+TABLE_NOTES);
//        DB.execSQL(CREATE_NOTES_TABLE);
        return DB.query(TABLE_NOTES, null, null, null, null, null, null);
    }

    // добавить запись в DB_TABLE
    public void addRec(String title, String text,String theme) {
      //  Log.d(LOG_TAG,"Add record to base");
        ContentValues cv = new ContentValues();
        cv.put(KEY_NOTES_TITLE, title);
        cv.put(KEY_NOTES_TEXT, text);
        cv.put(KEY_NOTES_DATE, Calendar.getInstance().toString());
        cv.put(KEY_NOTES_THEME, theme);
        DB.insert(TABLE_NOTES, null, cv);
    }

    public void addRec(ContentValues cv) {
        //  Log.d(LOG_TAG,"Add record to base");


        cv.put(KEY_NOTES_DATE, Calendar.getInstance().toString());

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
      int count =  DB.delete(TABLE_NOTES, KEY_NOTES_ID + " = " + id, null);
        //Log.d(LOG_TAG,"Delete rows = "+count);
    }
    public Cursor getRec(long id){
        //)true, Table, from, where, null, null, null, null, null)
      return   DB.query(TABLE_NOTES,null,KEY_NOTES_ID+" = "+id,null,null,null,null);
    }

    public Cursor getAllThemes(){

        return  DB.query(TABLE_THEMES,null,null,null,null,null,null);
    }

    public void addTheme(String theme){
        ContentValues cv = new ContentValues();
        cv.put(KEY_THEMES_THEME,theme);
        DB.insert(TABLE_THEMES, null, cv);
    }
public Cursor getAllWords(){

    return DB.query(TABLE_DATACLASSIFIER,null,null,null,null,null,null);
}
    public void increaseWordCount(String word, String theme){
        Cursor c = DB.query(TABLE_DATACLASSIFIER,null,KEY_DATACLASSIFIER_WORD+" = ? ",new String[]{word},null,null,null);
        if(c.moveToFirst()) {
            int count = c.getInt(c.getColumnIndex(KEY_DATACLASSIFIER_COUNT));
            count++;
            ContentValues cv= new ContentValues();
            cv.put(KEY_DATACLASSIFIER_COUNT,count);
            cv.put(KEY_DATACLASSIFIER_THEME,c.getString(c.getColumnIndex(KEY_DATACLASSIFIER_THEME)));
            cv.put(KEY_DATACLASSIFIER_WORD,word);
            DB.update(TABLE_DATACLASSIFIER,cv,KEY_DATACLASSIFIER_WORD+" = "+word,null);
            return;
        }
        else{
            ContentValues cv= new ContentValues();
            cv.put(KEY_DATACLASSIFIER_COUNT,1);
            cv.put(KEY_DATACLASSIFIER_THEME,theme);
            cv.put(KEY_DATACLASSIFIER_WORD,word);
            DB.update(TABLE_DATACLASSIFIER,cv,KEY_DATACLASSIFIER_WORD+" = "+word,null);
        }
    }
        public void delTheme(String theme){
          //  if(theme.length()!=0) {
                int count = DB.delete(TABLE_THEMES, KEY_THEMES_THEME + " = '" + theme+"'", null);
                count = DB.delete(TABLE_DATACLASSIFIER, KEY_DATACLASSIFIER_THEME + " = '" + theme+"'", null);

//            }
//            else {
//                DB.execSQL("delete from" + TABLE_THEMES + "where " + KEY_THEMES_THEME + " = ''");
//                DB.execSQL("delete from"+TABLE_DATACLASSIFIER+"where "+KEY_DATACLASSIFIER_THEME+" = ''");
//
//            }

    }
    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                        int version) {
            super(context, name, factory, version);
          //  Log.d(LOG_TAG,"Constructor DBHelper");
        }

        // создаем и заполняем БД
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_THEMES_TABLE);
            db.execSQL(CREATE_NOTES_TABLE);
            db.execSQL(CREATE_DATACLASSIFIER_TABLE);
        }


        //Drop old Database
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (oldVersion == 1 && newVersion == 2) {
                db.beginTransaction();
                try {
              //  db.execSQL("drop table "+TABLE_NOTES);
                  //  db.execSQL(CREATE_THEMES_TABLE);
                  //  db.execSQL(CREATE_NOTES_TABLE);
                  //  db.execSQL(CREATE_DATACLASSIFIER_TABLE);
                } finally {
                    db.endTransaction();
                }
            }
        }
    }
}
