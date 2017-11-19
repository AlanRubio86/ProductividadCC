package com.productividadcc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.widget.TextView;

import com.productividadcc.database.DatabaseHelper;
import com.productividadcc.database.Event;
import com.productividadcc.database.NewGroups;
import com.productividadcc.database.OldGroups;

public class Main_Activity extends AppCompatActivity {

    private String strNombreEmpleado;
    private TextView nombreEmpleado;

    public static SQLiteDatabase db;
    public static Event event;
    public static NewGroups newGroups;
    public static OldGroups oldGroups;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        DatabaseHelper dbh = new DatabaseHelper(this);
        db = dbh.getWritableDatabase();
        dbh.onCreate(db);

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
                Intent intent = new Intent(Main_Activity.this, NewGroupsList_Activity.class);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.agregarEventoBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main_Activity.this, OldGroupsList_Activity.class);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.sincronizarBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main_Activity.this, SincAgenda.class);
                startActivity(intent);
                finish();
            }
        });


    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
