package creations.appmaking.thsvhsapp;

/**
 *Copyright © 2017 Sriharsha Singam. All rights reserved.
 **/

import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class tutoringcreate extends AppCompatActivity {

    Firebase ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutoringcreate);
        final GlobalVariables gs123456 = (GlobalVariables) tutoringcreate.this.getApplicationContext();
        final AutoCompleteTextView name = (AutoCompleteTextView)findViewById(R.id.nameTutor);
        final AutoCompleteTextView subjects = (AutoCompleteTextView)findViewById(R.id.subjectsTutor);
        final AutoCompleteTextView classes = (AutoCompleteTextView)findViewById(R.id.classesTutor);
        final AutoCompleteTextView password = (AutoCompleteTextView)findViewById(R.id.passwordTutor);
        Button tutor = (Button)findViewById(R.id.applyTutor);

        tutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String actualpassword =  gs123456.getEmailAddress();
                final String user = gs123456.getUsername();
                Log.e("Getting:","  email-"+actualpassword+" Username-"+user);
                Log.e("Tutoring Search: ","email-"+password.getText().toString()+" Username-"+name.getText().toString());
                if(actualpassword.equals(password.getText().toString()) && user.equals(name.getText().toString())){

                    Firebase.setAndroidContext(tutoringcreate.this);
                    ref = new Firebase("//fir-learn-4991c.firebaseio.com/tutor");

                    ValueEventListener valueEventListener=new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

//                            setTutor(dataSnapshot, subjects, classes, user, gs123456);
                            if(dataSnapshot.child(user).exists()){
                                final CharSequence[] items = {"Close"};
                                AlertDialog.Builder builder = new AlertDialog.Builder(tutoringcreate.this);
                                builder.setTitle("Oops!!! This User Already Exists In The Database Records!! Please Contact: harshasingam3@gmail.com, if you want to edit your previous response.");
                                builder.setIcon(R.drawable.redx);
                                builder.setItems(items, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int item) {
                                        if (items[item].equals("Close")) {
                                            dialog.dismiss();
                                            return;
                                        }
                                    }
                                });
                                builder.show();
                            }
                            else{

                                tutorregister register = new tutorregister(subjects.getText().toString(), classes.getText().toString());
                                ref.child(gs123456.getUsername()).setValue(register);
                                final CharSequence[] items = {"Close"};
                                AlertDialog.Builder builder = new AlertDialog.Builder(tutoringcreate.this);
                                builder.setTitle(gs123456.getUsername()+" congratulations, you were successfully able register to be a Tutor. You may get further emails in the future from a counselor regarding the information.");
//                                builder.setIcon(R.drawable.redx);
                                builder.setItems(items, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int item) {
                                        if (items[item].equals("Close")) {
                                            dialog.dismiss();
                                            return;
                                        }
                                    }
                                });
                                builder.show();
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    };
                    ref.addListenerForSingleValueEvent(valueEventListener);



                }else{
                    final CharSequence[] items = {"Close"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(tutoringcreate.this);
                    builder.setTitle("Oops!!! You have entered a wrong email address or name!!! Try Again");
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
            }
        });

    }
    public void setTutor(DataSnapshot dataSnapshot, AutoCompleteTextView subjects, AutoCompleteTextView classes, String user, GlobalVariables gs123456 ){

        if(dataSnapshot.child(user).exists()){
            final CharSequence[] items = {"Close"};
            AlertDialog.Builder builder = new AlertDialog.Builder(tutoringcreate.this);
            builder.setTitle("Oops!!! This User Already Exists In The Database Records!! Please Contact: harshasingam3@gmail.com, if you want to edit your previous response.");
            builder.setIcon(R.drawable.redx);
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (items[item].equals("Close")) {
                        dialog.dismiss();
                        return;
                    }
                }
            });
            builder.show();
        }
        else{

            tutorregister register = new tutorregister(subjects.getText().toString(), classes.getText().toString());
            ref.child(gs123456.getUsername()).setValue(register);
            final CharSequence[] items = {"Close"};
            AlertDialog.Builder builder = new AlertDialog.Builder(tutoringcreate.this);
            builder.setTitle(gs123456.getUsername()+" congratulations, you were successfully able register to be a Tutor. You may get further emails in the future from a counselor regarding the information.");
//                                builder.setIcon(R.drawable.redx);
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (items[item].equals("Close")) {
                        dialog.dismiss();
                        return;
                    }
                }
            });
            builder.show();
        }

    }
}
