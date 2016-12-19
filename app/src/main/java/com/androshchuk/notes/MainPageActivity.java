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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainPageActivity extends AppCompatActivity {
    DataBase dbHelper;
    final String LOG_TAG = "myLogs";
    ArrayList<Integer> listOfIndex;
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
        listOfIndex=new ArrayList<>();
        dbHelper=new DataBase(this);


        dbHelper.open();
//        ContentValues cv = new ContentValues();
//        cv.put("notes_title", "Title");
//        cv.put("notes_text", "NoteText");
//        cv.put("notes_date",Calendar.getInstance().toString() );
//        cv.put("notes_theme","Nothing");
      //  long rowID = db.insert("notes", null, cv);
      //  Log.d(LOG_TAG, "row inserted, ID = " + rowID);
       Cursor c= dbHelper.getAllData();
       // Cursor c = db.query("notes", null, null, null, null, null, null);
        if (c.moveToFirst()) {

        int idColIndex = c.getColumnIndex("id");

        int nameColIndex = c.getColumnIndex("notes_text");
        int titleColIndex = c.getColumnIndex("notes_title");
        Log.d(LOG_TAG, (Integer.toString(c.getInt(idColIndex))));
        do {
            Log.d(LOG_TAG,c.getInt(idColIndex)+" id of element");
            listOfIndex.add(c.getInt(idColIndex));

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
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent myIntent = new Intent(MainPageActivity.class, EditNoteActivity.class);
//
//                myIntent.putExtra("test", "hello");
//                startActivity(myIntent);
//                Intent intent = new Intent(this,  NewNoteActivity.class)
//            }
//
//        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                    long id) {
                //TextView textView = (TextView) itemClicked;
                Intent intent = new Intent(getApplicationContext(), EditNoteActivity.class);

                   intent.putExtra("myId",-listOfIndex.get(position)+listOfIndex.size()+1);

                Log.d(LOG_TAG,listOfIndex.size()+" Size");
                Log.d(LOG_TAG,position+" Position");
                Log.d(LOG_TAG,listOfIndex.get((int)id)+" Data that goes to next activity");
                    startActivity(intent);

            }
        });
    }



    public void addNote(View view){
        Intent intent = new Intent(this,  NewNoteActivity.class);
       // intent.putExtra("","");
        startActivity(intent);
    }
}
