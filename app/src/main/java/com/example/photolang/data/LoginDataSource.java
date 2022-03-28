package com.example.photolang.data;

import android.os.NetworkOnMainThreadException;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.example.photolang.data.model.LoggedInUser;
import com.example.photolang.loginResponse.LoginRequest;
import com.example.photolang.loginResponse.ResponseRoot;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password) {
        final Result[] result = new Result[1];
        try {
            // TODO: handle loggedInUser authentication
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            LoginRequest loginRequest = new LoginRequest(username, password);
            RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(loginRequest));
            Request request = new Request.Builder()
                    .url("http://www.lancastertsa.com:1002/v1/auth/login")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    try (ResponseBody responseBody = response.body()) {
                        if (!response.isSuccessful())
                            throw new IOException("Unexpected code " + response);

                        Headers responseHeaders = response.headers();
                        for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                            System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                        }
                        ResponseRoot json = JSON.parseObject(response.body().string(), ResponseRoot.class);
                        if (json.getCode() == 0) {
                            //int to string
                            LoggedInUser user = new LoggedInUser(json.getData().getId(), json.getData().getNickname(), json.getData().getToken());
                            result[0] = new Result.Success<>(user);
                            return;
                        }
                        result[0] = new Result.Error(new Exception("Login failed"), json.status);
                    }
                }
            });
        } catch (NetworkOnMainThreadException e) {
            Log.e("LoginDataSource", e.toString());
            return new Result.Error(new IOException("Error logging in", e));
        }
        return result[0];
    }

    public void logout() {
        // TODO: revoke authentication
    }
}