package com.yohann.traffic107.utils;

import android.content.Context;
import android.util.Log;

import com.amap.api.maps.AMap;
import com.amap.api.maps.overlay.DrivingRouteOverlay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;

import java.util.List;

/**
 * Created by Yohann on 2016/8/27.
 */
public class MyRouteSearch implements RouteSearch.OnRouteSearchListener {
    private static final String TAG = "PathUtilsInfo";
    private Context context;
    private AMap aMap;

    public MyRouteSearch(Context context, AMap aMap) {
        this.context = context;
        this.aMap = aMap;
    }

    public void planPath(LatLonPoint from, LatLonPoint to, List<List<LatLonPoint>> avoid) {
        RouteSearch routeSearch = new RouteSearch(context);
        routeSearch.setRouteSearchListener(this);
        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(from, to);
        RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, RouteSearch.DrivingDefault, null, avoid, "");
        routeSearch.calculateDriveRouteAsyn(query);
    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {
        Log.i(TAG, "onDriveRouteSearched: 触发了路径规划监听器");
        List<DrivePath> paths = driveRouteResult.getPaths();
        DrivePath drivePath = paths.get(0);
        DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(context, aMap, drivePath,
                driveRouteResult.getStartPos(), driveRouteResult.getTargetPos());
        drivingRouteOverlay.addToMap();
        drivingRouteOverlay.setNodeIconVisibility(false);
        drivingRouteOverlay.zoomToSpan();
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }
}
