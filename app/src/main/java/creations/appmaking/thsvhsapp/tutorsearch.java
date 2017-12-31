package creations.appmaking.thsvhsapp;


/**
 *Copyright © 2017 Sriharsha Singam. All rights reserved.
 **/

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

public class tutorsearch extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorsearch);

        final ListView lv = (ListView)findViewById(R.id.listViewTutorSearch);
        final GlobalVariables gs = (GlobalVariables) tutorsearch.this.getApplicationContext();
        Firebase.setAndroidContext(tutorsearch.this);

        final Firebase ref = new Firebase("//fir-learn-4991c.firebaseio.com/tutors");

        final ValueEventListener valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<String> arrayList = new ArrayList<String>();
                ArrayList<String> arrayList1 = new ArrayList<String>();

                ArrayAdapter<String> aa= new ArrayAdapter<String>(
                        tutorsearch.this,
                        android.R.layout.simple_list_item_1,
                        arrayList);


                for (DataSnapshot child: dataSnapshot.getChildren()) {

                    tutorregister an=child.getValue(tutorregister.class);
                    arrayList.add(child.getKey()+"\n  Courses Offered:  "+an.getCourse()+"\n  Qualification Courses:  "+an.getCoursetaken());




                }

                lv.setAdapter(aa);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };
        ref.addValueEventListener(valueEventListener);
    }
}
