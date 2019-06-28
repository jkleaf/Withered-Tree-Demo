package com.example.myapplication12.function.map.regeocoder;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.example.myapplication12.R;
import com.example.myapplication12.util.AMapUtil;
import com.example.myapplication12.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 地理编码与逆地理编码功能介绍
 */
public class ReGeocoderActivity extends Activity implements
        GeocodeSearch.OnGeocodeSearchListener, View.OnClickListener, AMap.OnMarkerClickListener {
    private ProgressDialog progDialog = null;
    private GeocodeSearch geocoderSearch;
    private String addressName;
    private AMap aMap;
    private MapView mapView;
    private LatLonPoint latLonPoint;
    private Marker regeoMarker;
    private double longitude = 0;
    private double latitude = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.geocoder_activity);
        /*
         * 设置离线地图存储目录，在下载离线地图或初始化地图设置;
         * 使用过程中可自行设置, 若自行设置了离线地图存储的路径，
         * 则需要在离线地图下载和使用地图页面都进行路径设置
         * */
        //Demo中为了其他界面可以使用下载的离线地图，使用默认位置存储，屏蔽了自定义设置
//        MapsInitializer.sdcardDir =OffLineMapUtils.getSdCacheDir(this);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        init();
    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            regeoMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            aMap.setOnMarkerClickListener(this);
        }

        Button reverseButton = (Button) findViewById(R.id.loc);
        reverseButton.setOnClickListener(this);

        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
        progDialog = new ProgressDialog(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /**
             * 响应逆地理编码按钮
             */
            case R.id.loc: {
                EditText Longitude = (EditText) findViewById(R.id.longitude_tv);
                EditText Latitude = (EditText) findViewById(R.id.latitude);
                longitude = Double.valueOf(Longitude.getText().toString());
                latitude = Double.valueOf(Latitude.getText().toString());
                latLonPoint = new LatLonPoint(longitude, latitude);
                getAddress(latLonPoint);
                break;
            }
            default:
                break;
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    /**
     * 显示进度条对话框
     */
    public void showDialog() {
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(true);
        progDialog.setMessage("正在获取地址");
        progDialog.show();
    }

    /**
     * 隐藏进度条对话框
     */
    public void dismissDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }

    /**
     * 响应逆地理编码
     */
    public void getAddress(LatLonPoint latLonPoint) {
        showDialog();
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
                GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        geocoderSearch.getFromLocationAsyn(query);// 设置异步逆地理编码请求
    }

    /**
     * 地理编码查询回调
     */
    @Override
    public void onGeocodeSearched(GeocodeResult result, int rCode) {
    }

    /**
     * 逆地理编码回调
     */
    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        dismissDialog();
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null) {
                addressName = result.getRegeocodeAddress().getFormatAddress()
                        + "附近";
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        AMapUtil.convertToLatLng(latLonPoint), 15));
                regeoMarker.setPosition(AMapUtil.convertToLatLng(latLonPoint));
                ToastUtil.show(ReGeocoderActivity.this, addressName);
            } else {
                ToastUtil.show(ReGeocoderActivity.this, R.string.no_result);
            }
        } else {
            ToastUtil.showerror(this, rCode);
        }
    }

    private Handler msgHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            ToastUtil.showerror(ReGeocoderActivity.this, msg.arg1);
        }
    };

    private List<LatLonPoint> readLatLonPoints() {
        List<LatLonPoint> points = new ArrayList<LatLonPoint>();
        for (int i = 0; i < coords.length; i += 2) {
            points.add(new LatLonPoint(coords[i + 1], coords[i]));
        }
        return points;
    }

    private double[] coords = {116.339925, 39.976587,
            116.328467, 39.976357,
            116.345289, 39.966556,
    };

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return false;
    }
}


