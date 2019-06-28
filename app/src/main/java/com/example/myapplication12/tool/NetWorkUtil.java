package com.example.myapplication12.tool;

import android.content.Context;
import android.net.ConnectivityManager;

import java.io.IOException;
import java.net.InetAddress;

public class NetWorkUtil {

    public static boolean isNetworkConnected(Context context){
        ConnectivityManager manager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager.getActiveNetworkInfo()!=null){
            return manager.getActiveNetworkInfo().isAvailable();
        }
        return false;
    }

//    public static boolean isNodeReachable(String hostname){
//        try {
//            InetAddress address=InetAddress.getByName(hostname);
//            if (address.isReachable(3000)) {
//                return true;
//            }else {
//                return false;
//            }
//        } catch (IOException e) {
////            e.printStackTrace();
//            return false;
//        }
//    }

}
