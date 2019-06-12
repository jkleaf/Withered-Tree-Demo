package com.example.myapplication12.tool;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpUtil {

//    public static final int TIMEOUT=5000;
//
    public static final String ENCODE="UTT-8";
//
    private String urlString;

//    private HashMap<String,String> params;
//
//    private HashMap<String,String> headers;

    public OkHttpUtil(String urlString){
        this.urlString=urlString;
    }

    public HttpStatus doGet(){
        HttpUrl.Builder urlBuilder =HttpUrl.parse(urlString).newBuilder();
        Request request=new Request.Builder()
                .url(urlBuilder.build())
                .get()
                .build();
        Call call=new OkHttpClient().newCall(request);
        try {
            Response response=call.execute();
            return new HttpStatus(response.code(),response.body().string());
        } catch (IOException e) {
            return new HttpStatus(-1,e.getMessage());
        }
    }

    public void doPost(){

    }

}
