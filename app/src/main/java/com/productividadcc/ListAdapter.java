package com.productividadcc;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
        final ListCell cell = getItem(position);

        //If the cell is a section header we inflate the header layout
        if(cell.IsSectionHeader())
        {
            v = inflater.inflate(R.layout.section_header, null);

            v.setClickable(false);

            TextView header = (TextView) v.findViewById(R.id.section_header);
            header.setText(cell.getDate()+" "+cell.getGroupName()+" "+cell.getStatusName());
            header.setGravity(Gravity.RIGHT);
            header.setTypeface(null, Typeface.BOLD);
        }
        else {
            v = inflater.inflate(R.layout.item_cell, null);
            TextView name = (TextView) v.findViewById(R.id.name);
            TextView id = (TextView) v.findViewById(R.id.ID);
            ImageButton map = (ImageButton) v.findViewById(R.id.locationBtn);
            map.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if(Integer.parseInt(cell.getStatusId())<=4){
                        String url = "http://maps.google.com/maps?daddr=" + cell.getLatitute()+","+cell.getLongitude();
                        Intent goZe = new Intent(Intent.ACTION_VIEW);
                        goZe.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        goZe.setData(Uri.parse(url));
                        context.startActivity(goZe);
                    }

                    if(Integer.parseInt(cell.getStatusId())>4){
                        String url = "http://maps.google.com/maps?daddr=" + cell.getLatitute()+","+cell.getLongitude();
                        Intent goZe = new Intent(Intent.ACTION_VIEW);
                        goZe.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        goZe.setData(Uri.parse(url));
                        context.startActivity(goZe);
                    }


                }
            });

            name.setText(cell.getDate()+"\n");
            name.append(cell.getGroupNumber()+" "+cell.getGroupName()+"\n");
            name.append(cell.getStatusName());


            id.setText(cell.getGroupId());
        }
        return v;
    }
}
