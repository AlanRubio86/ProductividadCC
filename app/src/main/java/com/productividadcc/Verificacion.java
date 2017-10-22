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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.productividadcc.database.Event;
import com.productividadcc.utilerias.Globales;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Verificacion extends AppCompatActivity {
    String imeiNumber;
    EditText grupoTxt, notasTxt;

    String URL = Globales.URL_REGISTRO_AGENDA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verificacion_activity);

        // Find the toolbar view and set as ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // ...
        // Remove default title text
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Get access to the custom title view
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Verificación");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Verificacion.this, AgregarEvento.class);
                startActivity(intent);
                finish();
            }
        });

        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        imeiNumber = telephonyManager.getDeviceId();

        grupoTxt = (EditText) findViewById(R.id.grupoTxt);
        notasTxt = (EditText) findViewById(R.id.notasTxt);

        findViewById(R.id.guardarBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!grupoTxt.getText().toString().equals("") && !notasTxt.getText().toString().equals("")) {
                    if (Utils.isNetworkAvailable(Verificacion.this)) {
                        sendEventInfo();
                    } else {
                        saveEventInfo();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Faltan datos por capturar", Toast.LENGTH_LONG).show();
                }
            }
        });

        findViewById(R.id.cerrarBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Verificacion.this, AgendaActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void sendEventInfo() {
        final String notes = notasTxt.getText().toString();
        final String newNotes = notes.replace(" ", "").trim();

        final SharedPreferences shared = getSharedPreferences("userInfo", MODE_PRIVATE);

        URL +=  "fecha=" + Globales.STR_VACIO +
                "&hora=" + Globales.STR_VACIO +
                "&tipEve=" + Globales.VERIFICACION +
                "&emplea=" + shared.getString("userNumber", "0") +
                "&tipgru=" + Globales.STR_VACIO +
                "&grupo=" + grupoTxt.getText().toString() +
                "&ciclo=" + Globales.STR_VACIO +
                "&latitu=" + shared.getString("latitude", "0") +
                "&longit=" + shared.getString("longitude", "0") +
                "&nuAgSe=" + Globales.STR_CERO +
                "&imei=" + imeiNumber +
                "&stamp=" + (System.currentTimeMillis()/1000) +
                "&monGru=" + Globales.STR_CERO +
                "&integr=" + Globales.STR_CERO +
                "&semRen=" + Globales.STR_CERO +
                "&coment=" + newNotes;

        Log.d("WS Veri:", URL);

        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
        StringRequest MyStringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //This code is executed if the server responds, whether or not the response contains data.
                        //The String 'response' contains the server's response.
                        clearFields();
                        Toast.makeText(getApplicationContext(), "Evento guardado correctamente " + response, Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(Verificacion.this, MainActivity.class);
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
                params.put("fecha", "%20");
                params.put("hora", "%20");
                params.put("tipEve", "10");
                params.put("emplea", shared.getString("userNumber", "0"));
                params.put("grupo", grupoTxt.getText().toString());
                params.put("ciclo", "%20");
                params.put("latitu", shared.getString("latitude", "0"));
                params.put("longit", shared.getString("longitude", "0"));
                params.put("nuAgSe", "0");
                params.put("imei", imeiNumber);
                params.put("stamp", String.valueOf(System.currentTimeMillis()/1000));
                params.put("monGru", "0");
                params.put("integr", "0");
                params.put("semRen", "0");
                params.put("coment", newNotes);

                return params;
            }
        };

        MyRequestQueue.add(MyStringRequest);
    }

    public void saveEventInfo() {
        final String notes = notasTxt.getText().toString();
        final String newNotes = notes.replace(" ", "").trim();

        final SharedPreferences shared = getSharedPreferences("userInfo", MODE_PRIVATE);

        URL +=  "fecha=" + Globales.STR_VACIO +
                "&hora=" + Globales.STR_VACIO +
                "&tipEve=" + Globales.VERIFICACION +
                "&emplea=" + shared.getString("userNumber", "0") +
                "&tipgru=" + Globales.STR_VACIO +
                "&grupo=" + grupoTxt.getText().toString() +
                "&ciclo=" + Globales.STR_VACIO +
                "&latitu=" + shared.getString("latitude", "0") +
                "&longit=" + shared.getString("longitude", "0") +
                "&nuAgSe=" + Globales.STR_CERO +
                "&imei=" + imeiNumber +
                "&stamp=" + (System.currentTimeMillis()/1000) +
                "&monGru=" + Globales.STR_CERO +
                "&integr=" + Globales.STR_CERO +
                "&semRen=" + Globales.STR_CERO +
                "&coment=" + newNotes;

        Log.d("DB Veri:", URL);

        MainActivity.event = new Event();
        MainActivity.event.setUrlWS(URL);
        MainActivity.event.setStatus(0);
        MainActivity.event.insert();

        clearFields();
        Toast.makeText(getApplicationContext(), "Los datos han sido guardados de manera offline", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(Verificacion.this, MainActivity.class);
        startActivity(intent);
        finish();

    }

    public void clearFields () {
        grupoTxt.setText("");
        notasTxt.setText("");
    }
}
