package creations.appmaking.thsvhsapp;

/**
 *Copyright © 2017 Sriharsha Singam. All rights reserved.
 **/


import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.Manifest;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Random;

public class map extends AppCompatActivity {

    DownloadManager downloadManager;
    private long myDownloadReference;
    private BroadcastReceiver receiverDownloadComplete;
    Uri uri1;
    private BroadcastReceiver receiverNotificationClicked;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ActivityCompat.requestPermissions(map.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2909);

        ImageView iv=(ImageView)findViewById(R.id.imageView3);
        //iv.setImageResource(R.drawable.map);
        Drawable myDrawable = getResources().getDrawable(R.drawable.map);
        anImage      = ((BitmapDrawable) myDrawable).getBitmap();
        Button map  = (Button)findViewById(R.id.button2);

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(map.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2909);
                downloadFile();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.thsthe_app, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.action_settings) {
            ActivityCompat.requestPermissions(map.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2909);
            downloadFile();


            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    Bitmap anImage;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 2909: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("Permission", "Granted");
                } else {
                    Log.e("Permission", "Denied");
                }
                return;
            }
        }
    }

    private void saveImageToExternalStorage(Bitmap finalBitmap) {
        MediaStore.Images.Media.insertImage(this.getContentResolver(), finalBitmap,"THSMap", "Hope This Helps");


    }
    public void downloadFile() {
        saveImageToExternalStorage(anImage);


    }
}
