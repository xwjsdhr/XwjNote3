package com.xwj.xwjnote3.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xwj.xwjnote3.R;
import com.xwj.xwjnote3.presenter.RegPresenter;
import com.xwj.xwjnote3.presenter.impl.RegPresenterImpl;
import com.xwj.xwjnote3.view.RegView;

public class RegActivity extends AppCompatActivity implements View.OnClickListener, RegView {

    private EditText mEtUsername, mEtPassword, mEtEmail;
    private Button mBtnReg, mBtnCancel;
    private ProgressDialog mDialog;
    private RegPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        initView();
    }

    private void initView() {
        mEtPassword = (EditText) this.findViewById(R.id.et_reg_password);
        mEtUsername = (EditText) this.findViewById(R.id.et_reg_username);
        mEtEmail = (EditText) this.findViewById(R.id.et_reg_email);
        mBtnReg = (Button) this.findViewById(R.id.btn_reg);
        mBtnCancel = (Button) this.findViewById(R.id.btn_cancel);
        mPresenter = new RegPresenterImpl(this, this);
        mBtnCancel.setOnClickListener(this);
        mBtnReg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mBtnReg) {
            mPresenter.register();
        } else if (v == mBtnCancel) {
            this.finish();
        }
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
    public String getEmail() {
        return mEtEmail.getText().toString();
    }

    @Override
    public void showProgressDialog() {
        mDialog = ProgressDialog.show(this, "注册", "正在注册");
    }

    @Override
    public void hideProgressDialog() {
        mDialog.dismiss();
    }
}
