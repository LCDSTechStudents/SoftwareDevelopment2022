package com.example.photolang;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.photolang.loginResponse.Root;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button button2 = (Button) findViewById(R.id.cameraButton);
        button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.d("you trigger the button", "haha ");
                TextView user = findViewById(R.id.newUsername);
                TextView pass = findViewById(R.id.newPassword);
                String user_text = user.getText().toString();
                String pass_text = pass.getText().toString();
                if (user_text.isEmpty() || pass_text.isEmpty()) {
                    Log.d("you entered nothing", "haha");
                    return;
                }
                else {
                    RequestQueue queue = Volley.newRequestQueue(register.this);
                    String url = "http://www.lancastertsa.com:1002/v1/auth/login";

// Request a string response from the provided URL.
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    // Display the first 500 characters of the response string.
                                    Toast.makeText(getBaseContext(), "Response: " + response, Toast.LENGTH_LONG).show();
                                    Log.d(response, "this is the response");
                                    try {
                                        Root resp = JSON.parseObject(response, Root.class);
                                        Log.d(resp.data.toString(), "onResponse: ");
                                        if (resp.code == -1) {
                                            Log.d(resp.data.toString(), "onResponse: code -1");
                                        }
                                    } finally {
                                        Log.d("On error", response.toString());
                                    }

                                }
                            },
                            new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(register.this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
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
                            }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> paramV = new HashMap<>();
                            paramV.put(user_text, pass_text);
                            return paramV;
                        }
                    };
                    queue.add(stringRequest);
                    queue.start();
                    startRegister(view);
                }
            }
        });

    }

    public void startRegister(View view){
        Intent intent = new Intent(this, cameraActivity.class);
        startActivity(intent);
    }
}