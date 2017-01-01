package com.yohann.traffic107.user.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.yohann.traffic107.R;
import com.yohann.traffic107.user.activity.EditActivity;
import com.yohann.traffic107.user.activity.HomeActivity;
import com.yohann.traffic107.utils.BmobUtils;
import com.yohann.traffic107.utils.LocationInit;
import com.yohann.traffic107.utils.NetUtils;
import com.yohann.traffic107.utils.ViewUtils;

import java.util.ArrayList;

/**
 * Created by Yohann on 2016/8/28.
 */
public class AddFragment extends Fragment {
    private static final String TAG = "AddFragmentInfo";

    private HomeActivity activity;
    private ImageView ivMenu;
    private MapView mapView;
    private AMap aMap;
    private UiSettings uiSettings;
    private RadioButton rbStart;
    private RadioButton rbEnd;
    private ImageView ivFinishAdd;
    private NetUtils netUtils;

    private Double startLongitude;
    private Double endLongitude;
    private Double startLatitude;
    private Double endLatitude;

    private Marker startMarker;
    private Marker endMarker;

    ArrayList<Marker> markerStartList = new ArrayList<>();
    ArrayList<Marker> markerEndList = new ArrayList<>();
    private LocationInit locationInit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (HomeActivity) getActivity();
        BmobUtils.init(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, null);
        init(view);
        mapView.onCreate(savedInstanceState);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 初始化View
     *
     * @param view
     */
    private void init(View view) {
        ivMenu = (ImageView) view.findViewById(R.id.iv_menu_add);
        mapView = (MapView) view.findViewById(R.id.map_add);
        rbStart = (RadioButton) view.findViewById(R.id.rbtn_start_add);
        rbEnd = (RadioButton) view.findViewById(R.id.rbtn_end_add);
        ivFinishAdd = (ImageView) view.findViewById(R.id.iv_finish_add);
        aMap = mapView.getMap();
        uiSettings = aMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(false);
        netUtils = new NetUtils(activity, aMap);
        locationInit = new LocationInit(activity, aMap);
        locationInit.init();

        ivFinishAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startLongitude == null || startLatitude == null || endLongitude == null || endLatitude == null) {
                    ViewUtils.show(activity, "请选择起始点和终止点");
                } else {
                    Intent intent = new Intent(activity, EditActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putDouble("startLongitude", startLongitude);
                    bundle.putDouble("startLatitude", startLatitude);
                    bundle.putDouble("endLongitude", endLongitude);
                    bundle.putDouble("endLatitude", endLatitude);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

        aMap.setOnMapLongClickListener(new AMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions();

                //起始点
                if (rbStart.isChecked()) {
                    startLongitude = latLng.longitude;
                    startLatitude = latLng.latitude;

                    netUtils.initMarker(latLng, R.layout.marker_start_layout, markerOptions);
                    if (markerStartList.size() == 0) {
                        //第一次添加
                        startMarker = aMap.addMarker(markerOptions);
                        markerStartList.add(startMarker);
                    } else {
                        //非第一次添加
                        startMarker = aMap.addMarker(markerOptions);
                        Marker markerOld = markerStartList.get(0);
                        markerOld.remove();
                        markerStartList.clear();
                        markerStartList.add(startMarker);
                    }
                }

                //终止点
                if (rbEnd.isChecked()) {
                    endLongitude = latLng.longitude;
                    endLatitude = latLng.latitude;

                    netUtils.initMarker(latLng, R.layout.marker_end_layout, markerOptions);
                    if (markerEndList.size() == 0) {
                        endMarker = aMap.addMarker(markerOptions);
                        markerEndList.add(startMarker);
                    } else {
                        //非第一次添加
                        endMarker = aMap.addMarker(markerOptions);
                        Marker markerOld = markerEndList.get(0);
                        markerOld.remove();
                        markerEndList.clear();
                        markerEndList.add(endMarker);
                    }
                }
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //给ivMenu添加点击事件
        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.getDrawerLayout().openDrawer(Gravity.LEFT);
            }
        });
    }
}
