package creations.appmaking.thsvhsapp;

/**
 *Copyright © 2017 Sriharsha Singam. All rights reserved.
 **/

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class admincreate extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    ProgressDialog mpd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admincreate);

        mAuth = FirebaseAuth.getInstance();
        mpd=new ProgressDialog(this);

        Firebase.setAndroidContext(this);
        final Firebase ref = new Firebase("//fir-learn-4991c.firebaseio.com");

        Button create = (Button)findViewById(R.id.createuser);
        final AutoCompleteTextView createusername = (AutoCompleteTextView)findViewById(R.id.createusername);
        final AutoCompleteTextView createemail = (AutoCompleteTextView)findViewById(R.id.createemail);
        final AutoCompleteTextView createpassword = (AutoCompleteTextView)findViewById(R.id.createpassword);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mpd.setMessage("Creating User...");

                mpd.setCancelable(false);
                mpd.show();
                mAuth.createUserWithEmailAndPassword(createemail.getText().toString(), createpassword.getText().toString())
                        .addOnCompleteListener(admincreate.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d("", "createUserWithEmail:onComplete:" + task.isSuccessful());

                                if (!task.isSuccessful()) {
                                    Toast.makeText(admincreate.this, "Create User Failed!",
                                            Toast.LENGTH_LONG).show();
                                    mpd.dismiss();
                                    return;
                                }

                                Users createUser = new Users("0", "0", createemail.getText().toString(), "");
                                ref.child("Users").child(createusername.getText().toString()).setValue(createUser);
                                createemail.setText("");
                                createpassword.setText("");
                                createusername.setText("");
                                Toast.makeText(admincreate.this, "Create User Success!",
                                        Toast.LENGTH_LONG).show();
                               mpd.dismiss();

                            }
                        });
            }
        });

    }
}
