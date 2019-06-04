package com.example.myapplication12.tool;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class IntentUtil {

    public static void sendIntent(Context context,Class clazz){
        Intent intent =new Intent(context,clazz);
        context.startActivity(intent);
    }

    public static void sendIntent(Context context, Class clazz, Bundle bundle){
        Intent intent=new Intent(context,clazz);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

}
