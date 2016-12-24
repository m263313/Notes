package com.androshchuk.notes;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import java.lang.reflect.Array;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        Intent intent = getIntent();
         dbHelper=new DataBase(this);
    }
    //Action after pressing button
    public void addNoteDatabase(View view){
        //start get words from Database
        final Classifier<String, String> bayes =    new BayesClassifier<String, String>();

        dbHelper.open();
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
        dbHelper.close();
        //end
        //work with user data
        ContentValues cv = new ContentValues();
        EditText edit = (EditText) findViewById(R.id.noteTitle);
        Editable editable = edit.getText();
        String noteTitleText = editable.toString().trim();
        cv.put(DataBase.KEY_NOTES_TITLE,noteTitleText);

        edit = (EditText) findViewById(R.id.note);
        editable = edit.getText();
        String noteTextText = editable.toString().trim();
        cv.put(DataBase.KEY_NOTES_TEXT, noteTextText);

        cv.put(DataBase.KEY_NOTES_DATE, Calendar.getInstance().toString() );
        //end
        //start of prediction
        String[] textToPredict = (noteTitleText+" "+noteTextText).split("\\s");

        Resources res = getResources();
        List<String> wordsToIgnore =  Arrays.asList(res.getStringArray(R.array.ignored_words));

        List<String> finalWords = new ArrayList<String>();

        for(String word :textToPredict){
            word.toLowerCase();
            if(!wordsToIgnore.contains(word) && !finalWords.contains(word))
                finalWords.add(word);
        }

        bayes.learn("Buy",Arrays.asList(new String[]{"buy","Buy"}));


        cv.put(DataBase.KEY_NOTES_THEME,bayes.classify(finalWords).getCategory());


        Intent intent = new Intent(this,  ChooseThemePageActivity.class);
        intent.putExtra("ContentValues", cv);
        startActivity(intent);


    }


}
