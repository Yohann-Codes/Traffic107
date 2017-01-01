package com.yohann.traffic107.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.yohann.traffic107.R;
import com.yohann.traffic107.common.Constants.Variable;
import com.yohann.traffic107.common.activity.BaseActivity;
import com.yohann.traffic107.common.bean.User;
import com.yohann.traffic107.utils.BmobUtils;
import com.yohann.traffic107.utils.ViewUtils;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends BaseActivity {
    private EditText etUsername;
    private EditText etPassword;
    private ProgressBar pb;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        BmobUtils.init(this);
        init();
    }

    /**
     * 初始化View
     */
    private void init() {
        etUsername = (EditText) findViewById(R.id.et_username);
        etPassword = (EditText) findViewById(R.id.et_password);
        pb = (ProgressBar) findViewById(R.id.pb_register);
        user = new User();
    }

    /**
     * 注册
     *
     * @param view
     */
    public void register(View view) {
        final String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            return;
        } else {
            pb.setVisibility(View.VISIBLE);

            user.setUsername(username);
            user.setPassword(password);

            new Thread() {
                @Override
                public void run() {

                    user.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                Variable.userId = user.getObjectId();
                                Variable.userName = user.getUsername();
                                ViewUtils.show(RegisterActivity.this, "注册成功 id=" + Variable.userId);
                                startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                                finish();
                            } else {
                                ViewUtils.show(RegisterActivity.this, "注册失败 " + e.getErrorCode());
                            }
                        }
                    });

                    pb.post(new Runnable() {
                        @Override
                        public void run() {
                            pb.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }.start();
        }
    }
}
