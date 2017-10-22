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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.productividadcc.utilerias.Globales;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CancelaVoBo extends AppCompatActivity  {
    String imeiNumber;
    private Calendar calendar;
    String fechaTxt, horaTxt;
    String eventID;
    String motivoCancelacion;
    TextView nombreLbl;

    Spinner events;
    String URL = Globales.URL_REGISTRO_AGENDA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cancelavobo_activity);

        // Find the toolbar view and set as ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // ...
        // Remove default title text
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Get access to the custom title view
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        events = (Spinner) findViewById(R.id.motVoBo);
        mTitle.setText("Cancelacion VoBo");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CancelaVoBo.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        imeiNumber = telephonyManager.getDeviceId();

        nombreLbl = (TextView) findViewById(R.id.nombreLabel);
        if (getIntent().getExtras() != null) {
            String groupName = getIntent().getExtras().getString("groupName");
            nombreLbl.setText(groupName);
            eventID = getIntent().getExtras().getString("eventId").replace(" ", "");
        }else{
            eventID = "0";
        }

        findViewById(R.id.guardarBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if (!montoTxt.getText().toString().equals("") && !integrantesTxt.getText().toString().equals("") && !fechaTxt.getText().toString().equals("")
                        && !horaTxt.getText().toString().equals("")) {*/
                    sendEventInfo();
                /*} else {
                    Toast.makeText(getApplicationContext(), "Faltan datos por capturar", Toast.LENGTH_LONG).show();
                }*/
            }
        });
    }

    public void sendEventInfo() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        final SharedPreferences shared = getSharedPreferences("userInfo", MODE_PRIVATE);

        SimpleDateFormat formatFec = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatHor = new SimpleDateFormat("HH:mm");
        fechaTxt = formatFec.format(Calendar.getInstance().getTime());
        horaTxt = formatHor.format(Calendar.getInstance().getTime());

        int id = events.getSelectedItemPosition();
        if (id == Globales.MC_VOBO_ANALISISRECHAZA){
            motivoCancelacion = Globales.STR_VOBO_ANALISISRECHAZA;
        } else if( id == Globales.MC_VOBO_GERENTERECHAZA){
            motivoCancelacion = Globales.STR_VOBO_GERENTERECHAZA;
        } else if( id == Globales.MC_VOBO_GRUPOINCOMPLETO){
            motivoCancelacion = Globales.STR_VOBO_GRUPOINCOMPLETO;
        } else if( id == Globales.MC_VOBO_CLIENTENOPRESE){
            motivoCancelacion = Globales.STR_VOBO_CLIENTENOPRESE;
        } else if( id == Globales.MC_VOBO_FALTAINE){
            motivoCancelacion = Globales.STR_VOBO_FALTAINE;
        } else if( id == Globales.MC_VOBO_LISTANEGRA){
            motivoCancelacion = Globales.STR_VOBO_LISTANEGRA;
        }

        /*Log.d("requestUrl:","http://asistente.crediclub.com/2.0/altaAgenda.php?fecha="+selectedStringDate+"&hora="
                +horaTxt.getText().toString()+"&tipEve="+4+"&emplea="+shared.getString("userNumber", "0")+"&grupo=%20"+"&ciclo=%20"
                +"&latitu="+shared.getString("latitude", "0")+"&longit="+shared.getString("longitude", "0")+"&nuAgSe="
                +eventID+"&imei="+imeiNumber+"&stamp="+String.valueOf(System.currentTimeMillis()/1000)+"&monGru="+montoTxt.getText().toString()+
                "&integr="+integrantesTxt.getText().toString()+"&semRen="+0+"&coment=%20");*/

        URL +=  "fecha=" + fechaTxt +
                "&hora=" + horaTxt +
                "&tipEve=" + Globales.CANCELA_VOBO +
                "&emplea=" + shared.getString("userNumber", "0") +
                "&tipgru=" + motivoCancelacion +
                "&grupo=" + Globales.STR_VACIO +
                "&ciclo=" + Globales.STR_VACIO +
                "&latitu=" + shared.getString("latitude", "0") +
                "&longit=" + shared.getString("longitude", "0") +
                "&nuAgSe=" + eventID +
                "&imei=" + imeiNumber +
                "&stamp=" + (System.currentTimeMillis()/1000) +
                "&monGru=" + Globales.STR_CERO +
                "&integr=" + Globales.STR_CERO +
                "&semRen=" + Globales.STR_CERO +
                "&coment=" + nombreLbl.getText().toString();

        Log.d("WS Cancela vObO:", URL);

        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
        StringRequest MyStringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //This code is executed if the server responds, whether or not the response contains data.
                        //The String 'response' contains the server's response.
                        clearFields();
                        Toast.makeText(getApplicationContext(), "Evento guardado correctamente " + response, Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(CancelaVoBo.this, MainActivity.class);
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
                //params.put("fecha", selectedStringDate);
                //params.put("hora", horaTxt.getText().toString());
                params.put("tipEve", "4");
                params.put("emplea", shared.getString("userNumber", "0"));
                params.put("grupo", "%20");
                params.put("ciclo", "%20");
                params.put("latitu", shared.getString("latitude", "0"));
                params.put("longit", shared.getString("longitude", "0"));
                params.put("nuAgSe", eventID);
                params.put("imei", imeiNumber);
                params.put("stamp", String.valueOf(System.currentTimeMillis()/1000));
                //params.put("monGru", montoTxt.getText().toString());
                //params.put("integr", integrantesTxt.getText().toString());
                params.put("semRen", "0");
                params.put("coment", "%20");

                return params;
            }
        };

        MyRequestQueue.add(MyStringRequest);
    }

    public void clearFields () {
    }
}
