package com.example.photolang;

import android.content.Intent;
import android.os.Bundle;

import com.example.photolang.ui.profile.ProfileFragment;
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

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView navView;

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;


    private FlashcardsFragment flashcardsFragment;
    private ScanFragment scanFragment;
    private ProfileFragment profileFragment;

    private Fragment currentFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        Intent login = new Intent(this, LoginActivity.class);
        setContentView(binding.getRoot());



        initFragments(savedInstanceState);
        navView = findViewById(R.id.bottom_navigation);
        navView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.page_1) {
                    if (scanFragment == null) {
                        scanFragment = ScanFragment.newInstance();
                    }
                    switchFragment(currentFragment, scanFragment);
                    return true;
                }
                if (item.getItemId() == R.id.page_2) {
                    if (flashcardsFragment == null) {
                        flashcardsFragment = FlashcardsFragment.newInstance();
                    }
                    switchFragment(currentFragment, flashcardsFragment);
                    return true;
                }
                if (item.getItemId() == R.id.page_3) {
                    if (profileFragment == null) {
                        profileFragment = ProfileFragment.newInstance();
                    }
                    switchFragment(currentFragment, profileFragment);
                    return true;
                }
                return false;
            }


        });
    }

    public void initFragments(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            scanFragment = ScanFragment.newInstance();
            currentFragment = scanFragment;
            ft.add(R.id.fragment_container, scanFragment).commit();
        }
    }

    public void switchFragment(Fragment from, Fragment to) {
        if (currentFragment != to) {
            currentFragment = to;
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            if (!to.isAdded()) {
                ft.hide(from).add(R.id.fragment_container, to).commit();
            } else {
                ft.hide(from).show(to).commit();
            }
        }
    }



}