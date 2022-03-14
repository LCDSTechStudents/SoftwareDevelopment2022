package com.example.photolang;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.navigation.ui.AppBarConfiguration;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.photolang.databinding.LoginPageBinding;

import org.json.JSONException;
import org.json.JSONObject;

//import com.example.photolang.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
   // private Object Loginpage;

    //  private ActivityMainBinding binding;
    LoginPageBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = LoginPageBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(binding.getRoot());

        Button button2 = (Button) findViewById(R.id.register);
        button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.d("you trigger the button", "haha ");
                startRegister(view);
            }
        });
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
    public void startRegister(View view){
        Intent intent = new Intent(this, register.class);
        startActivity(intent);
    }

}