package com.productividadcc;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
import android.content.Intent;
import android.view.View;
import android.net.Uri;
import android.widget.TableRow;
import android.widget.TableLayout;
import android.widget.LinearLayout;
import android.support.v7.widget.Toolbar.LayoutParams;
import android.graphics.Color;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.view.ViewGroup;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.content.res.Configuration;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.ArrayList;
import java.util.Collections;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.productividadcc.utilerias.Globales;
import com.productividadcc.utilerias.GroupModel;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class OldGroupsActivity extends AppCompatActivity {

    ListView listView;
    String[] agendaArray;
    String[] idArray;
    private static Context context;
    ProgressBar mprogressBar;
    String weekDay;
    ArrayList<ListCell> items = new ArrayList<ListCell>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.oldgroupsactivity);
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
                Intent intent = new Intent(OldGroupsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.agregaGrupoBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OldGroupsActivity.this, OldGroupNewActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mprogressBar = (ProgressBar) findViewById(R.id.progressBar);
        loadOldGroups();
        mprogressBar.setVisibility(View.GONE);
        /*findViewById(R.id.testLabel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://maps.google.com/maps?daddr=25.6787037,-100.2864898";
                Intent goZe = new Intent(Intent.ACTION_VIEW);
                goZe.setData(Uri.parse(url));
                startActivity(goZe);
            }
        });*/
    }

   private ArrayList<ListCell> sortAndAddSections(ArrayList<ListCell> itemList)
    {

        ArrayList<ListCell> tempList = new ArrayList<ListCell>();
        //First we sort the array
        //Collections.sort(itemList);

        //Loops thorugh the list and add a section before each sectioncell start
        String header ="";
        for(int i = 0; i < itemList.size(); i++)
        {
            //If it is the start of a new section we create a new listcell and add it to our array
            if(!header.equalsIgnoreCase(itemList.get(i).getCategory())){
                ListCell sectionCell = new ListCell(itemList.get(i).getName(), itemList.get(i).getStatusId(),
                        itemList.get(i).getId(), itemList.get(i).getDate() , itemList.get(i).getCategory(),itemList.get(i).getParent(),itemList.get(i).getUbication());
                sectionCell.setToSectionHeader();
                tempList.add(sectionCell);
                header = itemList.get(i).getCategory();
            }

            //if(!itemList.get(i).getParent())
            tempList.add(itemList.get(i));
        }

        return tempList;
    }

    public void loadOldGroups() {
        GroupModel arrayObjetos[] = new GroupModel[3];

        //Creamos objetos en cada posicion
        arrayObjetos[0] = new GroupModel("11-09-2017", "Grupo 23776", "1", "1", "1","old","25.7081288,-100.31593951");
        arrayObjetos[1] = new GroupModel("13-09-2017", "Grupo 21345", "2", "2", "2","old","25.7083839,-100.31657691");
        arrayObjetos[2] = new GroupModel("14-09-2017", "Grupo 23776", "3", "3", "1","old","25.7080813,-100.31587884");


        for (int i = 0; i < arrayObjetos.length; i++) {
            String statusName = "",categoryName="";
            switch (arrayObjetos[i].get_statusId()) {
                case "1":
                    statusName = "Capacitacion 1";
                    break;
                case "2":
                    statusName = "Capacitacion 2";
                    break;
            }

            switch (arrayObjetos[i].getCategory()) {
                case "1":
                    categoryName = "En gestión";
                    break;
                case "2":
                    categoryName = "Sin gestionar";
                    break;
            }


            items.add(new ListCell(arrayObjetos[i].get_date() + " " + arrayObjetos[i].get_name() + " " + statusName, arrayObjetos[i].get_statusId(), arrayObjetos[i].get_Id(), arrayObjetos[i].get_date(), arrayObjetos[i].getCategory(), arrayObjetos[i].get_Parent(),arrayObjetos[i].get_Ubication()));
            //items.add(new ListCell(categoryName,"","","",arrayObjetos[i].getCategory(),true));

        }
        final ListView list = (ListView) findViewById(R.id.oldGroupsListView);

        items = sortAndAddSections(items);

        ListAdapter adapter = new ListAdapter(getContext(), items);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                if (view.findViewById(R.id.section_header) == null) {

                    TextView textView = (TextView) view.findViewById(R.id.name);
                    TextView idTextView = (TextView) view.findViewById(R.id.ID);
                        for (int i=0; i<items.size(); i++) {
                            String  itemId = idTextView.getText().toString();
                            if (items.get(i).getId().equals(itemId))
                            {
                                    if (!idTextView.getText().toString().equals(""))
                                    {
                                        Intent intent = null;
                                        switch (items.get(i).getStatusId()) {
                                            case "2":
                                                intent = new Intent(OldGroupsActivity.this,OldGroupVoBo.class);
                                                break;
                                            case "3":
                                                intent = new Intent(OldGroupsActivity.this, OldGroupDisrbursementActivity.class);
                                                break;
                                            case "1":
                                                intent = new Intent(OldGroupsActivity.this, OldGroupRecontrationActivity.class);
                                                break;
                                        }
                                        intent.putExtra("groupName", textView.getText());
                                        intent.putExtra("groupId", idTextView.getText());
                                        startActivity(intent);
                                        finish();

                                    }
                            }
                        }
                }

            }
        });

    }

    // Menu icons are inflated just as they were with actionbar
        /*public void seeScheule() {
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
                                    if (currentDay.equals("lunes") || currentDay.equals("Monday")) {
                                        //mondayArray.add(agendaArray[i]);
                                        if (!appointmentArray[5].equals(" ")) {
                                            items.add(new ListCell(appointmentArray[2] + " " + tipoEvento + ", Grupo: " + appointmentArray[5], "Lunes "+ fechaFormato, appointmentArray[0]));
                                        } else {
                                            items.add(new ListCell(appointmentArray[2] + " " + tipoEvento, "Lunes "+ fechaFormato, appointmentArray[0]));
                                        }
                                        items.add(new ListCell(appointmentArray[7]+", "+appointmentArray[8],  "Lunes "+ fechaFormato, null));
                                    } else if (currentDay.equals("martes") || currentDay.equals("Tuesday")) {
                                        //tuesdayArray.add(agendaArray[i]);
                                        if (!appointmentArray[5].equals(" ")) {
                                            items.add(new ListCell(appointmentArray[2] + " " + tipoEvento + ", Grupo: " + appointmentArray[5], "Martes "+ fechaFormato, appointmentArray[0]));
                                        } else {
                                            items.add(new ListCell(appointmentArray[2] + " " + tipoEvento, "Martes "+ fechaFormato, appointmentArray[0]));
                                        }
                                        items.add(new ListCell(appointmentArray[7]+", "+appointmentArray[8],  "Martes "+ fechaFormato, null));
                                    } else if (currentDay.equals("miércoles") || currentDay.equals("Wednesday")) {
                                        //wednesdayArray.add(agendaArray[i]);
                                        if (!appointmentArray[5].equals(" ")) {
                                            items.add(new ListCell(appointmentArray[2] + " " + tipoEvento + ", Grupo: " + appointmentArray[5], "Miércoles "+ fechaFormato, appointmentArray[0]));
                                        } else {
                                            items.add(new ListCell(appointmentArray[2] + " " + tipoEvento, "Miércoles "+ fechaFormato, appointmentArray[0]));
                                        }
                                        items.add(new ListCell(appointmentArray[7]+", "+appointmentArray[8],  "Miércoles "+ fechaFormato, null));
                                    } else if (currentDay.equals("jueves") || currentDay.equals("Thursday")) {
                                        //thursdayArray.add(agendaArray[i]);
                                        if (!appointmentArray[5].equals(" ")) {
                                            items.add(new ListCell(appointmentArray[2] + " " + tipoEvento + ", Grupo: " + appointmentArray[5], "Jueves "+ fechaFormato, appointmentArray[0]));
                                        } else {
                                            items.add(new ListCell(appointmentArray[2] + " " + tipoEvento, "Jueves "+ fechaFormato, appointmentArray[0]));
                                        }
                                        items.add(new ListCell(appointmentArray[7]+", "+appointmentArray[8],  "Jueves "+ fechaFormato, null));
                                    } else if (currentDay.equals("viernes") || currentDay.equals("Friday")) {
                                        //fridayArray.add(agendaArray[i]);
                                        if (!appointmentArray[5].equals(" ")) {
                                            items.add(new ListCell(appointmentArray[2] + " " + tipoEvento + ", Grupo: " + appointmentArray[5], "Viernes "+ fechaFormato, appointmentArray[0]));
                                        } else {
                                            items.add(new ListCell(appointmentArray[2] + " " + tipoEvento, "Viernes "+ fechaFormato, appointmentArray[0]));
                                        }
                                        items.add(new ListCell(appointmentArray[7]+", "+appointmentArray[8],  "Viernes "+ fechaFormato, null));
                                    } else if (currentDay.equals("sábado") || currentDay.equals("Saturday")) {
                                        //fridayArray.add(agendaArray[i]);
                                        if (!appointmentArray[5].equals(" ")) {
                                            items.add(new ListCell(appointmentArray[2] + " " + tipoEvento + ", Grupo: " + appointmentArray[5], "Sábado "+ fechaFormato, appointmentArray[0]));
                                        } else {
                                            items.add(new ListCell(appointmentArray[2] + " " + tipoEvento, "Sábado "+ fechaFormato, appointmentArray[0]));
                                        }
                                        items.add(new ListCell(appointmentArray[7]+", "+appointmentArray[8],  "Sábado "+ fechaFormato, null));
                                    } else if (currentDay.equals("domingo") || currentDay.equals("Sunday")) {
                                        //fridayArray.add(agendaArray[i]);
                                        if (!appointmentArray[5].equals(" ")) {
                                            items.add(new ListCell(appointmentArray[2] + " " + tipoEvento + ", Grupo: " + appointmentArray[5], "Domingo "+ fechaFormato, appointmentArray[0]));
                                        } else {
                                            items.add(new ListCell(appointmentArray[2] + " " + tipoEvento, "Domingo "+ fechaFormato, appointmentArray[0]));
                                        }
                                        items.add(new ListCell(appointmentArray[7]+", "+appointmentArray[8],  "Domingo "+ fechaFormato, null));
                                    }

                                    //Log.d("appointmentArray[0]", appointmentArray[0].toString());
                                }
                                idArray = new String[idList.size()];
                                idList.toArray(idArray);

                                final ListView list = (ListView) findViewById(R.id.agendaListView);

                                items = sortAndAddSections(items);

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

                                                        Intent intent = new Intent(AgendaActivity.this, AgendaLoader.class);
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
