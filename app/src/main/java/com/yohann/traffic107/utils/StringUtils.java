package com.yohann.traffic107.utils;

import java.util.ArrayList;

/**
 * Created by Yohann on 2016/8/14.
 */
public class StringUtils {

    /**
     * 将数组元素转换为"xxx&xxx&xxx"这种格式的字符串
     *
     * @param arr
     * @return
     */
    public static String getStringFromArray(String[] arr) {
        StringBuilder strBuilder = null;

        if (arr != null) {
            strBuilder = new StringBuilder();
            for (int i = 0; i < arr.length; i++) {
                if (i != arr.length - 1) {
                    strBuilder.append(arr[i] + "##");
                } else {
                    strBuilder.append(arr[i]);
                }
            }
            return strBuilder.toString();

        } else {
            return null;
        }
    }

    /**
     * 将"xxx&xxx&xxx"这种格式的字符串转换为String[]
     *
     * @param str
     * @return
     */
    public static String[] getArrayFromString(String str) {
        String[] arr = null;
        if (str != null) {
            arr = str.split("##");
        }
        return arr;
    }

    /**
     * 将ArryList转换为"xxx&xxx&xxx"这种格式的字符串
     *
     * @param list
     * @return
     */
    public static String getStringFromArrayList(ArrayList<String> list) {

        StringBuilder strBuilder = null;

        if (list != null) {
            strBuilder = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                if (i != list.size() - 1) {
                    strBuilder.append(list.get(i) + "##");
                } else {
                    strBuilder.append(list.get(i));
                }
            }
            return strBuilder.toString();

        } else {
            return null;
        }
    }

    /**
     * 打印信息
     *
     * @param str
     */
    public static void print(String str) {
        System.out.println(str);
    }
}
