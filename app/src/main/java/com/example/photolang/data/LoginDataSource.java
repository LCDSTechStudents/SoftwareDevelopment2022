package com.example.photolang.data;

import android.content.Context;
import android.os.NetworkOnMainThreadException;
import android.telecom.Call;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.photolang.data.model.LoggedInUser;
import com.example.photolang.loginResponse.LoginRequest;
import com.example.photolang.loginResponse.ResponseRoot;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {
    private boolean ok;

    public Result<LoggedInUser> login(String username, String password, RequestQueue queue) {
        final Result[] result = new Result[1];
        try {

            // .url("http://www.lancastertsa.com:1002/v1/auth/login")
            // TODO: handle loggedInUser authentication
            StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.lancastertsa.com:1002/v1/auth/login",
                    new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //parse response
                    ResponseRoot responseRoot = JSON.parseObject(response, ResponseRoot.class);
                    //get token
                    switch (responseRoot.getCode()) {
                        case 0:
                            //success
                            setOk(true);
                            result[0] = new Result.Success<>(new LoggedInUser(responseRoot.getData().getId(),responseRoot.getData().getNickname(), responseRoot.getData().getToken()));
                        case -1:
                            setOk(true);
                            result[0] = new Result.Error(new IOException("登录失败"));
                        default:
                            setOk(true);
                            throw new IllegalStateException("Unexpected value: " + responseRoot.getCode());
                    }
                }


            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("errorMSG", error.toString());
                    setOk(true);
                    result[0] = new Result.Error(new IOException("Error logging in", error));
                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> map = new HashMap<String,String>();
                    map.put("username",username);
                    map.put("password",password);
                    return map;

                }
            };
            queue.add(stringRequest);
        } catch (NetworkOnMainThreadException e) {
            Log.e("LoginDataSource", e.toString());
            result[0] = new Result.Error(new IOException("Error logging in", e));
        }finally{
            awaitForOk();
            return result[0];
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }

    public void awaitForOk(){
        while(true){
            if (ok){
                return;
            }
        }
    }
    public void setOk(boolean ok){
        this.ok = ok;
    }
}