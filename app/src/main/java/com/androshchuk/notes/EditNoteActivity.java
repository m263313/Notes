package com.androshchuk.notes;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class EditNoteActivity extends AppCompatActivity {
    final String LOG_TAG = "myLogs";
    DataBase dbHelper;
    int myID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
          myID=intent.getIntExtra("myId",0);

        dbHelper=new DataBase(this);
        dbHelper.open();
        Cursor c =dbHelper.getRec(myID);
        if (c.moveToFirst()) {
            int titleColIndex = c.getColumnIndex(DataBase.KEY_NOTES_TITLE);
            int textColIndex = c.getColumnIndex(DataBase.KEY_NOTES_TEXT);
            EditText editTitle = (EditText) findViewById(R.id.editNoteTitle);
            editTitle.setText(c.getString(titleColIndex), TextView.BufferType.EDITABLE);
            EditText  editText = (EditText) findViewById(R.id.editNote);
            editText.setText(c.getString(textColIndex), TextView.BufferType.EDITABLE);
        }
    }
    public void editNoteDatabase(View view){
        Intent intent = new Intent(this,  MainPageActivity.class);
        EditText editTitle = (EditText) findViewById(R.id.editNoteTitle);
        EditText  editText = (EditText) findViewById(R.id.editNote);
        dbHelper.updateRec(myID,editTitle.getText().toString().trim(),editText.getText().toString().trim());
        startActivity(intent);
    }
    public void deleteNoteDataBase(View view){
        Intent intent = new Intent(this,  MainPageActivity.class);
        dbHelper.delRec(myID);
        startActivity(intent);
    }

}
