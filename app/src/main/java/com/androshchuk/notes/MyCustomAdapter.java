package com.androshchuk.notes;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by m2633_000 on 19-Dec-16.
 */


public class MyCustomAdapter<String> extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list = new ArrayList<String>();
    private Context context;
    DataBase dbHelper;
    final java.lang.String LOG_TAG = "myLogs";

    public MyCustomAdapter(ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;
        dbHelper=new DataBase(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
        //just return 0 if your list items do not have an Id variable.
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item, null);
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView)view.findViewById(R.id.mainTitleNote);
        listItemText.setText(""+list.get(position));

        //Handle buttons and add onClickListeners
        FloatingActionButton deleteBtn = (FloatingActionButton)view.findViewById(R.id.mainDeleteButton);
        FloatingActionButton editBtn = (FloatingActionButton)view.findViewById(R.id.mainEditButton);

        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                Log.d(LOG_TAG,"Delete "+position);
                dbHelper.open();
                dbHelper.delRec(position);
                list.remove(position); //or some other task
                notifyDataSetChanged();
            }
        });
        editBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something

                notifyDataSetChanged();
            }
        });

        return view;
    }
}
