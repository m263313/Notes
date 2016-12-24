package com.androshchuk.notes;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ChooseThemePageActivity extends AppCompatActivity {
    DataBase dbHelper;
    private List<String> mList;
    ContentValues cv;
    ArrayAdapter<String> adapter;
    List<Integer> listOfIndex;
    final String LOG_TAG = "myLogs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_theme_page);
    listOfIndex= new ArrayList<Integer>();
        List arrayOfThemes = new ArrayList<String>();
        mList = new ArrayList<String>();
        //open database connection
        dbHelper = new DataBase(this);
        dbHelper.open();
        cv = getIntent().getExtras().getParcelable("ContentValues");
        mList.add("New theme");
        //get themes list
        Cursor c = dbHelper.getAllThemes();
        int themeColIndex = c.getColumnIndex(DataBase.KEY_THEMES_THEME);
        int idColIndex = c.getColumnIndex(DataBase.KEY_THEMES_ID);

        if (c.moveToFirst()) {

            do {

                mList.add(c.getString(themeColIndex));
                listOfIndex.add(c.getInt(idColIndex));
            } while (c.moveToNext());
        }
        final ListView listViewThemes = (ListView) findViewById(R.id.listviewListOfThemes);
        // создаем адаптер
         adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mList);

        // присваиваем адаптер списку
        listViewThemes.setAdapter(adapter);
        registerForContextMenu(listViewThemes);

        listViewThemes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                    long id) {
                if (position == 0)
                    setContentView(R.layout.input_theme);
                else {
                    cv.put(DataBase.KEY_NOTES_THEME, mList.get(position + 1));
                    dbHelper.addRec(cv);
                    Intent intent = new Intent(getApplicationContext(), MainPageActivity.class);
                    startActivity(intent);
                }


                //   startActivity(intent);

            }
        });

    }
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add("Delete theme");

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        // получаем инфу о пункте списка
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if(acmi.position==0)
            return false;
        else{

            dbHelper.open();
            dbHelper.delTheme(mList.get(acmi.position));
            mList.remove(acmi.position);
            listOfIndex.remove(listOfIndex.get(listOfIndex.size() - acmi.position));

            dbHelper.close();
            adapter.notifyDataSetChanged();
        }

        return super.onContextItemSelected(item);
    }



    public void addTheme(View view){
        EditText edit = (EditText) findViewById(R.id.editTextTheme);
        Editable editable = edit.getText();
        String theme = editable.toString().trim();

        if(!mList.contains(theme) )
    dbHelper.addTheme(theme);

    cv.put(DataBase.KEY_NOTES_THEME,theme);
        dbHelper.addRec(cv);
        Intent intent = new Intent(getApplicationContext(), MainPageActivity.class);
        startActivity(intent);
        dbHelper.close();
    }
}
