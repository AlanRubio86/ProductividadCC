package com.productividadcc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;


public class FinishOldGroup_Activity extends AppCompatActivity {
    String integrants="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        super.onCreate(savedInstanceState);
        setContentView(R.layout.finisholdgroup_activity);


        if (getIntent().getExtras() != null) {
            integrants = getIntent().getExtras().getString("integrants");
        } else {
            integrants = "0";
        }

        TextView lblClients= (TextView) findViewById(R.id.lblClients) ;
        lblClients.setText("Has renovado "+integrants+" clientes de tu cartera...");

        findViewById(R.id.btnNewGroups).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FinishOldGroup_Activity.this, OldGroupsList_Activity.class);
                startActivity(intent);
                finish();
            }
        });

    }


}
