package com.androshchuk.notes;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;


import com.androshchuk.notes.classifier.Classifier;
import com.androshchuk.notes.classifier.bayes.BayesClassifier;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.androshchuk.notes.R.id.note;
import static com.androshchuk.notes.R.id.noteTitle;

public class NewNoteActivity extends Activity {
    DataBase dbHelper;
    final String LOG_TAG = "myLogs";

    private AutoCompleteTextView mAutoCompleteTextView;
    private List<String> mList;
    private ArrayAdapter<String> mAutoCompleteAdapter;
    private TextView mAutoListTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        Intent intent = getIntent();


        List arrayOfThemes = new ArrayList<String>();
        mList  = new ArrayList<String>();
        mAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.newNoteTheme);
        mAutoCompleteAdapter = new ArrayAdapter<>(NewNoteActivity.this,
                android.R.layout.simple_dropdown_item_1line, mList);
        mAutoCompleteTextView.setAdapter(mAutoCompleteAdapter);
        dbHelper=new DataBase(this);
        dbHelper.open();

        Cursor c = dbHelper.getAllThemes();
        int themeColIndex=c.getColumnIndex(DataBase.KEY_THEMES_THEME);
        int idColIndex = c.getColumnIndex(DataBase.KEY_THEMES_ID);

        mList.add("Array");
        mList.add("ArrayList");
        mList.add("ArrayAdapter");
        if (c.moveToFirst()) {
            // Log.d(LOG_TAG, (Integer.toString(c.getInt(idColIndex))));
            do {
                // Log.d(LOG_TAG,c.getInt(idColIndex)+" id of element");
                mList.add(c.getString(themeColIndex));


                mAutoCompleteAdapter.notifyDataSetChanged();
            } while (c.moveToNext());
        }






    }
    public void addNoteDatabase(View view){
        final Classifier<String, String> bayes =
                new BayesClassifier<String, String>();

        Cursor cursorWords = dbHelper.getAllWords();

        int themeColIndex = cursorWords.getColumnIndex(DataBase.KEY_DATACLASSIFIER_THEME);
        int countColIndex = cursorWords.getColumnIndex(DataBase.KEY_DATACLASSIFIER_COUNT);
        int wordColIndex = cursorWords.getColumnIndex(DataBase.KEY_DATACLASSIFIER_WORD);
        if(cursorWords.moveToFirst()){
        do{
            List<String> tempWords =new ArrayList<String>();
            for(int i = 0; i<tempWords.size();i++)
                tempWords.add(cursorWords.getString(wordColIndex));
         bayes.learn(cursorWords.getString(themeColIndex),tempWords);
        }while (cursorWords.moveToNext());

        }
        ContentValues cv = new ContentValues();
        EditText edit = (EditText) findViewById(R.id.noteTitle);
        Editable editable = edit.getText();
        String allTheText = editable.toString().trim();

        cv.put("notes_title",allTheText);
        edit = (EditText) findViewById(R.id.note);
        editable = edit.getText();
       String allText = editable.toString().trim();
        cv.put("notes_text", allText);
        cv.put("notes_date", Calendar.getInstance().toString() );

       // cv.put(DataBase.KEY_NOTES_THEME,"Nothing");
       // long rowID = db.insert("notes", null, cv);


        String newAdd = mAutoCompleteTextView.getText().toString().trim();


        String[] textToPredict = newAdd.split("\\s");

        if (!mList.contains(newAdd.toLowerCase())) {
            dbHelper.addTheme(newAdd);
            mList.add(newAdd);

            // update the autocomplete words
            mAutoCompleteAdapter = new ArrayAdapter<>(
                    NewNoteActivity.this,
                    android.R.layout.simple_dropdown_item_1line, mList);

            mAutoCompleteTextView.setAdapter(mAutoCompleteAdapter);
        }
        String themeNote = bayes.classify(Arrays.asList(textToPredict)).getCategory();
        dbHelper.addRec(allTheText,allText,themeNote);
        // display the words in mList for your reference

        //mAutoListTextView.setText(s);


        Intent intent = new Intent(this,  MainPageActivity.class);

        startActivity(intent);


    }


}
