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

public class AgendarCobranza extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener {

    String imeiNumber;
    private Calendar calendar;
    EditText grupoTxt;
    TextView fechaTxt, horaTxt;

    String URL = Globales.URL_REGISTRO_AGENDA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agendar_cobranza_activity);

        // Find the toolbar view and set as ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // ...
        // Remove default title text
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Get access to the custom title view
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Agendar Cobranza");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AgendarCobranza.this, AgregarEvento.class);
                startActivity(intent);
                finish();
            }
        });

        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        imeiNumber = telephonyManager.getDeviceId();

        grupoTxt = (EditText) findViewById(R.id.grupoTxt);

        calendar = Calendar.getInstance();
        fechaTxt = (TextView) findViewById(R.id.fechaTxt);
        findViewById(R.id.fechaTxt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.newInstance(AgendarCobranza.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show(getFragmentManager(), "datePicker");
            }
        });

        horaTxt = (TextView) findViewById(R.id.horaTxt);
        findViewById(R.id.horaTxt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.newInstance(AgendarCobranza.this, calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), true).show(getFragmentManager(), "timePicker");
            }
        });

        findViewById(R.id.guardarBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!grupoTxt.getText().toString().equals("") && !fechaTxt.getText().toString().equals("")
                        && !horaTxt.getText().toString().equals("")) {
                    if (Utils.isNetworkAvailable(AgendarCobranza.this)) {
                        sendEventInfo();
                    } else {
                        saveEventInfo();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Faltan datos por capturar", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void sendEventInfo() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        final String selectedStringDate = fechaTxt.getText().toString();

        final SharedPreferences shared = getSharedPreferences("userInfo", MODE_PRIVATE);

        URL +=  "fecha=" + selectedStringDate +
                "&hora=" + horaTxt.getText().toString() +
                "&tipEve=" + Globales.AGENDAR_COBRANZATEMP +
                "&emplea=" + shared.getString("userNumber", "0") +
                "&tipgru=" + Globales.STR_VACIO +
                "&grupo=" + Globales.STR_VACIO +
                "&ciclo=" + Globales.STR_VACIO +
                "&latitu=" + shared.getString("latitude", "0") +
                "&longit=" + shared.getString("longitude", "0") +
                "&nuAgSe=" + Globales.STR_CERO +
                "&imei=" + imeiNumber +
                "&stamp=" + (System.currentTimeMillis()/1000) +
                "&monGru=" + Globales.STR_CERO +
                "&integr=" + Globales.STR_CERO +
                "&semRen=" + Globales.STR_CERO +
                "&coment=" + Globales.STR_VACIO;

        Log.d("WS AgeCob:", URL);

        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
        StringRequest MyStringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //This code is executed if the server responds, whether or not the response contains data.
                        //The String 'response' contains the server's response.
                        clearFields();
                        Toast.makeText(getApplicationContext(), "Evento guardado correctamente " + response, Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(AgendarCobranza.this, MainActivity.class);
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
                params.put("fecha", selectedStringDate);
                params.put("hora", horaTxt.getText().toString());
                params.put("tipEve", "3");
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
                params.put("coment", "%20");

                return params;
            }
        };

        MyRequestQueue.add(MyStringRequest);
    }

    public void saveEventInfo() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        final String selectedStringDate = fechaTxt.getText().toString();

        final SharedPreferences shared = getSharedPreferences("userInfo", MODE_PRIVATE);

        URL +=  "fecha=" + selectedStringDate +
                "&hora=" + horaTxt.getText().toString() +
                "&tipEve=" + Globales.AGENDAR_COBRANZATEMP +
                "&emplea=" + shared.getString("userNumber", "0") +
                "&tipgru=" + Globales.STR_VACIO +
                "&grupo=" + Globales.STR_VACIO +
                "&ciclo=" + Globales.STR_VACIO +
                "&latitu=" + shared.getString("latitude", "0") +
                "&longit=" + shared.getString("longitude", "0") +
                "&nuAgSe=" + Globales.STR_CERO +
                "&imei=" + imeiNumber +
                "&stamp=" + (System.currentTimeMillis()/1000) +
                "&monGru=" + Globales.STR_CERO +
                "&integr=" + Globales.STR_CERO +
                "&semRen=" + Globales.STR_CERO +
                "&coment=" + Globales.STR_VACIO;

        Log.d("DB AgeCob:", URL);

        MainActivity.event = new Event();
        MainActivity.event.setUrlWS(URL);
        MainActivity.event.setStatus(0);
        MainActivity.event.insert();

        clearFields();
        Toast.makeText(getApplicationContext(), "Los datos han sido guardados de manera offline", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(AgendarCobranza.this, MainActivity.class);
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
        grupoTxt.setText("");
        fechaTxt.setText("");
        horaTxt.setText("");
    }
}
