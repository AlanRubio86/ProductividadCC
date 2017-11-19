package com.productividadcc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class OldGroupVoBo_Activity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener {
    private Calendar calendar,calendar2;
    EditText montoTxt, editIntegrants;
    EditText editEstimated, horaTxt,editAmount,editReprogram;
    Spinner spnDispersion,spnMotiveReprogram,spnCancelMotive;
    String groupID,tokenId,employeeId;
    TextView ceros,nombreLbl,cicleLbl,weekLbl;
    String URL = "";
    private int movement=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oldgroupvobo_activity);

        // Find the toolbar view and set as ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        SharedPreferences shared = getSharedPreferences("userInfo", MODE_PRIVATE);
        tokenId = shared.getString("tokenId", "0");
        employeeId=shared.getString("numEmployee", "0");


        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        nombreLbl = (TextView) findViewById(R.id.nombreLabel);
        cicleLbl=  (TextView) findViewById(R.id.cicleLbl);
        weekLbl=  (TextView) findViewById(R.id.weekLbl);


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
        final LinearLayout llAmountGeneral=(LinearLayout) findViewById(R.id.llAmountGeneral);
        ceros=(TextView)findViewById(R.id.ceros);

        mTitle.setText("Visto Bueno");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        //imeiNumber = telephonyManager.getDeviceId();


        if (getIntent().getExtras() != null) {
            String groupName = getIntent().getExtras().getString("groupName");
            nombreLbl.setText(groupName);
            cicleLbl.setText("Ciclo:"+getIntent().getExtras().getString("cicle"));
            weekLbl.setText("Semana:"+ getIntent().getExtras().getString("week"));
            groupID = getIntent().getExtras().getString("groupId");
        } else {
            groupID = "0";
        }

        calendar = Calendar.getInstance();
        editEstimated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.newInstance(OldGroupVoBo_Activity.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show(getFragmentManager(), "datePicker");
            }
        });


        calendar2=Calendar.getInstance();
        editReprogram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.newInstance(OldGroupVoBo_Activity.this, calendar2.get(Calendar.YEAR), calendar2.get(Calendar.MONTH), calendar2.get(Calendar.DAY_OF_MONTH)).show(getFragmentManager(), "datePicker");
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SharedPreferences shared = getSharedPreferences("userInfo", MODE_PRIVATE);
                if(movement==1)
                {
                    if(editIntegrants.getText().toString().isEmpty())
                    {
                        integrants.setError("This field can not be blank");
                        Toast.makeText(getApplicationContext(), "Favor de capturar los datos solicitados", Toast.LENGTH_LONG).show();;
                        return;
                    } else {
                        if(Integer.parseInt(editIntegrants.getText().toString())<=0)
                        {
                            integrants.setError("Cantidad de integrantes tiene que ser mayor que 0");
                            Toast.makeText(getApplicationContext(), "Catnidad de integrantes tiene que ser mayor que 0", Toast.LENGTH_LONG).show();
                            return;
                        }else
                        {
                            integrants.setError(null);
                        }
                    }
                    if(editAmount.getText().toString().isEmpty())
                    {
                        amount.setError("This field can not be blank");
                        Toast.makeText(getApplicationContext(), "Favor de capturar los datos solicitados", Toast.LENGTH_LONG).show();;
                        return;
                    } else {
                        if(Double.parseDouble(editAmount.getText().toString())<=0){
                            amount.setError("El monto tiene que ser mayor que 0");
                            Toast.makeText(getApplicationContext(), "El monto tiene que ser mayor que 0", Toast.LENGTH_LONG).show();;
                            return;
                        }else{
                            amount.setError(null);
                        }
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


                    String dispersionType="";
                    switch(spnDispersion.getSelectedItem().toString())
                    {
                        case "SPEI":
                            dispersionType="1";
                            break;
                        case "ODP,Santader $20.00":
                            dispersionType="2";
                            break;
                        case "ODP,Scotiabank $15.00":
                            dispersionType="3";
                            break;
                    }

                    Double amount=Double.parseDouble(editAmount.getText().toString())*100;
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = new Date();
                    URL=String.format(Globales.URL_ACTUALIZAR_ETAPA,
                            tokenId,
                            employeeId,
                            groupID,
                            "6",
                            "",
                            amount.toString(),
                            editIntegrants.getText().toString(),
                            "0",
                            "0",
                            dateFormat.format(date),
                            editEstimated.getText().toString(),
                            dispersionType,
                            "0",
                            shared.getString("latitude", "0"),
                            shared.getString("longitude", "0"));

                    if (Utils.isNetworkAvailable(OldGroupVoBo_Activity.this)) {
                        updateGroup(URL);
                    } else {
                        saveOffline(URL);
                    }

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

                    String motive="";
                    switch(spnMotiveReprogram.getSelectedItem().toString())
                    {
                        case "Clientes no llegan a tiempo":
                            motive="1";
                            break;
                        case "Cambio de fecha":
                            motive="2";
                            break;
                        case "No están todas las integrantes presentes":
                            motive="3";
                            break;
                        case "Área de Análisis solicita reprogramación":
                            motive="4";
                            break;
                        case "Se cancela una cliente y se descompleta el grupo":
                            motive="5";
                            break;
                    }

                    URL=String.format(Globales.URL_REPROGRAMAR_ETAPA,tokenId,employeeId,groupID,"6",editReprogram.getText().toString(),motive,shared.getString("latitude", "0"),shared.getString("longitude", "0") );

                    if (Utils.isNetworkAvailable(OldGroupVoBo_Activity.this)) {
                        updateGroup(URL);
                    } else {
                        saveOffline(URL);
                    }

                } else if(movement==3)
                {

                    if (spnCancelMotive.getSelectedItem().toString().trim().equals("(Seleccionar)"))
                    {
                        Toast.makeText(getApplicationContext(), "Favor de seleccionar un motivo", Toast.LENGTH_LONG).show();;
                        return;
                    }
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = new Date();
                    String motive="";
                    switch(spnCancelMotive.getSelectedItem().toString())
                    {
                        case "Se detectó prestanombres":
                            motive="16";
                            break;
                        case "Información falsa":
                            motive="17";
                            break;
                        case "Clientes no se conocen":
                            motive="18";
                            break;
                        case "Se detectó algún riesgo":
                            motive="19";
                            break;
                        case "Se cancelo una cliente y ya no quiere buscar mas":
                            motive="20";
                    }

                    URL=String.format(Globales.URL_CANCELAR_ETAPA,tokenId,employeeId,groupID,"6",dateFormat.format(date),motive,shared.getString("latitude", "0"),shared.getString("longitude", "0") );
                    if (Utils.isNetworkAvailable(OldGroupVoBo_Activity.this))
                    {
                        updateGroup(URL);
                    } else {
                        saveOffline(URL);
                    }
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
                Intent intent = new Intent(OldGroupVoBo_Activity.this, OldGroupsList_Activity.class);
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

                llAmountGeneral.setVisibility(View.VISIBLE);
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

                llAmountGeneral.setVisibility(View.GONE);
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

                llAmountGeneral.setVisibility(View.GONE);
                movement=3;
            }
        });
    }


    public void updateGroup(String URL) {
        Log.d("WS OldGroupVoBo:", URL);

        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
        StringRequest MyStringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "Se realizo la actualización correctamente", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(OldGroupVoBo_Activity.this, OldGroupsList_Activity.class);
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


    public void saveOffline(String URL) {
        final SharedPreferences shared = getSharedPreferences("userInfo", MODE_PRIVATE);
        Log.d("DB Prom:", URL);
        Main_Activity.event = new Event();
        Main_Activity.event.setUrlWS(URL);
        Main_Activity.event.setStatus(0);
        Main_Activity.event.insert();

        Toast.makeText(getApplicationContext(), "Los datos han sido guardados de manera offline", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(OldGroupVoBo_Activity.this, OldGroupsList_Activity.class);
        startActivity(intent);
        finish();
    }



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
