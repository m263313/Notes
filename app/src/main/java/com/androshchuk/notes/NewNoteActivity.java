package com.androshchuk.notes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class NewNoteActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);


        Intent intent = getIntent();



    }
    public void addNoteDatabase(View view){
        Intent intent = new Intent(this,  ShowAllActivity.class);

        startActivity(intent);


    }
}
