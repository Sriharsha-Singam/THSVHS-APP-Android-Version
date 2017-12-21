package creations.appmaking.thsvhsapp;

/**
 *Copyright © 2017 Sriharsha Singam. All rights reserved.
 **/

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class adminuserlayout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminuserlayout);
        ListView lv = (ListView) findViewById(R.id.adminlist);


        List<String> arrayList = new ArrayList<String>();
        arrayList.add("ANNOUNCEMENTS");
        arrayList.add("FEATURES");
        arrayList.add("POSSIBLE EXPLICIT CONTENT");
        arrayList.add("CREATE USER");


        //final String user1=getIntent().getExtras().get("email").toString();


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                arrayList );

        lv.setAdapter(arrayAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                String item = ((TextView) view).getText().toString();
                if (item == "ANNOUNCEMENTS") {
                    Intent i=new Intent(adminuserlayout.this,announceadmin.class);
                    startActivity(i);


                } else if (item == "FEATURES") {

                    Intent i=new Intent(adminuserlayout.this,featuresadmin.class);
                    startActivity(i);
                }
                else if (item == "POSSIBLE EXPLICIT CONTENT") {

                    Intent i=new Intent(adminuserlayout.this,featuresadmin.class);
                    startActivity(i);
                }
                else if (item == "CREATE USER") {

                    Intent i=new Intent(adminuserlayout.this,admincreate.class);
                    startActivity(i);
                }

            }
        });
    }

}
