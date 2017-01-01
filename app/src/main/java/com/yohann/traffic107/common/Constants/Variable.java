package com.yohann.traffic107.common.Constants;

import com.yohann.traffic107.common.bean.Event;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yohann on 2016/8/26.
 */
public class Variable {
    public static String userId;
    public static String userName;
    public static String rootId;
    public static String eventId;
    public static String MsgId;

    public static Map<String, Event> eventMap = new HashMap<>();
}
