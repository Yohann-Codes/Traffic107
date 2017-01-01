package com.yohann.traffic107.common.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.yohann.traffic107.R;
import com.yohann.traffic107.common.Constants.Variable;
import com.yohann.traffic107.common.bean.Root;
import com.yohann.traffic107.common.bean.User;
import com.yohann.traffic107.user.activity.HomeActivity;
import com.yohann.traffic107.user.activity.RegisterActivity;
import com.yohann.traffic107.utils.BmobUtils;
import com.yohann.traffic107.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * 选择登录
 */
public class LoginActivity extends BaseActivity {
    private static final String TAG = "LoginActivityInfo";

    private ViewPager vp;
    private EditText etUsername_user;
    private EditText etPassword_user;
    private EditText etUsername_root;
    private EditText etPassword_root;
    private User user;
    private Root root;
    private ProgressBar pb_user;
    private ProgressBar pb_root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        BmobUtils.init(this);
        init();
    }

    private void init() {
        vp = (ViewPager) findViewById(R.id.vp);

        View viewUser = View.inflate(this, R.layout.login_user, null);
        View viewRoot = View.inflate(this, R.layout.login_root, null);

        etUsername_user = (EditText) viewUser.findViewById(R.id.et_username_user);
        etUsername_root = (EditText) viewRoot.findViewById(R.id.et_username_root);
        etPassword_user = (EditText) viewUser.findViewById(R.id.et_password_user);
        etPassword_root = (EditText) viewRoot.findViewById(R.id.et_password_root);
        pb_user = (ProgressBar) viewUser.findViewById(R.id.pb_user);
        pb_root = (ProgressBar) viewRoot.findViewById(R.id.pb_root);

        final ArrayList<View> viewList = new ArrayList<>();
        viewList.add(viewUser);
        viewList.add(viewRoot);

        PagerAdapter pagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return viewList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(viewList.get(position));
                return viewList.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(viewList.get(position));
            }
        };

        vp.setAdapter(pagerAdapter);
    }

    public void userLogin(View view) {
        final String username = etUsername_user.getText().toString();
        final String password = etPassword_user.getText().toString();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            return;
        } else {
            pb_user.setVisibility(View.VISIBLE);
            user = new User();
            user.setUsername(username);
            user.setPassword(password);

            new Thread() {
                @Override
                public void run() {
                    BmobQuery<User> query = new BmobQuery<>();

                    query.findObjects(new FindListener<User>() {
                        @Override
                        public void done(List<User> list, BmobException e) {
                            if (e == null) {
                                boolean isSuccessful = false;
                                for (User user : list) {
                                    if (username.equals(user.getUsername()) && password.equals(user.getPassword())) {
                                        //登录成功
                                        Variable.userId = user.getObjectId();
                                        Variable.userName = user.getUsername();
                                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                        finish();
                                        ViewUtils.show(LoginActivity.this, "登录成功");
                                        isSuccessful = true;
                                        break;
                                    }
                                }
                                if (isSuccessful) {
                                } else {
                                    ViewUtils.show(LoginActivity.this, "登录失败");
                                }
                            } else {
                                ViewUtils.show(LoginActivity.this, "异常 " + e.getErrorCode());
                            }
                        }
                    });

                    pb_user.post(new Runnable() {
                        @Override
                        public void run() {
                            pb_user.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }.start();
        }
    }

    public void userRegister(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
        finish();
    }

    public void rootLogin(View view) {
        final String username = etUsername_root.getText().toString();
        final String password = etPassword_root.getText().toString();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            return;
        } else {
            pb_root.setVisibility(View.VISIBLE);
            root = new Root();
            root.setUsername(username);
            root.setPassword(password);

            new Thread() {
                @Override
                public void run() {
                    BmobQuery<Root> query = new BmobQuery<>();

                    query.findObjects(new FindListener<Root>() {
                        @Override
                        public void done(List<Root> list, BmobException e) {
                            if (e == null) {
                                boolean isSuccessful = false;
                                for (Root root : list) {
                                    if (username.equals(root.getUsername()) && password.equals(root.getPassword())) {
                                        //登录成功
                                        Variable.rootId = root.getObjectId();
                                        startActivity(new Intent(LoginActivity.this, com.yohann.traffic107.root.MapActivity.class));
                                        finish();
                                        ViewUtils.show(LoginActivity.this, "登录成功");
                                        isSuccessful = true;
                                        break;
                                    }
                                }
                                if (isSuccessful) {
                                } else {
                                    ViewUtils.show(LoginActivity.this, "登录失败");
                                }
                            } else {
                                ViewUtils.show(LoginActivity.this, "异常 " + e.getErrorCode());
                            }
                        }
                    });

                    pb_root.post(new Runnable() {
                        @Override
                        public void run() {
                            pb_root.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }.start();
        }
    }
}
