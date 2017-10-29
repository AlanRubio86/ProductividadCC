package com.productividadcc;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class NewGroupMapActivity extends AppCompatActivity implements LocationListener {
    LocationManager locationManager;
    String mprovider;
    double latitude, longitude;
    TextView titleTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newgroupmap_activity);

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);

        titleTxt = (TextView) findViewById(R.id.titleTxt);

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

        MyCountDownTimer myCountDownTimer = new MyCountDownTimer(3000, 1000);
        myCountDownTimer.start();

        findViewById(R.id.continuarBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String eventData = getIntent().getExtras().getString("data");
                String eventID = eventData.replace(" ", "");
                /*Log.d("AgendaLoader, data", eventData);
                Log.d("AgendaLoader, eventID", eventID);
                Log.d("AgendaLoader, eventID", getIntent().getExtras().getString("eventId"));*/

                int tipoEvento = Integer.parseInt(eventID);

                Intent main = new Intent();

                switch (tipoEvento) {
                    case 1:
                        main = new Intent(NewGroupMapActivity.this, AgendarCapacitacion.class);
                        break;
                    case 2:
                        main = new Intent(NewGroupMapActivity.this, AgendarVoBo.class);
                        break;
                    case 3:
                        main = new Intent(NewGroupMapActivity.this, AgendarCobranza.class);
                        break;
                    case 4:
                        main = new Intent(NewGroupMapActivity.this, Capacitacion1.class);
                        break;
                    case 5:
                        main = new Intent(NewGroupMapActivity.this, Capacitacion2.class);
                        break;
                    case 6:
                        main = new Intent(NewGroupMapActivity.this, NewGroupVoBo.class);
                        break;
                    case 7:
                        main = new Intent(NewGroupMapActivity.this, VoBoRenovacion.class);
                        break;
                    case 8:
                        main = new Intent(NewGroupMapActivity.this, Desembolso.class);
                        break;
                    case 9:
                        main = new Intent(NewGroupMapActivity.this, VisitaSemanal.class);
                        break;
                    case 10:
                        main = new Intent(NewGroupMapActivity.this, Verificacion.class);
                        break;
                    case 11:
                        main = new Intent(NewGroupMapActivity.this, GrupoNuevo.class);
                        break;
                    case 12:
                        main = new Intent(NewGroupMapActivity.this, LlamadaGestion.class);
                        break;
                }
                main.putExtra("groupName",getIntent().getExtras().getString("groupName"));
                main.putExtra("eventId",getIntent().getExtras().getString("eventId"));
                startActivity(main);
                finish();
            }
        });
    }

    public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            int progress = (int) (millisUntilFinished/1000);
            if (progress == 1) {
                titleTxt.setText("Esperando por Localización GPS...");
            } else if (progress == 2) {
                titleTxt.setText("Esperando por Localización GPS..");
            }
        }

        @Override
        public void onFinish() {
            showLocationLabel();
            /*Button locationBtn = (Button) findViewById(R.id.locationBtn);
            locationBtn.setVisibility(View.VISIBLE);
            locationBtn.setOnClickListener(new  View.OnClickListener() {

                @Override
                public void onClick(View view) {

                }
            });*/
        }
    }

    public void showLocationLabel () {
        if (latitude == 0 && longitude == 0) {
            AlertDialog.Builder locationAlert = new AlertDialog.Builder(NewGroupMapActivity.this);
            locationAlert.setTitle("No detectamos tu localización, por favor activa el gps y vuelve a intentarlo");
            locationAlert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {

                }
            });
            locationAlert.show();
        } else {
            titleTxt.setText("Localización: "+ String.valueOf(latitude)+","+String.valueOf(longitude));
            Button continuarBtn = (Button) findViewById(R.id.continuarBtn);
            continuarBtn.setVisibility(View.VISIBLE);
        }
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
        showLocationLabel();
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
