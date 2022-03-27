package com.example.photolang;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

//import com.example.photolang.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private boolean mIsResumed = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent pull = new Intent(this, LoginPages.class);
        startActivity(pull);
    }


    //@Override
//    protected void onResume() {
//        mIsResumed = true;
//        super.onResume();
//    }
    @Override
    protected void onPause() {
       mIsResumed = false;
       super.onPause();
    }


}