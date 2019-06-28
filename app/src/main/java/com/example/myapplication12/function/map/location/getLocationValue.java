package com.example.myapplication12.function.map.location;

import android.app.Activity;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.myapplication12.function.map.route.WalkRouteActivity;

import static com.example.myapplication12.util.ToastUtil.TAG;

public class getLocationValue extends Activity {
    AMapLocationClient mlocationClient=null;
    AMapLocationClientOption mLocationOption=null;

    public void startLocation()
    {
        mlocationClient = new AMapLocationClient(getApplicationContext());
        mlocationClient.setLocationListener(mLocationListener);

        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);

        //给定位客户端对象设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        //启动定位
        mlocationClient.startLocation();
    }

    //声明定位回调监听器
    public  AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation !=null ) {
                if (amapLocation.getErrorCode() == 0) {
                    //定位成功回调信息，设置相关消息
                    WalkRouteActivity.startlo=amapLocation.getLongitude();
                    WalkRouteActivity.startla=amapLocation.getLatitude();
                    Log.i(TAG,"当前定位结果来源-----"+amapLocation.getLocationType());//获取当前定位结果来源，如网络定位结果，详见定位类型表
                    Log.i(TAG,"纬度 ----------------"+amapLocation.getLatitude());//获取纬度
                    Log.i(TAG,"经度-----------------"+amapLocation.getLongitude());//获取经度
                    Log.i(TAG,"精度信息-------------"+amapLocation.getAccuracy());//获取精度信息
                    Log.i(TAG,"地址-----------------"+amapLocation.getAddress());//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                    Log.i(TAG,"国家信息-------------"+amapLocation.getCountry());//国家信息
                    Log.i(TAG,"省信息---------------"+amapLocation.getProvince());//省信息
                    Log.i(TAG,"城市信息-------------"+amapLocation.getCity());//城市信息
                    Log.i(TAG,"城区信息-------------"+amapLocation.getDistrict());//城区信息
                    Log.i(TAG,"街道信息-------------"+amapLocation.getStreet());//街道信息
                    Log.i(TAG,"街道门牌号信息-------"+amapLocation.getStreetNum());//街道门牌号信息
                    Log.i(TAG,"城市编码-------------"+amapLocation.getCityCode());//城市编码
                    Log.i(TAG,"地区编码-------------"+amapLocation.getAdCode());//地区编码
                    Log.i(TAG,"当前定位点的信息-----"+amapLocation.getAoiName());//获取当前定位点的AOI信息
                } else {
                    //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        }
    };
}
