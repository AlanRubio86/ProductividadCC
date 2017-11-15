package com.productividadcc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
    EditText prospectosTxt;
    EditText fechaTxt, fechaCap1;
    private Calendar calendar,calendar2;
    String eventID;
    int idFechaDesembolso = 0;
    String tokenId,employeeId;

    String URL = Globales.URL_REGISTRO_RECONTRATACION;

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

        SharedPreferences shared = getSharedPreferences("userInfo", MODE_PRIVATE);
        tokenId = shared.getString("tokenId", "0");
        employeeId=shared.getString("numEmployee", "0");





        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OldGroupNewActivity.this, OldGroupsListActivity.class);
                startActivity(intent);
                finish();
            }
        });


        if (getIntent().getExtras() != null) {
            eventID = getIntent().getExtras().getString("eventId").replace(" ", "");
        } else {
            eventID = "0";
        }

        final TextInputLayout txtGroupNumber= (TextInputLayout) findViewById(R.id.txtGroupNumber);
        final TextInputLayout txtDisbursement = (TextInputLayout) findViewById(R.id.txtDisbursement);
        final TextInputLayout txtContact= (TextInputLayout) findViewById(R.id.txtContact);
        final TextInputLayout txtContactPhone = (TextInputLayout) findViewById(R.id.txtContactPhone);
        final TextInputLayout txtCap1 = (TextInputLayout) findViewById(R.id.txtCap1);
        final EditText editGroupNumber= (EditText) findViewById(R.id.editGroupNumber);
        EditText editContact = (EditText) findViewById(R.id.editContact);
        EditText editContactPhone = (EditText) findViewById(R.id.editContactPhone);
        fechaCap1 = (EditText) findViewById(R.id.dateCap1);
        fechaTxt = (EditText) findViewById(R.id.dateDisbursement);

        calendar = Calendar.getInstance();
        fechaTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View vv = view;
                idFechaDesembolso = view.getId();
                DatePickerDialog.newInstance(OldGroupNewActivity.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show(getFragmentManager(), "datePicker");
            }
        });

        calendar2 = Calendar.getInstance();
        fechaCap1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idFechaDesembolso = 0;
                DatePickerDialog.newInstance(OldGroupNewActivity.this, calendar2.get(Calendar.YEAR), calendar2.get(Calendar.MONTH), calendar2.get(Calendar.DAY_OF_MONTH)).show(getFragmentManager(), "datePicker");
            }
        });

        findViewById(R.id.guardarGpoBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(txtContact.getEditText().getText().toString().isEmpty())
                {
                    txtContact.setError("This field can not be blank");
                    Toast.makeText(getApplicationContext(), "Favor de capturar los datos solicitados", Toast.LENGTH_LONG).show();;
                    return;
                } else {
                    txtContact.setError(null);
                }

                if(txtContactPhone.getEditText().getText().toString().isEmpty())
                {
                    txtContactPhone.setError("This field can not be blank");
                    Toast.makeText(getApplicationContext(), "Favor de capturar los datos solicitados", Toast.LENGTH_LONG).show();;
                    return;
                } else {
                    txtContactPhone.setError(null);
                }

                if(editGroupNumber.getText().toString().isEmpty())
                {
                    txtGroupNumber.setError("This field can not be blank");
                    Toast.makeText(getApplicationContext(), "Favor de capturar los datos solicitados", Toast.LENGTH_LONG).show();;
                    return;
                } else {
                    txtGroupNumber.setError(null);
                }

                if(fechaTxt.getText().toString().isEmpty())
                {
                    txtDisbursement.setError("This field can not be blank");
                    Toast.makeText(getApplicationContext(), "Favor de capturar los datos solicitados", Toast.LENGTH_LONG).show();;
                    return;
                } else {
                    txtDisbursement.setError(null);
                }

                if(fechaCap1.getText().toString().isEmpty())
                {
                    txtCap1.setError("This field can not be blank");
                    Toast.makeText(getApplicationContext(), "Favor de capturar los datos solicitados", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    txtCap1.setError(null);
                }

                sendSaveGroup(editGroupNumber.getText().toString(),txtContact.getEditText().getText().toString(),txtContactPhone.getEditText().getText().toString()
                        ,fechaTxt.getText().toString(),fechaCap1.getText().toString());

            }
        });

        editContact.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtContact.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editContactPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtContactPhone.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

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
        fechaTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtDisbursement.setError(null);
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        fechaCap1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtCap1.setError(null);
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void sendSaveGroup (String grupo,String nomContacto,String telContacto, String dateDisbursement, String dateRecontratation) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        final String selectedStringDate = fechaTxt.getText().toString();
        final SharedPreferences shared = getSharedPreferences("userInfo", MODE_PRIVATE);
        URL= String.format(URL,tokenId,employeeId,grupo,nomContacto,telContacto,dateDisbursement,dateRecontratation,shared.getString("latitude", "0"),shared.getString("longitude", "0")  );

        Log.d("WS Prom:", URL);
        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
        StringRequest MyStringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        clearFields();
                        Toast.makeText(getApplicationContext(), "Se guardo el nuevo grupo de recontración correctamente", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(OldGroupNewActivity.this, OldGroupsListActivity.class);
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
        });


        /*{
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
        };*/

        MyRequestQueue.add(MyStringRequest);
    }

    public void saveGroupOffline(String nomContacto,String telContacto,String refContacto, String dateDisbursement, String dateTraining) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        final SharedPreferences shared = getSharedPreferences("userInfo", MODE_PRIVATE);
        URL= String.format(URL,tokenId,employeeId,nomContacto,telContacto,refContacto,dateDisbursement,dateTraining,shared.getString("latitude", "0"),shared.getString("longitude", "0")  );
        Log.d("DB Prom:", URL);
        MainActivity.event = new Event();
        MainActivity.event.setUrlWS(URL);
        MainActivity.event.setStatus(0);
        MainActivity.event.insert();

        clearFields();
        Toast.makeText(getApplicationContext(), "Los datos han sido guardados de manera offline", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(OldGroupNewActivity.this, NewGroupsListActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
        java.text.DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Log.d("Log Pruebas", String.valueOf(idFechaDesembolso));
        if(idFechaDesembolso == R.id.dateDisbursement){
            calendar.set(year, monthOfYear, dayOfMonth);
            fechaTxt.setText(df.format(calendar.getTime()));
        }else{
            calendar2.set(year, monthOfYear, dayOfMonth);
            fechaCap1.setText(df.format(calendar2.getTime()));
        }
    }

    public void clearFields () {

    }
}
