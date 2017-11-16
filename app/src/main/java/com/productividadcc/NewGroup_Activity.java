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

public class NewGroup_Activity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    String imeiNumber="";
    private Calendar calendar;
    private Calendar calendar2;
    EditText fechaTxt, fechaCap1;
    String eventID;
    int idFechaDesembolso = 0;
    String tokenId,employeeId;

    String URL = Globales.URL_REGISTRO_GRUPO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newgroup_activity);

        // Find the toolbar view and set as ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // ...

        //region Controls
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        fechaTxt = (EditText) findViewById(R.id.dateDisbursement);
        final TextInputLayout txtContact= (TextInputLayout) findViewById(R.id.txtContact);
        final TextInputLayout txtContactPhone = (TextInputLayout) findViewById(R.id.txtContactPhone);
        final TextInputLayout txtContactPhoneRef = (TextInputLayout) findViewById(R.id.txtContactPhoneRef);
        final TextInputLayout txtDisbursement = (TextInputLayout) findViewById(R.id.txtDisbursement);
        final TextInputLayout txtCap1 = (TextInputLayout) findViewById(R.id.txtCap1);
        fechaCap1 = (EditText) findViewById(R.id.dateCap1);
        EditText editContact = (EditText) findViewById(R.id.editContact);
        EditText editContactPhone = (EditText) findViewById(R.id.editContactPhone);
        EditText editContactPhoneRef = (EditText) findViewById(R.id.editContactPhoneRef);

        //endregion


        // Remove default title text
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Get access to the custom title view

        mTitle.setText("Registro de Grupo Nuevo");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences shared = getSharedPreferences("userInfo", MODE_PRIVATE);
        tokenId = shared.getString("tokenId", "0");
        employeeId=shared.getString("numEmployee", "0");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewGroup_Activity.this, NewGroupsList_Activity.class);
                startActivity(intent);
                finish();
            }
        });


        if (getIntent().getExtras() != null) {
            eventID = getIntent().getExtras().getString("eventId").replace(" ", "");
        } else {
            eventID = "0";
        }
        //Get All Controls



        calendar = Calendar.getInstance();
        fechaTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View vv = view;
                idFechaDesembolso = view.getId();
                DatePickerDialog.newInstance(NewGroup_Activity.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show(getFragmentManager(), "datePicker");
            }
        });

        calendar2 = Calendar.getInstance();
        fechaCap1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idFechaDesembolso = 0;
                DatePickerDialog.newInstance(NewGroup_Activity.this, calendar2.get(Calendar.YEAR), calendar2.get(Calendar.MONTH), calendar2.get(Calendar.DAY_OF_MONTH)).show(getFragmentManager(), "datePicker");
            }
        });


        findViewById(R.id.btnSaveGroup).setOnClickListener(new View.OnClickListener() {
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

                if(txtContactPhoneRef.getEditText().getText().toString().isEmpty())
                {
                    txtContactPhoneRef.setError("This field can not be blank");
                    Toast.makeText(getApplicationContext(), "Favor de capturar los datos solicitados", Toast.LENGTH_LONG).show();;
                    return;
                } else {
                    txtContactPhoneRef.setError(null);
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

                if (Utils.isNetworkAvailable(NewGroup_Activity.this)) {
                    sendSaveGroup(txtContact.getEditText().getText().toString(),txtContactPhone.getEditText().getText().toString(),
                            txtContactPhoneRef.getEditText().getText().toString(),fechaTxt.getText().toString(),fechaCap1.getText().toString());
                } else {
                    saveOffline(txtContact.getEditText().getText().toString(),txtContactPhone.getEditText().getText().toString(),
                            txtContactPhoneRef.getEditText().getText().toString(),fechaTxt.getText().toString(),fechaCap1.getText().toString());
                }

            }
        });

    //region Listeners

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


        editContactPhoneRef.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtContactPhoneRef.setError(null);
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

        //endregion
    }


    //region Public Methods
    public void sendSaveGroup (String nomContacto,String telContacto,String refContacto, String dateDisbursement, String dateTraining) {
        final SharedPreferences shared = getSharedPreferences("userInfo", MODE_PRIVATE);
        URL= String.format(URL,tokenId,employeeId,nomContacto,telContacto,refContacto,dateDisbursement,dateTraining,shared.getString("latitude", "0"),shared.getString("longitude", "0")  );

        Log.d("WS Prom:", URL);
        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
        StringRequest MyStringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "Se guardo el nuevo grupo correctamente", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(NewGroup_Activity.this, NewGroupsList_Activity.class);
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

       MyRequestQueue.add(MyStringRequest);
    }

    public void saveOffline(String nomContacto,String telContacto,String refContacto, String dateDisbursement, String dateTraining) {
        final SharedPreferences shared = getSharedPreferences("userInfo", MODE_PRIVATE);
        URL= String.format(URL,tokenId,employeeId,nomContacto,telContacto,refContacto,dateDisbursement,dateTraining,shared.getString("latitude", "0"),shared.getString("longitude", "0")  );
        Log.d("DB Prom:", URL);
        Main_Activity.event = new Event();
        Main_Activity.event.setUrlWS(URL);
        Main_Activity.event.setStatus(0);
        Main_Activity.event.insert();

        Toast.makeText(getApplicationContext(), "Los datos han sido guardados de manera offline", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(NewGroup_Activity.this, NewGroupsList_Activity.class);
        startActivity(intent);
        finish();
    }
    //endregion


   @Override
    public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
        java.text.DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        if(idFechaDesembolso == R.id.dateDisbursement){
            calendar.set(year, monthOfYear, dayOfMonth);
            fechaTxt.setText(df.format(calendar.getTime()));
        }else{
            calendar2.set(year, monthOfYear, dayOfMonth);
            fechaCap1.setText(df.format(calendar2.getTime()));
        }
    }


}
