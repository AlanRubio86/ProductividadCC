package com.productividadcc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.productividadcc.database.Event;
import com.productividadcc.database.NewGroups;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NewGroupsList_Activity extends AppCompatActivity {

    ListView listView;
    String[] agendaArray;
    String[] idArray;
    private static Context context;
    ProgressBar mprogressBar;
    String weekDay;
    String numEmpleado;
    String tokenId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.newgroupslist_activity);
        context = getApplicationContext();

        // Find the toolbar view and set as ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // ...
        // Display icon in the toolbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        // Remove default title text
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Get access to the custom title view
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Nuevos Grupos");

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewGroupsList_Activity.this, Main_Activity.class);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.agregaGrupoBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewGroupsList_Activity.this, NewGroup_Activity.class);
                startActivity(intent);
                finish();
            }
        });
        mprogressBar = (ProgressBar) findViewById(R.id.progressBar);

        if (Utils.isNetworkAvailable(NewGroupsList_Activity.this))
        {
            seeScheule();
        }
        else
            {
                LoadFromDatabase();
            }


    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void seeScheule() {
        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
        SharedPreferences shared = getSharedPreferences("userInfo", MODE_PRIVATE);
        String employeeID = shared.getString("numEmployee", "0");
        String tokenId = shared.getString("tokenId", "0");
        Log.d("WS Login:", "http://asistente.crediclub.com/2.0/consultaAgenda.php?TipCon=1&empleadoID="+employeeID+"&tokenID="+tokenId);



        StringRequest MyStringRequest = new StringRequest(Request.Method.GET,
                "http://asistente.crediclub.com/2.0/consultaAgenda.php?TipCon=1&empleadoID="+employeeID+"&tokenID="+tokenId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //This code is executed if the server responds, whether or not the response contains data.
                        //The String 'response' contains the server's response.
                        Log.d("Schedule", "response: " + response);
                        if (!response.equals("") && response != null) {
                            agendaArray = response.split("<br>");
                            Main_Activity.newGroups = new NewGroups();
                            Main_Activity.newGroups.deleteAll();

                            final ArrayList<ListCell> items = new ArrayList<ListCell>();
                            for(int i=0;i<agendaArray.length;i++)
                            {
                                Main_Activity.newGroups.setItem(agendaArray[i]);
                                Main_Activity.newGroups.insert();

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
                            }
                            final ListView list = (ListView) findViewById(R.id.groupsListView);

                            ListAdapter adapter = new ListAdapter(getContext(), items);
                            list.setAdapter(adapter);
                            list.setOnItemClickListener(new OnItemClickListener()
                            {

                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    // TODO Auto-generated method stub
                                    TextView idTextView = (TextView) view.findViewById(R.id.ID);
                                    if (!idTextView.getText().toString().equals("")) {
                                        for(int x=0; x<items.size();x++)
                                        {
                                            if( items.get(x).getGroupId().equals(idTextView.getText().toString()))
                                            {
                                                Intent intent = null;
                                                switch (items.get(x).getStatusId())
                                                {
                                                    case "3":
                                                        intent = new Intent(NewGroupsList_Activity.this, NewGroupVoBo_Activity.class);
                                                        break;
                                                    case "1":
                                                        intent = new Intent(NewGroupsList_Activity.this, NewGroupTraining_Activity.class);
                                                        break;
                                                    case "2":
                                                        intent = new Intent(NewGroupsList_Activity.this, NewGroupTrainingTwo_Activity.class);
                                                        break;
                                                    case "4":
                                                        intent = new Intent(NewGroupsList_Activity.this, NewGroupDisbursement_Activity.class);
                                                        break;
                                                }
                                                intent.putExtra("groupName", items.get(x).getGroupNumber()+"-"+items.get(x).getGroupName());
                                                intent.putExtra("groupID", items.get(x).getGroupId());
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
                            Toast.makeText(getApplicationContext(), "No hay uevos grupos que mostrar", Toast.LENGTH_LONG).show();
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
                Intent intent = new Intent(NewGroupsList_Activity.this, Main_Activity.class);
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

        NewGroups newGroups = new NewGroups();
        Cursor cursor = newGroups.load();

        int count = 0;
        int total = 0;
        if (cursor != null) {
            cursor.moveToFirst();
            total = cursor.getCount();

            if (total > 0) {

                while (!cursor.isAfterLast()) {
                    NewGroups ev = new NewGroups();
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
                final ListView list = (ListView) findViewById(R.id.groupsListView);

                ListAdapter adapter = new ListAdapter(getContext(), items);
                list.setAdapter(adapter);
                list.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // TODO Auto-generated method stub
                        TextView idTextView = (TextView) view.findViewById(R.id.ID);
                        if (!idTextView.getText().toString().equals("")) {
                            for (int x = 0; x < items.size(); x++) {
                                if (items.get(x).getGroupId().equals(idTextView.getText().toString())) {
                                    Intent intent = null;
                                    switch (items.get(x).getStatusId()) {
                                        case "3":
                                            intent = new Intent(NewGroupsList_Activity.this, NewGroupVoBo_Activity.class);
                                            break;
                                        case "1":
                                            intent = new Intent(NewGroupsList_Activity.this, NewGroupTraining_Activity.class);
                                            break;
                                        case "2":
                                            intent = new Intent(NewGroupsList_Activity.this, NewGroupTrainingTwo_Activity.class);
                                            break;
                                        case "4":
                                            intent = new Intent(NewGroupsList_Activity.this, NewGroupDisbursement_Activity.class);
                                            break;
                                    }
                                    intent.putExtra("groupName", items.get(x).getGroupNumber() + "-" + items.get(x).getGroupName());
                                    intent.putExtra("groupID", items.get(x).getGroupId());
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
                Toast.makeText(getApplicationContext(), "No hay nuevos grupos que mostrar", Toast.LENGTH_LONG).show();
            }

        }
    }




    public static Context getContext()
    {
        return context;
    }
}

