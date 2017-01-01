package com.yohann.traffic107.user.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.yohann.traffic107.R;
import com.yohann.traffic107.common.Constants.Variable;
import com.yohann.traffic107.common.bean.ChatMsgEntity;
import com.yohann.traffic107.common.bean.Message;
import com.yohann.traffic107.user.activity.HomeActivity;
import com.yohann.traffic107.user.adatper.ChatMsgViewAdapter;
import com.yohann.traffic107.utils.BmobUtils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Yohann on 2016/8/28.
 */
public class AskFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "AskFragmentInfo";
    private HomeActivity activity;
    private ImageView ivMenu;
    private int msgSize = 0;
    private boolean haveMsg;
    //发送消息
    private Button btnSend;
    private EditText etContent;
    private ListView lvRecord;
    //消息视图的adapter
    private ChatMsgViewAdapter adapter;
    //加载服务器上的聊天记录
    private List<ChatMsgEntity> dataList = new ArrayList<>();
    private boolean status = true;
    private Message message;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (HomeActivity) getActivity();
        BmobUtils.init(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ask, null);
        init(view);
        return view;
    }

    private void init(View view) {
        ivMenu = (ImageView) view.findViewById(R.id.iv_menu_ask);
        lvRecord = (ListView) view.findViewById(R.id.listview);
        btnSend = (Button) view.findViewById(R.id.btn_send);
        etContent = (EditText) view.findViewById(R.id.et_sendmessage);
        btnSend.setOnClickListener(this);

        adapter = new ChatMsgViewAdapter(activity, dataList);
        lvRecord.setAdapter(adapter);

        loadMsg();
    }

    private void loadMsg() {
        new Thread() {
            @Override
            public void run() {
                BmobQuery<Message> query = new BmobQuery<>();
                while (status) {
                    query.findObjects(new FindListener<Message>() {
                        @Override
                        public void done(List<Message> list, BmobException e) {
                            if (e == null) {
                                if (list.size() != msgSize) {
                                    haveMsg = true;
                                    dataList.clear();
                                    for (Message msg : list) {
                                        ChatMsgEntity entity = new ChatMsgEntity();
                                        entity.setName(msg.getUsername());
                                        entity.setMessage(msg.getMsg());
                                        if (Variable.userName.equals(msg.getUsername())) {
                                            entity.setMsgType(false);
                                        } else {
                                            entity.setMsgType(true);
                                        }
                                        dataList.add(entity);
                                    }
                                    Log.i(TAG, "有新的消息");
                                } else {
                                    haveMsg = false;
                                    Log.i(TAG, "没有新的消息");
                                }
                            }
                        }
                    });
                    if (haveMsg) {
                        lvRecord.post(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();// 通知ListView，数据已发生改变
                                lvRecord.setSelection(dataList.size() - 1);
                            }
                        });
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }.start();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:// 发送按钮点击事件
                send();
                break;
        }
    }

    /**
     * 发送消息
     */
    private void send() {
        String contString = etContent.getText().toString();
        if (contString.length() > 0) {
            etContent.setText("");// 清空编辑框数据

            //将数据发送到服务器
            message = new Message();
            message.setUsername(Variable.userName);
            message.setMsg(contString);
            new Thread() {
                @Override
                public void run() {
                    message.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                Variable.MsgId = message.getObjectId();
                            }
                        }
                    });
                }
            }.start();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        status = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        status = false;
    }
}
