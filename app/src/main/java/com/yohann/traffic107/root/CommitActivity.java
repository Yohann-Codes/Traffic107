package com.yohann.traffic107.root;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.yohann.traffic107.R;
import com.yohann.traffic107.common.activity.BaseActivity;
import com.yohann.traffic107.common.bean.Event;
import com.yohann.traffic107.utils.BmobUtils;
import com.yohann.traffic107.utils.ViewUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class CommitActivity extends BaseActivity {
    private static final int SHOW = 1;
    private static final String TAG = "CommitActivityInfo";

    private ListView lvCommit;
    private List<String> keyList;
    private Map<String, Event> eventMap;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW:
                    lvCommit.setAdapter(new MyAdapter());
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commit_root);
        BmobUtils.init(this);
        lvCommit = (ListView) findViewById(R.id.lv_commit);
        lvCommit.addHeaderView(View.inflate(this, R.layout.commit_header_root, null));
        init();
    }

    private void init() {
        keyList = new ArrayList<>();
        eventMap = new HashMap<>();
        query();

        lvCommit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String key = keyList.get(position - 1);
                Event event = eventMap.get(key);
                Intent intent = new Intent(CommitActivity.this, DetailCommitActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("event", event);
                bundle.putString("objectId", key);
                intent.putExtras(bundle);
                startActivityForResult(intent, 0);
            }
        });
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return keyList.size();
        }

        @Override
        public Object getItem(int position) {
            return keyList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(CommitActivity.this, R.layout.item_commit_root, null);

            TextView tvUsername = (TextView) view.findViewById(R.id.tv_username_commit_root);
            TextView tvTime = (TextView) view.findViewById(R.id.tv_time_commit_root);
            TextView tvCommitStatus = (TextView) view.findViewById(R.id.tv_status_commit_root);

            String key = keyList.get(position);
            Event event = eventMap.get(key);
            tvUsername.setText(event.getUsername());
            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(event.getStartTime());
            tvTime.setText(time);
            tvCommitStatus.setText(event.getCommStatus());
            return view;
        }
    }

    public void query() {
        //向服务器获取路况数据
        new Thread() {
            @Override
            public void run() {
                BmobQuery<Event> query = new BmobQuery<>();
                query.addWhereEqualTo("commStatus", "审核中");

                query.findObjects(new FindListener<Event>() {
                    @Override
                    public void done(List<Event> list, BmobException e) {
                        if (e == null) {
                            if (list.size() == 0) {
                                ViewUtils.show(CommitActivity.this, "没有数据可加载");
                            } else {
                                for (Event event : list) {
                                    keyList.add(event.getObjectId());
                                    eventMap.put(event.getObjectId(), event);
                                }
                                handler.sendEmptyMessage(SHOW);
                                ViewUtils.show(CommitActivity.this, "加载了" + list.size() + "条数据");
                            }
                        } else {
                            ViewUtils.show(CommitActivity.this, "数据加载失败 " + e.getErrorCode());
                        }
                    }
                });
            }


        }.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i(TAG, "onActivityResult: ");

        if (resultCode == RESULT_OK) {
            finish();
            startActivity(new Intent(this, CommitActivity.class));
        }
    }
}
