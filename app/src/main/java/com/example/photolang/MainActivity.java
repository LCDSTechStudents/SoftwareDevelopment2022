package com.example.photolang;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.ui.AppBarConfiguration;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.photolang.databinding.LoginPageBinding;
import com.example.photolang.loginResponse.Root;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

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
        //Register Button
        Button button2 = (Button) findViewById(R.id.register);
        button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.d("you trigger the button", "haha ");
                startRegister(view);
            }
        });


        //Send button
        Button sendButton = (Button) findViewById(R.id.send);
        sendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("opening", "opened");

                TextView user = findViewById(R.id.username);
                TextView pass = findViewById(R.id.password);
                String user_input = user.getText().toString();
                String pass_input = pass.getText().toString();
                String user_text = user.getText().toString();
                String pass_text = pass.getText().toString();
                if (user_text.isEmpty() || pass_text.isEmpty()){
                    Log.d("you entered nothing","haha");
                    return;
                }

                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                String url ="http://www.lancastertsa.com:1002/v1/auth/login";

                JSONObject jsonBody = new JSONObject();
                jsonBody.put("email", user_input);
                jsonBody.put("password", pass_input);
                String requestBody = jsonBody.toString();
// Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.
                                Toast.makeText(MainActivity.this, "Response: " + response, Toast.LENGTH_LONG).show();
                                Log.d(response,"this is the response");
                                try {
                                    Root resp = JSON.parseObject(response, Root.class);
                                    Log.d(resp.data.toString(), "onResponse: ");
                                    if(resp.code == -1){
                                        Log.d(resp.data.toString(), "onResponse: code -1");
                                        openCameraActivity(view);
                                    }
                                }
                                finally{
                                    Log.d("On error", response.toString());
                                }

                            }
                        },
                        new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this,"Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                                NetworkResponse response = error.networkResponse;
                                if (error instanceof ServerError && response != null) {
                                    try {
                                        String res = new String(response.data,
                                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                        // Now you can use any deserializer to make sense of data
                                        JSONObject obj = new JSONObject(Boolean.parseBoolean(res));
                                    } catch (UnsupportedEncodingException e1) {
                                        // Couldn't properly decode data to string
                                        e1.printStackTrace();
                                    }
                                }
                            }

                        }){
                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }
                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        try {
                            Log.d("requestbody", requestBody);
                            return requestBody == null ? null : requestBody.getBytes("utf-8");
                        } catch (UnsupportedEncodingException uee) {
                            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                            return null;
                        }
                    }
                };
                queue.add(stringRequest);
                queue.start();


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
    public void openCameraActivity(View view){
        Intent intent = new Intent(this,cameraActivity.class);
        startActivity(intent);
    }

}