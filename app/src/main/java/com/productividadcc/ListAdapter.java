package com.productividadcc;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by cesarrdz on 9/9/16.
 */
public class ListAdapter extends ArrayAdapter<ListCell> {

    LayoutInflater inflater;
    private Context context;
    public ListAdapter(Context context, ArrayList<ListCell> items) {
        super(context, 0, items);
        this.context=context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ListCell cell = getItem(position);

        //If the cell is a section header we inflate the header layout

            v = inflater.inflate(R.layout.item_cell, null);
            TextView name = (TextView) v.findViewById(R.id.name);
            TextView id = (TextView) v.findViewById(R.id.ID);
            ImageButton map = (ImageButton) v.findViewById(R.id.locationBtn);
       // Button Button1= (Button)  convertView  .findViewById(R.id.BUTTON1_ID);
            map.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View view) {
                             Intent intent = new Intent(getContext(), AgendaLoader.class);
                             intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                             context.startActivity(intent);
                           }
            });

            name.setText(cell.getName());
            id.setText(cell.getStatusId().toString());

        return v;
    }
}
