package com.example.photolang;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

//import com.example.photolang.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private final static int REQUEST_ID = 123;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Intent pull = new Intent(this, LoginPages.class);
        //startActivity(pull);
        dispatchTakePictureIntent();

    }
    private void dispatchTakePictureIntent() {
        final Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, REQUEST_ID);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null && requestCode == REQUEST_ID) {
            Bitmap photo = data.getExtras().getParcelable("data");
            ImageView imageView = (ImageView) findViewById(R.id.imageView2);
            imageView.setImageBitmap(photo);
        }
    }

    //@Override
//    protected void onResume() {
//        mIsResumed = true;
//        super.onResume();
//    }
//    @Override
//    protected void onPause() {
//       mIsResumed = false;
//       super.onPause();
//    }


}