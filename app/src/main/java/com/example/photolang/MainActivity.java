package com.example.photolang;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuItem;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import androidx.navigation.ui.AppBarConfiguration;


import com.example.photolang.databinding.ActivityMainBinding;
import com.example.photolang.ui.login.LoginActivity;

import com.example.photolang.R;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView navView;

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;



    private Fragment currentFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        Intent login = new Intent(this, LoginActivity.class);
        setContentView(binding.getRoot());
        initFragments(savedInstanceState);
//        NavController navController = Navigation.findNavController(this, R.id.);
//        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.bottomNavigation.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.page_1) {

                }
                if (item.getItemId() == R.id.page_2) {


                }
                if (item.getItemId() == R.id.page_3) {
                    startActivity(login);
                }
                return false;
            }


        });
    }
    public void initFragments(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

        }
    }

    public void switchFragment(Fragment from, Fragment to) {
        if (currentFragment != to) {
            currentFragment = to;
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            if (!to.isAdded()) {

            }else{
                ft.hide(from).show(to).commit();

            }
        }
    }
        //startActivity(login);


//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
//        return NavigationUI.navigateUp(navController, appBarConfiguration)
//                || super.onSupportNavigateUp();
//    }
}