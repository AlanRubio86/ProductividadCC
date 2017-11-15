package com.productividadcc;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

//import com.testfairy.TestFairy;


public class finishnewgroup  extends AppCompatActivity {
    String integrants="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        super.onCreate(savedInstanceState);
        setContentView(R.layout.finishnewgroup);

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
                Intent intent = new Intent(finishnewgroup.this, NewGroupsListActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }


}
