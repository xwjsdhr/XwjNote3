package com.xwj.xwjnote3.presenter.impl;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.xwj.xwjnote3.R;
import com.xwj.xwjnote3.interactor.OnFinishListener;
import com.xwj.xwjnote3.interactor.SwitchListInteractor;
import com.xwj.xwjnote3.interactor.impl.SwitchListInteractorImpl;
import com.xwj.xwjnote3.model.MyUser;
import com.xwj.xwjnote3.model.Note;
import com.xwj.xwjnote3.model.NoteModel;
import com.xwj.xwjnote3.model.impl.NoteModelImpl;
import com.xwj.xwjnote3.presenter.Presenter;
import com.xwj.xwjnote3.ui.LoginActivity;
import com.xwj.xwjnote3.ui.UserInfoActivity;
import com.xwj.xwjnote3.utils.UserUtils;
import com.xwj.xwjnote3.view.NoteListView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;


/**
 * 业务逻辑类
 * Created by xwjsd on 2015/12/3.
 */
public class PresenterImpl implements Presenter, OnFinishListener {
    private static final String TAG = PresenterImpl.class.getSimpleName();
    private NoteListView mListView;
    private SwitchListInteractor mSwitchListInteractor;
    private Context mContext;
    private NoteModel mNoteModel;
    private UserUtils mUserUtils;

    public PresenterImpl(NoteListView iNoteListView) {
        this.mListView = iNoteListView;
        mContext = (Context) iNoteListView;
        this.mSwitchListInteractor = new SwitchListInteractorImpl(mContext);
        mNoteModel = new NoteModelImpl(mContext);
        mUserUtils = UserUtils.getInstance(mContext);
    }


    @Override
    public void onFinish(List<Note> list) {
        mListView.setList(list);
        mListView.hideProgress();
    }

    @Override
    public void showProgress() {
        mListView.showProgress();
        mSwitchListInteractor.switchList(0, this);
    }

    @Override
    public void onResume() {
        Log.e(TAG, "onResume");
        mListView.showProgress();
        mSwitchListInteractor.switchList(0, this);
        if (mUserUtils.isLogin()) {
            MyUser myUser = new MyUser();
            myUser.setUsername(mUserUtils.getUserName());
            mListView.setUserInfo(myUser);
        } else {
            MyUser myUser = new MyUser();
            myUser.setUsername("未登录");
            mListView.setUserInfo(myUser);
        }
    }

    @Override
    public void hideProgress() {
        mListView.hideProgress();
    }

    @Override
    public void closeDrawer() {
        mListView.closeDrawer();
    }

    @Override
    public void openDrawer() {
        mListView.openDrawer();
    }

    public void showMessage(String msg) {
        mListView.showMessage(msg);
    }

    @Override
    public void switchList(int checkId) {
        mListView.showProgress();
        mSwitchListInteractor.switchList(checkId, this);
        mListView.hideProgress();
        mListView.closeDrawer();
    }

    @Override
    public void switchAction(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                mListView.showMessage("Setting");
                break;
        }
    }

    @Override
    public void searchByContent(String title, boolean b) {
        ArrayList<Note> arrayList = mNoteModel.getNotesByTitle(title, b);
        mListView.setList(arrayList);

    }

    @Override
    public void moveToTrash(final Note note) {

        mNoteModel.moveToTrashBin(note);

    }

    @Override
    public void deleteNote(final Note note) {
        mNoteModel.deleteNote(note);
    }

    @Override
    public void restoreNote(Note note) {
        mNoteModel.restoreNote(note);
    }

    /**
     * 分享便签到第三方平台。
     *
     * @param note
     */
    @Override
    public void shareNote(Note note) {
    }

    @Override
    public void favoriteNote(Note note, boolean isFavorite) {
        note.setFavorite(isFavorite);
        mNoteModel.updateNote(note);
    }

    @Override
    public void clearTrashBin() {
        mNoteModel.clearAllTrash();
    }

    /**
     * 同步便签
     */
    @Override
    public void syncNote(final List<Note> mlist) {
        if (mUserUtils.isLogin()) {
            BmobQuery<Note> query = new BmobQuery<>();
            query.addWhereEqualTo("userAuth", mUserUtils.getUserName());
            query.findObjects(mContext, new FindListener<Note>() {
                @Override
                public void onSuccess(List<Note> list) {
                    mlist.addAll(mNoteModel.addAllNotes(list));
                    mListView.hideSwipeProgress();
                }

                @Override
                public void onError(int i, String s) {
                    Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(mContext, "请先登录", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getBinSize() {
    }

    @Override
    public void startUserInfoActivity() {
        Intent intent = null;
        if (mUserUtils.isLogin()) {
            intent = new Intent(mContext, UserInfoActivity.class);
        } else {
            intent = new Intent(mContext, LoginActivity.class);
        }
        mContext.startActivity(intent);
    }
}
