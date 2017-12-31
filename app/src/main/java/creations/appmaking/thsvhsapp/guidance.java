package creations.appmaking.thsvhsapp;


/**
 *Copyright © 2017 Sriharsha Singam. All rights reserved.
 **/


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class guidance extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guidance);
        ListView lv = (ListView) findViewById(R.id.listView);

        final GlobalVariables gs = (GlobalVariables) guidance.this.getApplicationContext();
        List<String> arrayList = new ArrayList<String>();
        arrayList.add("Fern Edwards");
        arrayList.add("April Trench");
        arrayList.add("Jessica Compton");
        arrayList.add("Michelle Tracy");
        arrayList.add("Karla Warner");
        arrayList.add("Antoine Burks");


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                arrayList );

        lv.setAdapter(arrayAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                String item = ((TextView)view).getText().toString();
                if(item.equals("Fern Edwards")){
                    Intent email = new Intent(Intent.ACTION_SEND);
                    email.putExtra(Intent.EXTRA_EMAIL, new String[]{"mfc2@vigoschools.org"});
                    email.putExtra(Intent.EXTRA_SUBJECT, "Sent Via The THSVHS APP from: "+gs.getUsername());
                    email.setType("message/rfc822");
                    startActivity(Intent.createChooser(email, "Choose an Email client :"));
                }
                if(item.equals("April Trench")){
                    Intent email = new Intent(Intent.ACTION_SEND);
                    email.putExtra(Intent.EXTRA_EMAIL, new String[]{"adt@vigoschools.org"});
                    email.putExtra(Intent.EXTRA_SUBJECT, "Sent Via The THSVHS APP from: "+gs.getUsername());
                    email.setType("message/rfc822");
                    startActivity(Intent.createChooser(email, "Choose an Email client :"));
                }
                if(item.equals("Jessica Compton")){
                    Intent email = new Intent(Intent.ACTION_SEND);
                    email.putExtra(Intent.EXTRA_EMAIL, new String[]{"jlc5@vigoschools.org"});
                    email.putExtra(Intent.EXTRA_SUBJECT, "Sent Via The THSVHS APP from: "+gs.getUsername());
                    email.setType("message/rfc822");
                    startActivity(Intent.createChooser(email, "Choose an Email client :"));
                }
                if(item.equals("Michelle Tracy")){
                    Intent email = new Intent(Intent.ACTION_SEND);
                    email.putExtra(Intent.EXTRA_EMAIL, new String[]{"mlt@vigoschools.org"});
                    email.putExtra(Intent.EXTRA_SUBJECT, "Sent Via The THSVHS APP from: "+gs.getUsername());
                    email.setType("message/rfc822");
                    startActivity(Intent.createChooser(email, "Choose an Email client :"));
                }
                if(item.equals("Karla Warner")){
                    Intent email = new Intent(Intent.ACTION_SEND);
                    email.putExtra(Intent.EXTRA_EMAIL, new String[]{"kkw@vigoschools.org"});
                    email.putExtra(Intent.EXTRA_SUBJECT, "Sent Via The THSVHS APP from: "+gs.getUsername());
                    email.setType("message/rfc822");
                    startActivity(Intent.createChooser(email, "Choose an Email client :"));
                }
                if(item.equals("Antoine Burks")){
                    Intent email = new Intent(Intent.ACTION_SEND);
                    email.putExtra(Intent.EXTRA_EMAIL, new String[]{"aburks19@vivytech.edu"});
                    email.putExtra(Intent.EXTRA_SUBJECT, "Sent Via The THSVHS APP from: "+gs.getUsername());
                    email.setType("message/rfc822");
                    startActivity(Intent.createChooser(email, "Choose an Email client :"));
                }

//                Toast.makeText(getBaseContext(), item, Toast.LENGTH_LONG).show();

            }
        });
    }

}
