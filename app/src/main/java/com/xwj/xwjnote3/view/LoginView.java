package com.xwj.xwjnote3.view;

/**
 * Created by xwjsd on 2015/12/20.
 */
public interface LoginView {
    String getUsername();

    String getPassword();

    void showProgressDialog();

    void hideProgressDialog();

    void startRegActivity();
}
