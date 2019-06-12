package com.example.myapplication12.tool;

public class HttpStatus {

    private int statusCode;

    private String responseAns;

    public HttpStatus(int state,String ans){
        this.statusCode=state;
        this.responseAns=ans;
    }

    public int getStatus(){
        return statusCode;
    }

    public String getResponseAns(){
        return responseAns;
    }
}
