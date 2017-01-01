package com.yohann.traffic107.user.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yohann.traffic107.R;
import com.yohann.traffic107.user.activity.HomeActivity;
import com.yohann.traffic107.utils.BmobUtils;

/**
 * Created by Yohann on 2016/8/28.
 */
public class StatisticsFragment extends Fragment {
    private HomeActivity activity;
    private ImageView ivMenu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (HomeActivity) getActivity();
        BmobUtils.init(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, null);
        init(view);
        return view;
    }

    private void init(View view) {
        ivMenu = (ImageView) view.findViewById(R.id.iv_menu_statistics);
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
