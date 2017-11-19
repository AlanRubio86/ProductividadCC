package com.productividadcc;

import com.productividadcc.database.Event;

import android.database.Cursor;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by icortesb on 11/10/16.
 */
public class SincAgenda extends AppCompatActivity {

    ProgressDialog progressDialog;
    ProgressBar mprogressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sinc_agenda_activity);

        // Find the toolbar view and set as ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // ...
        // Remove default title text
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Get access to the custom title view
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Sincronizar Agenda");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SincAgenda.this, Main_Activity.class);
                startActivity(intent);
                finish();
            }
        });

        //mprogressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressDialog = ProgressDialog.show(SincAgenda.this, "", "Sincronizando eventos...", true );

        //TextView test = (TextView) findViewById(R.id.element);
        SharedPreferences shared = getSharedPreferences("userInfo", MODE_PRIVATE);
        //int numEventos = shared.getInt("eventosOffline", 0);

        if (Utils.isNetworkAvailable(SincAgenda.this)) {

            Event event = new Event();
            Cursor cursor = event.load();

            int count = 0;
            int total = 0;
            if (cursor != null){
                cursor.moveToFirst();
                total = cursor.getCount();

                while (!cursor.isAfterLast()){
                    final Event ev = new Event();
                    ev.copy(cursor);

                    Log.d("Cursos DB:", ev.toString());

                    Log.d("WS Sincronizacion", ev.getUrlWS());
                    RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
                    StringRequest MyStringRequest = new StringRequest(Request.Method.GET, ev.getUrlWS(),
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.d("WS-Sync-response", response);
                                    if(response.trim().equals("ok"))
                                    {
                                        ev.delete();
                                    }
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
                    count++;
                    cursor.moveToNext();
                }
                cursor.close();
                Toast.makeText(getApplicationContext(), "Agenda Sincronizada. Se subieron un total de " +  total + " eventos.", Toast.LENGTH_LONG).show();
                progressDialog.cancel();

                Intent intent = new Intent(SincAgenda.this, Main_Activity.class);
                startActivity(intent);
                finish();
            }

        } else {
            progressDialog.cancel();
            Toast.makeText(getApplicationContext(), "Necesitas tener conexión a internet para poder Sincronizar tu agenda", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(SincAgenda.this, Main_Activity.class);
            startActivity(intent);
            finish();
        }
    }
}
