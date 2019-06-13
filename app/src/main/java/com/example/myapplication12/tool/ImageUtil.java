package com.example.myapplication12.tool;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.File;
import java.io.IOException;

import id.zelory.compressor.Compressor;

public class ImageUtil {

    private static final Bitmap.CompressFormat DEFAULT_COMPRESSFORMAT = Bitmap.CompressFormat.PNG;

    //Compress
    public static Bitmap getCompressBitmap(Context context, File imgFile) {
        Bitmap compressedImgBitmap = null;
        try {
            compressedImgBitmap = new Compressor(context)
                    .setCompressFormat(DEFAULT_COMPRESSFORMAT)
                    .compressToBitmap(imgFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return compressedImgBitmap;
    }

    public static File getCompressFile(Context context, File imgFile) {
        File compressedImgFile = null;
        try {
            compressedImgFile = new Compressor(context).compressToFile(imgFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return compressedImgFile;
    }

}
