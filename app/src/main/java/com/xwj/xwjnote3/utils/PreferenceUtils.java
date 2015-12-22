package com.xwj.xwjnote3.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * sharedPreference的工具类。
 * Created by xwjsd on 2015/12/4.
 */
public class PreferenceUtils {

    public static final String ISLINEARLAYOUTMANAGER = "ISLINEARLAYOUTMANAGER";

    public static final String INDEX_LAYOUTMANAGER = "INDEX_LAYOUTMANAGER";

    public static void setBoolean(Context context, String key, Boolean value) {
        SharedPreferences preferences = context.getSharedPreferences(ConstantUtils.SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
        preferences.edit().putBoolean(key, value).apply();
    }

    public static Boolean getBoolean(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(ConstantUtils.SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(key, false);
    }

    public static void setInt(Context context, String key, int value) {
        SharedPreferences preferences = context.getSharedPreferences(ConstantUtils.SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
        preferences.edit().putInt(key, value).apply();
    }

    public static int getInt(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(ConstantUtils.SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return preferences.getInt(key, 0);
    }
}
