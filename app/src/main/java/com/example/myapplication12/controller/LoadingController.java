package com.example.myapplication12.controller;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class LoadingController extends AsyncTask<Integer, Integer, String> {

    Context context;

    ProgressDialog progressDialog;

    public LoadingController() {}

    public LoadingController(Context context) {
        this.context=context;
    }

    @Override
    protected void onPreExecute() {
        progressDialog=new ProgressDialog(context);
//        progressDialog.setIcon();
//        progressDialog.setTitle("");
        progressDialog.setMessage("文件正在上传中...");
        progressDialog.setMax(100);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();
    }

    @Override
    protected String doInBackground(Integer... integers) {
        for(int i=integers[0];i<100;i++){//待修改
            publishProgress(i);
            try {
                Thread.sleep(100);//time
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return "文件上传完毕!";
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        progressDialog.setProgress(values[0]);
    }

    @Override
    protected void onPostExecute(String s) {//done
        super.onPostExecute(s);
        progressDialog.dismiss();
        //
        Toast.makeText(context,"上传成功!",Toast.LENGTH_SHORT).show();
    }
}
