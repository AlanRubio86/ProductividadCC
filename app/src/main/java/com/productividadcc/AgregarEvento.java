package com.productividadcc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.view.View;

public class AgregarEvento extends AppCompatActivity {

    String selectedOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agregar_evento_activity);

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AgregarEvento.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.siguienteBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadioGroup radioEventGroup = (RadioGroup) findViewById(R.id.radioEvent);
                // get selected radio button from radioGroup
                final int selectedId = radioEventGroup.getCheckedRadioButtonId();
                // find the radiobutton by returned id
                RadioButton radioEventButton = (RadioButton) findViewById(selectedId);
                selectedOption = (String) radioEventButton.getText();

                if (selectedOption.equals("Agendar Capacitación 1")) {
                    Intent intent = new Intent(AgregarEvento.this, AgendarCapacitacion.class);
                    startActivity(intent);
                    finish();
                } else if (selectedOption.equals("Agendar Capacitación 2")) {
                    Intent intent = new Intent(AgregarEvento.this, AgendarCapacitacion2.class);
                    startActivity(intent);
                    finish();
                } else if (selectedOption.equals("Agendar NewGroupVoBo")) {
                    Intent intent = new Intent(AgregarEvento.this, AgendarVoBo.class);
                    startActivity(intent);
                    finish();
                } else if (selectedOption.equals("Agendar NewGroupVoBo Renovación")) {
                    Intent intent = new Intent(AgregarEvento.this, AgendarVoBoRen.class);
                    startActivity(intent);
                    finish();
                } /*else if (selectedOption.equals("Capacitación 1")) {
                    Intent intent = new Intent(AgregarEvento.this, Capacitacion1.class);
                    startActivity(intent);
                    finish();
                } else if (selectedOption.equals("Capacitación 2")) {
                    Intent intent = new Intent(AgregarEvento.this, Capacitacion2.class);
                    startActivity(intent);
                    finish();
                }*/ else if (selectedOption.equals("NewGroupVoBo")) {
                    Intent intent = new Intent(AgregarEvento.this, NewGroupVoBo.class);
                    startActivity(intent);
                    finish();
                } else if (selectedOption.equals("NewGroupVoBo Renovación")) {
                    Intent intent = new Intent(AgregarEvento.this, VoBoRenovacion.class);
                    startActivity(intent);
                    finish();
                } else if (selectedOption.equals("Desembolso")) {
                    Intent intent = new Intent(AgregarEvento.this, Desembolso.class);
                    startActivity(intent);
                    finish();
                } else if (selectedOption.equals("Reprogramación de Agenda")) {
                    Intent intent = new Intent(AgregarEvento.this, Reprogramacion.class);
                    startActivity(intent);
                    finish();
                } else if (selectedOption.equals("Evaluación Semanal")) {
                    Intent intent = new Intent(AgregarEvento.this, VisitaSemanal.class);
                    startActivity(intent);
                    finish();
                } else if (selectedOption.equals("Verificación")) {
                    Intent intent = new Intent(AgregarEvento.this, Verificacion.class);
                    startActivity(intent);
                    finish();
                } else if (selectedOption.equals("Agendar Promoción")) {
                    Intent intent = new Intent(AgregarEvento.this, AgendarPromocion.class);
                    startActivity(intent);
                    finish();

                } else if (selectedOption.equals("Llamada de Gestion")) {
                    Intent intent = new Intent(AgregarEvento.this, LlamadaGestion.class);
                    startActivity(intent);
                    finish();

                } else if (selectedOption.equals("Agendar Cobranza Temprana")) {
                    Intent intent = new Intent(AgregarEvento.this, AgendarCobranza.class);
                    startActivity(intent);
                    finish();
                } else if (selectedOption.equals("Promoción")) {
                    Intent intent = new Intent(AgregarEvento.this, GrupoNuevo.class);
                    startActivity(intent);
                    finish();

                }
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
