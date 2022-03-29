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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.photolang.data.model.LoggedInUser;
import com.example.photolang.loginResponse.LoginRequest;
import com.example.photolang.loginResponse.ResponseRoot;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;


/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {
    private boolean ok;

    public Result<LoggedInUser> login(String username, String password, RequestQueue queue) {

        JSONObject req = new JSONObject();
        try {
            req.put("username", username);
            req.put("password", password);
        }catch (JSONException e){
            throw new RuntimeException(e);
        }
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        // TODO: handle loggedInUser authentication
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "http://www.lancastertsa.com:1002/v1/auth/login", req, null, future);
        queue.add(request);
        try {
            JSONObject response = future.get();
            String code = response.getString("code");
            switch (code){
                case "0":
                    JSONObject data = response.getJSONObject("Data");
                    return new Result.Success<>(new LoggedInUser(data.getInt("id"),data.getString("nickname"),data.getString("token")));
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return new Result.Error(new IOException("Error logging in"));
    }

    public void logout() {
        // TODO: revoke authentication
    }




}