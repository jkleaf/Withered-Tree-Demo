package com.example.myapplication12.tool;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.myapplication12.tool.Constant.DEMO;
import static com.example.myapplication12.tool.Constant.TREEIMG;

public class FileUtil {

    private static String rootPath;

    public static void setRootPath(Context context) {
        rootPath = context.getExternalFilesDir(DEMO).getPath();
    }

    public static boolean fileExists(String dir) {
        File file = getFilePath(dir);
        return file.exists();
    }

    public static boolean mkDir(String dir) {
        File file = getFilePath(dir);
        return file.mkdirs();
    }

    public static File getFilePath(String dir) {
        return new File(rootPath + dir);
    }

    public static boolean createFile(String dir, String filename) {
        try {
            return getFilePath(dir + File.separator + filename).createNewFile();
        } catch (IOException e) {
            return false;
        }
    }

    public static String setFileNameWithDate(String fileName) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "PNG_" + timeStamp + "_";//PNG无损
        return imageFileName + fileName;
    }

    public static String getPhotoPathName(){
        return getFilePath(TREEIMG) + File.separator + setFileNameWithDate(TREEIMG) + ".png";
    }

    public static void deleteFile(String pathName){
        new File(pathName).delete();
    }

    public static String getRootPath() {
        return rootPath;
    }
}
