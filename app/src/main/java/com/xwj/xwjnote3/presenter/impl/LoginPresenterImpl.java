package com.xwj.xwjnote3.presenter.impl;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.xwj.xwjnote3.model.MyUser;
import com.xwj.xwjnote3.presenter.LoginPresenter;
import com.xwj.xwjnote3.ui.LoginActivity;
import com.xwj.xwjnote3.view.LoginView;

import cn.bmob.v3.listener.SaveListener;

/**
 * Created by xwjsd on 2015/12/20.
 */
public class LoginPresenterImpl implements LoginPresenter {

    private LoginView mView;
    private Context mContext;
    private LoginActivity mActivity;

    public LoginPresenterImpl(Context context, LoginView view) {
        mView = view;
        mContext = context;
        mActivity = (LoginActivity) context;
    }

    @Override
    public void login() {
        String password = mView.getPassword();
        String username = mView.getUsername();

        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
            mView.showProgressDialog();
            MyUser myUser = new MyUser();
            myUser.setUsername(username);
            myUser.setPassword(password);
            myUser.login(mContext, new SaveListener() {
                @Override
                public void onSuccess() {
                    Toast.makeText(mContext, "登陆成功", Toast.LENGTH_SHORT).show();
                    mView.hideProgressDialog();
                    mActivity.finish();
                }

                @Override
                public void onFailure(int i, String s) {
                    mView.hideProgressDialog();
                    Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(mContext, "用户名和密码不能为空", Toast.LENGTH_SHORT).show();
        }
    }
}
