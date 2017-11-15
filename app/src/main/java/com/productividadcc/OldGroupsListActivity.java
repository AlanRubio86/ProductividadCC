package com.productividadcc;

import android.content.Context;
import android.content.SharedPreferences;
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



public class OldGroupsListActivity extends AppCompatActivity {

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
                Intent intent = new Intent(OldGroupsListActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.agregaGrupoBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OldGroupsListActivity.this, OldGroupNewActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mprogressBar = (ProgressBar) findViewById(R.id.progressBar);
        LoadOldGroups();
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
                                items.add(new ListCell(appointmentArray[0].toString(),
                                        appointmentArray[1].toString(),
                                        appointmentArray[2].toString(),
                                        appointmentArray[3].toString(),
                                        appointmentArray[4].toString(),
                                        appointmentArray[5].toString(),
                                        appointmentArray[6].toString(),
                                        appointmentArray[7].toString(),
                                        appointmentArray[8].toString(),
                                        appointmentArray[9].toString(),
                                        appointmentArray[10].toString(),
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
                                                            intent = new Intent(OldGroupsListActivity.this, OldGroupVoBo.class);
                                                            break;
                                                        case "5":
                                                            intent = new Intent(OldGroupsListActivity.this, OldGroupRecontrationActivity.class);
                                                            break;
                                                        case "4":
                                                            intent = new Intent(OldGroupsListActivity.this, OldGroupDisrbursementActivity.class);
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
    public static Context getContext()
    {
        return context;
    }
}
