package creations.appmaking.thsvhsapp;

/**
*Copyright © 2017 Sriharsha Singam. All rights reserved.
 **/

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.FileProvider;
import android.os.Bundle;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static android.R.id.progress;


public class socialpage2 extends ListActivity {

    String user;
    String email;
    VideoView vid;
    Context contextis;
    Uri outputFileUri;
    String mCurrentPhotoPath;
    ProgressDialog uploading;
    Boolean isAdmin;
    Boolean isEvent;
    private static final int CAMERA_REQUEST_CODE = 1;
    //boolean photo;

    Map<String,ChatMessage> map=new HashMap<String, ChatMessage>();

    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_socialpage2);
        final GlobalVariables gs12 = (GlobalVariables) socialpage2.this.getApplicationContext();
//        getActionBar().setHomeButtonEnabled(true);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        //setSupportActionBar(myToolbar);
        isAdmin = gs12.getAdminUser();
        isEvent = gs12.getEventUser();
        user = getIntent().getExtras().get("UserName").toString();



        email=getIntent().getExtras().get("email").toString();
        contextis=socialpage2.this;
        uploading = new ProgressDialog(this);
        //final String[] message = new String[1];
        Button sendBtn = (Button) findViewById(R.id.btnSend);
        final Button photo = (Button) findViewById(R.id.photo);
        final EditText txt = (EditText) findViewById(R.id.txt);
        txt.setVisibility(View.GONE);
        sendBtn.setVisibility(View.GONE);
        photo.setVisibility(View.GONE);

        if(isAdmin || isEvent){
            //LinearLayout textLayout = (LinearLayout)findViewById(R.id.textLayout);
            txt.setVisibility(View.VISIBLE);
            sendBtn.setVisibility(View.VISIBLE);
            photo.setVisibility(View.VISIBLE);
        }
        Button reloadBtn = (Button) findViewById(R.id.button4);
        reloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setList();
            }
        });

        Button backBtn = (Button) findViewById(R.id.button3);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(socialpage2.this, THSTheApp.class);
                i.putExtra("email",gs12.getEmailAddress());
                i.putExtra("UserName", gs12.getUsername());
                startActivity(i);
            }
        });


        Firebase.setAndroidContext(this);
        final Firebase ref = new Firebase("//fir-learn-4991c.firebaseio.com/Message");

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectFile();
            }
        });
        txt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEND){
                    sendToFirebase(txt,ref);
                    return true;

                }
                return false;
            }
        });


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendToFirebase(txt,ref);
            }
        });



        setList();


//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.set(Calendar.SECOND, 0);
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.HOUR, 0);
//        calendar.set(Calendar.AM_PM, Calendar.AM);
//        calendar.add(Calendar.DAY_OF_MONTH, 1);
//        Intent i = new Intent(socialpage2.this, UpdateService.class);
//        PendingIntent service=null;
//
//            service = PendingIntent.getService(socialpage2.this, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
//
//
//        AlarmManager alarmManager = (AlarmManager)socialpage2.this.getSystemService(Context.ALARM_SERVICE);
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, service);

        // final StorageReference videosRef = storage.getReferenceFromUrl("gs://fir-learn-4991c.appspot.com/videos");
        Log.e("DOES THIS WORK!!!", "" );
        //messageList.


        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }
    public void setList(){

        Firebase.setAndroidContext(this);
        final Firebase ref = new Firebase("//fir-learn-4991c.firebaseio.com/Message");
        final ListView messageList = getListView();
        //messageList.setBackgroundResource(R.drawable.redrectangle);
        //messageList.setBackgroundColor(Color.RED);
        messageList.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
        messageList.setStackFromBottom(true);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference[] StorageRef = {storage.getReferenceFromUrl("gs://fir-learn-4991c.appspot.com")};

        ValueEventListener valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<String> key = new ArrayList<String>();
                ArrayList<String> usernameinfo = new ArrayList<String>();
                ArrayList<String> messageinfo=new ArrayList<String>();
                ArrayList<String> photoUri=new ArrayList<String>();
                ArrayList<String> videoUri=new ArrayList<String>();
                ArrayList<String> dateyup=new ArrayList<String>();

                for (DataSnapshot child: dataSnapshot.getChildren()) {

                    ChatMessage chat=child.getValue(ChatMessage.class);
                    key.add(child.getKey());
                    usernameinfo.add(chat.getName());
                    messageinfo.add(decrypt(chat.getMessage()));
                    photoUri.add(chat.getPhoto());
                    videoUri.add(chat.getVideo());
                    dateyup.add(chat.getDate());



                }
                final Firebase ref = new Firebase("//fir-learn-4991c.firebaseio.com/Users/"+user+"/social");
                ref.setValue(String.valueOf(key.size()));
                messageList.setAdapter(new CustomAdapter(contextis, usernameinfo,messageinfo,photoUri,videoUri,dateyup,key,email));

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };
        ref.addValueEventListener(valueEventListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final String[] message = new String[1];
        Button sendBtn = (Button) findViewById(R.id.btnSend);
        byte[] dat = null;
        Firebase.setAndroidContext(this);
        final Firebase ref = new Firebase("//fir-learn-4991c.firebaseio.com/Message");
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
            final EditText txt = (EditText) findViewById(R.id.txt);
            txt.setHint("Image Has Been Selected, Enter Any Comments Or Click Send");
            final byte[] finalDat = dat;
            sendBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   sendToFirebasePhoto(txt,ref,imagesRef, finalDat,uri[0]);
                }
            });
            txt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if(event.getKeyCode()==KeyEvent.KEYCODE_ENTER){
                        sendToFirebasePhoto(txt,ref,imagesRef, finalDat,uri[0]);
                        return true;

                    }
                    return false;
                }
            });


        }
        else if(requestCode==12345&& data != null && data.getData() != null)
        {
            // final StorageMetadata metadata = new StorageMetadata.Builder()
            //      .setContentType("video/mpeg")
            //      .build();
            FirebaseStorage storage = FirebaseStorage.getInstance();
            final Uri urii = data.getData();
            final StorageReference[] videosRef = {storage.getReferenceFromUrl("gs://fir-learn-4991c.appspot.com/videos").child(urii.getLastPathSegment())};
            final EditText txt = (EditText) findViewById(R.id.txt);
            txt.setHint("Video Has Been Selected, Enter Any Comments Or Click Send");
            sendBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendToFirebaseVideo(txt,ref,videosRef,urii);
                }
            });
            txt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if(actionId== EditorInfo.IME_ACTION_DONE){
                        sendToFirebaseVideo(txt,ref,videosRef,urii);
                        return true;

                    }
                    return false;
                }
            });

        }
        else if(requestCode==0 && data != null && data.getData() != null && outputFileUri != null)
        {
            //getIntent().getExtras().get(MediaStore.EXTRA_OUTPUT);
            //Bitmap bitmap = null;
            //String _path = Environment.getExternalStorageDirectory()
            //       + File.separator ;
            // bitmap = BitmapFactory.decodeFile(_path);
            Log.e("PLEASEPLEASEPLEAEPLEASE","");
            FirebaseStorage storage = FirebaseStorage.getInstance();
            //File f=new File(_path);
            final Uri[] uri = {data.getData()};
            final StorageReference[] imagesRef = {storage.getReferenceFromUrl("gs://fir-learn-4991c.appspot.com/images").child(uri[0].getLastPathSegment())};

            final EditText txt = (EditText) findViewById(R.id.txt);
            txt.setHint("Image Has Been Selected, Enter Any Comments Or Click Send");
            sendBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    message[0] = txt.getText().toString();
                    if (message[0] != "" && message[0] != null) {


                        txt.setText("");
                        txt.setHint("");
                        UploadTask uploadTask = imagesRef[0].putFile(uri[0]);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {

                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                //downloadUrl[0] = taskSnapshot.getDownloadUrl();
                                Long datenow2=new Date().getTime();
                                ChatMessage chat123 = new ChatMessage(user, message[0], uri[0].getLastPathSegment(), "", dateMaker(datenow2));

                                ref.push().setValue(chat123);
                                restart();
                                txt.setText("Type Here");
                            }
                        });

                    }
                }
            });
        }

    }
    public void restart(){

        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
    public void selectFile(){
        final CharSequence[] items = {"Camera","Select image","Select video","Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(socialpage2.this);
        //builder.setTitle("Add file");
        //builder.setIcon(R.drawable.ic_photo_black_24dp);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
               /*if (items[item].equals("Camera")) {
                   dispatchTakePictureIntent();
                    //Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    //startActivityForResult(i, 0);
                } else */if (items[item].equals("Select image")) {
                    Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    i.setType("image/*");
                    startActivityForResult(Intent.createChooser(i, "Select image"), 1);
                } else if (items[item].equals("Select video")) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("video/*");
                    startActivityForResult(Intent.createChooser(intent, "Select video"), 12345);
                }else if (items[item].equals("Cancel")) {
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

        /*for(int i=0; i< (array.length-1); i++ )
        {Log.e("hello",""+array[i]);

           for(int j=0; j< (words.length-1);j++)
                if(array[i]==words[j])
                 {
                     Log.e("Words",""+array[i]+"=="+words[j]);
                    return true;
                  }
        }
        return false;*/
    }
    public void filtered()
    {
        final CharSequence[] items = {"Close"};
        AlertDialog.Builder builder = new AlertDialog.Builder(socialpage2.this);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(socialpage2.this);
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
    public void video(Uri uri)
    {
        MediaController mediaController=new MediaController(socialpage2.this);
        mediaController.setAnchorView(vid);
        vid.setMediaController(mediaController);
        vid.setVideoURI(uri);
        vid.start();

    }
    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {


            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }
    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",

                storageDir
        );


        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
    public void sendToFirebase( EditText txt, Firebase ref)
    {

        final String[] message = new String[1];
        message[0] = txt.getText().toString();
        int swearwords=filter(message[0]);
        if (message[0] != "" && message[0] != null) {
            Log.e("hello",""+swearwords);
            if(swearwords==3)
            {
                String x1 = "";
                Long datenowe=new Date().getTime();
                ChatMessage chat = new ChatMessage(user, encrypt(message[0]), x1, x1,dateMaker(datenowe));

                ref.push().setValue(chat);
                txt.setText("");

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
            Toast.makeText(socialpage2.this, "Please Type In Something",

                    Toast.LENGTH_SHORT).show();
        }

    }
    public void sendToFirebasePhoto(final EditText txt, final Firebase ref, StorageReference[] imagesRef, byte[] dat, final Uri uri)
    {
        uploading.setMessage("Uploading...");
        uploading.show();
        uploading.setCancelable(false);
        final String[] message = new String[1];
        final Uri[] downloadUrl = new Uri[1];
        final byte[][] dat1 = {dat};
        message[0] = txt.getText().toString();
        if (message[0] != "" && message[0] != null) {


            txt.setText("");
            txt.setHint("");
            UploadTask uploadTask = imagesRef[0].putBytes(dat1[0]);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    //downloadUrl[0] = taskSnapshot.getDownloadUrl();
                    Long datenow=new Date().getTime();
                    ChatMessage chat1 = new ChatMessage(user, encrypt(message[0]), uri.getLastPathSegment(), "", dateMaker(datenow));

                    ref.push().setValue(chat1);
                    restart();
                    txt.setText("Type Here");
                    uploading.dismiss();
                }
            });

        }
    }
    public void sendToFirebaseVideo(final EditText txt, final Firebase ref, StorageReference[] videosRef, final Uri uri)
    {
        uploading.setMessage("Uploading...");
        uploading.show();
        uploading.setCancelable(false);
        final String[] message = new String[1];
        //final Uri[] downloadUrl = new Uri[1];

        message[0] = txt.getText().toString();
        if (message[0] != "" && message[0] != null) {


            txt.setText("");
            txt.setHint("");
            UploadTask uploadTask = videosRef[0].putFile(uri);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //Log.e(TAG, "onFailure sendFileFirebase " + e.getMessage());
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //Log.i(TAG, "onSuccess sendFileFirebase");
                    //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    Long datenow1=new Date().getTime();

                    ChatMessage chat12 = new ChatMessage(user, encrypt(message[0]),"", uri.getLastPathSegment(),dateMaker(datenow1));
                    //ref.child(Integer.toString(numberdata)).setValue(chat1);
                    ref.push().setValue(chat12);
                    restart();
                    txt.setText("Type Here");
                    uploading.dismiss();
                }
            });
//            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                    double progress = 100.0 * (taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
//                    System.out.println("Upload is " + progress + "% done");
//                    int currentprogress = (int) progress;
//                    onProgressUpdate(currentprogress);
//                }
//            }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
//                    System.out.println("Upload is paused");
//                    uploading.setMessage("Upload is paused");
//                }
//            });
        }
    }

//    protected void onProgressUpdate(final int currentprogress) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                uploading.setMessage("Uploading..."+currentprogress);
//            }
//        });
//    }
//    public void uploadProgress(int currentprogress){
//        uploading.setMessage("Uploading..."+currentprogress);
//    }
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
    public String encrypt(String message){

        ArrayList<String> encryptedMessageArray = new ArrayList<String>();
        String[] letters = message.split("(?!^)");
        Log.e("", "HAHAHA"+message);

        for(String letter: letters){
            Log.e("", "encrypt"+letter);
            if(letter.equals("A")){
                encryptedMessageArray.add("1");
            }
            else if(letter.equals("B")){
                encryptedMessageArray.add(",");
            }
            else if(letter.equals("C")){
                encryptedMessageArray.add("a");
            }
            else if(letter.equals("D")){
                encryptedMessageArray.add("o");
            }
            else if(letter.equals("E")){
                encryptedMessageArray.add("b");
            }
            else if(letter.equals("F")){
                encryptedMessageArray.add("p");
            }
            else if(letter.equals("G")){
                encryptedMessageArray.add("7");
            }
            else if(letter.equals("H")){
                encryptedMessageArray.add("F");
            }
            else if(letter.equals("I")){
                encryptedMessageArray.add("r");
            }
            else if(letter.equals("J")){
                encryptedMessageArray.add("I");
            }
            else if(letter.equals("K")){
                encryptedMessageArray.add("q");
            }
            else if(letter.equals("L")){
                encryptedMessageArray.add("(");
            }
            else if(letter.equals("M")){
                encryptedMessageArray.add("E");
            }
            else if(letter.equals("N")){
                encryptedMessageArray.add("3");
            }
            else if(letter.equals("O")){
                encryptedMessageArray.add("G");
            }
            else if(letter.equals("P")){
                encryptedMessageArray.add("\\");
            }
            else if(letter.equals("Q")){
                encryptedMessageArray.add(";");
            }
            else if(letter.equals("R")){
                encryptedMessageArray.add("c");
            }
            else if(letter.equals("S")){
                encryptedMessageArray.add("|");
            }
            else if(letter.equals("T")){
                encryptedMessageArray.add("8");
            }
            else if(letter.equals("U")){
                encryptedMessageArray.add("}");
            }
            else if(letter.equals("V")){
                encryptedMessageArray.add("A");
            }
            else if(letter.equals("W")){
                encryptedMessageArray.add("<");
            }
            else if(letter.equals("X")){
                encryptedMessageArray.add("w");
            }
            else if(letter.equals("Y")){
                encryptedMessageArray.add("0");
            }
            else if(letter.equals("Z")){
                encryptedMessageArray.add(">");
            }
            else if(letter.equals("a")){
                encryptedMessageArray.add("5");
            }
            else if(letter.equals("b")){
                encryptedMessageArray.add("-");
            }
            else if(letter.equals("c")){
                encryptedMessageArray.add("C");
            }
            else if(letter.equals("d")){
                encryptedMessageArray.add("@");
            }
            else if(letter.equals("e")){
                encryptedMessageArray.add("!");
            }
            else if(letter.equals("f")){
                encryptedMessageArray.add("n");
            }
            else if(letter.equals("g")){
                encryptedMessageArray.add("d");
            }
            else if(letter.equals("h")){
                Log.e("", "here");
                encryptedMessageArray.add(".");
            }
            else if(letter.equals("i")){
                encryptedMessageArray.add("{");
            }
            else if(letter.equals("j")){
                encryptedMessageArray.add("2");
            }
            else if(letter.equals("k")){
                encryptedMessageArray.add("B");
            }
            else if(letter.equals("l")){
                encryptedMessageArray.add("H");
            }
            else if(letter.equals("m")){
                encryptedMessageArray.add("$");
            }
            else if(letter.equals("n")){
                encryptedMessageArray.add("J");
            }
            else if(letter.equals("o")){
                encryptedMessageArray.add("9");
            }
            else if(letter.equals("p")){
                encryptedMessageArray.add("?");
            }
            else if(letter.equals("q")){
                encryptedMessageArray.add(")");
            }
            else if(letter.equals("r")){
                encryptedMessageArray.add("~");
            }
            else if(letter.equals("s")){
                encryptedMessageArray.add("\"");
            }
            else if(letter.equals("t")){
                encryptedMessageArray.add("4");
            }
            else if(letter.equals("u")){
                encryptedMessageArray.add("&");
            }
            else if(letter.equals("v")){
                encryptedMessageArray.add(":");
            }
            else if(letter.equals("w")){
                encryptedMessageArray.add("D");
            }
            else if(letter.equals("x")){
                encryptedMessageArray.add("/");
            }
            else if(letter.equals("y")){
                encryptedMessageArray.add("v");
            }
            else if(letter.equals("z")){
                encryptedMessageArray.add("6");
            }
            else if(letter.equals("!")){
                encryptedMessageArray.add("h");
            }
            else if(letter.equals("?")){
                encryptedMessageArray.add("L");
            }

            else if(letter.equals("$")){
                encryptedMessageArray.add("i");
            }

            else if(letter.equals("(")){
                encryptedMessageArray.add("Y");
            }
            else if(letter.equals(")")){
                encryptedMessageArray.add("s");
            }
            else if(letter.equals(":")){
                encryptedMessageArray.add("M");
            }
            else if(letter.equals("/")){
                encryptedMessageArray.add("v");
            }
            else if(letter.equals("-")){
                encryptedMessageArray.add("W");
            }
            else if(letter.equals("&")){
                encryptedMessageArray.add("f");
            }
            else if(letter.equals("@")){
                encryptedMessageArray.add("K");
            }
            else if(letter.equals("\"")){
                encryptedMessageArray.add("e");
            }
            else if(letter.equals(".")){
                encryptedMessageArray.add("t");
            }
            else if(letter.equals(",")){
                encryptedMessageArray.add("X");
            }
            else if(letter.equals(";")){
                encryptedMessageArray.add("N");
            }
            else if(letter.equals("#")){
                encryptedMessageArray.add("U");
            }
            else if(letter.equals("[")){
                encryptedMessageArray.add("z");
            }
            else if(letter.equals("]")){
                encryptedMessageArray.add("P");
            }
            else if(letter.equals("\\")){
                encryptedMessageArray.add("^");
            }
            else if(letter.equals("|")){
                encryptedMessageArray.add("]");
            }
            else if(letter.equals("~")){
                encryptedMessageArray.add("*");
            }
            else if(letter.equals("<")){
                encryptedMessageArray.add("S");
            }
            else if(letter.equals(">")){
                encryptedMessageArray.add("j");
            }
            else if(letter.equals("%")){
                encryptedMessageArray.add("[");
            }
            else if(letter.equals("^")){
                encryptedMessageArray.add("y");
            }
            else if(letter.equals("*")){
                encryptedMessageArray.add("Q");
            }
            else if(letter.equals("+")){
                encryptedMessageArray.add("g");
            }
            else if(letter.equals("=")){
                encryptedMessageArray.add("Z");
            }
            else if(letter.equals("{")){
                encryptedMessageArray.add("%");
            }
            else if(letter.equals("}")){
                encryptedMessageArray.add("T");
            }
            else if(letter.equals("1")){
                encryptedMessageArray.add("O");
            }
            else if(letter.equals("2")){
                encryptedMessageArray.add("=");
            }
            else if(letter.equals("3")){
                encryptedMessageArray.add("l");
            }
            else if(letter.equals("4")){
                encryptedMessageArray.add("m");
            }
            else if(letter.equals("5")){
                encryptedMessageArray.add("#");
            }
            else if(letter.equals("6")){
                encryptedMessageArray.add("V");
            }
            else if(letter.equals("7")){
                encryptedMessageArray.add("x");
            }
            else if(letter.equals("8")){
                encryptedMessageArray.add("k");
            }
            else if(letter.equals("9")){
                encryptedMessageArray.add("+");
            }
            else if(letter.equals("0")){
                encryptedMessageArray.add("R");
            }
            else{
                encryptedMessageArray.add(letter);
            }

        }
        for(String x: encryptedMessageArray){
            Log.e("", "MESSAGE" + x);
        }
        String encryptedMessage = TextUtils.join("", encryptedMessageArray);
        return encryptedMessage;
    }
    public String decrypt(String message){
        ArrayList<String> encryptedMessageArray = new ArrayList<String>();
        String[] letters = message.split("(?!^)");

        Log.e("", "decrypt"+letters);
        for(String letter: letters){
            if(letter.equals("1")) {
                encryptedMessageArray.add("A");
            }
            else if(letter.equals(",")){
                encryptedMessageArray.add("B");
            }
            else if(letter.equals("a")){
                encryptedMessageArray.add("C");
            }
            else if(letter.equals("o")){
                encryptedMessageArray.add("D");
            }
            else if(letter.equals("b")){
                encryptedMessageArray.add("E");
            }
            else if(letter.equals("p")){
                encryptedMessageArray.add("F");
            }
            else if(letter.equals("7")){
                encryptedMessageArray.add("G");
            }
            else if(letter.equals("F")){
                encryptedMessageArray.add("H");
            }
            else if(letter.equals("r")){
                encryptedMessageArray.add("I");
            }
            else if(letter.equals("I")){
                encryptedMessageArray.add("J");
            }
            else if(letter.equals("q")){
                encryptedMessageArray.add("K");
            }
            else if(letter.equals("(")){
                encryptedMessageArray.add("L");
            }
            else if(letter.equals("E")){
                encryptedMessageArray.add("M");
            }
            else if(letter.equals("3")){
                encryptedMessageArray.add("N");
            }
            else if(letter.equals("G")){
                encryptedMessageArray.add("O");
            }
            else if(letter.equals("\\")){
                encryptedMessageArray.add("P");
            }
            else if(letter.equals(";")){
                encryptedMessageArray.add("Q");
            }
            else if(letter.equals("c")){
                encryptedMessageArray.add("R");
            }
            else if(letter.equals("|")){
                encryptedMessageArray.add("S");
            }
            else if(letter.equals("8")){
                encryptedMessageArray.add("T");
            }
            else if(letter.equals("}")){
                encryptedMessageArray.add("U");
            }
            else if(letter.equals("A")){
                encryptedMessageArray.add("V");
            }
            else if(letter.equals("<")){
                encryptedMessageArray.add("W");
            }
            else if(letter.equals("w")){
                encryptedMessageArray.add("X");
            }
            else if(letter.equals("0")){
                encryptedMessageArray.add("Y");
            }
            else if(letter.equals(">")){
                encryptedMessageArray.add("Z");
            }
            else if(letter.equals("5")){
                encryptedMessageArray.add("a");
            }
            else if(letter.equals("-")){
                encryptedMessageArray.add("b");
            }
            else if(letter.equals("C")){
                encryptedMessageArray.add("c");
            }
            else if(letter.equals("@")){
                encryptedMessageArray.add("d");
            }
            else if(letter.equals("!")){
                encryptedMessageArray.add("e");
            }
            else if(letter.equals("n")){
                encryptedMessageArray.add("f");
            }
            else if(letter.equals("d")){
                encryptedMessageArray.add("g");
            }
            else if(letter.equals(".")){
                encryptedMessageArray.add("h");
            }
            else if(letter.equals("{")){
                encryptedMessageArray.add("i");
            }
            else if(letter.equals("2")){
                encryptedMessageArray.add("j");
            }
            else if(letter.equals("B")){
                encryptedMessageArray.add("k");
            }
            else if(letter.equals("H")){
                encryptedMessageArray.add("l");
            }
            else if(letter.equals("$")){
                encryptedMessageArray.add("m");
            }
            else if(letter.equals("J")){
                encryptedMessageArray.add("n");
            }
            else if(letter.equals("9")){
                encryptedMessageArray.add("o");
            }
            else if(letter.equals("?")){
                encryptedMessageArray.add("p");
            }
            else if(letter.equals(")")){
                encryptedMessageArray.add("q");
            }
            else if(letter.equals("~")){
                encryptedMessageArray.add("r");
            }
            else if(letter.equals("\"")){
                encryptedMessageArray.add("s");
            }
            else if(letter.equals("4")){
                encryptedMessageArray.add("t");
            }
            else if(letter.equals("&")){
                encryptedMessageArray.add("u");
            }
            else if(letter.equals(":")){
                encryptedMessageArray.add("v");
            }
            else if(letter.equals("D")){
                encryptedMessageArray.add("w");
            }
            else if(letter.equals("/")){
                encryptedMessageArray.add("x");
            }
            else if(letter.equals("v")){
                encryptedMessageArray.add("y");
            }
            else if(letter.equals("6")){
                encryptedMessageArray.add("z");
            }
            else if(letter.equals("h")){
                encryptedMessageArray.add("!");
            }
            else if(letter.equals("L")){
                encryptedMessageArray.add("?");
            }

            else if(letter.equals("i")){
                encryptedMessageArray.add("$");
            }
            else if(letter.equals("Y")){
                encryptedMessageArray.add("(");
            }
            else if(letter.equals("s")){
                encryptedMessageArray.add(")");
            }
            else if(letter.equals("M")){
                encryptedMessageArray.add(":");
            }
            else if(letter.equals("u")){
                encryptedMessageArray.add("/");
            }
            else if(letter.equals("W")){
                encryptedMessageArray.add("-");
            }
            else if(letter.equals("f")){
                encryptedMessageArray.add("&");
            }
            else if(letter.equals("K")){
                encryptedMessageArray.add("@");
            }
            else if(letter.equals("e")){
                encryptedMessageArray.add("\"");
            }
            else if(letter.equals("t")){
                encryptedMessageArray.add(".");
            }
            else if(letter.equals("X")){
                encryptedMessageArray.add(",");
            }
            else if(letter.equals("N")){
                encryptedMessageArray.add(";");
            }
            else if(letter.equals("U")){
                encryptedMessageArray.add("#");
            }
            else if(letter.equals("z")){
                encryptedMessageArray.add("[");
            }
            else if(letter.equals("P")){
                encryptedMessageArray.add("]");
            }
            else if(letter.equals("^")){
                encryptedMessageArray.add("\\");
            }
            else if(letter.equals("]")){
                encryptedMessageArray.add("|");
            }
            else if(letter.equals("*")){
                encryptedMessageArray.add("~");
            }
            else if(letter.equals("S")){
                encryptedMessageArray.add("<");
            }
            else if(letter.equals(";")){
                encryptedMessageArray.add(">");
            }
            else if(letter.equals("[")){
                encryptedMessageArray.add("%");
            }
            else if(letter.equals("y")){
                encryptedMessageArray.add("^");
            }
            else if(letter.equals("Q")){
                encryptedMessageArray.add("*");
            }
            else if(letter.equals("g")){
                encryptedMessageArray.add("+");
            }
            else if(letter.equals("Z")){
                encryptedMessageArray.add("=");
            }
            else if(letter.equals("%")){
                encryptedMessageArray.add("{");
            }
            else if(letter.equals("T")){
                encryptedMessageArray.add("}");
            }
            else if(letter.equals("O")){
                encryptedMessageArray.add("1");
            }
            else if(letter.equals("=")){
                encryptedMessageArray.add("2");
            }
            else if(letter.equals("l")){
                encryptedMessageArray.add("3");
            }
            else if(letter.equals("m")){
                encryptedMessageArray.add("4");
            }
            else if(letter.equals("#")){
                encryptedMessageArray.add("5");
            }
            else if(letter.equals("V")){
                encryptedMessageArray.add("6");
            }
            else if(letter.equals("x")){
                encryptedMessageArray.add("7");
            }
            else if(letter.equals("k")){
                encryptedMessageArray.add("8");
            }
            else if(letter.equals("+")){
                encryptedMessageArray.add("9");
            }
            else if(letter.equals("R")){
                encryptedMessageArray.add("0");
            }
            else{
                encryptedMessageArray.add(letter);
            }

        }

        String encryptedMessage = TextUtils.join("", encryptedMessageArray);
        return encryptedMessage;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                // ProjectsActivity is my 'home' activity
                startActivityAfterCleanup(THSTheApp.class);
                return true;
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    private void startActivityAfterCleanup(Class<?> cls) {
        //if (projectsDao != null) projectsDao.close();
        Intent intent = new Intent(getApplicationContext(), cls);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


}

