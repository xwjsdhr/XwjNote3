package com.xwj.xwjnote3.presenter.impl;

import android.content.Context;
import android.widget.Toast;

import com.xwj.xwjnote3.model.MyUser;
import com.xwj.xwjnote3.presenter.UserPresenter;
import com.xwj.xwjnote3.utils.NoteUtil;
import com.xwj.xwjnote3.view.UserInfoView;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by xwjsd on 2015/12/22.
 */
public class UserPresenterImpl implements UserPresenter {

    private Context mContext;
    private UserInfoView mView;
    private NoteUtil mNoteUtils;

    public UserPresenterImpl(Context context, UserInfoView view) {
        mContext = context;
        mView = view;
        mNoteUtils = NoteUtil.getInstance(context);
    }

    @Override
    public void setCurrentUser() {
        MyUser currentUser = BmobUser.getCurrentUser(mContext, MyUser.class);
        if (currentUser != null) {
            mView.bindViewByUser(currentUser);
        }
    }

    @Override
    public void logOut() {
        BmobUser.logOut(mContext);
        Toast.makeText(mContext, "当前帐号已退出", Toast.LENGTH_SHORT).show();
    }

    /**
     * 同步便签
     */
    @Override
    public void syncNotes() {
        mView.showProgressDialog();
        final List<BmobObject> notes = mNoteUtils.getAllWillSyncNotes();

        if (notes != null && notes.size() != 0) {
            new BmobObject().insertBatch(mContext, notes, new SaveListener() {
                @Override
                public void onSuccess() {
                    Toast.makeText(mContext, "同步成功", Toast.LENGTH_SHORT).show();
                    mView.hideProgressDialog();
                    mNoteUtils.updateSyncTrue(notes);
                }

                @Override
                public void onFailure(int i, String s) {
                    Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(mContext, "无可同步的便签", Toast.LENGTH_SHORT).show();
            mView.hideProgressDialog();
        }
    }
}
