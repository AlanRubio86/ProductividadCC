package com.productividadcc;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
import android.content.Intent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.productividadcc.database.Event;
import com.productividadcc.database.NewGroups;
import com.productividadcc.database.OldGroups;


public class OldGroupsList_Activity extends AppCompatActivity {

    String[] agendaArray;
    private static Context context;
    ProgressBar mprogressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.oldgroupslist_activity);
        context = getApplicationContext();

        // Find the toolbar view and set as ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // ...
        // Display icon in the toolbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        // Remove default title text
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Get access to the custom title view
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Grupos de Recontratación");

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OldGroupsList_Activity.this, Main_Activity.class);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.agregaGrupoBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OldGroupsList_Activity.this, OldGroupNew_Activity.class);
                startActivity(intent);
                finish();
            }
        });

        mprogressBar = (ProgressBar) findViewById(R.id.progressBar);
        if (Utils.isNetworkAvailable(OldGroupsList_Activity.this))
        {
            LoadOldGroups();
        }
        else
        {
            LoadFromDatabase();
        }
        mprogressBar.setVisibility(View.GONE);
    }

    public void LoadOldGroups() {
        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
        SharedPreferences shared = getSharedPreferences("userInfo", MODE_PRIVATE);
        String employeeID = shared.getString("numEmployee", "0");
        String tokenId = shared.getString("tokenId", "0");
        Log.d("WS Login:", "http://asistente.crediclub.com/2.0/consultaAgenda.php?TipCon=2&empleadoID="+employeeID+"&tokenID="+tokenId);

        StringRequest MyStringRequest = new StringRequest(Request.Method.GET,
                "http://asistente.crediclub.com/2.0/consultaAgenda.php?TipCon=2&empleadoID="+employeeID+"&tokenID="+tokenId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //This code is executed if the server responds, whether or not the response contains data.
                        //The String 'response' contains the server's response.
                        Log.d("Schedule", "response: " + response);
                        if (!response.equals("") && response != null) {
                            agendaArray = response.split("<br>");
                             final ArrayList<ListCell> items = new ArrayList<ListCell>();
                            for(int i=0;i<agendaArray.length;i++)
                            {

                                Main_Activity.oldGroups = new OldGroups();
                                Main_Activity.oldGroups.setItem(agendaArray[i]);
                                Main_Activity.oldGroups.insert();

                                String[] appointmentArray = agendaArray[i].split(", ");
                                Date date = null;
                                try {
                                    date = new SimpleDateFormat("yyyy-MM-dd").parse(appointmentArray[11]);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                String statusName = "";
                                String  input = appointmentArray[10].trim();
                                switch (input) {
                                    case "1":
                                        statusName = "Capacitación 1";
                                        break;
                                    case "2":
                                        statusName = "Capacitación 2";
                                        break;
                                    case "3":
                                        statusName = "Visto Bueno";
                                        break;
                                    case "4":
                                        statusName = "Desembolso";
                                        break;
                                    case "5":
                                        statusName = "Recontratación";
                                        break;
                                    case "6":
                                        statusName = "Visto Bueno";
                                        break;
                                    case "7":
                                        statusName = "Cancelación";
                                        break;
                                    case "8":
                                        statusName = "Reagendar";
                                        break;

                                }
                                items.add(new ListCell(appointmentArray[0],
                                        appointmentArray[1],
                                        appointmentArray[2],
                                        appointmentArray[3],
                                        appointmentArray[4],
                                        appointmentArray[5],
                                        appointmentArray[6],
                                        appointmentArray[7],
                                        appointmentArray[8],
                                        appointmentArray[9],
                                        appointmentArray[10],
                                        new SimpleDateFormat("yyyy-MM-dd").format(date),
                                        statusName));
                            }
                            final ListView list = (ListView) findViewById(R.id.oldGroupsListView);

                            ListAdapter adapter = new ListAdapter(getContext(), items);

                            list.setAdapter(adapter);
                            list.setOnItemClickListener(new OnItemClickListener()
                            {

                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    // TODO Auto-generated method stub
                                    TextView textView = (TextView) view.findViewById(R.id.name);
                                    TextView idTextView = (TextView) view.findViewById(R.id.ID);
                                    if (!idTextView.getText().toString().equals("")) {

                                        for(int x=0; x<items.size();x++)
                                        {
                                            if( items.get(x).getGroupId().equals(idTextView.getText().toString()))
                                            {
                                                    Intent intent=null;
                                                    switch(items.get(x).getStatusId())
                                                    {
                                                        case "6":
                                                            intent = new Intent(OldGroupsList_Activity.this, OldGroupVoBo_Activity.class);
                                                            break;
                                                        case "5":
                                                            intent = new Intent(OldGroupsList_Activity.this, OldGroupRehire_Activity.class);
                                                            break;
                                                        case "4":
                                                            intent = new Intent(OldGroupsList_Activity.this, OldGroupDisbursement_Activity.class);
                                                            break;
                                                    }

                                                intent.putExtra("cicle",items.get(x).getCicle());
                                                intent.putExtra("week",items.get(x).getWeek());
                                                intent.putExtra("groupName",items.get(x).getGroupNumber()+"-"+items.get(x).getGroupName());
                                                intent.putExtra("groupId",items.get(x).getGroupId());
                                                startActivity(intent);
                                                finish();
                                                break;
                                            }
                                        }
                                    }
                                }
                            });

                            mprogressBar.setVisibility(View.GONE);

                        } else {
                            Toast.makeText(getApplicationContext(), "No hay eventos que mostrar", Toast.LENGTH_LONG).show();
                        }
                        mprogressBar.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
                Log.d("Schedule", "response error" + error.toString());
                Toast.makeText(getApplicationContext(), "Error de conexión, por favor vuelve a intentar.", Toast.LENGTH_LONG).show();
                mprogressBar.setVisibility(View.GONE);
                Intent intent = new Intent(OldGroupsList_Activity.this, Main_Activity.class);
                startActivity(intent);
                finish();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("Field", "Value"); //Add the data you'd like to send to the server.
                return MyData;
            }
        };

        MyRequestQueue.add(MyStringRequest);
    }

    public void LoadFromDatabase() {
        final ArrayList<ListCell> items = new ArrayList<ListCell>();

        OldGroups oldGroups = new OldGroups();
        Cursor cursor = oldGroups.load();

        int count = 0;
        int total = 0;
        if (cursor != null) {
            cursor.moveToFirst();
            total = cursor.getCount();

            if (total > 0) {

                while (!cursor.isAfterLast()) {
                    OldGroups ev = new OldGroups();
                    ev.copy(cursor);


                    String[] appointmentArray = ev.getItem().split(", ");
                    Date date = null;
                    try {
                        date = new SimpleDateFormat("yyyy-MM-dd").parse(appointmentArray[11]);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    String statusName = "";
                    String input = appointmentArray[10].trim();
                    switch (input) {
                        case "1":
                            statusName = "Capacitacion 1";
                            break;
                        case "2":
                            statusName = "Capacitacion 2";
                            break;
                        case "3":
                            statusName = "VoBo";
                            break;
                        case "4":
                            statusName = "Desembolso";
                            break;
                        case "5":
                            statusName = "Recontratacion";
                            break;
                        case "6":
                            statusName = "VoBo Renovacion";
                            break;
                        case "7":
                            statusName = "Cancelacion";
                            break;
                        case "8":
                            statusName = "Reagendar";
                            break;

                    }
                    items.add(new ListCell(appointmentArray[0],
                            appointmentArray[1],
                            appointmentArray[2],
                            appointmentArray[3],
                            appointmentArray[4],
                            appointmentArray[5],
                            appointmentArray[6],
                            appointmentArray[7],
                            appointmentArray[8],
                            appointmentArray[9],
                            appointmentArray[10],
                            new SimpleDateFormat("yyyy-MM-dd").format(date),
                            statusName));
                    count++;
                    cursor.moveToNext();
                }
                cursor.close();
                final ListView list = (ListView) findViewById(R.id.oldGroupsListView);

                ListAdapter adapter = new ListAdapter(getContext(), items);
                list.setAdapter(adapter);
                list.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // TODO Auto-generated method stub
                        TextView textView = (TextView) view.findViewById(R.id.name);
                        TextView idTextView = (TextView) view.findViewById(R.id.ID);
                        if (!idTextView.getText().toString().equals("")) {

                            for(int x=0; x<items.size();x++)
                            {
                                if( items.get(x).getGroupId().equals(idTextView.getText().toString()))
                                {
                                    Intent intent=null;
                                    switch(items.get(x).getStatusId())
                                    {
                                        case "6":
                                            intent = new Intent(OldGroupsList_Activity.this, OldGroupVoBo_Activity.class);
                                            break;
                                        case "5":
                                            intent = new Intent(OldGroupsList_Activity.this, OldGroupRehire_Activity.class);
                                            break;
                                        case "4":
                                            intent = new Intent(OldGroupsList_Activity.this, OldGroupDisbursement_Activity.class);
                                            break;
                                    }

                                    intent.putExtra("cicle",items.get(x).getCicle());
                                    intent.putExtra("week",items.get(x).getWeek());
                                    intent.putExtra("groupName",items.get(x).getGroupNumber()+"-"+items.get(x).getGroupName());
                                    intent.putExtra("groupId",items.get(x).getGroupId());
                                    startActivity(intent);
                                    finish();
                                    break;
                                }
                            }
                        }
                    }
                });
                mprogressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Se cargo la lista fuera de linea", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(), "No hay nuevos grupos de recontratacion que mostrar", Toast.LENGTH_LONG).show();
            }

        }
    }


    public static Context getContext()
    {
        return context;
    }
}
