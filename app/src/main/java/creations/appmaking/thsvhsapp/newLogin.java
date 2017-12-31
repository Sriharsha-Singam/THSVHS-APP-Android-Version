package creations.appmaking.thsvhsapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Date;

/**
 *Copyright © 2017 Sriharsha Singam. All rights reserved.
 **/

public class newLogin extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText username;
    private EditText password;
    ProgressDialog mpd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseMessaging.getInstance().subscribeToTopic("texting");

        username=(EditText)findViewById(R.id.username);
        password=(EditText)findViewById(R.id.password);
        mpd=new ProgressDialog(this);

        Firebase.setAndroidContext(this);
        //final Firebase ref=new Firebase("//fir-learn-4991c.firebaseio.com/Users");
        final Button login=(Button)findViewById(R.id.loginbtn);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() != null){
            mpd.setMessage("Logging In...");

            mpd.setCancelable(false);
            mpd.show();
            final Firebase ref=new Firebase("//fir-learn-4991c.firebaseio.com/Users");
            final String email = auth.getCurrentUser().getEmail();
            System.out.println("Email: "+email);
            final ArrayList<String> announces = new ArrayList<String>();
            final ArrayList<String> socials = new ArrayList<String>();
            final ArrayList<String> emails = new ArrayList<String>();
            final ArrayList<String> names = new ArrayList<String>();
            final ArrayList<String> profileImages = new ArrayList<String>();

            ValueEventListener valueEventListener=new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot child: dataSnapshot.getChildren()) {
                        System.out.println(child);
                        Users usersing=child.getValue(Users.class);
                        names.add(child.getKey());
                        announces.add(usersing.getAnnounce());
                        socials.add(usersing.getSocial());
                        emails.add(usersing.getEmail());
                        profileImages.add(usersing.getProfileImage());
                    }
                    int count = 0;
                    int i = -1;
                    for(String emailsing: emails){
                        i = i + 1;
                        System.out.println(i+" = "+emailsing);
                        if(emailsing.equals(email)){
                            System.out.println("Actual Email = "+email+"Gotten Email"+emailsing);
                            count = i;
                        }
                    }
                    Log.e("Emails:",email);
                    Log.e("Counts:",""+count);
                    Log.e("Name:",names.get(count));
                    for(String s: names){
                        Log.e("Names", s);
                    }
                    for(String s: announces){
                        Log.e("Annouce", s);
                    }

                    newActivity(email, names.get(count), profileImages.get(count));

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            };
            ref.addListenerForSingleValueEvent(valueEventListener);

//            String social = socials.get(count);
//            String
        }

        final String[] email = new String[1];
        email[0]="";


        assert login != null;
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mpd.setMessage("Logging In...");
                mpd.setCancelable(false);
                mpd.show();
                final String user=username.getText().toString();
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference dref=database.getReference("Users/"+user);

                Firebase ref=new Firebase("//fir-learn-4991c.firebaseio.com/Users/");
                //Firebase ref1=ref.child("email");
                final String pass=password.getText().toString();

                ref.addValueEventListener(new com.firebase.client.ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(user).exists()){

                            Firebase ref1= new Firebase("//fir-learn-4991c.firebaseio.com/Users/"+user+"/email");;
                            final String pass=password.getText().toString();

                            ref1.addValueEventListener(new com.firebase.client.ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String post=dataSnapshot.getValue(String.class);
                                    Log.v("Email#1",post);
                                    authentication(post,pass,user);

                                }

                                @Override
                                public void onCancelled(FirebaseError firebaseError) {

                                }
                            });
                        }
                        else{
                            x();
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

                Log.e("Email",""+ email[0]);
                Log.e("Ref",""+ ref);

            }

        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.accesscode, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.action_settings) {

            checkAccessCode();


            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void checkAccessCode(){

    }
    public void newActivity(String email, String user, String profileImage)
    {

//        ((GlobalVariables) this.getApplication()).setUsername(user);
//        ((GlobalVariables) this.getApplication()).setEmailAddress(email);
        GlobalVariables gs = (GlobalVariables) newLogin.this.getApplicationContext();
        gs.setUsername(user);
        gs.setEmailAddress(email);
        gs.setProfileImage(profileImage);
        Long time=new Date().getTime();
        String date = dateMaker(time);

        ActiveNameUpdate update = new ActiveNameUpdate(user, email);
        Firebase.setAndroidContext(this);
        Firebase updateRef =new Firebase("//fir-learn-4991c.firebaseio.com/ActiveLoginTimes");
        updateRef.push().child(date).setValue(update);

        Log.d("Email=",""+gs.getEmailAddress()+",  Username="+gs.getUsername());
        Intent intent = new Intent(newLogin.this, THSTheApp.class);
        intent.putExtra("email",email);
        intent.putExtra("UserName", user);
        //intent.putExtra("Password", pass);
        mpd.dismiss();
        startActivity(intent);
    }
    public String dateMaker(Long date){
        String datehi="Date: ";
        CharSequence month= DateFormat.format("MM",date);
        String monthis="";
        int monthi=Integer.parseInt((String) month);
        if(monthi==1)
        {
            monthis="January";
        }
        else if(monthi==2)
        {
            monthis="February";
        }
        else if(monthi==3)
        {
            monthis="March";
        }
        else if(monthi==4)
        {
            monthis="April";
        }
        else if(monthi==5)
        {
            monthis="May";
        }
        else if(monthi==6)
        {
            monthis="June";
        }
        else if(monthi==7)
        {
            monthis="July";
        }
        else if(monthi==8)
        {
            monthis="August";
        }
        else if(monthi==9)
        {
            monthis="September";
        }
        else if(monthi==10)
        {
            monthis="October";
        }
        else if(monthi==11)
        {
            monthis="November";
        }
        else if(monthi==12)
        {
            monthis="December";
        }
        String datehi1= (String) DateFormat.format("dd",date);
        String year= (String) DateFormat.format("yyyy",date);
        String time="  ||  Time = ";
        String time1= (String) DateFormat.format(" hh:mm:ss a ",date);
        String everything= monthis+" "+datehi1+", "+time1;

        return everything;
    }

    public void x()
    {
        Toast.makeText(newLogin.this, "There Was A Problem",
                Toast.LENGTH_SHORT).show();
        password.setText("");

    }
    public void authentication(final String email1, final String pass,final String user)
    {
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email1, pass).addOnCompleteListener(newLogin.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    final String[] profileImage = {""};
                    Firebase ref=new Firebase("//fir-learn-4991c.firebaseio.com/Users/"+user);

                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //for (DataSnapshot child: dataSnapshot.getChildren()) {
                            System.out.println(dataSnapshot);
                            Users usersing=dataSnapshot.getValue(Users.class);

                            profileImage[0] = usersing.getProfileImage();

                            //}
                            newActivity(email1,user, profileImage[0]);
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });

                }
                else if (!task.isSuccessful()) {
                    mpd.dismiss();
                    x();
                }
            }
        });
    }

}
