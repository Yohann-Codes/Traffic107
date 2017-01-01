package com.yohann.traffic107.user.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.yohann.traffic107.R;
import com.yohann.traffic107.common.Constants.Variable;
import com.yohann.traffic107.common.activity.BaseActivity;
import com.yohann.traffic107.common.bean.Event;
import com.yohann.traffic107.utils.BmobUtils;
import com.yohann.traffic107.utils.StringUtils;
import com.yohann.traffic107.utils.ViewUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import me.gujun.android.taggroup.TagGroup;

public class EditActivity extends BaseActivity {

    private static final String TAG = "EditActivityInfo";
    private static int counter = 0;

    private EditText etStartLoc;
    private EditText etEndLoc;
    private EditText etTitle;
    private EditText etDesc;
    private ImageView ivAddLabels;
    private TagGroup tagGroup;
    private TextView tvLabelHint;
    private TextView tvTime;
    private Button btnCommit;
    private ArrayList<String> addressList;
    private ArrayList<String> labelList;
    private GeocodeSearch geocodeSearch;
    private String address;
    private Double startLongitude;
    private Double startLatitude;
    private Double endLongitude;
    private Double endLatitude;
    private String startLoc;
    private String endLoc;
    private Date startDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        BmobUtils.init(this);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        etStartLoc = (EditText) findViewById(R.id.et_start_Loc);
        etEndLoc = (EditText) findViewById(R.id.et_end_Loc);
        ivAddLabels = (ImageView) findViewById(R.id.iv_add_labels);
        tagGroup = (TagGroup) findViewById(R.id.label_group);
        tvLabelHint = (TextView) findViewById(R.id.tv_label_hint);
        tvTime = (TextView) findViewById(R.id.tv_time);
        btnCommit = (Button) findViewById(R.id.btn_commit);
        etTitle = (EditText) findViewById(R.id.et_title);
        etDesc = (EditText) findViewById(R.id.et_desc);

        geocodeSearch = new GeocodeSearch(this);
        addressList = new ArrayList<>();

        //装载标签
        labelList = new ArrayList<>();

        //点击删除标签
        tagGroup.setOnTagClickListener(new TagGroup.OnTagClickListener() {
            @Override
            public void onTagClick(String tag) {
                labelList.remove(tag);

                if (labelList.size() == 0) {
                    tvLabelHint.setVisibility(View.VISIBLE);
                }

                tagGroup.setTags(labelList);
            }
        });


        //添加监听
        MyOnClickListener listener = new MyOnClickListener();
        ivAddLabels.setOnClickListener(listener);
        btnCommit.setOnClickListener(listener);
        geocodeSearch.setOnGeocodeSearchListener(new AddressListener());

        //获取当前时间
        startDate = new Date();
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startDate);
        tvTime.setText(time);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        startLongitude = bundle.getDouble("startLongitude");
        startLatitude = bundle.getDouble("startLatitude");
        endLongitude = bundle.getDouble("endLongitude");
        endLatitude = bundle.getDouble("endLatitude");

        Log.i(TAG, "startLongitude" + startLongitude + "startLatitude" +
                startLatitude + "endLongitude" + endLongitude + "endLatitude" + endLatitude);

        getAddress(startLatitude, startLongitude);
        getAddress(endLatitude, endLongitude);
    }

    public void getAddress(double latitude, double longitude) {
        RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(latitude, longitude), 100, GeocodeSearch.AMAP);
        geocodeSearch.getFromLocationAsyn(query);
    }

    public void saveAddress() {
        startLoc = addressList.get(0);
        endLoc = addressList.get(1);
        addressList.clear();
        etStartLoc.setText(startLoc);
        etEndLoc.setText(endLoc);
    }

    /**
     * 按钮监听
     */
    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                //填写标签内容
                case R.id.iv_add_labels:
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                    final AlertDialog dialog = builder.create();
                    //将自定义布局设置给dialog
                    View view = View.inflate(EditActivity.this, R.layout.labels_input, null);

                    final EditText etLabel = (EditText) view.findViewById(R.id.et_label);
                    Button btnConfirm = (Button) view.findViewById(R.id.btn_confirm);
                    Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);

                    dialog.setView(view);
                    dialog.show();

                    btnConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.i(TAG, "确定标签");
                            labelList.add(etLabel.getText().toString());
                            dialog.dismiss();
                            if (labelList.size() != 0) {
                                tvLabelHint.setVisibility(View.GONE);
                            }
                            tagGroup.setTags(labelList);
                        }
                    });

                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.i(TAG, "取消");
                            if (labelList.size() != 0) {
                                tvLabelHint.setVisibility(View.GONE);
                            }
                            dialog.dismiss();
                        }
                    });
                    break;

                case R.id.btn_commit:
                    String labels = StringUtils.getStringFromArrayList(labelList);

                    final Event event = new Event();
                    event.setStartLocation(startLoc);
                    event.setEndLocation(endLoc);
                    event.setStartLatitude(startLatitude);
                    event.setStartLongitude(startLongitude);
                    event.setEndLatitude(endLatitude);
                    event.setEndLongitude(endLongitude);
                    event.setLabels(labels);
                    event.setTitle(etTitle.getText().toString());
                    event.setDesc(etDesc.getText().toString());
                    event.setStartTime(startDate);
                    event.setFinished(false);
                    event.setCommStatus("审核中");
                    event.setUsername(Variable.userName);

                    //上传
                    new Thread() {
                        @Override
                        public void run() {

                            event.save(new SaveListener<String>() {
                                @Override
                                public void done(String s, BmobException e) {
                                    if (e == null) {
                                        ViewUtils.show(EditActivity.this, "提交成功");
                                        finish();
                                    } else {
                                        ViewUtils.show(EditActivity.this, "提交失败" + e.getErrorCode());
                                    }
                                }
                            });
                        }
                    }.start();
                    break;
            }
        }
    }

    class AddressListener implements GeocodeSearch.OnGeocodeSearchListener {

        @Override
        public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
            address = regeocodeResult.getRegeocodeAddress().getFormatAddress();
            Log.i(TAG, "address=" + address);
            addressList.add(address);

            counter++;
            if (counter == 2) {
                saveAddress();
                counter = 0;
            }
        }

        @Override
        public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
        }
    }
}
