package creations.appmaking.thsvhsapp;

/**
 *Copyright © 2017 Sriharsha Singam. All rights reserved.
 **/


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.VideoView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static java.security.AccessController.getContext;

class CustomAdapter extends BaseAdapter{

    ArrayList<String> usernameinfo;
    ArrayList<String> messageinfo;
    ArrayList<String> photoUri;
    ArrayList<String> videoUri;
    ArrayList<String> date;
    ArrayList<String> key;
    String email;

    Context context;


    private static LayoutInflater inflater=null;
    public CustomAdapter(Context contexti,ArrayList<String> user,ArrayList<String> message,ArrayList<String> photo,ArrayList<String> video,ArrayList<String> date,ArrayList<String> keys, String emailAdres) {

        usernameinfo = user;
        messageinfo = message;
        photoUri = photo;
        videoUri = video;
        context = contexti;
        email = emailAdres;
        this.date=date;
        key = keys;

        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {

        return usernameinfo.size();
    }

    @Override
    public Object getItem(int position) {

        return position;
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    public class Holder
    {
        LinearLayout l2;
        LinearLayout lm;
        TextView userin;
        TextView messagein;
        TextView date;
        ImageView img;
        VideoView vv;
        FrameLayout fl1;
        ToggleButton tb;
        ImageView pImage;
        TextView likeNumber;
        GridLayout gridViewMessage;

    }
    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {

        final boolean[] like = new boolean[1];
        final Holder holder=new Holder();
        final View rowView;

        final GlobalVariables gs1 = (GlobalVariables) context.getApplicationContext();
//        rowView.setLayoutParams(params);

        Firebase.setAndroidContext(context);
        final Firebase likeref = new Firebase("//fir-learn-4991c.firebaseio.com/like/"+gs1.getUsername());
        final Firebase likeref1 = new Firebase("//fir-learn-4991c.firebaseio.com/like");
        final Firebase profile = new Firebase("//fir-learn-4991c.firebaseio.com/Users/"+usernameinfo.get(position)+"/profileImage");

        System.out.println("Key Per Position"+ key.get(position));

        rowView = inflater.inflate(R.layout.listset, null);
        holder.tb = (ToggleButton)rowView.findViewById(R.id.toggleButton);
        holder.tb.setText(null);
        holder.tb.setTextOff(null);
        holder.tb.setTextOn(null);
        holder.pImage = (ImageView) rowView.findViewById(R.id.pImage);
        holder.gridViewMessage = (GridLayout) rowView.findViewById(R.id.gridViewMessage);
        holder.lm = (LinearLayout)rowView.findViewById(R.id.linear1);
       // holder.l2 = (LinearLayout)rowView.findViewById(R.id.linear2);
        //holder.lm = (LinearLayout)rowView.findViewById(R.id.linearmain);
        //LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        //LinearLayout ll = (LinearLayout)rowView.findViewById(R.id.linear1);
        holder.userin=(TextView) rowView.findViewById(R.id.usernametxt);
        holder.messagein=(TextView) rowView.findViewById(R.id.messagetxt);
        holder.img=(ImageView) rowView.findViewById(R.id.image1);
        holder.vv=(VideoView)rowView.findViewById(R.id.videoView);
//        holder.sw=(Switch)rowView.findViewById(R.id.switch1);
        holder.date=(TextView)rowView.findViewById(R.id.date);
//        holder.explicit = (TextView)rowView.findViewById(R.id.switch1);
        //holder.ll = (LinearLayout) rowView.findViewById(R.id.linear1);
        holder.vv.setVisibility(View.GONE);
        holder.img.setVisibility(View.GONE);
//        holder.fl1 = (FrameLayout)rowView.findViewById(R.id.fl1);
//        holder.fl1.setVisibility(View.GONE);

        holder.userin.setText(usernameinfo.get(position));
        holder.messagein.setText(messageinfo.get(position));
        holder.messagein.setTextColor(ContextCompat.getColor(context, R.color.Black));
        holder.messagein.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
        //holder.messagein.setTextSize(17);
//        holder.explicit.setText("Explicit?");
//        holder.explicit.setTextColor(ContextCompat.getColor(context, R.color.CornflowerBlue));
//        holder.explicit.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        holder.userin.setTextColor(ContextCompat.getColor(context, R.color.gray));
//        holder.l2.setVisibility(View.GONE);

        holder.date.setText(date.get(position));
        holder.date.setTextColor(ContextCompat.getColor(context, R.color.gray));
        holder.date.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        holder.userin.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        holder.tb.setBackgroundResource(R.drawable.like);

        holder.likeNumber = (TextView)rowView.findViewById(R.id.likeNumber);
        //holder.likeNumber.setText("0");
        holder.likeNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);


//        Display display = getWindowManager().getDefaultDisplay();
//        DisplayMetrics outMetrics = new DisplayMetrics ();
//        display.getMetrics(outMetrics);
//
//        float density  = getResources().getDisplayMetrics().density;
//        float dpHeight = outMetrics.heightPixels / density;
//        float dpWidth  = outMetrics.widthPixels / density;
//        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
//        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
//        int dpWidth = (int) (displayMetrics.widthPixels / displayMetrics.density);
//
//        float size = context.getResources().getDimensionPixelSize(R.dimen.pad_minus20dp);

//        GridLayout.LayoutParams gllp = (GridLayout.LayoutParams) .getLayoutParams();

//        if((dpWidth - size) == 0){
//
//        }else{
////            gllp.width = (;
////            gllp.height = GridLayout.LayoutParams.MATCH_PARENT;
//            holder.lm.setLayoutParams(new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
//        }


        likeref1.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> likingNumber = new ArrayList<String>();
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    for(DataSnapshot children: child.getChildren()){
                        System.out.println("Like Number Getter"+children);
                        like liking123 = children.getValue(like.class);
                        likingNumber.add(liking123.getLiking());

                    }
                }
                int countLike = 0;
                for(String likeNum: likingNumber){
                    System.out.println("Initial CountLike: "+ countLike);
                    System.out.println("Like Key "+likeNum);
                    System.out.println("Main Key "+key.get(position));
                    if(likeNum.equals(key.get(position))){

                        countLike = countLike + 1;
                        System.out.println("CountLike Next Next: "+ countLike);
                    }
                }
                if(countLike == 0){
                    holder.likeNumber.setText("");
                }
                else{
                    holder.likeNumber.setText(String.valueOf(countLike));
                }

                //likingNumber = null;

                //countLike = 0;
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        FirebaseStorage storage1 = FirebaseStorage.getInstance();
        final StorageReference[] StorageRef1 = {storage1.getReferenceFromUrl("gs://fir-learn-4991c.appspot.com")};
        profile.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.getValue() != ""){
                    StorageReference storageRef1 = StorageRef1[0].child("images/" + dataSnapshot.getValue());
                    storageRef1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            Picasso.with(context).load(uri).into(holder.pImage);
                            Log.e("Image Here", "" + uri);


                        }
                    });
                }
                else{
                    holder.pImage.setBackgroundResource(R.drawable.code);
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        likeref.addValueEventListener(new ValueEventListener() {
            ArrayList<String> likekey = new ArrayList<String>();

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> likes = new ArrayList<String>();

                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    //System.out.println(child);
                    like liking1 = child.getValue(like.class);
                    System.out.println("Like Dat Snap Shot"+liking1.getLiking());
                    //likekey.add(child.getKey());
                    likes.add(liking1.getLiking());

                }
                System.out.println("Array List For Likes: "+ likes +" Like Size: "+ likes.size());
                for(int i = 0; i < likes.size(); i++){
                    System.out.println("For Iteration: "+likes.get(i));
                    if(likes.get(i).equals(key.get(position))){
                        System.out.println("Like Keys For Specific For Loops The Is Equal: "+ key.get(position));
                        holder.tb.setBackgroundResource(R.drawable.likered);
                    }
                }
                //likes = [""];

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        holder.tb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(b){
                    holder.tb.setBackgroundColor(Color.RED);

                    holder.tb.setBackgroundResource(R.drawable.likered);

                    //Log.d("ON",""+messageinfo.get(position));
                    likeref.push().child("liking").setValue(key.get(position));
                    likeref.addValueEventListener(new ValueEventListener() {
                        ArrayList<String> likekey = new ArrayList<String>();

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            ArrayList<String> likes123 = new ArrayList<String>();
                            for (DataSnapshot child: dataSnapshot.getChildren()) {
                                System.out.println(child);
                                like liking1 = child.getValue(like.class);
                                //likekey.add(child.getKey());
                                likes123.add(liking1.getLiking());

                            }
                            for(String likeskey :likes123){
                                if(likeskey.equals(key.get(position))){
                                    //like[0] = true;
                                    holder.tb.setBackgroundResource(R.drawable.likered);
                                }
                                //return;
                            }
                            //likes123 = null;

                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }
                else{

                    final Firebase ref=new Firebase("//fir-learn-4991c.firebaseio.com/like/"+gs1.getUsername());


                    final ArrayList<String> likekey = new ArrayList<String>();
                    final ArrayList<String> liking = new ArrayList<String>();

                    ValueEventListener valueEventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot child: dataSnapshot.getChildren()) {
                                // System.out.println(child);
                                like liking1 = child.getValue(like.class);
                                likekey.add(child.getKey());
                                liking.add(liking1.getLiking());

                            }
                            int count = 0;
                            int i = -1;
                            for(String likesing: liking){
                                i = i + 1;

                                if(likesing.equals(key.get(position))){

                                    count = i;
                                }
                            }
                            ref.child(likekey.get(count)).removeValue();
                            holder.tb.setBackgroundColor(Color.WHITE);

                            holder.tb.setBackgroundResource(R.drawable.like);
                            likeref.addValueEventListener(new ValueEventListener() {
                                ArrayList<String> likekey = new ArrayList<String>();

                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    ArrayList<String> likes12345 = new ArrayList<String>();
                                    for (DataSnapshot child: dataSnapshot.getChildren()) {
                                        // System.out.println(child);
                                        like liking1 = child.getValue(like.class);
                                        //likekey.add(child.getKey());
                                        likes12345.add(liking1.getLiking());

                                    }
                                    for(String likeskey :likes12345){
                                        if(likeskey.equals(key.get(position))){
                                            //like[0] = true;
                                            holder.tb.setBackgroundResource(R.drawable.likered);
                                        }
                                        //return;
                                    }
                                    //likes12345 = null;

                                }

                                @Override
                                public void onCancelled(FirebaseError firebaseError) {

                                }
                            });

                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    };
                    ref.addValueEventListener(valueEventListener);

                }


            }
        });



        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference[] StorageRef = {storage.getReferenceFromUrl("gs://fir-learn-4991c.appspot.com")};
        if(!photoUri.get(position).equals("") && !videoUri.get(position).equals("")){
            holder.vv.setVisibility(View.VISIBLE);
            //holder.l2.setVisibility(View.VISIBLE);
            //rowView.setMinimumHeight(800);
//            holder.fl1.setVisibility(View.VISIBLE);
            //LinearLayout.LayoutParams params1 =
            //holder.vv.setLayoutParams();
            //holder.lm.setLayoutParams(new LayoutParams(width, height));
            StorageRef[0].child("videos/"+videoUri.get(position)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri){


                    Log.e("Video Here", "" + uri);
                    Bitmap thumb = ThumbnailUtils.createVideoThumbnail(String.valueOf(uri),
                            MediaStore.Images.Thumbnails.MINI_KIND);

                    MediaController mediaController=new MediaController(context);
                    mediaController.setAnchorView(holder.vv);
                    holder.vv.setMediaController(mediaController);
                    BitmapDrawable bitmapDrawable = new BitmapDrawable(thumb);
                    holder.vv.setBackgroundDrawable(bitmapDrawable);
                    holder.vv.setVideoURI(uri);
                    //holder.vv.start();
                    Log.e("Hoorah", "" );
                }
            });
        }
        else if (!photoUri.get(position).equals("") && videoUri.get(position).equals("")) {

            holder.img.setVisibility(View.VISIBLE);
            //rowView.setMinimumHeight(800);
            //holder.l2.setVisibility(View.VISIBLE);
            //holder.fl1.setVisibility(View.VISIBLE);
            StorageReference storageRef1 = StorageRef[0].child("images/" + photoUri.get(position));
            storageRef1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
//                    Bitmap b = ((BitmapDrawable)uri.getBackground()).getBitmap();
//                    int w = b.getWidth();
//                    int h = b.getHeight();
                    Picasso.with(context).load(uri).into(holder.img);
                    Log.e("Image Here", "" + uri);


                }
            });
        }

        else if(!videoUri.get(position).equals("") && photoUri.get(position).equals("")) {
            holder.vv.setVisibility(View.VISIBLE);
            //rowView.setMinimumHeight(800);
            //holder.l2.setVisibility(View.VISIBLE);
            //holder.fl1.setVisibility(View.VISIBLE);
            StorageRef[0].child("videos/"+videoUri.get(position)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri){


                    Log.e("Video Here", "" + uri);
                    //Bitmap thumb = ThumbnailUtils.createVideoThumbnail(String.valueOf(uri),
                      //      MediaStore.Images.Thumbnails.MINI_KIND);
                    MediaController mediaController=new MediaController(context);
                    mediaController.setAnchorView(holder.vv);
                    holder.vv.setMediaController(mediaController);
                    //BitmapDrawable bitmapDrawable = new BitmapDrawable(thumb);
                    //holder.vv.setBackgroundDrawable(bitmapDrawable);
                    holder.vv.setVideoURI(uri);
                    //holder.vv.start();
                    Log.e("Hoorah", "" );
                }
            });

        }


        holder.vv.setOnErrorListener(new MediaPlayer.OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.d("video", "setOnErrorListener ");
                return true;
            }
        });


//        rowView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                final CharSequence[] items = {"Explicit Message","Comment","Cancel"};
//                AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                //builder.setTitle("Add file");
//                //builder.setIcon(R.drawable.ic_photo_black_24dp);
//                builder.setItems(items, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int item) {
//               /*if (items[item].equals("Camera")) {
//                   dispatchTakePictureIntent();
//                    //Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                    //startActivityForResult(i, 0);
//                } else */if (items[item].equals("Explicit Message")) {
////                            Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
////                            i.setType("image/*");
////                            startActivityForResult(Intent.createChooser(i, "Select image"), 1);
//                            explicit(position);
//                        } else if (items[item].equals("Comment")) {
//                            //Toast.makeText(context,"This Feature Will Come In Time", Toast.LENGTH_LONG).show();
//                            Intent intent = new Intent(context, CommentController.class);
//                            intent.putExtra("commentKey", key.get(position));
//                            intent.putExtra("commentDate",date.get(position));
//                            intent.putExtra("commentName",usernameinfo.get(position));
//                            intent.putExtra("commentMessage",messageinfo.get(position));
//                            intent.putExtra("commentPhoto",photoUri.get(position));
//                            intent.putExtra("commentVideo",videoUri.get(position));
//                            //intent.putExtra("Password", pass);
//                            context.startActivity(intent);
//                        }else if (items[item].equals("Cancel")) {
//                            dialog.dismiss();
//                        }
//                    }
//                });
//                builder.show();
//                return false;
//            }
//        });

//        rowView.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//               // Toast.makeText(context, "AUTHOR: "+usernameinfo.get(position), Toast.LENGTH_LONG).show();
//
//
//
//            }
//        });
        return rowView;
    }
    public void explicit(final int position){
//        Log.e("IT WORKS!!!","");
//                Firebase.setAndroidContext(context);
//                final Firebase fire = new Firebase("//fir-learn-4991c.firebaseio.com/explicitcontent");
//                Firebase fire1=new Firebase("//fir-learn-4991c.firebaseio.com/Users/"+usernameinfo.get(position));
//                final Firebase fire2=fire1.child("email");
//                Log.e("FIRE@ = ",""+fire2);
//
//                final String[] posting = new String[1];
//                fire2.addValueEventListener(new com.firebase.client.ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        posting[0] =dataSnapshot.getValue(String.class);
//                        Log.e("POSTING = ",""+posting[0]);
//                        //Log.e("POSTING = ",""+posting[0]);
//                        explicitstuff ex=new explicitstuff(usernameinfo.get(position), posting[0],email,key.get(position));
//                        Log.e("EXPLICIT = ",""+ex.getPersonemail());
//                        fire.push().setValue(ex);
//
//                        Toast.makeText(context,"YOUR EXPLICIT REPORT HAS BEEN SENT!!!", Toast.LENGTH_LONG).show();
//
//                    }
//
//                    @Override
//                    public void onCancelled(FirebaseError firebaseError) {
//
//                    }
//                });
                //String stuffing=posting[0];
        final Firebase fire = new Firebase("//fir-learn-4991c.firebaseio.com/explicitcontent");
        explicitstuff ex=new explicitstuff(key.get(position));

                        fire.push().setValue(ex);

                        Toast.makeText(context,"YOUR EXPLICIT REPORT HAS BEEN SENT!!!", Toast.LENGTH_LONG).show();

    }
    public static int getScreenWidth() {

        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }





}