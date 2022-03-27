package com.example.photolang;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class cameraActivity extends AppCompatActivity {
    private static final int Camera_Action_Code = 1;
    ImageView Photo_Taken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Photo_Taken = findViewById(R.id.picture);
        Intent intent =new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, Camera_Action_Code);
        setContentView(R.layout.activity_camera);


     /* Button card1 = findViewById(R.id.Card1);
      card1.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Intent intent = new Intent(cameraActivity.this, FirstFragment.class);
          }
      });*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       if (requestCode == Camera_Action_Code && resultCode == RESULT_OK){
           Bundle bundle = data.getExtras();
           Bitmap photo = (Bitmap) bundle.get(data.toString());
           Photo_Taken.setImageBitmap(photo);
       }

    }


}