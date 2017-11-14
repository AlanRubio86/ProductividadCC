package com.productividadcc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.productividadcc.utilerias.GroupModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NewGroupsListActivity extends AppCompatActivity {

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
                Intent intent = new Intent(NewGroupsListActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.agregaGrupoBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewGroupsListActivity.this, GrupoNuevo.class);
                startActivity(intent);
                finish();
            }
        });




        mprogressBar = (ProgressBar) findViewById(R.id.progressBar);
        seeScheule();
        LoadNewGroups();
    }

    public void LoadNewGroups()
    {
        GroupModel arrayObjetos[]=new GroupModel[4];

        //Creamos objetos en cada posicion
        arrayObjetos[0]=new GroupModel("11-09-2017", "Grupo 23776", "1","1", "","new","25.7081288,-100.31593951");
        arrayObjetos[1]=new GroupModel("13-09-2017", "Grupo 21345", "2","2", "","new","25.7083839,-100.31657691");
        arrayObjetos[2]=new GroupModel("15-09-2017", "Grupo 23776", "3","4","","new","25.7082823,-100.31601878");
        arrayObjetos[3]=new GroupModel("14-09-2017", "Grupo 19556", "4","5","","new","25.70821548,-100.31595457");

        ArrayList<ListCell> items = new ArrayList<ListCell>();
        for (int i=0; i<arrayObjetos.length; i++)
        {
            String statusName = "";
            switch (arrayObjetos[i].get_statusId()) {
                case "1":
                    statusName = "Visto Bueno";
                    break;
                case "2":
                    statusName = "Capacitacion 1";
                    break;
                case "3":
                    statusName = "Capacitacion 2";
                    break;
                case "4":
                    statusName = "Desembolso";
                    break;

            }

            items.add(new ListCell(arrayObjetos[i].get_date()+" "+ arrayObjetos[i].get_name() +" "+statusName,arrayObjetos[i].get_statusId(),arrayObjetos[i].get_Id(),arrayObjetos[i].get_date(), "", arrayObjetos[i].get_Parent(), arrayObjetos[i].get_Ubication()));
        }

        final ListView list = (ListView) findViewById(R.id.groupsListView);

        //items = sortAndAddSections(items);

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
                                 Intent intent=null;
                                switch(idTextView.getText().toString())
                                {
                                    case "1":
                                        intent = new Intent(NewGroupsListActivity.this, NewGroupVoBo.class);
                                        break;
                                    case "2":
                                        intent = new Intent(NewGroupsListActivity.this, GroupTrainingActivity.class);
                                        break;
                                    case "3":
                                        intent = new Intent(NewGroupsListActivity.this, GroupTraining2Activity.class);
                                        break;
                                    case "4":
                                        intent = new Intent(NewGroupsListActivity.this, GroupDisrbursementActivity.class);
                                        break;

                                }
                                intent.putExtra("groupName",textView.getText());
                                intent.putExtra("groupId",idTextView.getText());
                                startActivity(intent);
                                finish();

                    }

            }
        });

        mprogressBar.setVisibility(View.GONE);

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
                            //String[] newAgendaArray;


                            // ListView on item selected listener.

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

