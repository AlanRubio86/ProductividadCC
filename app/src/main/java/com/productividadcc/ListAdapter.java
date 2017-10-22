package com.productividadcc;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by cesarrdz on 9/9/16.
 */
public class ListAdapter extends ArrayAdapter<ListCell> {

    LayoutInflater inflater;
    public ListAdapter(Context context, ArrayList<ListCell> items) {
        super(context, 0, items);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ListCell cell = getItem(position);

        //If the cell is a section header we inflate the header layout
        if(cell.isSectionHeader())
        {
            v = inflater.inflate(R.layout.section_header, null);

            v.setClickable(false);

            TextView header = (TextView) v.findViewById(R.id.section_header);
            header.setText(cell.getName());
            header.setGravity(Gravity.RIGHT);
            header.setTypeface(null, Typeface.BOLD);
        }
        else
        {
            v = inflater.inflate(R.layout.item_cell, null);
            TextView name = (TextView) v.findViewById(R.id.name);
            TextView id = (TextView) v.findViewById(R.id.ID);

            name.setText(cell.getName());
            // REVISAMOS SI EL TEXTO TRAE LOS DOS PUNTOS DEL FORMATO DE LA HORA, DE SER ASI, SE LE DA OTRO COLOR AL BACKGROUND DEL TEXTO
            if(name.getText().toString().substring(2,3).equalsIgnoreCase(":")){
                name.setTypeface(null, Typeface.BOLD);
                name.setTextColor(Color.BLUE);
            }
            id.setText(cell.getId());
        }
        return v;
    }
}
