package com.androshchuk.notes;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainPageActivity extends AppCompatActivity {
    DBHelper dbHelper;
    final String LOG_TAG = "myLogs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ListView listView = (ListView)findViewById(R.id.listOfNotes);
        final List<String> arrayOfNotes = new ArrayList<String>();
        final ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, arrayOfNotes);
        listView.setAdapter(adapter);

        dbHelper=new DBHelper(this);


        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        ContentValues cv = new ContentValues();
//        cv.put("notes_title", "Title");
//        cv.put("notes_text", "NoteText");
//        cv.put("notes_date",Calendar.getInstance().toString() );
//        cv.put("notes_theme","Nothing");
      //  long rowID = db.insert("notes", null, cv);
      //  Log.d(LOG_TAG, "row inserted, ID = " + rowID);
        Cursor c = db.query("notes", null, null, null, null, null, null);
        if (c.moveToFirst()) {
        int idColIndex = c.getColumnIndex("id");
        int nameColIndex = c.getColumnIndex("notes_text");
        int titleColIndex = c.getColumnIndex("notes_title");
        Log.d(LOG_TAG, (Integer.toString(c.getInt(idColIndex))));
        do {
            arrayOfNotes.add(0, c.getString(titleColIndex));
            adapter.notifyDataSetChanged();
            // получаем значения по номерам столбцов и пишем все в лог
//            Log.d(LOG_TAG,
//                    "ID = " + c.getInt(idColIndex) +
//                            ", name = " + c.getString(nameColIndex) +
//                            ", title = " + c.getString(titleColIndex));
            
            // переход на следующую строку
            // а если следующей нет (текущая - последняя), то false - выходим из цикла
        } while (c.moveToNext());
        }

    c.close();
        dbHelper.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void addNote(View view){
        Intent intent = new Intent(this,  NewNoteActivity.class);

        startActivity(intent);
    }
}
