package com.xwj.xwjnote3.presenter.impl;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.xwj.xwjnote3.model.MyUser;
import com.xwj.xwjnote3.presenter.RegPresenter;
import com.xwj.xwjnote3.ui.RegActivity;
import com.xwj.xwjnote3.view.RegView;

import cn.bmob.v3.listener.SaveListener;

/**
 * Created by xwjsd on 2015/12/20.
 */
public class RegPresenterImpl implements RegPresenter {

    private Context mContext;
    private RegView mView;
    private RegActivity mActivity;

    public RegPresenterImpl(Context context, RegView view) {
        mContext = context;
        mView = view;
        mActivity = (RegActivity) context;
    }

    @Override
    public void register() {
        String username = mView.getUsername();
        String password = mView.getPassword();
        String email = mView.getEmail();
        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(email)) {
            MyUser myUser = new MyUser();
            myUser.setUsername(username);
            myUser.setPassword(password);
            myUser.setEmail(email);
            mView.showProgressDialog();
            myUser.signUp(mContext, new SaveListener() {
                @Override
                public void onSuccess() {
                    Toast.makeText(mContext, "登陆成功", Toast.LENGTH_SHORT).show();
                    mView.hideProgressDialog();
                    mActivity.finish();
                }

                @Override
                public void onFailure(int i, String s) {
                    Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
                    mView.hideProgressDialog();
                }
            });
        } else {
            Toast.makeText(mContext, "用户名密码和邮箱都不能为空", Toast.LENGTH_SHORT).show();
        }
    }
}
