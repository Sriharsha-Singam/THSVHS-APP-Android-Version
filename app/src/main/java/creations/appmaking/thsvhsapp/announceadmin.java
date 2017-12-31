package creations.appmaking.thsvhsapp;

/**
 *Copyright © 2017 Sriharsha Singam. All rights reserved.
 **/

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class announceadmin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announceadmin);
        final String[] message = new String[1];
        final EditText txtannounce = (EditText) findViewById(R.id.txtannounce);
        Button sendBtn = (Button) findViewById(R.id.btnSendannounce );

        final ListView lvannounce = (ListView)findViewById(R.id.listannounce);
        Firebase.setAndroidContext(this);
        final Firebase ref = new Firebase("https://fir-learn-4991c.firebaseio.com/announce");


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                message[0] = txtannounce.getText().toString();
                int swearwords=filter(message[0]);
                if (message[0] != "" || message[0] != null) {
                    Log.e("hello",""+swearwords);
                    if(swearwords==3)
                    {
                        String x1 = "";
                        Long datenowe=new Date().getTime();
                        String announce=message[0];
                        Announcements chat = new Announcements(announce);

                        ref.push().setValue(chat);
                        txtannounce.setText("");

                    }
                    else if(swearwords==1)
                    {
                        filtered();
                    }
                    else if(swearwords==2)
                    {
                        spacing();
                    }

                }
                else
                {
                    Toast.makeText(announceadmin.this, "Please Type In Something",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        ValueEventListener valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<String> arrayList = new ArrayList<String>();

                ArrayAdapter<String> aa= new ArrayAdapter<String>(
                        announceadmin.this,
                        android.R.layout.simple_list_item_1,
                        arrayList);


                for (DataSnapshot child: dataSnapshot.getChildren()) {

                    Announcements announce=child.getValue(Announcements.class);
                    arrayList.add(announce.getAnnounce());



                }

                lvannounce.setAdapter(aa);

                //}

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };
        ref.addValueEventListener(valueEventListener);
    }
    public void filtered()
    {
        final CharSequence[] items = {"Close"};
        AlertDialog.Builder builder = new AlertDialog.Builder(announceadmin.this);
        builder.setTitle("NO EXPLICIT WORDS OR CONTENT, PLEASE!");
        builder.setIcon(R.drawable.redx);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Close")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    public void spacing()
    {
        final CharSequence[] items = {"Close"};
        AlertDialog.Builder builder = new AlertDialog.Builder(announceadmin.this);
        builder.setTitle("PLEASE ENTER SOMETHING!");
        //builder.setIcon(R.drawable.redx);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Close")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    public int filter(String message)
    {
        Log.e("hello","");

        String[] words={"fuck","shit","nigga","nigger","damn","hell","ass","asshole","ahole","cock","faggot","fag","motherfucker","queer","cunt","negro","piss","pussy","penis","shit","twat","whore","anus","arse","axwound","son of a","crap","bitch","bastard","bloody","butt","bloody","bollock","boob","vagina","sex"};
        String[] space={""};
        //List<String> stringList = new ArrayList<String>(Arrays.asList(words));
        Set<String> set = new HashSet<String>(Arrays.asList(words));
        Set<String> spaced = new HashSet<String>(Arrays.asList(space));
        String array[] = message.split(" ");


        for(String s : array){
            if(set.contains(s.toLowerCase()))
                return 1;
        }
        for(String s : array){
            if(spaced.contains(s.toLowerCase()))
                return 2;
        }
        return 3;
    }
}
