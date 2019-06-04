package com.example.myapplication12.tool;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.util.Log;

import java.io.IOException;
import java.util.List;

public class LocateUtil {

    public static Location exif2Loc(String flNm) {//文件内部信息
        String sLat = "", sLatR = "", sLon = "", sLonR = "";
        try {
            ExifInterface ef = new ExifInterface(flNm);
            sLat  = ef.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
            sLon  = ef.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
            sLatR = ef.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
            sLonR = ef.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
        } catch (IOException e) {return null;}

        double lat = dms2Dbl(sLat);
        if (lat > 180.0) return null;
        double lon = dms2Dbl(sLon);
        if (lon > 180.0) return null;

        lat = sLatR.contains("S") ? -lat : lat;
        lon = sLonR.contains("W") ? -lon : lon;

        Location loc = new Location("exif");
        loc.setLatitude(lat);
        loc.setLongitude(lon);
        return loc;
    }

    private static double dms2Dbl(String sDMS){
        double dRV = 999.0;
        try {
            String[] DMSs = sDMS.split(",", 3);
            String s[] = DMSs[0].split("/", 2);
            dRV = (new Double(s[0])/new Double(s[1]));
            s = DMSs[1].split("/", 2);
            dRV += ((new Double(s[0])/new Double(s[1]))/60);
            s = DMSs[2].split("/", 2);
            dRV += ((new Double(s[0])/new Double(s[1]))/3600);
        } catch (Exception e) {}
        return dRV;
    }

    public static Location getLocFromSys(Context context){
        LocationManager locationManager=((LocationManager) context.getSystemService(Context.LOCATION_SERVICE));/////////////Fuck it!!!
        String locationProvider=null;
        List<String> providers = locationManager.getProviders( true );
        if (providers.contains( LocationManager.NETWORK_PROVIDER )) {
            Log.i( "TAG", "网络定位" );
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else if (providers.contains( LocationManager.GPS_PROVIDER )) {
            Log.i( "TAG", "GPS定位" );
            locationProvider = LocationManager.GPS_PROVIDER;
        }
        @SuppressLint("MissingPermission") Location location = locationManager.getLastKnownLocation(locationProvider);/////////
        return location;
    }

//    LocationListener locationListener = new LocationListener() {//位置监听
//
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle arg2) {
//
//        }
//
//        @Override
//        public void onProviderEnabled(String provider) {
//
//        }
//
//        @Override
//        public void onProviderDisabled(String provider) {
//
//        }
//
//        @Override
//        public void onLocationChanged(Location location) {
//            location.getAccuracy();
////            setLocation( location );
//        }
//    };

}
