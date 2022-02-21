package com.example.photolang;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.navigation.ui.AppBarConfiguration;

//import com.example.photolang.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

  //  private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        //binding = ActivityMainBinding.inflate(getLayoutInflater());
        //setContentView(binding.getRoot());


       // BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
 //       NavController navController = Navigation.findNavController(this, R.id.nav_view);
       // NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        //NavigationUI.setupWithNavController(binding.nav_view, navController);
    }
    public void username_input(View view){
        TextView t = findViewById(R.id.username);
        String input = t.getText().toString();
        Log.d("info", input);

    }

    public void sendMessage(View view) {
        View b = findViewById(R.id.button);
        Button button = (Button) b;

    }
    public void registration(View view) {
        View b = findViewById(R.id.button2);
        Button button = (Button) b;
        Intent intent_login= new Intent(MainActivity.this, register.class);
        startActivity(intent_login);
    }

}