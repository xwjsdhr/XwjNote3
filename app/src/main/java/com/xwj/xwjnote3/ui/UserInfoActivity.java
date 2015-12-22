package com.xwj.xwjnote3.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xwj.xwjnote3.R;
import com.xwj.xwjnote3.model.MyUser;
import com.xwj.xwjnote3.presenter.UserPresenter;
import com.xwj.xwjnote3.presenter.impl.UserPresenterImpl;
import com.xwj.xwjnote3.view.UserInfoView;

public class UserInfoActivity extends AppCompatActivity implements UserInfoView, View.OnClickListener {

    private TextView mTvUsername, mTvEmail;
    private UserPresenter mPresenter;
    private Button mBtnLogout, mBtnSyncNotes;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        initView();
        mPresenter = new UserPresenterImpl(this, this);
        mPresenter.setCurrentUser();
    }

    private void initView() {
        mTvUsername = (TextView) this.findViewById(R.id.tv_userInfo_username);
        mTvEmail = (TextView) this.findViewById(R.id.tv_userInfo_email);
        mBtnLogout = (Button) this.findViewById(R.id.btn_userInfo_logout);
        mBtnSyncNotes = (Button) this.findViewById(R.id.btn_userInfo_sync);
        mBtnLogout.setOnClickListener(this);
        mBtnSyncNotes.setOnClickListener(this);
    }

    @Override
    public void bindViewByUser(MyUser user) {
        mTvUsername.setText(user.getUsername());
        mTvEmail.setText(user.getEmail());
    }

    @Override
    public void showProgressDialog() {
        mDialog = ProgressDialog.show(this, "同步便签", "正在同步");
    }

    @Override
    public void hideProgressDialog() {
        mDialog.dismiss();
    }

    @Override
    public void onClick(View v) {
        if (v == mBtnLogout) {
            mPresenter.logOut();
            this.finish();
        } else if (v == mBtnSyncNotes) {
            mPresenter.syncNotes();
        }
    }
}
