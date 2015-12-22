package com.xwj.xwjnote3.view;

import com.xwj.xwjnote3.model.MyUser;

/**
 * Created by xwjsd on 2015/12/22.
 */
public interface UserInfoView {

    void bindViewByUser(MyUser user);

    void showProgressDialog();

    void hideProgressDialog();

}
