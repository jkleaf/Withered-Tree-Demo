package com.example.myapplication12.tool;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpUtil {

//    public static final int TIMEOUT=5000;
//
    public static final String ENCODE="UTT-8";
//
    private String urlString;

    private HttpUrl.Builder urlBuilder;

//    private HashMap<String,String> params;
//
//    private HashMap<String,String> headers;

    public OkHttpUtil(String urlString){
        this.urlString=urlString;
        urlBuilder =HttpUrl.parse(urlString).newBuilder();
    }

    public void setUrl(String url){
        this.urlString+=url;
    }

    public HttpStatus doGet(){
        Request request=new Request.Builder()
                .url(urlBuilder.build())
                .get()
                .build();
        try {
            Call call=new OkHttpClient().newCall(request);
            Response response=call.execute();
            return new HttpStatus(response.code(),response.body().string());
        } catch (IOException e) {
            return new HttpStatus(-1,e.getMessage());
        }
    }

    public HttpStatus doPost(RequestBody body){
        Request request=new Request.Builder()
                .url(urlBuilder.build())
                .post(body)
                .build();
        try {
            Response response= new OkHttpClient().newCall(request).execute();
            return new HttpStatus(response.code(),response.body().string());
        } catch (IOException e) {
            return new HttpStatus(-1,e.getMessage());
        }
    }

}
