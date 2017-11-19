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

public class NewGroupDisbursement_Activity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener {
    private Calendar calendar;
    EditText editIntegrant,editAmount,editDisbursement;
    Spinner spnGroupType,spnMotiveReprogram,spnCancelMotive;
    String groupID,tokenId,employeeId;
    TextView ceros,nombreLbl;
    String URL = "";
    private int movement=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newgroupdisbursement_activity);

        // Find the toolbar view and set as ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        SharedPreferences shared = getSharedPreferences("userInfo", MODE_PRIVATE);
        tokenId = shared.getString("tokenId", "0");
        employeeId=shared.getString("numEmployee", "0");



        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        nombreLbl = (TextView) findViewById(R.id.nombreLabel);

        final TextInputLayout integrants = (TextInputLayout) findViewById(R.id.llIntegrant);
        final TextInputLayout amount = (TextInputLayout) findViewById(R.id.llAmount);
        final LinearLayout groupType = (LinearLayout) findViewById(R.id.llgrouptype);
        final LinearLayout motiveReprogram = (LinearLayout) findViewById(R.id.llMotiveReprogram);
        final TextInputLayout dateReprogram = (TextInputLayout) findViewById(R.id.llDate);
        final LinearLayout cancelMotive = (LinearLayout) findViewById(R.id.llCancelMotive);
        final LinearLayout btnsave = (LinearLayout) findViewById(R.id.btnSave);
        final LinearLayout llAmountGeneral=(LinearLayout) findViewById(R.id.llAmountGeneral);
        ceros=(TextView)findViewById(R.id.ceros);


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
                DatePickerDialog.newInstance(NewGroupDisbursement_Activity.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show(getFragmentManager(), "datePicker");
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SharedPreferences shared = getSharedPreferences("userInfo", MODE_PRIVATE);
                if(movement==1)
                {
                    if(editIntegrant.getText().toString().isEmpty())
                    {
                        integrants.setError("This field can not be blank");
                        Toast.makeText(getApplicationContext(), "Favor de capturar los datos solicitados", Toast.LENGTH_LONG).show();;
                        return;
                    } else {
                        if(Integer.parseInt(editIntegrant.getText().toString())<=0)
                        {
                            integrants.setError("El total de integrantes tiene que ser mayor que 0");
                            Toast.makeText(getApplicationContext(), "El total de integrantes tiene que ser mayor que 0", Toast.LENGTH_LONG).show();;
                            return;
                        }else{
                        integrants.setError(null);
                        }
                    }
                    if(editAmount.getText().toString().isEmpty())
                    {
                        amount.setError("This field can not be blank");
                        Toast.makeText(getApplicationContext(), "Favor de capturar los datos solicitados", Toast.LENGTH_LONG).show();;
                        return;
                    } else {
                        if(Double.parseDouble(editAmount.getText().toString())<=0)
                        {
                            amount.setError("El monto tiene que ser mayor que 0");
                            Toast.makeText(getApplicationContext(), "El monto tiene que ser mayor que 0", Toast.LENGTH_LONG).show();;
                            return;

                        }else{
                        amount.setError(null);
                        }
                    }
                    if (spnGroupType.getSelectedItem().toString().trim().equals("(Seleccionar)"))
                    {
                        Toast.makeText(getApplicationContext(), "Favor de seleccionar un motivo", Toast.LENGTH_LONG).show();;
                        return;
                    }

                    Double amount=Double.parseDouble(editAmount.getText().toString())*1000;
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = new Date();

                    String groupType="";
                    switch(spnGroupType.getSelectedItem().toString())
                    {
                        case "Super Estrella (27.32% -$70.00)":
                            groupType="1";
                            break;
                        case "Super Estrella (43.35% -$72.00)":
                            groupType="2";
                            break;
                        case "Super Estrella (55.22% -$73.50)":
                            groupType="3";
                            break;
                        case "Super Estrella (59.13% -$74.00)":
                            groupType="4";
                            break;
                    }
                    URL=String.format(Globales.URL_ACTUALIZAR_ETAPA,tokenId,employeeId,groupID,"4","",amount.toString(),editIntegrant.getText().toString(),"0","0",dateFormat.format(date),"", "0",groupType,
                            shared.getString("latitude", "0"),
                            shared.getString("longitude", "0"));

                    if (Utils.isNetworkAvailable(NewGroupDisbursement_Activity.this)) {
                        updateGroup(movement,URL,editIntegrant.getText().toString());
                    } else {
                        saveOffline(movement,URL,editIntegrant.getText().toString());
                    }

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

                    String motive="";
                    switch(spnMotiveReprogram.getSelectedItem().toString())
                    {
                        case "Clientes no llegan a tiempo":
                            motive="6";
                            break;
                        case "No están todas las integrantes presentes":
                            motive="7";
                            break;
                        case "Error en montos":
                            motive="8";
                            break;
                        case "Error en nombres":
                            motive="9";
                            break;
                        case "Cambio de fecha":
                            motive="10";
                            break;
                    }

                    URL=String.format(Globales.URL_REPROGRAMAR_ETAPA,tokenId,employeeId,groupID,"4",editDisbursement.getText().toString(),motive,shared.getString("latitude", "0"),shared.getString("longitude", "0") );

                    if (Utils.isNetworkAvailable(NewGroupDisbursement_Activity.this)) {
                        updateGroup(movement,URL,editIntegrant.getText().toString());
                    } else {
                        saveOffline(movement,URL,editIntegrant.getText().toString());
                    }


                }else if(movement==3)
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
                        case "Se detectó algún riesgo":
                            motive="21";
                            break;
                        case "El grupo ya no desea el crédito":
                            motive="22";
                            break;
                        case "Mal comportamiento del grupo":
                            motive="23";
                            break;
                        case "Credito vigente con adeudo en la competencia":
                            motive="24";
                           break;
                    }

                    URL=String.format(Globales.URL_CANCELAR_ETAPA,tokenId,employeeId,groupID,"4",dateFormat.format(date),motive,shared.getString("latitude", "0"),shared.getString("longitude", "0") );
                    if (Utils.isNetworkAvailable(NewGroupDisbursement_Activity.this)) {
                        updateGroup(movement,URL,editIntegrant.getText().toString());
                    } else {
                        saveOffline(movement,URL,editIntegrant.getText().toString());
                    }
                }
            }
        });

        //region Listeners

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
                Intent intent = new Intent(NewGroupDisbursement_Activity.this, NewGroupsList_Activity.class);
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
                llAmountGeneral.setVisibility(View.VISIBLE);
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
                llAmountGeneral.setVisibility(View.GONE);
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
                llAmountGeneral.setVisibility(View.GONE);
                movement=3;
            }
        });
        //endregion
    }
    public void updateGroup(final int movement, String URL,final String integrants) {

        Log.d("WS VoBoNewGroup:", URL);

        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
        StringRequest MyStringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        clearFields();
                        if(movement==1)
                        {
                            Toast.makeText(getApplicationContext(), "Se realizo el desembolso correctamente", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(NewGroupDisbursement_Activity.this, FinishNewGroup_Activity.class);
                            intent.putExtra("integrants",integrants);
                            startActivity(intent);
                            finish();
                        }else
                        {
                            Toast.makeText(getApplicationContext(), "Se realizo la actualización correctamente", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(NewGroupDisbursement_Activity.this, NewGroupsList_Activity.class);
                            startActivity(intent);
                            finish();
                        }
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

    public void saveOffline(final int movement, String URL,final String integrants) {
        final SharedPreferences shared = getSharedPreferences("userInfo", MODE_PRIVATE);
        Log.d("DB Prom:", URL);
        Main_Activity.event = new Event();
        Main_Activity.event.setUrlWS(URL);
        Main_Activity.event.setStatus(0);
        Main_Activity.event.insert();

        Toast.makeText(getApplicationContext(), "Los datos han sido guardados de manera offline", Toast.LENGTH_LONG).show();

        if(movement==1)
        {
            Toast.makeText(getApplicationContext(), "Se realizo el desembolso correctamente", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(NewGroupDisbursement_Activity.this, FinishNewGroup_Activity.class);
            intent.putExtra("integrants",integrants);
            startActivity(intent);
            finish();
        }else
        {
            Toast.makeText(getApplicationContext(), "Se realizo la actualización correctamente", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(NewGroupDisbursement_Activity.this, NewGroupsList_Activity.class);
            startActivity(intent);
            finish();
        }
    }

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
