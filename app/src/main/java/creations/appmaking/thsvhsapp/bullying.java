package creations.appmaking.thsvhsapp;

/**
 *Copyright © 2017 Sriharsha Singam. All rights reserved.
 **/


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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

public class bullying extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bullying);
        ListView lv = (ListView) findViewById(R.id.listView);


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
                if(item=="Fern Edwards")
                {

                }
                else if(item=="April Trench")
                {

                }
                else if(item=="Jessica Compton")
                {

                }
                else if(item=="Michelle Tracy")
                {

                } else if(item=="Karla Warner")
                {

                }
                else if(item=="Antoine Burks")
                {

                }

                Toast.makeText(getBaseContext(), item, Toast.LENGTH_LONG).show();

            }
        });
    }
}
