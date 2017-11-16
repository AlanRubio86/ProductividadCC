package com.productividadcc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

//import com.testfairy.TestFairy;


public class FinishNewGroup_Activity extends AppCompatActivity {
    String integrants="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        super.onCreate(savedInstanceState);
        setContentView(R.layout.finishnewgroup_activity);

        if (getIntent().getExtras() != null) {
            integrants = getIntent().getExtras().getString("integrants");
        } else {
            integrants = "0";
        }

        TextView lblClients= (TextView) findViewById(R.id.lblClients) ;
        lblClients.setText("Has agregado "+integrants+" clientes más a tu cartera...");

        findViewById(R.id.btnNewGroups).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FinishNewGroup_Activity.this, NewGroupsList_Activity.class);
                startActivity(intent);
                finish();
            }
        });

    }


}
