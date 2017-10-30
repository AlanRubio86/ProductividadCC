package com.productividadcc;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.datetimepicker.date.DatePickerDialog;
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
import java.util.Map;

public class OldGroupNewActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    String imeiNumber;
    EditText prospectosTxt;
    TextView fechaTxt, fechaCap1;

    String eventID;
    int idFechaDesembolso = 0;

    String URL = Globales.URL_REGISTRO_GRUPO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oldgroupnew_activity);

        // Find the toolbar view and set as ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // ...
        // Remove default title text
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Get access to the custom title view
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Registro de Grupo Recontratacion");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OldGroupNewActivity.this, OldGroupsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        /*TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        imeiNumber = telephonyManager.getDeviceId();*/

        if (getIntent().getExtras() != null) {
            eventID = getIntent().getExtras().getString("eventId").replace(" ", "");
        } else {
            eventID = "0";
        }

        final TextInputLayout txtGroupNumber= (TextInputLayout) findViewById(R.id.txtGroupNumber);
        final TextInputLayout txtGroupName= (TextInputLayout) findViewById(R.id.txtGroupName);
        final TextInputLayout txtLastCicle= (TextInputLayout) findViewById(R.id.txtLastCicle);
        final EditText editGroupNumber= (EditText) findViewById(R.id.editGroupNumber);
        final EditText editGroupName= (EditText) findViewById(R.id.editGroupName);
        final EditText editLastCicle= (EditText) findViewById(R.id.editLastCicle);

        findViewById(R.id.guardarGpoBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editGroupNumber.getText().toString().isEmpty())
                {
                    txtGroupNumber.setError("This field can not be blank");
                    Toast.makeText(getApplicationContext(), "Favor de capturar los datos solicitados", Toast.LENGTH_LONG).show();;
                    return;
                } else {
                    txtGroupNumber.setError(null);
                }

                if(editGroupName.getText().toString().isEmpty())
                {
                    txtGroupName.setError("This field can not be blank");
                    Toast.makeText(getApplicationContext(), "Favor de capturar los datos solicitados", Toast.LENGTH_LONG).show();;
                    return;
                } else {
                    txtGroupName.setError(null);
                }
                if(editLastCicle.getText().toString().isEmpty())
                {
                    txtLastCicle.setError("This field can not be blank");
                    Toast.makeText(getApplicationContext(), "Favor de capturar los datos solicitados", Toast.LENGTH_LONG).show();;
                    return;
                } else {
                    txtLastCicle.setError(null);
                }
                Toast.makeText(getApplicationContext(), "Se guardo el nuevo grupo correctamente", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(OldGroupNewActivity.this, OldGroupsActivity.class);
                startActivity(intent);
                finish();

            }
        });
        editGroupNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtGroupNumber.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editGroupName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtGroupName.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editLastCicle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtLastCicle.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    public void sendEventInfo() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        final String selectedStringDate = fechaTxt.getText().toString();
        final String prospectos = prospectosTxt.getText().toString();
        final SharedPreferences shared = getSharedPreferences("userInfo", MODE_PRIVATE);

        URL +=  "fecha=" + selectedStringDate +
                "&hora=" + fechaCap1.getText().toString() +
                "&tipEve=" + Globales.STR_VACIO +
                "&emplea=" + shared.getString("userNumber", "0") +
                "&tipgru=" + Globales.STR_VACIO +
                "&grupo=" + Globales.STR_VACIO +
                "&ciclo=" + Globales.STR_VACIO +
                "&latitu=" + shared.getString("latitude", "0") +
                "&longit=" + shared.getString("longitude", "0") +
                "&nuAgSe=" + eventID +
                "&imei=" + imeiNumber +
                "&stamp=" + (System.currentTimeMillis()/1000) +
                "&monGru=" + Globales.STR_CERO +
                "&integr=" + prospectos +
                "&semRen=" + Globales.STR_CERO +
                "&coment=" + Globales.STR_CERO;

        Log.d("WS Prom:", URL);

        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
        StringRequest MyStringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //This code is executed if the server responds, whether or not the response contains data.
                        //The String 'response' contains the server's response.
                        clearFields();
                        Toast.makeText(getApplicationContext(), "Evento guardado correctamente " + response, Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(OldGroupNewActivity.this, MainActivity.class);
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
                params.put("hora", fechaCap1.getText().toString());
                params.put("tipEve", "11");
                params.put("emplea", shared.getString("userNumber", "0"));
                params.put("grupo", "%20");
                params.put("ciclo", "%20");
                params.put("latitu", shared.getString("latitude", "0"));
                params.put("longit", shared.getString("longitude", "0"));
                params.put("nuAgSe", "0");
                params.put("imei", imeiNumber);
                params.put("stamp", String.valueOf(System.currentTimeMillis()/1000));
                params.put("monGru", "0");
                params.put("integr", "0");
                params.put("semRen", "0");
                params.put("coment", "");

                return params;
            }
        };

        MyRequestQueue.add(MyStringRequest);
    }

    public void saveEventInfo() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        final String selectedStringDate = fechaTxt.getText().toString();
        final String prospectos = prospectosTxt.getText().toString();
        final SharedPreferences shared = getSharedPreferences("userInfo", MODE_PRIVATE);

        URL +=  "fecha=" + selectedStringDate +
                "&hora=" + fechaCap1.getText().toString() +
                "&tipEve=" + Globales.STR_VACIO +
                "&emplea=" + shared.getString("userNumber", "0") +
                "&tipgru=" + Globales.STR_VACIO +
                "&grupo=" + Globales.STR_VACIO +
                "&ciclo=" + Globales.STR_VACIO +
                "&latitu=" + shared.getString("latitude", "0") +
                "&longit=" + shared.getString("longitude", "0") +
                "&nuAgSe=" + eventID +
                "&imei=" + imeiNumber +
                "&stamp=" + (System.currentTimeMillis()/1000) +
                "&monGru=" + Globales.STR_CERO +
                "&integr=" + prospectos +
                "&semRen=" + Globales.STR_CERO +
                "&coment=" + Globales.STR_CERO;

        Log.d("DB Prom:", URL);

        MainActivity.event = new Event();
        MainActivity.event.setUrlWS(URL);
        MainActivity.event.setStatus(0);
        MainActivity.event.insert();

        clearFields();
        Toast.makeText(getApplicationContext(), "Los datos han sido guardados de manera offline", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(OldGroupNewActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }


    @Override
    public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
        java.text.DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Log.d("Log Pruebas", String.valueOf(idFechaDesembolso));

    }

    public void clearFields () {
        prospectosTxt.setText("");
        fechaTxt.setText("");
        fechaCap1.setText("");
    }
}
