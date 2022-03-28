package com.example.photolang;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class cameraActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView Photo_Taken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Photo_Taken = findViewById(R.id.picture);
        //start camera activity and get image
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
    }
        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                ImageView view = new ImageView(this);
                view.setImageBitmap(photo);
               // Photo_Taken.setImageBitmap(photo);//set image to imageview
            }
    }

}