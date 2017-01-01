package com.yohann.traffic107.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Yohann on 2016/8/26.
 */
public class ViewUtils {
    public static void show(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
