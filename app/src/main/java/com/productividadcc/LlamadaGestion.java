package com.productividadcc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.productividadcc.database.Event;
import com.productividadcc.utilerias.Globales;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class LlamadaGestion extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener {

    private TextView nombreLbl;
    private EditText numGrupo;
    private EditText ciclo;
    private EditText semana;
    private Spinner llamada;
    private Spinner ficha;
    private Spinner registros;

    private Calendar calendar;
    TextView fechaTxt, horaTxt;

    String imeiNumber;
    String eventID;
    String hizoLlamada;
    String cerroFicha;
    String subioRegistros;

    String strGrupo;
    String strCiclo;
    String strSemana;
    String nombreGrupo;

    String URLV = Globales.URL_REGISTRO_VISITA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.llamadagestion_activity);

        // Find the toolbar view and set as ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // ...
        // Remove default title text
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Get access to the custom title view
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        llamada = (Spinner) findViewById(R.id.gestionsi);
        ficha = (Spinner) findViewById(R.id.gestionsi);
        registros = (Spinner) findViewById(R.id.gestionsi);
        mTitle.setText("Llamada de Gestion");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LlamadaGestion.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        imeiNumber = telephonyManager.getDeviceId();

        nombreLbl = (TextView) findViewById(R.id.nombreLabel);
        if (getIntent().getExtras() != null) {
            String groupName = getIntent().getExtras().getString("groupName");
            nombreLbl.setText(groupName);
            eventID = getIntent().getExtras().getString("eventId").replace(" ", "");
        }else{
            eventID = "0";
        }

        numGrupo = (EditText) findViewById(R.id.numGrupo);
        ciclo = (EditText) findViewById(R.id.ciclo);
        semana = (EditText) findViewById(R.id.semana);

        calendar = Calendar.getInstance();
        fechaTxt = (TextView) findViewById(R.id.fechaTxt);
        findViewById(R.id.fechaTxt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.newInstance(LlamadaGestion.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show(getFragmentManager(), "datePicker");
            }
        });

        horaTxt = (TextView) findViewById(R.id.horaTxt);
        findViewById(R.id.horaTxt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.newInstance(LlamadaGestion.this, calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), true).show(getFragmentManager(), "timePicker");
            }
        });

        findViewById(R.id.guardarBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!numGrupo.getText().toString().equals("")) {
                    if (Utils.isNetworkAvailable(LlamadaGestion.this)) {
                        sendEventInfo();
                    } else {
                        saveEventInfo();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Faltan datos por capturar", Toast.LENGTH_LONG).show();
                }
            }
        });

        /*findViewById(R.id.cerrarBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LlamadaGestion.this, AgendaActivity.class);
                startActivity(intent);
                finish();
            }
        });*/
    }

        public void sendEventInfo() {
            SimpleDateFormat formatFec = new SimpleDateFormat("yyyy-MM-dd");
            final String selectedStringDate = fechaTxt.getText().toString();

            final SharedPreferences shared = getSharedPreferences("userInfo", MODE_PRIVATE);

            int id = llamada.getSelectedItemPosition();
            if (id == Globales.GESTION_SI){
                hizoLlamada = "1";
            } else if( id == Globales.GESTION_NO){
                hizoLlamada = "0";
            }

            int id2 = ficha.getSelectedItemPosition();
            if (id2 == Globales.GESTION_SI){
                cerroFicha = "1";
            } else if( id2 == Globales.GESTION_NO){
                cerroFicha = "0";
            }

            int id3 = registros.getSelectedItemPosition();
            if (id3 == Globales.GESTION_SI){
                subioRegistros = "1";
            } else if( id3 == Globales.GESTION_NO){
                subioRegistros = "0";
            }

            strGrupo = numGrupo.getText().toString();
            strCiclo = ciclo.getText().toString();
            strSemana = semana.getText().toString();

            /* GENERA URL PARA GUARDAR INFORMACION DE LA VISITA */
            URLV += "phone=" + imeiNumber +
                    "&group=" + strGrupo +
                    "&ciclo=" + strCiclo +
                    "&semana=" + strSemana +
                    "&integrantes=" + Globales.STR_VACIO +
                    "&event=Llamada" +
                    "&stamp=" + (System.currentTimeMillis()/1000) +
                    "&latitude=" + shared.getString("latitude", "0") +
                    "&longitude=" + shared.getString("longitude", "0") +
                    "&asistencia=" + Globales.STR_VACIO +
                    "&pago=" + Globales.STR_VACIO +
                    "&ahorro=" + Globales.STR_VACIO +
                    "&grupoNoAsiste=" + Globales.STR_VACIO +
                    "&noVisitoGrupo=" + Globales.STR_VACIO +
                    "&visit=" + Globales.LLAMADA_GESTION +
                    "&llamada=" + hizoLlamada +
                    "&cerroFicha=" + cerroFicha +
                    "&subenReg=" + subioRegistros +
                    "&fechaLla=" +selectedStringDate +
                    "&horaLla=" + horaTxt.getText().toString() +
                    "&nuAgSe=" + eventID ;

            Log.d("WS Visita:", URLV);

        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
        StringRequest MyStringRequest = new StringRequest(Request.Method.GET, URLV,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //This code is executed if the server responds, whether or not the response contains data.
                        //The String 'response' contains the server's response.
                        clearFields();
                        Toast.makeText(getApplicationContext(), "Evento guardado correctamente " + response, Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(LlamadaGestion.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
                Log.d("Send Event info", "response error" + error.toString());
                Toast.makeText(getApplicationContext(), "Error de conexión, por favor vuelve a intentar: " + error.toString(), Toast.LENGTH_LONG).show();
                //mprogressBar.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                //params.put("fecha", fechaTxt);
                //params.put("hora", horaTxt);
                params.put("tipEve", "4");
                params.put("emplea", shared.getString("userNumber", "0"));
                params.put("grupo", "%20");
                params.put("ciclo", "%20");
                params.put("latitu", shared.getString("latitude", "0"));
                params.put("longit", shared.getString("longitude", "0"));
                params.put("nuAgSe", eventID);
                params.put("imei", imeiNumber);
                params.put("stamp", String.valueOf(System.currentTimeMillis()/1000));
                params.put("monGru", Globales.STR_CERO);
                params.put("integr", Globales.STR_CERO);
                params.put("semRen", "0");
                params.put("coment", "%20");

                return params;
            }
        };

        MyRequestQueue.add(MyStringRequest);
    }

    public void saveEventInfo() {
        SimpleDateFormat formatFec = new SimpleDateFormat("yyyy-MM-dd");
        final String selectedStringDate = fechaTxt.getText().toString();

        final SharedPreferences shared = getSharedPreferences("userInfo", MODE_PRIVATE);

        int id = llamada.getSelectedItemPosition();
        if (id == Globales.GESTION_SI){
            hizoLlamada = "1";
        } else if( id == Globales.GESTION_NO){
            hizoLlamada = "0";
        }

        int id2 = ficha.getSelectedItemPosition();
        if (id2 == Globales.GESTION_SI){
            cerroFicha = "1";
        } else if( id2 == Globales.GESTION_NO){
            cerroFicha = "0";
        }

        int id3 = registros.getSelectedItemPosition();
        if (id3 == Globales.GESTION_SI){
            subioRegistros = "1";
        } else if( id3 == Globales.GESTION_NO){
            subioRegistros = "0";
        }

        strGrupo = numGrupo.getText().toString();
        strCiclo = ciclo.getText().toString();
        strSemana = semana.getText().toString();

        /* GENERA URL PARA GUARDAR INFORMACION DE LA VISITA */
        URLV += "phone=" + imeiNumber +
                "&group=" + strGrupo +
                "&ciclo=" + strCiclo +
                "&semana=" + strSemana +
                "&integrantes=" + Globales.STR_VACIO +
                "&event=Llamada" +
                "&stamp=" + (System.currentTimeMillis()/1000) +
                "&latitude=" + shared.getString("latitude", "0") +
                "&longitude=" + shared.getString("longitude", "0") +
                "&asistencia=" + Globales.STR_VACIO +
                "&pago=" + Globales.STR_VACIO +
                "&ahorro=" + Globales.STR_VACIO +
                "&grupoNoAsiste=" + Globales.STR_VACIO +
                "&noVisitoGrupo=" + Globales.STR_VACIO +
                "&visit=" + Globales.LLAMADA_GESTION +
                "&llamada=" + hizoLlamada +
                "&cerroFicha=" + cerroFicha +
                "&subenReg=" + subioRegistros +
                "&fechaLla=" +selectedStringDate +
                "&horaLla=" + horaTxt.getText().toString() +
                "&nuAgSe=" + eventID ;

        Log.d("WS Visita:", URLV);

        MainActivity.event = new Event();
        MainActivity.event.setUrlWS(URLV);
        MainActivity.event.setStatus(0);
        MainActivity.event.insert();

        clearFields();
        Toast.makeText(getApplicationContext(), "Los datos han sido guardados de manera offline", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(LlamadaGestion.this, MainActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
        java.text.DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        calendar.set(year, monthOfYear, dayOfMonth);
        fechaTxt.setText(df.format(calendar.getTime()));
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        String time = String.format(Locale.ENGLISH, "%02d:%02d", hourOfDay, minute);
        horaTxt.setText(time);

    }

    public void clearFields () {
        numGrupo.setText("");
    }
}
