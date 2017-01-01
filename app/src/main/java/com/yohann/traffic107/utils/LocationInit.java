package com.yohann.traffic107.utils;

import android.content.Context;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.overlay.PoiOverlay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yohann on 2016/8/29.
 */
public class LocationInit {
    private Context context;
    private AMap aMap;

    public LocationInit(Context context, AMap aMap) {
        this.context = context;
        this.aMap = aMap;
    }

    public void init() {
        PoiItem poiItem = new PoiItem("id", new LatLonPoint(37.850181, 112.541711), "初始化位置", "太原市中心");
        List<PoiItem> list = new ArrayList<>();
        list.add(poiItem);
        PoiOverlay poiOverlay = new PoiOverlay(aMap, list);
        poiOverlay.addToMap();
        poiOverlay.zoomToSpan();
        poiOverlay.removeFromMap();
        aMap.moveCamera(CameraUpdateFactory.zoomTo(12));
    }
}
