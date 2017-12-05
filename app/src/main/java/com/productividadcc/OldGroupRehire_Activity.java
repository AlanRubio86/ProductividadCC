package com.productividadcc;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
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

public class OldGroupRehire_Activity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener {
    private Calendar calendar,calendar2;
    EditText editIntegrant ,editNewIntegrant, editIntegrantRec,editAmount,editDateEstimated,editDateReprogram;
    Spinner spnCancelMotive;
    TextView ceros, horaTxt;
    String groupID,tokenId,employeeId;
    TextView nombreLbl;
    String URL ="";
    private int movement=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oldgrouprehire_activity);

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
        final TextInputLayout llnews = (TextInputLayout) findViewById(R.id.llNews);
        final TextInputLayout llrecontratation = (TextInputLayout) findViewById(R.id.llRecontratation);
        final TextInputLayout date = (TextInputLayout) findViewById(R.id.llDate);
        final TextInputLayout amount = (TextInputLayout) findViewById(R.id.llAmount);
        final TextInputLayout reprogram = (TextInputLayout) findViewById(R.id.llDateReprogram);
        final LinearLayout cancelmotive = (LinearLayout) findViewById(R.id.llmotivecancel);
        final LinearLayout btnsave = (LinearLayout) findViewById(R.id.btnSave);

       editIntegrant = (EditText) findViewById(R.id.editIntegrant);
       editNewIntegrant = (EditText) findViewById(R.id.editNewIntegrant);
       editIntegrantRec = (EditText) findViewById(R.id.editIntegrantRec);
       editAmount = (EditText) findViewById(R.id.editAmount);
       editDateEstimated = (EditText) findViewById(R.id.editDateEstimated);
       editDateReprogram = (EditText) findViewById(R.id.editDateReprogram);
       spnCancelMotive=(Spinner) findViewById(R.id.spnCancelMotive);

        final LinearLayout llAmountGeneral=(LinearLayout) findViewById(R.id.llAmountGeneral);
        ceros=(TextView)findViewById(R.id.ceros);

        ArrayAdapter adapter2 = ArrayAdapter.createFromResource(this, R.array.recontratationCancelItems, R.layout.spinner_item);
        spnCancelMotive.setAdapter(adapter2);

        mTitle.setText("Inicio de Recontratación");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if (getIntent().getExtras() != null) {
            String groupName = getIntent().getExtras().getString("groupName");
            nombreLbl.setText(groupName);
            groupID = getIntent().getExtras().getString("groupId");
        } else {
            groupID = "0";
        }


        calendar = Calendar.getInstance();

        editDateEstimated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.newInstance(OldGroupRehire_Activity.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show(getFragmentManager(), "datePicker");
            }
        });

        calendar2 = Calendar.getInstance();

        editDateReprogram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.newInstance(OldGroupRehire_Activity.this, calendar2.get(Calendar.YEAR), calendar2.get(Calendar.MONTH), calendar2.get(Calendar.DAY_OF_MONTH)).show(getFragmentManager(), "datePicker");
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            final SharedPreferences shared = getSharedPreferences("userInfo", MODE_PRIVATE);
                if(movement==1) {
                    if (editIntegrant.getText().toString().isEmpty()) {
                        integrants.setError("This field can not be blank");
                        Toast.makeText(getApplicationContext(), "Favor de capturar los datos solicitados", Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        if(Integer.parseInt(editIntegrant.getText().toString())<=0)
                        {
                            integrants.setError("El total de integrantes tiene que ser mayor que 0");
                            Toast.makeText(getApplicationContext(), "El total de integrantes tiene que ser mayor que 0", Toast.LENGTH_LONG).show();
                            return;
                        }else
                            {
                                integrants.setError(null);
                            }

                    }
                    if (editNewIntegrant.getText().toString().isEmpty()) {
                        llnews.setError("This field can not be blank");
                        Toast.makeText(getApplicationContext(), "Favor de capturar los datos solicitados", Toast.LENGTH_LONG).show();
                        return;
                    } else {
                            llnews.setError(null);
                    }
                    if (editIntegrantRec.getText().toString().isEmpty()) {
                        llrecontratation.setError("This field can not be blank");
                        Toast.makeText(getApplicationContext(), "Favor de capturar los datos solicitados", Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        if(Integer.parseInt(editIntegrantRec.getText().toString())<=0)
                        {
                            llrecontratation.setError("Recontratados tiene que ser mayor que 0");
                            Toast.makeText(getApplicationContext(), "Recontratados tiene que ser mayor que 0", Toast.LENGTH_LONG).show();
                            return;
                        }else
                        {
                            llrecontratation.setError(null);
                        }
                    }
                    if (editAmount.getText().toString().isEmpty()) {
                        amount.setError("This field can not be blank");
                        Toast.makeText(getApplicationContext(), "Favor de capturar los datos solicitados", Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        if(Double.parseDouble(editAmount.getText().toString())<=0){
                            amount.setError("El monto tiene que ser mayor que 0");
                            Toast.makeText(getApplicationContext(), "El monto tiene que ser mayor que 0", Toast.LENGTH_LONG).show();
                            return;
                        }else{
                            amount.setError(null);
                        }
                    }

                    if (editDateEstimated.getText().toString().isEmpty()) {
                        date.setError("This field can not be blank");
                        Toast.makeText(getApplicationContext(), "Favor de capturar los datos solicitados", Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        date.setError(null);
                    }

                    Double amount=Double.parseDouble(editAmount.getText().toString())*1000;
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = new Date();
                        URL=String.format(Globales.URL_ACTUALIZAR_ETAPA,
                                         tokenId,
                                         employeeId,
                                         groupID,
                                         "5",
                                        "",
                                        amount.toString(),
                                        editIntegrant.getText().toString(),
                                        editNewIntegrant.getText().toString(),
                                        editIntegrantRec.getText().toString(),
                                        dateFormat.format(date),
                                        editDateEstimated.getText().toString(),
                                        "0",
                                        "0",
                                        shared.getString("latitude", "0"),
                                        shared.getString("longitude", "0"));

                    if (Utils.isNetworkAvailable(OldGroupRehire_Activity.this)) {
                        updateGroup(URL);
                    } else {
                        saveOffline(URL);
                    }


                }else if(movement==2)
                {
                    if (editDateReprogram.getText().toString().isEmpty()) {
                        reprogram.setError("This field can not be blank");
                        Toast.makeText(getApplicationContext(), "Favor de capturar los datos solicitados", Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        reprogram.setError(null);
                    }

                    URL=String.format(Globales.URL_REPROGRAMAR_ETAPA,tokenId,employeeId,groupID,"5",editDateReprogram.getText().toString(),"0",shared.getString("latitude", "0"),shared.getString("longitude", "0") );

                    if (Utils.isNetworkAvailable(OldGroupRehire_Activity.this)) {
                        updateGroup(URL);
                    } else {
                        saveOffline(URL);
                    }


                }else if(movement==3) {
                    if (spnCancelMotive.getSelectedItem().toString().trim().equals("(Seleccionar)"))
                    {
                        Toast.makeText(getApplicationContext(), "Favor de seleccionar un motivo", Toast.LENGTH_LONG).show();
                        return;
                    }
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = new Date();
                    String motive="";
                    switch(spnCancelMotive.getSelectedItem().toString())
                    {

                        case "No se completó el grupo":
                            motive="2";
                            break;
                        case "No les interesa nuestro producto":
                            motive="5";
                            break;

                    }
                    URL=String.format(Globales.URL_CANCELAR_ETAPA,tokenId,employeeId,groupID,"5",dateFormat.format(date),motive,shared.getString("latitude", "0"),shared.getString("longitude", "0") );
                    if (Utils.isNetworkAvailable(OldGroupRehire_Activity.this))
                    {
                        updateGroup(URL);
                    } else {
                        saveOffline(URL);
                    }


                }

            }
        });

        //region Listener
        editIntegrant.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { integrants.setError(null);  }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        editNewIntegrant.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { llnews.setError(null);  }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        editIntegrantRec.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { llrecontratation.setError(null);  }
            @Override
            public void afterTextChanged(Editable s) { }
        });


        editAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { amount.setError(null);  }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        editDateEstimated.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { date.setError(null);  }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        editDateEstimated.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });

        editDateEstimated.setInputType(InputType.TYPE_NULL);

        editDateReprogram.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { reprogram.setError(null);  }
            @Override
            public void afterTextChanged(Editable s) { }
        });


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OldGroupRehire_Activity.this, OldGroupsList_Activity.class);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.continueBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                integrants.setVisibility(View.VISIBLE);
                llnews.setVisibility(View.VISIBLE);
                llrecontratation.setVisibility(View.VISIBLE);
                date.setVisibility(View.VISIBLE);
                amount.setVisibility(View.VISIBLE);
                btnsave.setVisibility(View.VISIBLE);
                reprogram.setVisibility(View.GONE);
                cancelmotive.setVisibility(View.GONE);
                llAmountGeneral.setVisibility(View.VISIBLE);
                movement=1;
            }
        });
        /*
        findViewById(R.id.reprogBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    integrants.setVisibility(View.GONE);
                    llnews.setVisibility(View.GONE);
                    llrecontratation.setVisibility(View.GONE);
                    date.setVisibility(View.GONE);
                    amount.setVisibility(View.GONE);
                    btnsave.setVisibility(View.VISIBLE);
                    reprogram.setVisibility(View.VISIBLE);
                    cancelmotive.setVisibility(View.GONE);
                     movement=2;

            }
        });
        */

        findViewById(R.id.cancelBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                integrants.setVisibility(View.GONE);
                llnews.setVisibility(View.GONE);
                llrecontratation.setVisibility(View.GONE);
                date.setVisibility(View.GONE);
                amount.setVisibility(View.GONE);
                btnsave.setVisibility(View.VISIBLE);
                reprogram.setVisibility(View.GONE);
                cancelmotive.setVisibility(View.VISIBLE);
                llAmountGeneral.setVisibility(View.GONE);
                movement=3;
            }
        });

        //endregion
    }

    public void updateGroup(final String URL) {
        Log.d("WS RehireOldGroup:", URL);

        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
        StringRequest MyStringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.toLowerCase().equals("ok")) {
                            Toast.makeText(getApplicationContext(), "Se realizo la actualización correctamente", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(OldGroupRehire_Activity.this, OldGroupsList_Activity.class);
                            startActivity(intent);
                            finish();
                        }else
                        {
                            saveOffline(URL);
                        }
                    }
                }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
                saveOffline(URL);
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

        Intent intent = new Intent(OldGroupRehire_Activity.this, OldGroupsList_Activity.class);
        startActivity(intent);
        finish();
    }



    @Override
    public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
        java.text.DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        calendar.set(year, monthOfYear, dayOfMonth);
        editDateEstimated.setText(df.format(calendar.getTime()));
        calendar2.set(year, monthOfYear, dayOfMonth);
        editDateReprogram.setText(df.format(calendar2.getTime()));
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        String time = String.format(Locale.ENGLISH, "%02d:%02d", hourOfDay, minute);
        horaTxt.setText(time);

    }

}
