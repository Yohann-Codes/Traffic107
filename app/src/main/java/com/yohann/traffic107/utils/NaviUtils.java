package com.yohann.traffic107.utils;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.overlay.PoiOverlay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yohann on 2016/8/29.
 */
public class NaviUtils implements AMapLocationListener {
    private Context context;
    private AMap aMap;
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationClientOption;


    public NaviUtils(Context context, AMap aMap) {
        this.context = context;
        this.aMap = aMap;
    }

    public void navi() {
        locationClientOption = new AMapLocationClientOption();
        locationClient = new AMapLocationClient(context);
        locationClient.setLocationListener(this);
        locationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        locationClientOption.setInterval(2000);
        locationClientOption.setOnceLocation(true);
        locationClientOption.setNeedAddress(true);
        locationClientOption.setWifiActiveScan(true);
        locationClientOption.setMockEnable(false);
        locationClient.setLocationOption(locationClientOption);
        locationClient.startLocation();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        double latitude = aMapLocation.getLatitude();
        double longitude = aMapLocation.getLongitude();
        PoiItem poiItem = new PoiItem("id", new LatLonPoint(latitude, longitude), "我的位置", "");
        List<PoiItem> list = new ArrayList<>();
        list.add(poiItem);
        PoiOverlay poiOverlay = new PoiOverlay(aMap, list);
        poiOverlay.addToMap();
        poiOverlay.zoomToSpan();
    }
}
