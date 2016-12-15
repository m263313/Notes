package com.androshchuk.notes;
import android.content.Context;
import  android.database.sqlite.*;


/**
 * Created by Androshchuk on 15-Dec-16.
 */

public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "NotesDataBase.db";
    public static final String TABLE_NOTES = "notes";

//
    private static final String KEY_NOTES_ID = "id";
    private static final String KEY_NOTES_TITLE="notes_title";
    private static final String KEY_NOTES_TEXT="notes_text";
    private static final String KEY_NOTES_DATE="notes_date";
    private static final String KEY_NOTES_THEME="notes_theme";


    //private static final int DATABASE_VERSION = 2;
    private static final String DICTIONARY_TABLE_NAME = "dictionary";
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    private static final String CREATE_NOTES_TABLE =
            "CREATE TABLE " + TABLE_NOTES + " (" +
                    KEY_NOTES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_NOTES_TITLE + " TEXT," +
                    KEY_NOTES_TEXT+ " TEXT,"+
                    KEY_NOTES_DATE+ " TEXT,"+
                    KEY_NOTES_THEME+ " TEXT"+
                    ");";
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_NOTES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
