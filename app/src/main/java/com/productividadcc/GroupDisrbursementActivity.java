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

public class GroupDisrbursementActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener {
    String imeiNumber;
    private Calendar calendar;
    EditText editIntegrant,editAmount,editDisbursement;
    Spinner spnGroupType,spnMotiveReprogram,spnCancelMotive;
    String groupID;
    TextView nombreLbl;
    String URL = Globales.URL_REGISTRO_AGENDA;
    private int movement=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.groupdisbursement);

        // Find the toolbar view and set as ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);



        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        nombreLbl = (TextView) findViewById(R.id.nombreLabel);

        final TextInputLayout integrants = (TextInputLayout) findViewById(R.id.llIntegrant);
        final TextInputLayout amount = (TextInputLayout) findViewById(R.id.llAmount);
        final LinearLayout groupType = (LinearLayout) findViewById(R.id.llgrouptype);
        final LinearLayout motiveReprogram = (LinearLayout) findViewById(R.id.llMotiveReprogram);
        final TextInputLayout dateReprogram = (TextInputLayout) findViewById(R.id.llDate);
        final LinearLayout cancelMotive = (LinearLayout) findViewById(R.id.llCancelMotive);
        final LinearLayout btnsave = (LinearLayout) findViewById(R.id.btnSave);


        editAmount = (EditText) findViewById(R.id.editAmount);
        editIntegrant = (EditText) findViewById(R.id.editIntegrant);
        editDisbursement= (EditText) findViewById(R.id.editDisbursement);
        spnCancelMotive=(Spinner) findViewById(R.id.spnCancelMotive);
        spnMotiveReprogram=(Spinner) findViewById(R.id.spnMotiveReprogram);
        spnGroupType=(Spinner) findViewById(R.id.spnGroupType);


        mTitle.setText("Desembolso");
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

        editDisbursement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.newInstance(GroupDisrbursementActivity.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show(getFragmentManager(), "datePicker");
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(movement==1)
                {

                    if(editIntegrant.getText().toString().isEmpty())
                    {
                        integrants.setError("This field can not be blank");
                        Toast.makeText(getApplicationContext(), "Favor de capturar los datos solicitados", Toast.LENGTH_LONG).show();;
                        return;
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
                    if (spnGroupType.getSelectedItem().toString().trim().equals("(Seleccionar)"))
                    {
                        Toast.makeText(getApplicationContext(), "Favor de seleccionar un motivo", Toast.LENGTH_LONG).show();;
                        return;
                    }

                    Toast.makeText(getApplicationContext(), "Se realizo el desembolso correctamente", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(GroupDisrbursementActivity.this, finishnewgroup.class);
                    startActivity(intent);
                    finish();
                }else if(movement==2)
                {
                    if (spnMotiveReprogram.getSelectedItem().toString().trim().equals("(Seleccionar)"))
                    {
                        Toast.makeText(getApplicationContext(), "Favor de seleccionar un motivo", Toast.LENGTH_LONG).show();;
                        return;
                    }
                    if(editDisbursement.getText().toString().isEmpty())
                    {
                        dateReprogram.setError("This field can not be blank");
                        Toast.makeText(getApplicationContext(), "Favor de capturar los datos solicitados", Toast.LENGTH_LONG).show();;
                        return;
                    } else {
                        dateReprogram.setError(null);
                    }

                    Toast.makeText(getApplicationContext(), "Se realizo la reprogramación de desembolso correctamente", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(GroupDisrbursementActivity.this, NewGroupsActivity.class);
                    startActivity(intent);
                    finish();
                }else if(movement==3)
                {
                    if (spnCancelMotive.getSelectedItem().toString().trim().equals("(Seleccionar)"))
                    {
                        Toast.makeText(getApplicationContext(), "Favor de seleccionar un motivo", Toast.LENGTH_LONG).show();;
                        return;
                    }
                    Toast.makeText(getApplicationContext(), "Se realizo la cancelación de  desembolso correctamente", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(GroupDisrbursementActivity.this, NewGroupsActivity.class);
                    startActivity(intent);
                    finish();
                }
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
        editIntegrant.addTextChangedListener(new TextWatcher() {
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

        editDisbursement.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dateReprogram.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupDisrbursementActivity.this, NewGroupsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.continueBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                integrants.setVisibility(View.VISIBLE);
                amount.setVisibility(View.VISIBLE);
                groupType.setVisibility(View.VISIBLE);
                btnsave.setVisibility(View.VISIBLE);
                motiveReprogram.setVisibility(View.GONE);
                dateReprogram.setVisibility(View.GONE);
                cancelMotive.setVisibility(View.GONE);
                movement=1;

            }
        });

        findViewById(R.id.reprogBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                integrants.setVisibility(View.GONE);
                amount.setVisibility(View.GONE);
                groupType.setVisibility(View.GONE);
                btnsave.setVisibility(View.VISIBLE);
                motiveReprogram.setVisibility(View.VISIBLE);
                dateReprogram.setVisibility(View.VISIBLE);
                cancelMotive.setVisibility(View.GONE);
                     movement=2;

            }
        });

        findViewById(R.id.cancelBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                integrants.setVisibility(View.GONE);
                amount.setVisibility(View.GONE);
                groupType.setVisibility(View.GONE);
                btnsave.setVisibility(View.VISIBLE);
                motiveReprogram.setVisibility(View.GONE);
                dateReprogram.setVisibility(View.GONE);
                cancelMotive.setVisibility(View.VISIBLE);
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
        editDisbursement.setText(df.format(calendar.getTime()));
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        String time = String.format(Locale.ENGLISH, "%02d:%02d", hourOfDay, minute);


    }

    public void clearFields () {
        editAmount.setText("");
        editIntegrant.setText("");
        editDisbursement.setText("");
    }
}
