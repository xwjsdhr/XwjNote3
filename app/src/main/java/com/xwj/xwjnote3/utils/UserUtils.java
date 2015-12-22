package com.xwj.xwjnote3.utils;

import android.content.Context;

import com.xwj.xwjnote3.model.MyUser;

import cn.bmob.v3.BmobUser;

/**
 * Created by xwjsd on 2015/12/20.
 */
public class UserUtils {

    private static UserUtils sInstance = new UserUtils();
    private static Context sContext;
    private static MyUser sCurrentUser;

    private UserUtils() {
    }

    public static UserUtils getInstance(Context context) {
        sContext = context;
        sCurrentUser = BmobUser.getCurrentUser(sContext, MyUser.class);
        return sInstance;
    }

    public boolean isLogin() {
        return sCurrentUser != null;
    }

    public String getUserName() {
        return sCurrentUser.getUsername();
    }
}
