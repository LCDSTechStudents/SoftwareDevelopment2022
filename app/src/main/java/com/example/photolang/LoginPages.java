package com.example.photolang;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.photolang.loginResponse.Root;

import java.util.HashMap;
import java.util.Map;

public class LoginPages extends AppCompatActivity {

    public void notification(View v){
        Log.d("you trigger the button", "haha ");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        //  final TextView textView = (TextView) findViewById(R.id.SendResponse);

        TextView user = findViewById(R.id.username);
        TextView pass = findViewById(R.id.password);
        String user_input = user.getText().toString();
        String pass_input = pass.getText().toString();
        //For Send button
        //Button button = (Button) findViewById(R.id.send);
        /*button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("opening", "opened");
               String user_text = user.getText().toString();
               String pass_text = pass.getText().toString();
               if (user_text.isEmpty() || pass_text.isEmpty()){
                   return;
               }

                RequestQueue queue = Volley.newRequestQueue(LoginPages.this);
                String url ="http://www.lancastertsa.com:1002/v1/auth/login";

// Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.
                              Toast.makeText(LoginPages.this, "Response: " + response, Toast.LENGTH_LONG).show();
                              try {
                                  Root resp = JSON.parseObject(response, Root.class);
                                  Log.d(resp.data.toString(), "onResponse: ");
                                  if(resp.code == -1){
                                      Log.d(resp.data.toString(), "onResponse: code -1");
                                  }
                              }catch (JSONException e){
                                  //switch e here
                                  Log.d("Parsing error", e.toString());
                              }finally{
                                  Log.d("On error", response.toString());
                              }

                            }
                        },
                        new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(LoginPages.this,"Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }){
                    @Override
                    protected Map<String, String> getParams(){
                        Map<String, String> paramV = new HashMap<>();
                        paramV.put(user_input, pass_input);
                        return paramV;
                    }
                };
               queue.add(stringRequest);
               queue.start();

            //    opencameraActivity();
            }
        });*/

        //Second button
        Button button2 = (Button) findViewById(R.id.register);
        button2.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Log.d("you trigger the button", "haha ");
            }
        });
    }

}
