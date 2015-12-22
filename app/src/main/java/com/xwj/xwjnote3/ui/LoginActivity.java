package com.xwj.xwjnote3.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xwj.xwjnote3.R;
import com.xwj.xwjnote3.presenter.LoginPresenter;
import com.xwj.xwjnote3.presenter.impl.LoginPresenterImpl;
import com.xwj.xwjnote3.view.LoginView;

public class LoginActivity extends AppCompatActivity implements LoginView, View.OnClickListener {

    private EditText mEtUsername, mEtPassword;
    private Button mBtnLogin, mBtnStartReg;
    private ProgressDialog mDialog;
    private LoginPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        mEtPassword = (EditText) this.findViewById(R.id.et_password);
        mEtUsername = (EditText) this.findViewById(R.id.et_username);
        mBtnLogin = (Button) this.findViewById(R.id.btn_login);
        mBtnStartReg = (Button) this.findViewById(R.id.btn_reg);
        mPresenter = new LoginPresenterImpl(this, this);
        mBtnStartReg.setOnClickListener(this);
        mBtnLogin.setOnClickListener(this);
    }

    @Override
    public String getUsername() {
        return mEtUsername.getText().toString();
    }

    @Override
    public String getPassword() {
        return mEtPassword.getText().toString();
    }

    @Override
    public void showProgressDialog() {
        mDialog = ProgressDialog.show(this, "登录", "正在登录");
    }

    @Override
    public void hideProgressDialog() {
        mDialog.dismiss();
    }

    @Override
    public void startRegActivity() {
        Intent intent = new Intent(this, RegActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if (v == mBtnStartReg) {
            startRegActivity();
        } else if (v == mBtnLogin) {
            mPresenter.login();
        }
    }
}
