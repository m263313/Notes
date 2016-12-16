package com.androshchuk.notes;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;

import static com.androshchuk.notes.R.id.note;
import static com.androshchuk.notes.R.id.noteTitle;

public class NewNoteActivity extends Activity {
    DBHelper dbHelper;
    final String LOG_TAG = "myLogs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);


        Intent intent = getIntent();



    }
    public void addNoteDatabase(View view){
        Intent intent = new Intent(this,  MainPageActivity.class);
        dbHelper=new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        // get data from inputs
     //   String name = note.getText().toString();
      //  String email = View.;
        cv.put("notes_title", "Title");
        cv.put("notes_text", "NoteText");
        cv.put("notes_date", Calendar.getInstance().toString() );
        cv.put("notes_theme","Nothing");
        long rowID = db.insert("notes", null, cv);

        startActivity(intent);


    }
}
