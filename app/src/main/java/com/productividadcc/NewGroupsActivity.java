package com.productividadcc;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.productividadcc.utilerias.GroupModel;

import java.util.ArrayList;

public class NewGroupsActivity extends AppCompatActivity {

    ListView listView;
    String[] agendaArray;
    String[] idArray;
    private static Context context;
    ProgressBar mprogressBar;
    String weekDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.newgroups_activity);
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
                Intent intent = new Intent(NewGroupsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.agregaGrupoBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewGroupsActivity.this, GrupoNuevo.class);
                startActivity(intent);
                finish();
            }
        });

        mprogressBar = (ProgressBar) findViewById(R.id.progressBar);
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
                                        intent = new Intent(NewGroupsActivity.this, NewGroupVoBo.class);
                                        break;
                                    case "2":
                                        intent = new Intent(NewGroupsActivity.this, GroupTrainingActivity.class);
                                        break;
                                    case "3":
                                        intent = new Intent(NewGroupsActivity.this, GroupTraining2Activity.class);
                                        break;
                                    case "4":
                                        intent = new Intent(NewGroupsActivity.this, GroupDisrbursementActivity.class);
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
/*
    public void seeScheule() {
        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
        SharedPreferences shared = getSharedPreferences("userInfo", MODE_PRIVATE);
        String empleadoID = shared.getString("userNumber", "0");

        StringRequest MyStringRequest = new StringRequest(Request.Method.GET,
                "http://asistente.crediclub.com/2.0/consultaAgenda.php?empleadoID="+empleadoID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //This code is executed if the server responds, whether or not the response contains data.
                        //The String 'response' contains the server's response.
                        Log.d("Schedule", "response: " + response);
                        if (!response.equals("") && response != null) {
                            agendaArray = response.split("<br>");
                            //String[] newAgendaArray;
                            ArrayList<String> newAgendaArray = new ArrayList<String>();
                            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
                            Calendar calendar = Calendar.getInstance();
                            weekDay = dayFormat.format(calendar.getTime());

                            int cDay = calendar.get(Calendar.DAY_OF_MONTH);
                            int cMonth = calendar.get(Calendar.MONTH) + 1;
                            int cYear = calendar.get(Calendar.YEAR);

                            ArrayList<String> mondayArray = new ArrayList<String>();
                            mondayArray.add("Monday");
                            ArrayList<String> tuesdayArray = new ArrayList<String>();
                            tuesdayArray.add("Tuesday");
                            ArrayList<String> wednesdayArray = new ArrayList<String>();
                            wednesdayArray.add("Wednesday");
                            ArrayList<String> thursdayArray = new ArrayList<String>();
                            thursdayArray.add("Thursday");
                            ArrayList<String> fridayArray = new ArrayList<String>();
                            fridayArray.add("Friday");

                            ArrayList<ListCell> items = new ArrayList<ListCell>();
                            ArrayList<String> idList = new ArrayList<String>();

                            for (int i=0; i<agendaArray.length; i++) {
                                String[] appointmentArray = agendaArray[i].split(", ");
                                Date date = null;
                                try {
                                    date = new SimpleDateFormat("yyyy-MM-dd").parse(appointmentArray[1]);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                String tipoEvento = "";
                                String  input = appointmentArray[3];
                                input = input.replace(" ", "");
                                switch (input) {
                                    case "1":
                                        tipoEvento = "Agendar Capacitacion 1";
                                        break;
                                    case "2":
                                        tipoEvento = "Agendar NewGroupVoBo Renovación";
                                        break;
                                    case "3":
                                        tipoEvento = "Agendar Cobranza Temprana";
                                        break;
                                    case "4":
                                        tipoEvento = "Capacitación 1";
                                        break;
                                    case "5":
                                        tipoEvento = "Capacitación 2";
                                        break;
                                    case "6":
                                        tipoEvento = "NewGroupVoBo";
                                        break;
                                    case "7":
                                        tipoEvento = "NewGroupVoBo Renovación";
                                        break;
                                    case "8":
                                        tipoEvento = "Desembolso";
                                        break;
                                    case "9":
                                        tipoEvento = "Evaluación Semanal";
                                        break;
                                    case "10":
                                        tipoEvento = "Verificación";
                                        break;
                                    case "11":
                                        tipoEvento = "Promoción";
                                        break;
                                    case "12":
                                        tipoEvento = "Llamada Gestion";
                                        break;

                                }

                                idList.add(appointmentArray[0]);
                                String fechaFormato = Globales.STR_VACIO;
                                fechaFormato = Globales.convierteTexto(appointmentArray[1].replace(" ",""));
                                String currentDay = new SimpleDateFormat("EEEE").format(date);


                                //Log.d("appointmentArray[0]", appointmentArray[0].toString());
                            }
                            idArray = new String[idList.size()];
                            idList.toArray(idArray);

                            final ListView list = (ListView) findViewById(R.id.agendaListView);
                            list.setItemsCanFocus(true);



                            ListAdapter adapter = new ListAdapter(getContext(), items);
                            list.setAdapter(adapter);

                            // ListView on item selected listener.
                            list.setOnItemClickListener(new OnItemClickListener()
                            {

                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    // TODO Auto-generated method stub

                                    if (view.findViewById(R.id.section_header) == null) {
                                        TextView textView = (TextView) view.findViewById(R.id.name);
                                        TextView idTextView = (TextView) view.findViewById(R.id.ID);
                                        String currentText = textView.getText().toString();

                                        if (!idTextView.getText().toString().equals("")) {
                                            for (int i=0; i<agendaArray.length; i++) {
                                                String[] appointmentArray = agendaArray[i].split(", ");
                                                String  appointmentID = idTextView.getText().toString().replace(" ", "");
                                                String appointmentArrayID = appointmentArray[0].replace(" ", "");
                                                if (appointmentArrayID.equals(appointmentID)) {

                                                    Intent intent = new Intent(NewGroupsActivity.this, AgendaLoader.class);
                                                    intent.putExtra("data", appointmentArray[3]);
                                                    intent.putExtra("groupName", appointmentArray[5]);
                                                    intent.putExtra("eventId", appointmentArray[0]);
                                                    startActivity(intent);
                                                    finish();
                                                    break;
                                                }
                                            }
                                        } else {
                                            //Show location in Google Maps
                                            //Toast.makeText(AgendaActivity.this, selectedEventArray[7] +","+ selectedEventArray[8], Toast.LENGTH_SHORT).show();
                                            String url = "http://maps.google.com/maps?daddr=" + currentText;
                                            Intent goZe = new Intent(Intent.ACTION_VIEW);
                                            goZe.setData(Uri.parse(url));
                                            startActivity(goZe);
                                        }
                                    }
                                }
                            });
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
*/
    public static Context getContext()
    {
        return context;
    }
}

