package creations.appmaking.thsvhsapp;

/**
 *Copyright © 2017 Sriharsha Singam. All rights reserved.
 **/


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class settings extends AppCompatActivity {

    ProgressDialog uploading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ListView lv = (ListView) findViewById(R.id.listView2);
        uploading = new ProgressDialog(this);
        Firebase.setAndroidContext(this);
        final Firebase ref=new Firebase("//fir-learn-4991c.firebaseio.com");

        List<String> arrayList = new ArrayList<String>();
        arrayList.add("Change Password");
        arrayList.add("Profile Picture");
        arrayList.add("Questions");
        arrayList.add("Have Ideas?");


        final String user1=getIntent().getExtras().get("email").toString();


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
                if(item=="Change Password")
                {
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    String emailAddress = user1;

                    auth.sendPasswordResetEmail(emailAddress)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("", "Email sent.");
                                        Toast.makeText(getBaseContext(), "An email has been sent to your Email. Go to your email and click on the link which will allow you to change your password.", Toast.LENGTH_LONG).show();
                                    }
                                    else{
                                        Toast.makeText(getBaseContext(), "Sorry, please try again later.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
                else if(item=="Questions")
                {
                    questions();
                }
                else if(item=="Profile Picture")
                {
                    profilePicture();
                }
                else if (item=="Have Ideas?") {
                    ideas() ;
                };



            }
        });
    }
    public void ideas()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(settings.this);
        builder.setTitle("Ideas:");
        builder
                .setMessage("Send your ideas to: codingths@gmail.com");
        builder .setCancelable(true);
        AlertDialog alertDialog = builder.create();

        // show it
        alertDialog.show();


    }
    public void profilePicture(){
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i, "Select image"), 1);
    }
    public void questions()
    {
        final CharSequence[] items = {"Close"};
        AlertDialog.Builder builder = new AlertDialog.Builder(settings.this);
        builder.setTitle("EMAIL US AT: codingths@gmail.com");
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final String[] message = new String[1];
        Button sendBtn = (Button) findViewById(R.id.btnSend);
        byte[] dat = null;
        GlobalVariables gs = (GlobalVariables) settings.this.getApplicationContext();
        Firebase.setAndroidContext(this);
        final Firebase ref = new Firebase("//fir-learn-4991c.firebaseio.com/Users/"+gs.getUsername()+"/profileImage");
        final Uri[] downloadUrl = new Uri[1];
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {


            FirebaseStorage storage = FirebaseStorage.getInstance();
            final Uri[] uri = {data.getData()};
            final StorageReference[] imagesRef = {storage.getReferenceFromUrl("gs://fir-learn-4991c.appspot.com/images").child(uri[0].getLastPathSegment())};


            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            dat = baos.toByteArray();
            final byte[][] dat1 = {dat};
            //final EditText txt = (EditText) findViewById(R.id.txt);
            //txt.setHint("Image Has Been Selected, Enter Any Comments Or Click Send");
            final byte[] finalDat = dat;


            sendPhotoToProfile(ref,imagesRef, finalDat,uri[0]);

        }

    }
    public void sendPhotoToProfile( final Firebase ref, StorageReference[] imagesRef, byte[] dat, final Uri uri){
        uploading.setMessage("Uploading...");
        uploading.show();
        uploading.setCancelable(false);
        final String[] message = new String[1];
        final Uri[] downloadUrl = new Uri[1];
        final byte[][] dat1 = {dat};

            UploadTask uploadTask = imagesRef[0].putBytes(dat1[0]);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    downloadUrl[0] = taskSnapshot.getDownloadUrl();

                    ref.setValue(uri.getLastPathSegment());

                    uploading.dismiss();
                }
            });


    }
}