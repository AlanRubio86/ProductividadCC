package com.productividadcc;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;
import com.productividadcc.utilerias.Globales;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class NewGroupVoBo extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener {
    String imeiNumber;
    private Calendar calendar,calendar2;
    EditText numGrupo ,montoTxt, editIntegrants;
    EditText editEstimated, horaTxt,editAmount,editReprogram;
    Spinner spnDispersion,spnMotiveReprogram,spnCancelMotive;
    String groupID;
    TextView nombreLbl;
    String URL = Globales.URL_REGISTRO_AGENDA;
    private int movement=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newgroupvobo_activity);

        // Find the toolbar view and set as ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        nombreLbl = (TextView) findViewById(R.id.nombreLabel);
       /* montoTxt = (EditText) findViewById(R.id.montoTxt);*/

        final TextInputLayout integrants = (TextInputLayout) findViewById(R.id.llIntegrant);
        final TextInputLayout date = (TextInputLayout) findViewById(R.id.llDate);
        final TextInputLayout amount = (TextInputLayout) findViewById(R.id.llAmount);
        final LinearLayout dispersion = (LinearLayout) findViewById(R.id.lldispersion);
        final TextInputLayout reprogram = (TextInputLayout) findViewById(R.id.llDateReprogram);
        final LinearLayout reprogrammotive = (LinearLayout) findViewById(R.id.llmotive);
        final LinearLayout cancelmotive = (LinearLayout) findViewById(R.id.llmotivecancel);
        final LinearLayout btnsave = (LinearLayout) findViewById(R.id.btnSave);
        editIntegrants = (EditText) findViewById(R.id.editIntegrantes);
        editAmount = (EditText) findViewById(R.id.editAmount);
        editEstimated = (EditText) findViewById(R.id.editEstimated);
        spnDispersion=(Spinner) findViewById(R.id.spnDispersion);
        spnMotiveReprogram=(Spinner)findViewById(R.id.spnMotiveReprogram);
        spnCancelMotive=(Spinner)findViewById(R.id.spnCancelMotive);

        editReprogram = (EditText) findViewById(R.id.editReprogram);

        mTitle.setText("Visto Bueno");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        //imeiNumber = telephonyManager.getDeviceId();


        if (getIntent().getExtras() != null) {
            String groupName = getIntent().getExtras().getString("groupName");
            nombreLbl.setText(groupName);
            groupID = getIntent().getExtras().getString("groupID");
        } else {
            groupID = "0";
        }

        calendar = Calendar.getInstance();
        editEstimated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.newInstance(NewGroupVoBo.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show(getFragmentManager(), "datePicker");
            }
        });


        calendar2=Calendar.getInstance();
        editReprogram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.newInstance(NewGroupVoBo.this, calendar2.get(Calendar.YEAR), calendar2.get(Calendar.MONTH), calendar2.get(Calendar.DAY_OF_MONTH)).show(getFragmentManager(), "datePicker");
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(movement==1)
                {
                    if(editIntegrants.getText().toString().isEmpty())
                    {
                        integrants.setError("This field can not be blank");
                        Toast.makeText(getApplicationContext(), "Favor de capturar los datos solicitados", Toast.LENGTH_LONG).show();;
                        return;
                    } else {
                        integrants.setError(null);
                    }
                    if(!editIntegrants.getText().toString().isEmpty())
                    {
                        int num= Integer.parseInt( editIntegrants.getText().toString());
                        if(num<=0)
                        {
                            integrants.setError("El número de integrantes tiene que ser mayor que 0");
                            Toast.makeText(getApplicationContext(), "El número de integrantes tiene que ser mayor que 0", Toast.LENGTH_LONG).show();
                            return;
                        }

                    } else {
                        integrants.setError(null);
                    }


                    if(editAmount.getText().toString().isEmpty())
                    {
                        amount.setError("This field can not be blank");
                        Toast.makeText(getApplicationContext(), "Favor de capturar los datos solicitados", Toast.LENGTH_LONG).show();;
                        return;
                    } else {
                        amount.setError(null);
                    }

                    if(!editAmount.getText().toString().isEmpty())
                    {

                        Double money= Double.parseDouble( editAmount.getText().toString());
                        if(money<=0)
                        {
                            amount.setError("El monto autorizado tiene que ser mayor que 0");
                            Toast.makeText(getApplicationContext(), "El monto autorizado tiene que ser mayor que 0", Toast.LENGTH_LONG).show();;
                            return;
                        }

                    } else {
                        amount.setError(null);
                    }


                    if(editEstimated.getText().toString().isEmpty())
                    {
                        date.setError("This field can not be blank");
                        Toast.makeText(getApplicationContext(), "Favor de capturar los datos solicitados", Toast.LENGTH_LONG).show();;
                        return;
                    } else {
                        amount.setError(null);
                    }

                    if (spnDispersion.getSelectedItem().toString().trim().equals("(Seleccionar)"))
                    {
                        Toast.makeText(getApplicationContext(), "Favor de seleccionar un tipo de dispersión", Toast.LENGTH_LONG).show();;
                        return;
                    }
                    Toast.makeText(getApplicationContext(), "Se realizo el visto bueno correctamente", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(NewGroupVoBo.this, NewGroupsActivity.class);
                    startActivity(intent);
                    finish();
                } else if(movement==2)
                {
                    if(editReprogram.getText().toString().isEmpty())
                    {
                        reprogram.setError("This field can not be blank");
                        Toast.makeText(getApplicationContext(), "Favor de capturar los datos solicitados", Toast.LENGTH_LONG).show();;
                        return;
                    } else {
                        reprogram.setError(null);
                    }

                    if (spnMotiveReprogram.getSelectedItem().toString().trim().equals("(Seleccionar)"))
                    {
                        Toast.makeText(getApplicationContext(), "Favor de seleccionar un motivo", Toast.LENGTH_LONG).show();;
                        return;
                    }
                    Toast.makeText(getApplicationContext(), "Se realizo la reprogramacion del visto bueno correctamente", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(NewGroupVoBo.this, NewGroupsActivity.class);
                    startActivity(intent);
                    finish();
                } else if(movement==3)
                {

                    if (spnCancelMotive.getSelectedItem().toString().trim().equals("(Seleccionar)"))
                    {
                        Toast.makeText(getApplicationContext(), "Favor de seleccionar un motivo", Toast.LENGTH_LONG).show();;
                        return;
                    }
                    Toast.makeText(getApplicationContext(), "Se realizo la cancelación del visto bueno correctamente", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(NewGroupVoBo.this, NewGroupsActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });

        editReprogram.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                reprogram.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editEstimated.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                date.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        editIntegrants.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                integrants.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        editAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                amount.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewGroupVoBo.this, NewGroupsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.continueBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                integrants.setVisibility(View.VISIBLE);
                date.setVisibility(View.VISIBLE);
                amount.setVisibility(View.VISIBLE);
                btnsave.setVisibility(View.VISIBLE);
                dispersion.setVisibility(View.VISIBLE);
                reprogrammotive.setVisibility(View.GONE);
                reprogram.setVisibility(View.GONE);
                cancelmotive.setVisibility(View.GONE);
                movement=1;

            }
        });

        findViewById(R.id.reprogBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    integrants.setVisibility(View.GONE);
                    date.setVisibility(View.GONE);
                    amount.setVisibility(View.GONE);
                    btnsave.setVisibility(View.VISIBLE);
                    dispersion.setVisibility(View.GONE);
                    reprogrammotive.setVisibility(View.VISIBLE);
                    reprogram.setVisibility(View.VISIBLE);
                    cancelmotive.setVisibility(View.GONE);
                     movement=2;

            }
        });

        findViewById(R.id.cancelBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                integrants.setVisibility(View.GONE);
                date.setVisibility(View.GONE);
                amount.setVisibility(View.GONE);
                btnsave.setVisibility(View.VISIBLE);
                dispersion.setVisibility(View.GONE);
                reprogrammotive.setVisibility(View.GONE);
                reprogram.setVisibility(View.GONE);
                cancelmotive.setVisibility(View.VISIBLE);
                movement=3;
            }
        });
    }
/*
    public void sendEventInfo() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        final String selectedStringDate = fechaTxt.getText().toString();
        final SharedPreferences shared = getSharedPreferences("userInfo", MODE_PRIVATE);

        URL +=  "fecha=" + selectedStringDate +
                "&hora=" + horaTxt.getText().toString() +
                "&tipEve=" + Globales.VOBO +
                "&emplea=" + shared.getString("userNumber", "0") +
                "&tipgru=" + Globales.STR_VACIO +
                "&grupo=" + numGrupo.getText().toString() +
                "&ciclo=" + Globales.STR_VACIO +
                "&latitu=" + shared.getString("latitude", "0") +
                "&longit=" + shared.getString("longitude", "0") +
                "&nuAgSe=" + eventID +
                "&imei=" + imeiNumber +
                "&stamp=" + (System.currentTimeMillis()/1000) +
                "&monGru=" + montoTxt.getText().toString() +
                "&integr=" + integrantesTxt.getText().toString() +
                "&semRen=" + Globales.STR_CERO +
                "&coment=" + Globales.STR_VACIO;

        Log.d("WS NewGroupVoBo:", URL);

        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
        StringRequest MyStringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //This code is executed if the server responds, whether or not the response contains data.
                        //The String 'response' contains the server's response.
                        clearFields();
                        Toast.makeText(getApplicationContext(), "Evento guardado correctamente " + response, Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(NewGroupVoBo.this, MainActivity.class);
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
                params.put("tipEve", "6");
                params.put("emplea", shared.getString("userNumber", "0"));
                params.put("grupo", numGrupo.getText().toString());
                params.put("ciclo", "%20");
                params.put("latitu", shared.getString("latitude", "0"));
                params.put("longit", shared.getString("longitude", "0"));
                params.put("nuAgSe", eventID);
                params.put("imei", imeiNumber);
                params.put("stamp", String.valueOf(System.currentTimeMillis()/1000));
                params.put("monGru", montoTxt.getText().toString());
                params.put("integr", integrantesTxt.getText().toString());
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
                "&tipEve=" + Globales.VOBO +
                "&emplea=" + shared.getString("userNumber", "0") +
                "&tipgru=" + Globales.STR_VACIO +
                "&grupo=" + numGrupo.getText().toString() +
                "&ciclo=" + Globales.STR_VACIO +
                "&latitu=" + shared.getString("latitude", "0") +
                "&longit=" + shared.getString("longitude", "0") +
                "&nuAgSe=" + eventID +
                "&imei=" + imeiNumber +
                "&stamp=" + (System.currentTimeMillis()/1000) +
                "&monGru=" + montoTxt.getText().toString() +
                "&integr=" + integrantesTxt.getText().toString() +
                "&semRen=" + Globales.STR_CERO +
                "&coment=" + Globales.STR_VACIO;

        Log.d("DB NewGroupVoBo:", URL);

        MainActivity.event = new Event();
        MainActivity.event.setUrlWS(URL);
        MainActivity.event.setStatus(0);
        MainActivity.event.insert();

        clearFields();
        Toast.makeText(getApplicationContext(), "Los datos han sido guardados de manera offline", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(NewGroupVoBo.this, MainActivity.class);
        startActivity(intent);
        finish();

    }

    */

    @Override
    public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
        java.text.DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        calendar.set(year, monthOfYear, dayOfMonth);
        editEstimated.setText(df.format(calendar.getTime()));
        calendar2.set(year, monthOfYear, dayOfMonth);
        editReprogram.setText(df.format(calendar2.getTime()));
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        String time = String.format(Locale.ENGLISH, "%02d:%02d", hourOfDay, minute);
        horaTxt.setText(time);

    }

    public void clearFields () {
        montoTxt.setText("");
        editIntegrants.setText("");
        editEstimated.setText("");
        horaTxt.setText("");
    }
}
