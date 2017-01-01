package com.yohann.traffic107.utils;

import android.content.Context;

import cn.bmob.v3.Bmob;

/**
 * Created by Yohann on 2016/8/26.
 */
public class BmobUtils {
    public static void init(Context context) {
        Bmob.initialize(context, "bd137d63a2440b674b28886748a58d16");
    }
}
