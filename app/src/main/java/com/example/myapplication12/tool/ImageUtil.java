package com.example.myapplication12.tool;

import android.graphics.Bitmap;
import android.util.Log;

public class ImageUtil {

    public static Bitmap getTranslateImage(Bitmap bitma, String Lng, String Lat)//alpha 0 - 255  左上角两个单位  右下角两个单位嵌入
    {
        Bitmap bitmap = bitma.copy(Bitmap.Config.ARGB_8888, true);
        boolean flag_lng = true;
        boolean flag_lat = true;
        boolean flag_lng_decimal = false;
        boolean flag_lat_decimal =false;
        int h = bitmap.getHeight();
        if (Lng.indexOf(0) == '-') {
            flag_lng = false;

        }
        if (Lat.indexOf(0) == '-') {
            flag_lat = false;
        }
        int n = 0;
        String str1, str2;
        if(Lng.indexOf(".")!=-1){
            flag_lng_decimal=true;
            str1 = Lng.substring(Lng.indexOf(".") + 1);//小数点后
        }
        else
            str1=null;

        while (n < 2) {
            int p = bitmap.getPixel(n, 0);
            int a, r, g, b;
            if (!flag_lng) {
                if(flag_lng_decimal)
                    str2 = Lng.substring(1, Lng.indexOf("."));//整数部分
                else str2 =Lng;
                a = (p >> 24) & 0xff;
                r = Integer.parseInt(str2);
                g = 255;
                b = 255;
                flag_lng = true;
            } else if (n == 0 && flag_lng) {
                if(flag_lng_decimal)
                    str2 = Lng.substring(0, Lng.indexOf("."));//整数部分
                else str2 = Lng;
                a = (p >> 24) & 0xff;
                r = Integer.parseInt(str2);
                g = 0;
                b = 0;
            } else {
                a = (p >> 24) & 0xff;
                if(str1!=null){
                    r = Integer.parseInt(str1.substring(0, 2));
                    g = Integer.parseInt(str1.substring(2, 4));
                    b = Integer.parseInt(str1.substring(4, 6));}
                else{
                    r=0;
                    g=0;
                    b=0;
                }
            }
            int rgb = (a << 24) | (r << 16) | (g << 8) | b;
            bitmap.setPixel(n, 0, rgb);
            n++;
        }
        n = 0;
        if(Lat.indexOf(".")!=-1) {
            flag_lat_decimal=true;
            str1 = Lat.substring(Lat.indexOf(".") + 1);//小数点后
        }
        else
            str1=null;

        while (n < 2) {
            int p = bitmap.getPixel(n, bitmap.getHeight() - 1);
            int a, r, g, b;
            if (!flag_lat) {
                if(flag_lat_decimal)
                    str2 = Lat.substring(1, Lat.indexOf("."));//整数部分
                else str2=Lat;
                a = (p >> 24) & 0xff;
                r = Integer.parseInt(str2);
                g = 255;
                b = 255;
                flag_lat = true;
            } else if (n == 0 && flag_lat) {
                if(flag_lat_decimal)
                    str2 = Lat.substring(0, Lat.indexOf("."));//整数部分
                else str2=Lat;
                a = (p >> 24) & 0xff;
                r = Integer.parseInt(str2);
                g = 0;
                b = 0;
            } else {
                a = (p >> 24) & 0xff;
                if(str1!=null) {
                    r = Integer.parseInt(str1.substring(0, 2));
                    g = Integer.parseInt(str1.substring(2, 4));
                    b = Integer.parseInt(str1.substring(4, 6));
                }
                else{
                    r=0;
                    g=0;
                    b=0;
                }
            }
            int rgb = (a << 24) | (r << 16) | (g << 8) | b;
            bitmap.setPixel(n, bitmap.getHeight() - 1, rgb);
            n++;
        }
        return bitmap;
    }

    public static String getLng(Bitmap bitmap) {
        String Lng;
        int p = bitmap.getPixel(0, 0);
        Integer r = (p >> 16) & 0xff;
        Integer g = (p >> 8) & 0xff;
        Integer b = p & 0xff;
        if (g == 255 && b == 255) {
            Lng = "-" + r.toString();
        } else {
            Lng = r.toString();
        }
        p = bitmap.getPixel(1, 0);

        r = (p >> 16) & 0xff;
        g = (p >> 8) & 0xff;
        b = p & 0xff;
        Lng = Lng + "." + (r>10?r.toString():"0"+r.toString()) + (g>10?g.toString():"0"+g.toString()) + (b>10?b.toString():"0"+b.toString());
        return Lng;
    }

    public static String getLat(Bitmap bitmap) {
        String Lat;
        int p = bitmap.getPixel(0, bitmap.getHeight()-1);
        Integer r = (p >> 16) & 0xff;
        Integer g = (p >> 8) & 0xff;
        Integer b = p & 0xff;
        if (g == 255 && b == 255) {
            Lat = "-" + r.toString();
        } else {
            Lat = r.toString();
        }
        p = bitmap.getPixel(1, bitmap.getHeight() - 1);


        r = (p >> 16) & 0xff;
        g = (p >> 8) & 0xff;
        b = p & 0xff;
        Lat = Lat + "." + (r>10?r.toString():"0"+r.toString()) + (g>10?g.toString():"0"+g.toString()) + (b>10?b.toString():"0"+b.toString());
        return Lat;
    }

}
