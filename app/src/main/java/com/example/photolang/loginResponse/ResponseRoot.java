package com.example.photolang.loginResponse;

public class ResponseRoot {
    public int code;
    public String status;
    public ResponseData data;
    //getters
    public int getCode(){
        return code;
    }
    public String getStatus(){
        return status;
    }
    public ResponseData getData(){
        return data;
    }
}
