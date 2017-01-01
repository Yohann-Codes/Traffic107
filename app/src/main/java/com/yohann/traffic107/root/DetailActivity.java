package com.yohann.traffic107.root;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yohann.traffic107.R;
import com.yohann.traffic107.common.Constants.Variable;
import com.yohann.traffic107.common.activity.BaseActivity;
import com.yohann.traffic107.common.bean.Event;
import com.yohann.traffic107.utils.StringUtils;
import com.yohann.traffic107.utils.ViewUtils;

import java.util.Date;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import me.gujun.android.taggroup.TagGroup;

public class DetailActivity extends BaseActivity {
    private TextView tvTime;
    private TextView tvStartLoc;
    private TextView tvEndLoc;
    private TagGroup labelGroup;
    private TextView tvTitle;
    private TextView tvDesc;
    private Button btnRemove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        init();
        loadData();
    }

    private void init() {
        tvTime = (TextView) findViewById(R.id.tv_time);
        tvStartLoc = (TextView) findViewById(R.id.tv_start_Loc);
        tvEndLoc = (TextView) findViewById(R.id.tv_end_loc);
        labelGroup = (TagGroup) findViewById(R.id.label_group);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvDesc = (TextView) findViewById(R.id.tv_desc);
        btnRemove = (Button) findViewById(R.id.btn_remove);

        //删除该Marker
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
                builder.setTitle("确认删除");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (TextUtils.isEmpty(Variable.eventId)) {
                        } else {
                            Event event = new Event();
                            event.setFinished(true);
                            event.setEndTime(new Date(System.currentTimeMillis()));

                            event.update(Variable.eventId, new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        ViewUtils.show(DetailActivity.this, "删除成功");
                                        Variable.eventMap.remove(Variable.eventId);
                                        setResult(RESULT_OK, null);
                                        finish();
                                    } else {
                                        ViewUtils.show(DetailActivity.this, "删除失败 " + e.getErrorCode());
                                    }
                                }
                            });
                        }
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
            }
        });
    }

    private void loadData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        bundle.getString("startLoc");
        bundle.getString("endLoc");
        bundle.getString("labels");
        bundle.getString("title");
        bundle.getString("desc");

        tvTime.setText(bundle.getString("startTime"));
        tvStartLoc.setText(bundle.getString("endLoc"));
        tvEndLoc.setText(bundle.getString("endLoc"));
        tvTitle.setText(bundle.getString("title"));
        tvDesc.setText(bundle.getString("desc"));

        String[] labels = StringUtils.getArrayFromString(bundle.getString("labels"));
        labelGroup.setTags(labels);
    }
}
