package com.productividadcc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Bitacora extends AppCompatActivity {

    ListView listView;
    String[] agendaArray;
    String[] idArray;
    private static Context context;
    ProgressBar mprogressBar;
    String weekDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*String languageToLoad  = "es"; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());*/

        setContentView(R.layout.bitacora_activity);
        context = getApplicationContext();

        // Find the toolbar view and set as ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // ...
        // Display icon in the toolbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        // Remove default title text
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Get access to the custom title view
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Bitácora de Eventos Cerrados");

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Bitacora.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mprogressBar = (ProgressBar) findViewById(R.id.progressBar);
        seeScheule();

    }

    private ArrayList<ListCell> sortAndAddSections(ArrayList<ListCell> itemList)
    {

        ArrayList<ListCell> tempList = new ArrayList<ListCell>();
        //First we sort the array
        //Collections.sort(itemList);

        //Loops thorugh the list and add a section before each sectioncell start
        String header = "";
        for(int i = 0; i < itemList.size(); i++)
        {
            //If it is the start of a new section we create a new listcell and add it to our array
            if(!header.equals(itemList.get(i).getCategory())){
                ListCell sectionCell = new ListCell(itemList.get(i).getCategory(), null, null);
                sectionCell.setToSectionHeader();
                tempList.add(sectionCell);
                header = itemList.get(i).getCategory();
            }
            tempList.add(itemList.get(i));
        }

        return tempList;
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
        String empleadoID = shared.getString("userNumber", "0");
        StringRequest MyStringRequest = new StringRequest(Request.Method.GET,
                "http://asistente.crediclub.com/2.0/consultaBitacora.php?empleadoID="+empleadoID,
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
                                        tipoEvento = "Agendar VoBo Renovación";
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
                                        tipoEvento = "VoBo";
                                        break;
                                    case "7":
                                        tipoEvento = "VoBo Renovación";
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
                                        tipoEvento = "LLamada de Gestion";
                                        break;

                                }

                                idList.add(appointmentArray[0]);
                                String currentDay = new SimpleDateFormat("EEEE").format(date);
                                if (currentDay.equals("lunes") || currentDay.equals("Monday")) {
                                    //mondayArray.add(agendaArray[i]);
                                    if (!appointmentArray[5].equals(" ")) {
                                        items.add(new ListCell(tipoEvento + " " + appointmentArray[2] + ", Grupo: " + appointmentArray[5], "Lunes "+ appointmentArray[1].replace(" ",""), appointmentArray[0]));
                                    } else {
                                        items.add(new ListCell(tipoEvento + " " + appointmentArray[2], "Lunes "+ appointmentArray[1].replace(" ",""), appointmentArray[0]));
                                    }
                                    //items.add(new ListCell(appointmentArray[7]+", "+appointmentArray[8],  "Lunes "+ appointmentArray[1].replace(" ",""), null));
                                } else if (currentDay.equals("martes") || currentDay.equals("Tuesday")) {
                                    //tuesdayArray.add(agendaArray[i]);
                                    if (!appointmentArray[5].equals(" ")) {
                                        items.add(new ListCell(tipoEvento + " " + appointmentArray[2] + ", Grupo: " + appointmentArray[5], "Martes "+ appointmentArray[1].replace(" ",""), appointmentArray[0]));
                                    } else {
                                        items.add(new ListCell(tipoEvento + " " + appointmentArray[2], "Martes "+ appointmentArray[1].replace(" ",""), appointmentArray[0]));
                                    }
                                    //items.add(new ListCell(appointmentArray[7]+", "+appointmentArray[8],  "Martes "+ appointmentArray[1].replace(" ",""), null));
                                } else if (currentDay.equals("miércoles") || currentDay.equals("Thursday")) {
                                    //wednesdayArray.add(agendaArray[i]);
                                    if (!appointmentArray[5].equals(" ")) {
                                        items.add(new ListCell(tipoEvento + " " + appointmentArray[2] + ", Grupo: " + appointmentArray[5], "Miércoles "+ appointmentArray[1].replace(" ",""), appointmentArray[0]));
                                    } else {
                                        items.add(new ListCell(tipoEvento + " " + appointmentArray[2], "Miércoles "+ appointmentArray[1].replace(" ",""), appointmentArray[0]));
                                    }
                                    //items.add(new ListCell(appointmentArray[7]+", "+appointmentArray[8],  "Miércoles "+ appointmentArray[1].replace(" ",""), null));
                                } else if (currentDay.equals("jueves") || currentDay.equals("Wednesday")) {
                                    //thursdayArray.add(agendaArray[i]);
                                    if (!appointmentArray[5].equals(" ")) {
                                        items.add(new ListCell(tipoEvento + " " + appointmentArray[2] + ", Grupo: " + appointmentArray[5], "Jueves "+ appointmentArray[1].replace(" ",""), appointmentArray[0]));
                                    } else {
                                        items.add(new ListCell(tipoEvento + " " + appointmentArray[2], "Jueves "+ appointmentArray[1].replace(" ",""), appointmentArray[0]));
                                    }
                                    //items.add(new ListCell(appointmentArray[7]+", "+appointmentArray[8],  "Jueves "+ appointmentArray[1].replace(" ",""), null));
                                } else if (currentDay.equals("viernes") || currentDay.equals("Friday")) {
                                    //fridayArray.add(agendaArray[i]);
                                    if (!appointmentArray[5].equals(" ")) {
                                        items.add(new ListCell(tipoEvento + " " + appointmentArray[2] + ", Grupo: " + appointmentArray[5], "Viernes "+ appointmentArray[1].replace(" ",""), appointmentArray[0]));
                                    } else {
                                        items.add(new ListCell(tipoEvento + " " + appointmentArray[2], "Viernes "+ appointmentArray[1].replace(" ",""), appointmentArray[0]));
                                    }
                                    //items.add(new ListCell(appointmentArray[7]+", "+appointmentArray[8],  "Viernes "+ appointmentArray[1].replace(" ",""), null));
                                } else if (currentDay.equals("sábado") || currentDay.equals("Saturday")) {
                                    //fridayArray.add(agendaArray[i]);
                                    if (!appointmentArray[5].equals(" ")) {
                                        items.add(new ListCell(tipoEvento + " " + appointmentArray[2] + ", Grupo: " + appointmentArray[5], "Sábado "+ appointmentArray[1].replace(" ",""), appointmentArray[0]));
                                    } else {
                                        items.add(new ListCell(tipoEvento + " " + appointmentArray[2], "Sábado "+ appointmentArray[1].replace(" ",""), appointmentArray[0]));
                                    }
                                    //items.add(new ListCell(appointmentArray[7]+", "+appointmentArray[8],  "Sábado "+ appointmentArray[1].replace(" ",""), null));
                                } else if (currentDay.equals("domingo") || currentDay.equals("Sunday")) {
                                    //fridayArray.add(agendaArray[i]);
                                    if (!appointmentArray[5].equals(" ")) {
                                        items.add(new ListCell(tipoEvento + " " + appointmentArray[2] + ", Grupo: " + appointmentArray[5], "Domingo "+ appointmentArray[1].replace(" ",""), appointmentArray[0]));
                                    } else {
                                        items.add(new ListCell(tipoEvento + " " + appointmentArray[2], "Domingo "+ appointmentArray[1].replace(" ",""), appointmentArray[0]));
                                    }
                                    //items.add(new ListCell(appointmentArray[7]+", "+appointmentArray[8],  "Domingo "+ appointmentArray[1].replace(" ",""), null));
                                }
                            }
                            idArray = new String[idList.size()];
                            idList.toArray(idArray);

                            final ListView list = (ListView) findViewById(R.id.agendaListView);

                            items = sortAndAddSections(items);

                            ListAdapter adapter = new ListAdapter(getContext(), items);
                            list.setAdapter(adapter);

                            // ListView on item selected listener.
                            /*list.setOnItemClickListener(new AdapterView.OnItemClickListener()
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

                                                    Intent intent = new Intent(Bitacora.this, AgendaLoader.class);
                                                    intent.putExtra("data", appointmentArray[3]);
                                                    intent.putExtra("groupName", appointmentArray[5]);
                                                    intent.putExtra("eventId", appointmentArray[9]);
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
                            });*/
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
                Toast.makeText(getApplicationContext(), "Error de conexión, por favor vuelve a intentar: "+error.toString(), Toast.LENGTH_LONG).show();
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