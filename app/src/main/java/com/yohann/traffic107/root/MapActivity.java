package com.yohann.traffic107.root;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.yohann.traffic107.R;
import com.yohann.traffic107.common.Constants.Constants;
import com.yohann.traffic107.common.Constants.Variable;
import com.yohann.traffic107.common.activity.BaseActivity;
import com.yohann.traffic107.common.bean.Event;
import com.yohann.traffic107.utils.BmobUtils;
import com.yohann.traffic107.utils.LocationInit;
import com.yohann.traffic107.utils.NetUtils;
import com.yohann.traffic107.utils.ViewUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class MapActivity extends BaseActivity implements AMap.OnMarkerClickListener {

    private static final String TAG = "MapActivityInfo";

    private MapView mapView;
    private ImageView ivPlus;
    private RadioGroup rg;
    private RadioButton rbtnStart;
    private RadioButton rbtnEnd;
    private ImageView ivFinish;
    private RelativeLayout rlBtn;
    private ImageView ivCommitMsg;
    private Animation animOpen;
    private AMap aMap;
    private ImageView ivReminder;
    private ImageView ivFlush;

    private String editflag;
    private boolean btnStatus;

    private Double startLongitude;
    private Double endLongitude;
    private Double startLatitude;
    private Double endLatitude;

    ArrayList<Marker> markerStartList = new ArrayList<>();
    ArrayList<Marker> markerEndList = new ArrayList<>();
    private Marker startMarker;
    private Marker endMarker;
    private NetUtils netUtils;
    private LocationInit locationInit;
    private Animation animEditOpen;
    private Animation animEditClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_root);
        BmobUtils.init(this);
        init();
        mapView.onCreate(savedInstanceState);
        netUtils.loadMarker();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        Variable.eventMap.clear();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * 初始化View
     */
    private void init() {
        mapView = (MapView) findViewById(R.id.map);
        ivPlus = (ImageView) findViewById(R.id.iv_plus);
        rg = (RadioGroup) findViewById(R.id.rg);
        rbtnStart = (RadioButton) findViewById(R.id.rbtn_start);
        rbtnEnd = (RadioButton) findViewById(R.id.rbtn_end);
        ivFinish = (ImageView) findViewById(R.id.iv_finish);
        rlBtn = (RelativeLayout) findViewById(R.id.rl_btn);
        ivCommitMsg = (ImageView) findViewById(R.id.iv_commit_msg);
        ivReminder = (ImageView) findViewById(R.id.iv_msg_reminder);
        aMap = mapView.getMap();
        animOpen = AnimationUtils.loadAnimation(this, R.anim.plus_open_anim);
        animEditOpen = AnimationUtils.loadAnimation(this, R.anim.edit_open);
        animEditClose = AnimationUtils.loadAnimation(this, R.anim.edit_close);
        ivFlush = (ImageView) findViewById(R.id.iv_flush_root);

        MyOnClickListener listener = new MyOnClickListener();
        ivPlus.setOnClickListener(listener);
        rbtnStart.setOnClickListener(listener);
        rbtnEnd.setOnClickListener(listener);
        ivFinish.setOnClickListener(listener);
        ivCommitMsg.setOnClickListener(listener);
        ivFlush.setOnClickListener(listener);

        aMap.setOnMarkerClickListener(this);
        UiSettings uiSettings = aMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(false);
        netUtils = new NetUtils(this, aMap);
        locationInit = new LocationInit(this, aMap);
        locationInit.init();

        //地图长按监听
        aMap.setOnMapLongClickListener(new AMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {

                MarkerOptions markerOptions = new MarkerOptions();
                //编辑状态
                if (btnStatus == true) {

                    //起始点
                    if (rbtnStart.isChecked()) {
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
                    if (rbtnEnd.isChecked()) {
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


                } else {
                    //非编辑状态
                }
            }
        });
    }


    @Override
    public boolean onMarkerClick(Marker marker) {

        LatLng latLng = marker.getPosition();

        Iterator<Map.Entry<String, Event>> it = Variable.eventMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Event> entry = it.next();
            Variable.eventId = entry.getKey();
            Event event = entry.getValue();
            Double startLatitude = event.getStartLatitude();
            Double startLongitude = event.getStartLongitude();
            Double endLatitude = event.getEndLatitude();
            Double endLongitude = event.getEndLongitude();

            if ((latLng.latitude == startLatitude && latLng.longitude == startLongitude)
                    || (latLng.latitude == endLatitude && latLng.longitude == endLongitude)) {

                Intent intent = new Intent(MapActivity.this, DetailActivity.class);
                Bundle bundle = new Bundle();
                String startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(event.getStartTime());
                bundle.putString("startTime", startTime);
                bundle.putString("startLoc", event.getStartLocation());
                bundle.putString("endLoc", event.getEndLocation());
                bundle.putString("labels", event.getLabels());
                bundle.putString("title", event.getTitle());
                bundle.putString("desc", event.getDesc());
                intent.putExtras(bundle);
                startActivityForResult(intent, Constants.WATCH);
                break;
            }
        }

        return true;
    }

    /**
     * 按钮监听
     */
    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.iv_plus:
                    Log.i(TAG, "点击添加事件");
                    ivPlus.startAnimation(animOpen);
                    if (btnStatus) {
                        rlBtn.startAnimation(animEditClose);
                        rlBtn.setVisibility(View.INVISIBLE);
                        btnStatus = false;
                        //清屏
                        if (markerStartList.size() > 0 || markerEndList.size() > 0) {
                            markerStartList.get(0).remove();
                            markerStartList.clear();
                            markerEndList.get(0).remove();
                            markerEndList.clear();

                            startLongitude = null;
                            endLongitude = null;
                            startLatitude = null;
                            endLatitude = null;

                            //刷新地图
                            mapView.invalidate();
                        }
                    } else {
                        rlBtn.startAnimation(animEditOpen);
                        rlBtn.setVisibility(View.VISIBLE);
                        btnStatus = true;
                    }
                    break;

                case R.id.iv_commit_msg:
                    Log.i(TAG, "跳转用户提交数据菜单");
                    ivCommitMsg.startAnimation(animOpen);
                    startActivity(new Intent(MapActivity.this, CommitActivity.class));
                    break;

                case R.id.rbtn_start:
                    //添加起始点
                    editflag = "start";
                    break;

                case R.id.rbtn_end:
                    //添加终止点
                    editflag = "end";
                    break;

                case R.id.iv_finish:
                    if (startLongitude == null || startLatitude == null || endLongitude == null || endLatitude == null) {
                        ViewUtils.show(MapActivity.this, "请选择起始点和终止点");
                    } else {
                        rlBtn.setVisibility(View.INVISIBLE);
                        btnStatus = false;

                        Intent intent = new Intent(MapActivity.this, EditActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putDouble("startLongitude", startLongitude);
                        bundle.putDouble("startLatitude", startLatitude);
                        bundle.putDouble("endLongitude", endLongitude);
                        bundle.putDouble("endLatitude", endLatitude);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, Constants.EDIT);
                    }
                    break;

                case R.id.iv_flush_root:
                    netUtils.loadMarker();
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case Constants.EDIT:
                if (resultCode == RESULT_OK) {
                    netUtils.loadMarker();
                }
                break;

            case Constants.WATCH:
                if (resultCode == RESULT_OK) {
                    Log.i(TAG, "onActivityResult:");
                    aMap.clear();
                    netUtils.loadMarker();
                }
                break;
        }
    }
}

