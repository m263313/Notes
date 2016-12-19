package com.androshchuk.notes;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

import static com.androshchuk.notes.R.id.note;
import static com.androshchuk.notes.R.id.noteTitle;

public class NewNoteActivity extends Activity {
    DataBase dbHelper;
    final String LOG_TAG = "myLogs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);


        Intent intent = getIntent();



    }
    public void addNoteDatabase(View view){

        dbHelper=new DataBase(this);
        dbHelper.open();
      //  SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        // get data from inputs
     //   String name = note.getText().toString();
      //  String email = View.;
        EditText edit = (EditText) findViewById(R.id.noteTitle);
        Editable editable = edit.getText();
        String allTheText = editable.toString().trim();

        cv.put("notes_title",allTheText);
        edit = (EditText) findViewById(R.id.note);
        editable = edit.getText();
       String allText = editable.toString().trim();
        cv.put("notes_text", allText);
        cv.put("notes_date", Calendar.getInstance().toString() );
        cv.put("notes_theme","Nothing");
       // long rowID = db.insert("notes", null, cv);
        dbHelper.addRec(allTheText,allText);
        Intent intent = new Intent(this,  MainPageActivity.class);
        startActivity(intent);


    }
}
