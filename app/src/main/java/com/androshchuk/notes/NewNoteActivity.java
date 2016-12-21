package com.androshchuk.notes;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.androshchuk.notes.naivebayesclassifier.NaiveBayes;

import com.androshchuk.notes.naivebayesclassifier.NaiveBayesKnowledgeBase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
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
        //Zestyak
        Map<String, String[]> trainingExamples = new HashMap<>();
//        for(Map.Entry<String, URL> entry : trainingFiles.entrySet()) {
//            try {
//                trainingExamples.put(entry.getKey(), readLines(entry.getValue()));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        trainingExamples.put("Buy",new String[]{"Buy","buy","deal","investment","Buy","Buy","Buy","Buy","Buy","Buy","Buy","Buy","Buy","Buy","Buy","Buy","Buy"});
        trainingExamples.put("Travel",new String[]{"go","Go","Travel","Ride","ride","travel"});
        //train classifier
        NaiveBayes nb = new NaiveBayes();
        nb.setChisquareCriticalValue(6.63); //0.01 pvalue
        nb.train(trainingExamples);

        //get trained classifier knowledgeBase
        NaiveBayesKnowledgeBase knowledgeBase = nb.getKnowledgeBase();

        nb = null;
        trainingExamples = null;


        //Use classifier
        nb = new NaiveBayes(knowledgeBase);

        String outputTheme = nb.predict(allText);


        cv.put(DataBase.KEY_NOTES_THEME,outputTheme);
       // cv.put(DataBase.KEY_NOTES_THEME,"Nothing");
       // long rowID = db.insert("notes", null, cv);
        dbHelper.addRec(allTheText,allText,outputTheme);
        Intent intent = new Intent(this,  MainPageActivity.class);

        startActivity(intent);


    }
    /**
     * Reads the all lines from a file and places it a String array. In each
     * record in the String array we store a training example text.
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static String[] readLines(URL url) throws IOException {

        Reader fileReader = new InputStreamReader(url.openStream(), Charset.forName("UTF-8"));
        List<String> lines;
        try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            lines = new ArrayList<>();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines.toArray(new String[lines.size()]);
    }


}
