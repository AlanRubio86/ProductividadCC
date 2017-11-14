package com.productividadcc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

//import com.testfairy.TestFairy;


public class finisholdgroup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        super.onCreate(savedInstanceState);
        setContentView(R.layout.finisholdgroup);
        //TestFairy.begin(this, "2c3bf8be0a88b65175d831f0ffc3007b50bdf8ee");
        findViewById(R.id.btnNewGroups).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(finisholdgroup.this, OldGroupsListActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }


}
