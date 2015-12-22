package com.xwj.xwjnote3.utils;

import android.content.Context;
import android.text.format.DateUtils;

import java.util.Date;
import java.util.UUID;

/**
 * 常用工具类
 * Created by xwjsd on 2015/12/4.
 */
public class CommonUtils {
    public static String getGuid() {
        return UUID.randomUUID().toString();
    }

    public static String getDate(Context context, Date date) {
        return DateUtils.getRelativeDateTimeString(context, date.getTime()
                , DateUtils.MINUTE_IN_MILLIS
                , DateUtils.DAY_IN_MILLIS
                , DateUtils.FORMAT_ABBREV_ALL
        ).toString();
    }
}
