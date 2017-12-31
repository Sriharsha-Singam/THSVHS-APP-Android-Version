package creations.appmaking.thsvhsapp;

/**
 *Copyright © 2017 Sriharsha Singam. All rights reserved.
 **/

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class announce extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announce);

        final String user = getIntent().getExtras().get("UserName").toString();

        //ListView lv = (ListView) findViewById(R.id.listView3);


        /*List<String> arrayList = new ArrayList<String>();
        arrayList.add("SPORTS");
        arrayList.add("EDUCATION");
        //arrayList.add("");

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

                //Toast.makeText(getBaseContext(), item, Toast.LENGTH_LONG).show();

                if(item=="SPORTS")
                {

                }
                else if(item=="EDUCATION")
                {

                }

            }
        });*/
        final ListView lv = (ListView)findViewById(R.id.listView3);
        Firebase.setAndroidContext(this);
        lv.setStackFromBottom(true);
        final Firebase ref = new Firebase("//fir-learn-4991c.firebaseio.com/announce");


        ValueEventListener valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<String> arrayList = new ArrayList<String>();

                ArrayAdapter<String> aa= new ArrayAdapter<String>(
                        announce.this,
                        android.R.layout.simple_list_item_1,
                        arrayList);


                for (DataSnapshot child: dataSnapshot.getChildren()) {

                    Announcements an=child.getValue(Announcements.class);
                    arrayList.add(an.getAnnounce());



                }

                lv.setAdapter(aa);
                final Firebase ref = new Firebase("//fir-learn-4991c.firebaseio.com/Users/"+user+"/announce");
                ref.setValue(String.valueOf(arrayList.size()));

                //}

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };
        ref.addValueEventListener(valueEventListener);
    }
}
