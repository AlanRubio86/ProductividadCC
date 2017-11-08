package com.productividadcc;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.productividadcc.utilerias.Globales;
import com.productividadcc.utilerias.Utilerias;


import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.HashMap;
import java.util.Map;


public class Login extends AppCompatActivity implements LocationListener {
    LocationManager locationManager;
    String mprovider;
    double latitude, longitude;
    String idEmpleado;
    String pwdEmpleado;
    String strNombreEmpleado;
    String[] consultaArray;

    String URL = Globales.URL_CONSULTA_EMPLEADO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        mprovider = locationManager.getBestProvider(criteria, false);
        if (mprovider != null && !mprovider.equals("")) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Location location = locationManager.getLastKnownLocation(mprovider);
            locationManager.requestLocationUpdates(mprovider, 15000, 1, this);

            if (location != null)
                onLocationChanged(location);
            else
                Toast.makeText(getBaseContext(), "No se ha podido obtener tu localización, asegurate de tener activado el gps", Toast.LENGTH_SHORT).show();
        }

        final EditText numTxt = (EditText) findViewById(R.id.numTxt);
        final EditText passTxt = (EditText) findViewById(R.id.passTxt);

        idEmpleado = numTxt.getText().toString();
        pwdEmpleado = passTxt.getText().toString();

        Log.d("Login idEmpleado", idEmpleado);
        Log.d("Login pwdEmpleado", pwdEmpleado);

        findViewById(R.id.loginBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!numTxt.getText().toString().equals("") && !passTxt.getText().toString().equals("")) {
                    if(numTxt.getText().toString().equals(passTxt.getText().toString())){
                        SharedPreferences shared = getSharedPreferences("userInfo", MODE_PRIVATE);
                        SharedPreferences.Editor editor = shared.edit();
                        editor.putString("userNumber", numTxt.getText().toString());
                        editor.commit();

                        consultaInfo();
                    } else {
                        Toast.makeText(getApplicationContext(), "El Numero de Empleado y Password incorrectos", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Ingrese su número de empleado y contraseña", Toast.LENGTH_LONG).show();
                }
            }
        });


    }


    public void consultaInfo() {
        final EditText numTxt = (EditText) findViewById(R.id.numTxt);
        final String strNumeroEmpleado = numTxt.getText().toString();

        //final String tokenId = DigestUtils.sha256Hex(strNumeroEmpleado);

        String s = new String(Hex.encodeHex(DigestUtils.sha256(strNumeroEmpleado)));

        final SharedPreferences shared = getSharedPreferences("userInfo", MODE_PRIVATE);
        URL = Globales.URL_CONSULTA_EMPLEADO;
        URL +=  strNumeroEmpleado;// + "&TokenId="+tokenId;
        Log.d("WS Login:", URL);

        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
        StringRequest MyStringRequest = new StringRequest(Request.Method.DEPRECATED_GET_OR_POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (!response.equals("") && response != null) {
                            consultaArray = response.split("<br>");

                            for (int i=0; i<consultaArray.length; i++) {
                                String[] appointmentArray = consultaArray[i].split(", ");
                                strNombreEmpleado = appointmentArray[1];
                            }

                            Log.d("WS Login Emplea:", strNombreEmpleado);
                            Intent intent = new Intent(Login.this, MainActivity.class);
                            //intent.putExtra("nombreEmpleado",getIntent().getExtras().getString("eventId"));
                            intent.putExtra("nombreEmpleado", strNombreEmpleado);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Empleado Inactivo. Revisarlo con RH", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.

                // SI TIENE UN ERROR DE CONEXION, SE REVISA SI NO TIENE ACCESO A INTERNET, PARA DAR ACCESO OFF LINE
                @Override
                public void onErrorResponse(VolleyError error) {
                    // SI NO TIENE CONEXION A WIFI SE DA ACCESO OFFLINE
                    if(Utils.isNetworkAvailable(getApplicationContext()) == false){
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        //intent.putExtra("nombreEmpleado",getIntent().getExtras().getString("eventId"));
                        intent.putExtra("nombreEmpleado", "Bienvenido Modo Off-Line");
                        startActivity(intent);
                        finish();
                    } else{
                        //This code is executed if there is an error.
                        Toast.makeText(getApplicationContext(), "Error de conexión, por favor vuelve a intentar.", Toast.LENGTH_LONG).show();
                    }
                }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("fecha", strNumeroEmpleado);
                return params;
            }
        };

        MyRequestQueue.add(MyStringRequest);
    }


    @Override
    public void onLocationChanged(Location location) {
        longitude = location.getLongitude();
        latitude = location.getLatitude();
        SharedPreferences shared = getSharedPreferences("userInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.putString("longitude", String.valueOf(longitude));
        editor.putString("latitude", String.valueOf(latitude));
        editor.commit();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
