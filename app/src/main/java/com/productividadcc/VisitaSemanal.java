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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.productividadcc.database.Event;
import com.productividadcc.utilerias.Globales;
import com.productividadcc.utilerias.Tasks.CheckinListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class VisitaSemanal extends AppCompatActivity implements View.OnClickListener, CheckinListener {

    private EditText 	group;
    private EditText 	ciclo;
    private EditText 	semana;
    private EditText 	integrantes;

    private CheckBox asistenciaCompleta;
    private CheckBox	pagoCompleto;
    private CheckBox	ahorroCompleto;
    private CheckBox	grupoNoAsistio;
    private CheckBox	noVisiteGrupo;

    String grupoId;
    String strCiclo;
    String strSemana;
    String strIntegrantes;
    String idEmpleado;
    String eventID;
    String imeiNumber;
    String nombreGrupo;
    String URLV = Globales.URL_REGISTRO_VISITA;
    String URLE = Globales.URL_REGISTRO_EVALUACION;

    //TextView nombreLbl = (TextView) findViewById(R.id.nombreLabel);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.visita_semanal_activity);

        group = (EditText) findViewById(R.id.group);
        ciclo = (EditText) findViewById(R.id.ciclo);
        semana = (EditText) findViewById(R.id.semana);
        integrantes = (EditText) findViewById(R.id.integrantes);

        asistenciaCompleta = (CheckBox) findViewById(R.id.checkBox1);
        pagoCompleto = (CheckBox) findViewById(R.id.checkBox2);
        ahorroCompleto = (CheckBox) findViewById(R.id.checkBox3);
        grupoNoAsistio = (CheckBox) findViewById(R.id.CheckBox01);
        noVisiteGrupo = (CheckBox) findViewById(R.id.CheckBox02);

        // Find the toolbar view and set as ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // ...
        // Remove default title text
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Get access to the custom title view
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Evaluación de Visita Semanal");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VisitaSemanal.this, AgregarEvento.class);
                startActivity(intent);
                finish();
            }
        });

        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        imeiNumber = telephonyManager.getDeviceId();

        TextView nombreLbl = (TextView) findViewById(R.id.nombreLabel);

        if (getIntent().getExtras() != null) {
            if(!getIntent().getExtras().getString("eventId").equalsIgnoreCase("")){
                nombreGrupo = getIntent().getExtras().getString("groupName");
                nombreLbl.setText(nombreGrupo);
                eventID = getIntent().getExtras().getString("eventId").replace(" ", "");
            }else{
                eventID = "0";
            }
        }

        if(eventID == null){
            eventID = "0";
        }

        findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                grupoId = group.getText().toString();
                strCiclo =ciclo.getText().toString();
                strSemana =semana.getText().toString();
                strIntegrantes =integrantes.getText().toString();
                String locAsistencia = Globales.STR_VACIO;
                String locPago = Globales.STR_VACIO;
                String locAhorro = Globales.STR_VACIO;
                String locGrupoNoAs = Globales.STR_VACIO;
                String locNoVisito = Globales.STR_VACIO;

                final SharedPreferences shared = getSharedPreferences("userInfo", MODE_PRIVATE);

                if(validaNumerosCampo(grupoId)) {
                    grupoId = grupoId.trim();

                    if(grupoId == null || grupoId.trim().length() == 0){
                        Toast.makeText(VisitaSemanal.this, R.string.group_error, Toast.LENGTH_LONG).show();
                        return;
                    } else if(Integer.valueOf(grupoId) == 0){
                        Toast.makeText(VisitaSemanal.this, R.string.group_error, Toast.LENGTH_LONG).show();
                        return;
                    } else if(grupoId.length() < MainActivity.GROUP_DIGITS){
                        Toast.makeText(VisitaSemanal.this, R.string.group_msg, Toast.LENGTH_LONG).show();
                        return;
                    } else if(strCiclo == null || strCiclo.trim().length() == 0 || Integer.valueOf(strCiclo) == 0){
                        Toast.makeText(VisitaSemanal.this, R.string.ciclo_errmsg, Toast.LENGTH_LONG).show();
                        return;
                    } else if(strCiclo.length() < MainActivity.CICLO_DIGITS){
                        Toast.makeText(VisitaSemanal.this, R.string.ciclo_msg, Toast.LENGTH_LONG).show();
                        return;
                    } else if(strSemana == null || strSemana.trim().length() == 0 || Integer.valueOf(strSemana) == 0){
                        Toast.makeText(VisitaSemanal.this, R.string.semana_errmsg, Toast.LENGTH_LONG).show();
                        return;
                    } else if(strSemana.length() < MainActivity.SEMANA_DIGITS){
                        Toast.makeText(VisitaSemanal.this, R.string.semana_msg, Toast.LENGTH_LONG).show();
                        return;
                    } else if(Integer.valueOf(strSemana) > MainActivity.SEMANA_MAXIMO){
                        Toast.makeText(VisitaSemanal.this, R.string.semana_errmax, Toast.LENGTH_LONG).show();
                        return;
                    } else if(strIntegrantes == null || strIntegrantes.trim().length() == 0 || Integer.valueOf(strIntegrantes) == 0){
                        Toast.makeText(VisitaSemanal.this, R.string.integra_errmsg, Toast.LENGTH_LONG).show();
                        return;
                    } else if(strIntegrantes.length() < MainActivity.INTEGRANTES_DIGITS){
                        Toast.makeText(VisitaSemanal.this, R.string.integra_msg, Toast.LENGTH_LONG).show();
                        return;
                    } else if(Integer.valueOf(strIntegrantes) < MainActivity.INTEGRANTES_MINIMO){
                        Toast.makeText(VisitaSemanal.this, R.string.integra_errmin, Toast.LENGTH_LONG).show();
                        return;
                    } else {

                        if(asistenciaCompleta.isChecked()) {
                             locAsistencia = "1";
                        }else {
                            locAsistencia = "0";
                        }

                        if(pagoCompleto.isChecked()) {
                            locPago = "1";
                        }else {
                            locPago = "0";
                        }

                        if(ahorroCompleto.isChecked()){
                            locAhorro = "1";
                        } else{
                            locAhorro = "0";
                        }

                        if(grupoNoAsistio.isChecked()){
                            locGrupoNoAs = "1";
                        }else{
                            locGrupoNoAs = "0";
                        }

                        if(noVisiteGrupo.isChecked()){
                            locNoVisito = "1";
                        }else{
                            locNoVisito = "0";
                        }

                        /* GENERA URL PARA GUARDAR INFORMACION DE LA VISITA */
                        URLV += "phone=" + imeiNumber +
                                "&group=" + grupoId +
                                "&ciclo=" + strCiclo +
                                "&semana=" + strSemana +
                                "&integrantes=" + strIntegrantes +
                                "&event=Visita" +
                                "&stamp=" + (System.currentTimeMillis()/1000) +
                                "&latitude=" + shared.getString("latitude", "0") +
                                "&longitude=" + shared.getString("longitude", "0") +
                                "&asistencia=" + locAsistencia +
                                "&pago=" + locPago +
                                "&ahorro=" + locAhorro +
                                "&grupoNoAsiste=" + locGrupoNoAs +
                                "&noVisitoGrupo=" + locNoVisito +
                                "&visit=" + Globales.EVALUACION_SEMANAL +
                                "&llamada=" + Globales.STR_VACIO +
                                "&cerroFicha=" + Globales.STR_VACIO +
                                "&subenReg=" + Globales.STR_VACIO +
                                "&fechaLla=" + Globales.STR_FECVAC +
                                "&horaLla=" + Globales.STR_VACIO +
                                "&nuAgSe=" + eventID +
                                "&emplea=" + shared.getString("userNumber", "0") ;

                        Log.d("WS Visita:", URLV);

                        if(asistenciaCompleta.isChecked() && pagoCompleto.isChecked() && ahorroCompleto.isChecked()){
                            procesaCeros(URLV, imeiNumber);
                        } else if(grupoNoAsistio.isChecked()){
                            procesaUnos(URLV, imeiNumber);
                        } else if(noVisiteGrupo.isChecked()){
                            procesaCeros(URLV, imeiNumber);
                        } else {
                            Intent intent = new Intent(VisitaSemanal.this, VisitaEvaluacion.class);
                            intent.putExtra("eventId", eventID);
                            intent.putExtra("grupo", grupoId);
                            intent.putExtra("ciclo", strCiclo);
                            intent.putExtra("semana", strSemana);
                            intent.putExtra("integrantes", strIntegrantes);
                            intent.putExtra("groupName", nombreGrupo);
                            intent.putExtra("URLV", URLV);
                            startActivity(intent);
                            finish();
                        }
                    }
                } else {
                    Toast.makeText(VisitaSemanal.this, "Numero de Grupo Invalido", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        });
    }

    void procesaCeros(String URLV, String IMEI){

        int iteraciones = Integer.valueOf(integrantes.getText().toString());
        String cliente, noAsistio, noPago, noAhorro, noSoli = Globales.STR_VACIO;
        String id = group.getText().toString();
        String locStrCiclo =ciclo.getText().toString();
        String locStrSemana =semana.getText().toString();
        String locStrIntegrantes =integrantes.getText().toString();
        String urlCiclo = Globales.STR_VACIO;

        if (Utils.isNetworkAvailable(VisitaSemanal.this)) {
            //sendEventInfo();
            RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
            StringRequest MyStringRequest = new StringRequest(Request.Method.GET, URLV,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //This code is executed if the server responds, whether or not the response contains data.
                            //The String 'response' contains the server's response.
                            //clearFields();
                            Toast.makeText(getApplicationContext(), "Evento guardado correctamente", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(VisitaSemanal.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                @Override
                public void onErrorResponse(VolleyError error) {
                    //This code is executed if there is an error.
                    Log.d("Send Event info", "response error" + error.toString());
                    Toast.makeText(getApplicationContext(), "Error de conexión, por favor vuelve a intentar.", Toast.LENGTH_LONG).show();
                    //mprogressBar.setVisibility(View.GONE);
                }
            });

            MyRequestQueue.add(MyStringRequest);

            for (int i = 0; i < iteraciones; i++) {
                cliente = String.valueOf(i+1);
                noAsistio = "0";
                noPago = "0";
                noAhorro = "0";
                noSoli = "0";
                urlCiclo = URLE;
                urlCiclo += "phone=" + IMEI +
                        "&grupo=" + id +
                        "&ciclo=" + locStrCiclo +
                        "&cliente=" + cliente +
                        "&semana=" + locStrSemana +
                        "&noasistio=" + noAsistio +
                        "&noPago=" + noPago +
                        "&noahorro=" + noAhorro +
                        "&nosolida=" + noSoli +
                        "&status=A";
                Log.d("WS VisSem", urlCiclo);

                MyRequestQueue = Volley.newRequestQueue(this);
                MyStringRequest = new StringRequest(Request.Method.GET, urlCiclo,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //This code is executed if the server responds, whether or not the response contains data.
                                //The String 'response' contains the server's response.
                                //clearFields();
                                Toast.makeText(getApplicationContext(), "Evento guardado correctamente", Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(VisitaSemanal.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //This code is executed if there is an error.
                        Log.d("Send Event info", "response error" + error.toString());
                        Toast.makeText(getApplicationContext(), "Error de conexión, por favor vuelve a intentar.", Toast.LENGTH_LONG).show();
                        //mprogressBar.setVisibility(View.GONE);
                    }
                });

                MyRequestQueue.add(MyStringRequest);
            }
        } else {
            //saveEventInfo();

            MainActivity.event = new Event();
            MainActivity.event.setUrlWS(URLV);
            MainActivity.event.setStatus(0);
            MainActivity.event.insert();

            for (int i = 0; i < iteraciones; i++) {
                cliente = String.valueOf(i+1);
                noAsistio = "0";
                noPago = "0";
                noAhorro = "0";
                noSoli = "0";
                urlCiclo = URLE;
                urlCiclo += "phone=" + IMEI +
                        "&grupo=" + id +
                        "&ciclo=" + locStrCiclo +
                        "&cliente=" + cliente +
                        "&semana=" + locStrSemana +
                        "&noasistio=" + noAsistio +
                        "&noPago=" + noPago +
                        "&noahorro=" + noAhorro +
                        "&nosolida=" + noSoli +
                        "&status=A";
                Log.d("DB VisSem", urlCiclo);

                MainActivity.event = new Event();
                MainActivity.event.setUrlWS(urlCiclo);
                MainActivity.event.setStatus(0);
                MainActivity.event.insert();

            }

            Toast.makeText(getApplicationContext(), "Los datos han sido guardados de manera offline", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(VisitaSemanal.this, MainActivity.class);
            startActivity(intent);
            finish();
        }


    }

    void procesaUnos(String URLV, String IMEI){

        int iteraciones = Integer.valueOf(integrantes.getText().toString());
        String cliente, noAsistio, noPago, noAhorro, noSoli = Globales.STR_VACIO;
        String id = group.getText().toString();
        String locStrCiclo =ciclo.getText().toString();
        String locStrSemana =semana.getText().toString();
        String locStrIntegrantes =integrantes.getText().toString();
        String urlCiclo = Globales.STR_VACIO;

        if (Utils.isNetworkAvailable(VisitaSemanal.this)) {
            RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
            StringRequest MyStringRequest = new StringRequest(Request.Method.GET, URLV,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //This code is executed if the server responds, whether or not the response contains data.
                            //The String 'response' contains the server's response.
                            //clearFields();
                            Toast.makeText(getApplicationContext(), "Evento guardado correctamente " + response, Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(VisitaSemanal.this, MainActivity.class);
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

            for (int i = 0; i < iteraciones; i++) {
                cliente = String.valueOf(i+1);
                noAsistio = "1";
                noPago = "1";
                noAhorro = "1";
                noSoli = "1";
                urlCiclo = URLE;
                urlCiclo += "phone=" + IMEI +
                        "&grupo=" + id +
                        "&ciclo=" + locStrCiclo +
                        "&cliente=" + cliente +
                        "&semana=" + locStrSemana +
                        "&noasistio=" + noAsistio +
                        "&noPago=" + noPago +
                        "&noahorro=" + noAhorro +
                        "&nosolida=" + noSoli +
                        "&status=A";
                Log.d("WS VisSem", urlCiclo);

                MyRequestQueue = Volley.newRequestQueue(this);
                MyStringRequest = new StringRequest(Request.Method.GET, urlCiclo,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //This code is executed if the server responds, whether or not the response contains data.
                                //The String 'response' contains the server's response.
                                //clearFields();
                                //Toast.makeText(getApplicationContext(), "Evento guardado correctamente " + response, Toast.LENGTH_LONG).show();

                                //Intent intent = new Intent(VisitaSemanal.this, MainActivity.class);
                                //startActivity(intent);
                                //finish();
                            }
                        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //This code is executed if there is an error.
                        Log.d("Send Event info", "response error" + error.toString());
                        Toast.makeText(getApplicationContext(), "Error de conexión, por favor vuelve a intentar." , Toast.LENGTH_LONG).show();
                        //mprogressBar.setVisibility(View.GONE);
                    }
                });

                MyRequestQueue.add(MyStringRequest);
            }
        }else {
            //saveEventInfo();

            MainActivity.event = new Event();
            MainActivity.event.setUrlWS(URLV);
            MainActivity.event.setStatus(0);
            MainActivity.event.insert();

            for (int i = 0; i < iteraciones; i++) {
                cliente = String.valueOf(i+1);
                noAsistio = "1";
                noPago = "1";
                noAhorro = "1";
                noSoli = "1";
                urlCiclo = URLE;
                urlCiclo += "phone=" + IMEI +
                        "&grupo=" + id +
                        "&ciclo=" + locStrCiclo +
                        "&cliente=" + cliente +
                        "&semana=" + locStrSemana +
                        "&noasistio=" + noAsistio +
                        "&noPago=" + noPago +
                        "&noahorro=" + noAhorro +
                        "&nosolida=" + noSoli +
                        "&status=A";
                Log.d("DB VisSem", urlCiclo);

                MainActivity.event = new Event();
                MainActivity.event.setUrlWS(urlCiclo);
                MainActivity.event.setStatus(0);
                MainActivity.event.insert();

            }

            Toast.makeText(getApplicationContext(), "Los datos han sido guardados de manera offline", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(VisitaSemanal.this, MainActivity.class);
            startActivity(intent);
            finish();
        }


    }

    public static boolean validaNumerosCampo(String strCadena){
        boolean respuesta = true;

        for(int  i = 0 ; i < strCadena.length() ; i ++){
            int valor = strCadena.charAt(i);
            //System.out.println("i:" + i + ", valor:" + valor);
            if (valor < 48 || valor > 57) {
                respuesta = false;
            }
        }

        return respuesta;
    }

    @Override
    public void onClick(View v){
    }

    @Override
    public void checkinFinished() {
    }

    @Override
    public void onCheckedChanged(View v) {
    }
}
