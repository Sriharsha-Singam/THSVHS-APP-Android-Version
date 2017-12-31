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

public class tutoring extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutoring);
        ListView lv = (ListView) findViewById(R.id.listViewtutor);


        List<String> arrayList = new ArrayList<String>();
        arrayList.add("To Become A Tutor");
        arrayList.add("Need A Tutor");


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
                if(item.equals("To Become A Tutor")){
                    Intent i = new Intent(tutoring.this, tutoringcreate.class);
                    startActivity(i);
                }
                if(item.equals("Need A Tutor")){
                    Intent i = new Intent(tutoring.this, tutorsearch.class);
                    startActivity(i);
                }

                //Toast.makeText(getBaseContext(), item, Toast.LENGTH_LONG).show();

            }
        });
    }
}
