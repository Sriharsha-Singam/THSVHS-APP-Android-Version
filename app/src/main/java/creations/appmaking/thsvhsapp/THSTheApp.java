package creations.appmaking.thsvhsapp;

/**
 *Copyright © 2017 Sriharsha Singam. All rights reserved.
 **/


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import java.util.logging.Handler;

public class THSTheApp extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String user1;
    int socialcount, announcecount, finalsocialcount, finalannouncecount;
    String email, announceuser1, socialuser1;
    final boolean[] adminuser = {false};
    final boolean[] eventuser = {false};
    TextView socialbadge, announcebadge;

    private Handler mRepeatHandler;
    private Runnable mRepeatRunnable;
    private final static int UPDATE_INTERVAL = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thsthe_app);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigation = (NavigationView) findViewById(R.id.nav_view);
        navigation.setNavigationItemSelectedListener(this);
        final View v=navigation.getHeaderView(0);
        email=getIntent().getExtras().get("email").toString();
        user1=getIntent().getExtras().get("UserName").toString();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){

            @Override
            public void onDrawerOpened(View drawerView) {
                //restart();
                initializeBadge();
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                //restart();
                initializeBadge();
                super.onDrawerClosed(drawerView);
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        // View v = getLayoutInflater().inflate(R.layout.nav_header_nav, null);

        TextView Text1=(TextView)v.findViewById(R.id.Text1);
        TextView Text2=(TextView)v.findViewById(R.id.Text2);

        Text1.setText(user1);

        Text2.setText(email);

        final GlobalVariables gs = (GlobalVariables) THSTheApp.this.getApplicationContext();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference[] StorageRef = {storage.getReferenceFromUrl("gs://fir-learn-4991c.appspot.com")};
        final ImageView profile = (ImageView)v.findViewById(R.id.imageView);
        if(gs.getProfileImage() != ""){
            StorageReference storageRef1 = StorageRef[0].child("images/" + gs.getProfileImage());
            storageRef1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {

                    Picasso.with(THSTheApp.this).load(uri).into(profile);
                    Log.e("Image Here", "" + uri);


                }
            });
        }
        else{
            profile.setBackgroundResource(R.drawable.code);
        }



//        socialbadge =(TextView) MenuItemCompat.getActionView(navigation.getMenu().
//                findItem(R.id.social));
        announcebadge =(TextView) MenuItemCompat.getActionView(navigation.getMenu().
                findItem(R.id.announce));
        Firebase.setAndroidContext(this);
        Firebase hot1 = new Firebase("//fir-learn-4991c.firebaseio.com/adminusers");

        ValueEventListener valueEventListener1=new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<String> adminusers = new ArrayList<String>();


                for (DataSnapshot child: dataSnapshot.getChildren()) {

                    adminuserdata po=child.getValue(adminuserdata.class);
                    adminusers.add(po.getAdmin());
                    Log.e("ADMIN",""+adminusers);



                }
                for(String s : adminusers){
                    Log.e("STRING S",""+s);
                    Log.e("EMAIL",""+email);
                    if(s.equals(email))
                        adminuser[0] =true;
                }
                Log.e("BOOLEAN ADMIN",""+adminuser[0]);

                gs.setAdminUser(adminuser[0]);

            }


            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };
        hot1.addListenerForSingleValueEvent(valueEventListener1);

        Firebase hot2 = new Firebase("//fir-learn-4991c.firebaseio.com/eventusers");

        ValueEventListener valueEventListener2=new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<String> eventusers = new ArrayList<String>();


                for (DataSnapshot child: dataSnapshot.getChildren()) {

                    eventuserdata po=child.getValue(eventuserdata.class);
                    eventusers.add(po.getEvent());
                    Log.e("ADMIN",""+eventusers);



                }
                for(String s : eventusers){
                    Log.e("STRING S",""+s);
                    Log.e("EMAIL",""+email);
                    if(s.equals(email))
                        eventuser[0] =true;
                }
                Log.e("BOOLEAN ADMIN",""+eventuser[0]);

                gs.setEventUser(eventuser[0]);

            }


            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };
        hot2.addListenerForSingleValueEvent(valueEventListener2);

        final Firebase ref = new Firebase("//fir-learn-4991c.firebaseio.com/Message");
        ValueEventListener valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<String> key = new ArrayList<String>();
                ArrayList<String> usernameinfo = new ArrayList<String>();


                for (DataSnapshot child: dataSnapshot.getChildren()) {

                    ChatMessage chat=child.getValue(ChatMessage.class);
                    key.add(child.getKey());
                    usernameinfo.add(chat.getName());
//                    messageinfo.add(decrypt(chat.getMessage()));
//                    photoUri.add(chat.getPhoto());
//                    videoUri.add(chat.getVideo());
//                    dateyup.add(chat.getDate());



                }
                //messageList.setAdapter(new CustomAdapter(contextis, usernameinfo,messageinfo,photoUri,videoUri,dateyup,key,email));
                socialcount = key.size();
                Log.e("Social", String.valueOf(socialcount));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };
        ref.addValueEventListener(valueEventListener);
        final Firebase ref1 = new Firebase("//fir-learn-4991c.firebaseio.com/announce");


        ValueEventListener valueEventListener1234=new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<String> arrayList = new ArrayList<String>();



                for (DataSnapshot child: dataSnapshot.getChildren()) {

                    Announcements an=child.getValue(Announcements.class);
                    arrayList.add(an.getAnnounce());



                }

                //lv.setAdapter(aa);
                announcecount = arrayList.size();
                Log.e("Announce", String.valueOf(announcecount));
                //}


            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };
        ref1.addValueEventListener(valueEventListener1234);


        /*if(!adminuser[0])
        {
            MenuItem mi=(MenuItem)findViewById(R.id.admin);
            mi.setVisible(true);
        }
        else
        {
            MenuItem mi=(MenuItem)findViewById(R.id.admin);
            mi.setVisible(false);
        }*/









        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        Firebase hot = new Firebase("//fir-learn-4991c.firebaseio.com/feature");

        final ListView lv=(ListView)findViewById(R.id.feature);
        lv.setStackFromBottom(true);



        ValueEventListener valueEventListener123=new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<String> arrayList = new ArrayList<String>();

                 ArrayAdapter<String> aa= new ArrayAdapter<String>(
                        THSTheApp.this,
                        android.R.layout.simple_list_item_1,
                        arrayList);


                for (DataSnapshot child: dataSnapshot.getChildren()) {

                    Post po=child.getValue(Post.class);
                    arrayList.add(po.getFeature());



                }

                lv.setAdapter(aa);

                //}

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };
        hot.addValueEventListener(valueEventListener123);
        //arrayList.add("");

//        initializeBadge();
//        mRepeatHandler = new Handler();
//        mRepeatRunnable = new Runnable() {
//            @Override
//            public void run() {
//                //Do something awesome
//
//                mRepeatHandler.postDelayed(mRepeatRunnable, UPDATE_INTERVAL);
//            };
//
//            mRepeatHandler.postDelayed(mRepeatRunnable, UPDATE_INTERVAL);
        //restart();
    }

    public void restart(){

        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.thsthe_app, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.announce) {

            Intent intent = new Intent(THSTheApp.this, announce.class);
            intent.putExtra("UserName", user1);
            //intent.putExtra("Password", pass);
            startActivity(intent);
        } else if (id == R.id.guidance) {
            Intent intent = new Intent(THSTheApp.this, guidance.class);
            intent.putExtra("UserName", user1);
            //intent.putExtra("Password", pass);
            startActivity(intent);
        } else if (id == R.id.tutor) {
            Intent intent = new Intent(THSTheApp.this, tutoring.class);
            intent.putExtra("UserName", user1);
            //intent.putExtra("Password", pass);
            startActivity(intent);
        } else if (id == R.id.settings) {
            Intent intent = new Intent(THSTheApp.this, settings.class);
            intent.putExtra("email", email);
            intent.putExtra("UserName", user1);
            //intent.putExtra("Password", pass);
            startActivity(intent);
        }
        else if (id == R.id.ads) {
            Intent intent = new Intent(THSTheApp.this, AdsPage.class);
            intent.putExtra("UserName", user1);
            //intent.putExtra("UserName", user1);
            //intent.putExtra("Password", pass);
            startActivity(intent);
        }
         else if (id == R.id.map) {
            Intent intent = new Intent(THSTheApp.this, map.class);
            intent.putExtra("UserName", user1);
            //intent.putExtra("Password", pass);
            startActivity(intent);
        }
        else if (id == R.id.social) {
            Intent intent = new Intent(THSTheApp.this, socialpage2.class);
            intent.putExtra("UserName", user1);
            intent.putExtra("email",email);
            //intent.putExtra("Password", pass);
            startActivity(intent);
        }
        else if (id == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(THSTheApp.this, Login.class);
            startActivity(intent);
        }
        else if (id == R.id.about) {
            Intent intent = new Intent(THSTheApp.this, aboutpage.class);
            intent.putExtra("UserName", user1);
            //intent.putExtra("Password", pass);
            startActivity(intent);
        }
          if (id == R.id.admin) {
            if(adminuser[0])
            {
                Intent intent = new Intent(THSTheApp.this, adminuserlayout.class);
                intent.putExtra("UserName", user1);
                //intent.putExtra("Password", pass);
                startActivity(intent);
            }
            else
            {
                notallowed();
            }
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(GravityCompat.START);
        if (drawerOpen) {
            initializeBadge();
            restart();
        }
        return super.onPrepareOptionsMenu(menu);
    }

    public void notallowed()
    {
        final CharSequence[] items = {"Close"};
        AlertDialog.Builder builder = new AlertDialog.Builder(THSTheApp.this);
        builder.setTitle("YOU ARE NOT PERMITTED TO ENTER!!!");
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
    public void initializeBadge(){
//         Timer timer = new Timer();
//         TimerTask timerTask;
//        timerTask = new TimerTask() {
//            @Override
//            public void run() {
                //refresh your textview
        Firebase hot12 = new Firebase("//fir-learn-4991c.firebaseio.com/Users/"+user1);
        ValueEventListener valueEventListener12=new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<String> announceuser = new ArrayList<String>();
                ArrayList<String> socialuser = new ArrayList<String>();
                ArrayList<String> emailuser=new ArrayList<String>();

                //for (DataSnapshot child: dataSnapshot.getChildren()) {
                //DataSnapshot child = (DataSnapshot) dataSnapshot.getChildren();

                Users user=  dataSnapshot.getValue(Users.class);
                announceuser.add(user.getAnnounce());
                socialuser.add(user.getSocial());
                emailuser.add(user.getEmail());

                //}
                //Log.e("BOOLEAN ADMIN",""+adminuser[0]);
                announceuser1 = announceuser.get(0);
                socialuser1 = socialuser.get(0);
                finalannouncecount = announcecount - Integer.valueOf(announceuser1);

                finalsocialcount = socialcount - Integer.valueOf(socialuser1);
                Log.e("Final Social Count",""+socialuser1);
//                for(String str : announceuser){
//                    announceuser1 = str;
//                }
//                for(String str : socialuser){
//                    socialuser1 = str;
//                }
                //initializeBadge();
//                if(finalsocialcount != 0) {
//                    socialbadge.setGravity(Gravity.CENTER_VERTICAL);
//                    socialbadge.setTypeface(null, Typeface.BOLD);
//                    socialbadge.setTextColor(getResources().getColor(R.color.colorAccent));
//                    socialbadge.setText(String.valueOf(finalsocialcount));
//                }
//                else{
//                    socialbadge.setGravity(Gravity.CENTER_VERTICAL);
//                    socialbadge.setTypeface(null, Typeface.BOLD);
//                    socialbadge.setTextColor(getResources().getColor(R.color.colorAccent));
//                    socialbadge.setText("");
//                }


                if(finalannouncecount !=0 ){
                    announcebadge.setGravity(Gravity.CENTER_VERTICAL);
                    announcebadge.setTypeface(null,Typeface.BOLD);
                    announcebadge.setTextColor(getResources().getColor(R.color.colorAccent));
                    announcebadge.setText(String.valueOf(finalannouncecount));
                }
                else{
                    announcebadge.setGravity(Gravity.CENTER_VERTICAL);
                    announcebadge.setTypeface(null,Typeface.BOLD);
                    announcebadge.setTextColor(getResources().getColor(R.color.colorAccent));
                    announcebadge.setText("");
                }


            }


            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };
        hot12.addListenerForSingleValueEvent(valueEventListener12);


//            }
//        };
//        timer.schedule(timerTask, 30000, 30000);
        //Gravity property aligns the text

    }
}

