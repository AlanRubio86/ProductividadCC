package com.productividadcc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.widget.EditText;
import android.widget.TextView;

import com.productividadcc.database.DatabaseHelper;
import com.productividadcc.database.Event;

public class MainActivity extends AppCompatActivity {

    private String strNombreEmpleado;
    private TextView nombreEmpleado;

    public static SQLiteDatabase db;
    public static Event event;

    public static final int GROUP_DIGITS = 4;
    public static final int CICLO_DIGITS = 1;
    public static final int SEMANA_DIGITS = 1;
    public static final int SEMANA_MAXIMO = 16;
    public static final int INTEGRANTES_DIGITS = 2;
    public static final int INTEGRANTES_MINIMO = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseHelper dbh = new DatabaseHelper(this);
        db = dbh.getWritableDatabase();

        nombreEmpleado = (TextView) findViewById(R.id.nombreEmpleado);

        if (getIntent().getExtras() != null) {
            if(!getIntent().getExtras().getString("nombreEmpleado").equalsIgnoreCase("")){
                strNombreEmpleado = getIntent().getExtras().getString("nombreEmpleado");
                nombreEmpleado.setText(strNombreEmpleado);
            }
        }

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);

        findViewById(R.id.agendaBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AgendaActivity.class);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.agregarEventoBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AgregarEvento.class);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.sincronizarBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SincAgenda.class);
                startActivity(intent);
                finish();
            }
        });

        /*findViewById(R.id.bitacoraBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Bitacora.class);
                startActivity(intent);
                finish();
            }
        });*/
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
