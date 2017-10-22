package com.productividadcc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
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

import com.productividadcc.database.EvaluaEvent;
import com.productividadcc.database.EvaluacionBean;

import java.util.ArrayList;
import java.util.List;

public class VisitaEvaluacion extends AppCompatActivity implements OnClickListener, CheckinListener {

    private Button 		next;
    private Button 		back;

    private TextView 	clave;
    LinearLayout linearLayout;

    private TableLayout etiquetaTabla;

    private List listaWS = new ArrayList();

    private EvaluacionBean bean;

    String eventID="";
    String clsStrGrupo="";
    String clsStrCiclo="";
    String clsStrSemana="";
    String clsStrIntegrantes="";
    String strNombreGrupo="";
    String imeiNumber;

    String URLV = Globales.STR_VACIO;
    String URLE = Globales.URL_REGISTRO_EVALUACION;

    String strCliente, strNoAsistio, strNoPago, strNoAhorro, strNoSoli = Globales.STR_VACIO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.visita_evaluacion_activity);

        clave=(TextView) findViewById(R.id.clave2);
        linearLayout = (LinearLayout) findViewById(R.id.linear);
        next = (Button) findViewById(R.id.next);
        back = (Button) findViewById(R.id.back);

        Integer filas = 0;

        Bundle bundle = getIntent().getExtras();

        if (bundle!=null){

            eventID  = getIntent().getExtras().getString("eventId");
            clsStrGrupo=(String)bundle.getString("grupo");
            clsStrCiclo=(String)bundle.getString("ciclo");
            clsStrSemana=(String)bundle.getString("semana");
            clsStrIntegrantes=(String)bundle.getString("integrantes");
            URLV = getIntent().getExtras().getString("URLV");
            strNombreGrupo = getIntent().getExtras().getString("groupName");
            clave.setText(strNombreGrupo+" Ciclo "+clsStrCiclo+" Semana "+clsStrSemana+" Int. "+clsStrIntegrantes);
            filas = Integer.valueOf(clsStrIntegrantes);
            etiquetaTabla = dibujarTabla(2, filas, 5);
            linearLayout.addView(etiquetaTabla);

            TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            imeiNumber = telephonyManager.getDeviceId();
        }

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VisitaEvaluacion.this, VisitaSemanal.class);
                intent.putExtra("eventId", eventID);
                intent.putExtra("groupName", strNombreGrupo);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EvaluaEvent evalua = new EvaluaEvent();
                LinearLayout datos;
                TableLayout tablaEvaluacion;
                TableRow renglon;
                String urlCiclo = Globales.STR_VACIO;
                TextView cliente = new TextView(VisitaEvaluacion.this);
                CheckBox noasistio = new CheckBox(VisitaEvaluacion.this);
                CheckBox nopago = new CheckBox(VisitaEvaluacion.this);
                CheckBox noahorro = new CheckBox(VisitaEvaluacion.this);
                CheckBox nosolidario = new CheckBox(VisitaEvaluacion.this);
                //String strCliente, strNoAsistio, strNoPago, strNoAhorro, strNoSoli = Globales.STR_VACIO;
                datos = (LinearLayout)findViewById(R.id.linear);

                SharedPreferences shared = getSharedPreferences("userInfo", MODE_PRIVATE);
                //procesaVisita(URLV);
                listaWS = new ArrayList();
                bean = new EvaluacionBean();
                bean.setUrlWS(URLV);
                listaWS.add(bean);

                for (int i = 0; i < datos.getChildCount(); i++) {
                    tablaEvaluacion = (TableLayout)datos.getChildAt(i);
                    for (int j = 0; j < tablaEvaluacion.getChildCount(); j++) {
                        renglon = (TableRow)tablaEvaluacion.getChildAt(j);
                        for (int k = 0; k < renglon.getChildCount(); k++) {

                            if(k == 0){			// Numero de Cliente
                                cliente = (TextView)renglon.getChildAt(k);
                            }else if(k == 1){ 	// Primer CheckBox
                                noasistio = (CheckBox)renglon.getChildAt(k);
                            }else if(k == 2){	// Segundo CheckBox
                                nopago = (CheckBox)renglon.getChildAt(k);
                            }else if(k == 3){ 	// Tercer CheckBox
                                noahorro = (CheckBox)renglon.getChildAt(k);
                            }else if(k == 4){ 	// Cuarto CheckBox
                                nosolidario = (CheckBox)renglon.getChildAt(k);
                            }

                            if(cliente != null){
                                //evalua.setCliente(cliente.getText().toString());
                                evalua.setCliente(String.valueOf(j+1));
                                strCliente = evalua.getCliente();
                                //System.out.println(cliente.getText());
                            }

                            if(noasistio.isChecked()){
                                evalua.setNoAsistio(1);
                                strNoAsistio = "1";
                                //System.out.println("Esta Check noasistio");
                            }else{
                                evalua.setNoAsistio(0);
                                strNoAsistio = "0";
                            }

                            if(nopago.isChecked()){
                                evalua.setNoPago(1);
                                strNoPago = "1";
                                //System.out.println("Esta Check nopago");
                            }else{
                                evalua.setNoPago(0);
                                strNoPago = "0";
                                //System.out.println("NO Esta Check nopago");
                            }
                            if(noahorro.isChecked()){
                                evalua.setNoAhorro(1);
                                strNoAhorro = "1";
                                //System.out.println("Esta Check noahorro");
                            }else{
                                evalua.setNoAhorro(0);
                                strNoAhorro = "0";
                                //System.out.println("NO Esta Check noahorro");
                            }
                            if(nosolidario.isChecked()){
                                evalua.setNoSolidario(1);
                                strNoSoli = "1";
                                //System.out.println("Esta Check nosolidario");
                            }else{
                                evalua.setNoSolidario(0);
                                strNoSoli = "1";
                                //System.out.println("NO Esta Check nosolidario");
                            }
                        }

                        urlCiclo = URLE;
                        urlCiclo += "phone=" + imeiNumber +
                                "&grupo=" + clsStrGrupo +
                                "&ciclo=" + clsStrCiclo +
                                "&cliente=" + strCliente +
                                "&semana=" + clsStrSemana +
                                "&noasistio=" + strNoAsistio +
                                "&noPago=" + strNoPago +
                                "&noahorro=" + strNoAhorro +
                                "&nosolida=" + strNoSoli +
                                "&status=A";

                        bean = new EvaluacionBean();
                        bean.setUrlWS(urlCiclo);
                        listaWS.add(bean);
                    }
                }
                procesa(URLV, imeiNumber, listaWS);
            }
        });
    }

    void procesaVisita(String URLV){
        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
        StringRequest MyStringRequest = new StringRequest(Request.Method.GET, URLV,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //This code is executed if the server responds, whether or not the response contains data.
                        //The String 'response' contains the server's response.
                        //clearFields();
                        Toast.makeText(getApplicationContext(), "Evento guardado correctamente " + response, Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(VisitaEvaluacion.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
                Log.d("Send Event info", "response error" + error.toString());
                Toast.makeText(getApplicationContext(), "Error de conexión, por favor vuelve a intentar: " + error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        MyRequestQueue.add(MyStringRequest);

    }

    void procesa(String URLV, String IMEI, List integrantesLst){
        EvaluacionBean beanURL;

        if (Utils.isNetworkAvailable(VisitaEvaluacion.this)) {
            for (int i = 0; i < integrantesLst.size()-1; i++) {
                beanURL = new EvaluacionBean();
                beanURL = (EvaluacionBean)integrantesLst.get(i);
                Log.d("WS Evaluacion", beanURL.getUrlWS());
                RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
                StringRequest MyStringRequest = new StringRequest(Request.Method.GET, beanURL.getUrlWS(),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //This code is executed if the server responds, whether or not the response contains data.
                                //The String 'response' contains the server's response.
                                //clearFields();

                            }
                        }, new Response.ErrorListener() {
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
        } else {
            //saveEventInfo();

            for (int i = 0; i < integrantesLst.size()-1; i++) {
                beanURL = new EvaluacionBean();
                beanURL = (EvaluacionBean)integrantesLst.get(i);

                Log.d("DB Evaluacion", beanURL.getUrlWS());

                MainActivity.event = new Event();
                MainActivity.event.setUrlWS(beanURL.getUrlWS());
                MainActivity.event.setStatus(0);
                MainActivity.event.insert();

            }
        }



        Intent intent = new Intent(VisitaEvaluacion.this, MainActivity.class);
        startActivity(intent);
        finish();


        /*int iteraciones = Integer.valueOf(integrantes.getText().toString());
        String cliente, noAsistio, noPago, noAhorro, noSoli = Globales.STR_VACIO;
        String id = group.getText().toString();
        String locStrCiclo =ciclo.getText().toString();
        String locStrSemana =semana.getText().toString();
        String locStrIntegrantes =integrantes.getText().toString();
        String urlCiclo = Globales.STR_VACIO;
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
            Log.d("WS Evaluacion", urlCiclo);
            MyRequestQueue = Volley.newRequestQueue(this);
            MyStringRequest = new StringRequest(Request.Method.GET, urlCiclo,
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
        }*/
    }

    private void doBack() {
        Intent intent = new Intent(VisitaEvaluacion.this, VisitaSemanal.class);
        intent.putExtra("eventId", eventID);
        intent.putExtra("groupName", strNombreGrupo);
        startActivity(intent);
        finish();
    }

    /*
     * Metodo dibujarTabla
     * Devuelve una TableLayout con borde
     * recibe:
     * 	tamano del borde - int
     *  numero de filas - int
     *  numero de columnas - int
     */
    public TableLayout dibujarTabla(int tamBorde, int numeroFilas, int numeroColumnas){
        TableLayout tabla = new TableLayout(this);
        tabla.setClickable(true);
        int contadorClientes = 1;

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int screenWidth = displaymetrics.widthPixels;
        //int screenHeight = displaymetrics.heightPixels;

        if(numeroFilas>0 && numeroColumnas>0){
            TableRow fila = new TableRow(this);

            int numeroCeldas = numeroFilas * numeroColumnas;

	    	/* Calculo el Ancho de la siguiente manera:
	    	 * El ancho de la pantalla los divido entre el no. de columnas y le resto
	    	 * el tama�o del borde que esta sumado al borde dividido entre el no. de columnas
	    	 * (a causa del borde derecho de la ultima columna)
	    	 */

            //int ancho=(getWindowManager().getDefaultDisplay().getWidth()/numeroColumnas)-(tamBorde+(tamBorde/(numeroColumnas)));
            int ancho=(screenWidth/numeroColumnas)-(tamBorde+(tamBorde/(numeroColumnas)));
            ancho--;

            int contadorColumnas=0;
            int contadorFilas=0;

            for (int i = 0; i <= numeroCeldas; i++) {
                //Si Ya ha dibujado la cantidad de columnas
                if(contadorColumnas==numeroColumnas){
                    tabla.addView(fila);
                    fila = new TableRow(this);
                    contadorColumnas=0;
                    contadorFilas++;

                }

                //Definimos los bordes de la tabla
                RelativeLayout borde = new RelativeLayout(this);
                //Dibuja los de arriba y la izquierda siempre
                borde.setPadding(tamBorde,tamBorde,0,0);
                //Pero
                //Si ya es la ultima columna de la fila...
                if(contadorColumnas==numeroColumnas-1){
                    //Dibuja los de arriba a la derecha e izquierda.
                    borde.setPadding(tamBorde, tamBorde, tamBorde, 0);
                }
                //Si Es la ultima fila
                if(contadorFilas==numeroFilas-1){
                    //Dibuja arriba, izquierda y abajo
                    borde.setPadding(tamBorde,tamBorde,0,tamBorde);
                    //Si ademas de ser la ultima fila es la ultima columna
                    if(contadorColumnas==(numeroColumnas)-1){
                        //Dibuja todos los lados
                        borde.setPadding(tamBorde,tamBorde,tamBorde,tamBorde);
                    }
                }
                //Color del borde.
                //borde.setBackgroundColor(Color.parseColor(colorBorde));

                // CONFIGURACION TEXTO
                TextView texto = new TextView(this);
                texto.setWidth(ancho);
                texto.setGravity(Gravity.CLIP_HORIZONTAL);
                texto.setPadding(2, 2, 2, 2);
                //borde.addView(texto);

                // CONFIGURACION RADIO BUTTON
                //final RadioButton radio = new RadioButton(this);
                CheckBox check = new CheckBox(this);
                check.setMaxWidth(100);

                if(i==0){
                    texto.setText("Cli:" + contadorClientes);
                    fila.addView(texto);
                    contadorClientes++;
                }else if(multiploCinco(i)){
                    texto.setText("Cli:" + contadorClientes);
                    fila.addView(texto);
                    contadorClientes++;
                }else{
                    //fila.addView(borde);
                    fila.addView(check);
                }
                contadorColumnas++;
            }
        }else{
            TextView error= new TextView(this);
            error.setText("Valores de columnas o filas deben ser mayor de 0");
            tabla.addView(error);
        }
        return tabla;
    }

    boolean multiploCinco(int numero){
        boolean valida = false;

        if((numero % 5) == 0){
            valida = true;
        }

        return valida;
    }

    @Override
    public void onClick(View v)
    {
    }

    @Override
    public void checkinFinished()
    {
    }

    @Override
    public void onCheckedChanged(View v) {
    }
}
