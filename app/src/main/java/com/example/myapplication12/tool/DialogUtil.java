package com.example.myapplication12.tool;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;

public class DialogUtil {

    public static Dialog showNormalDialog(Context context, String title, String message,
            String positiveBtnName, String negativeBtnName, OnClickListener positiveBtnListener,OnClickListener negativeBtnListener) {
        Dialog normalDialog=null;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        normalDialog.setIcon(R.drawable.icon_dialog);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positiveBtnName, positiveBtnListener);
        builder.setNegativeButton(negativeBtnName, negativeBtnListener);
        normalDialog=builder.create();
        return normalDialog;
    }

}
