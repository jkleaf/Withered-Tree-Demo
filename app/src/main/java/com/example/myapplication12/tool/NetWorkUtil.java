package com.example.myapplication12.tool;

import android.content.Context;
import android.net.ConnectivityManager;

public class NetWorkUtil {

    public static boolean isNetworkConnected(Context context){
        ConnectivityManager manager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager.getActiveNetworkInfo()!=null){
            return manager.getActiveNetworkInfo().isAvailable();
        }
        return false;
    }

}
