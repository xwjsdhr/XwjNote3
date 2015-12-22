package com.xwj.xwjnote3.presenter.impl;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.xwj.xwjnote3.model.Note;
import com.xwj.xwjnote3.model.NoteModel;
import com.xwj.xwjnote3.model.impl.NoteModelImpl;
import com.xwj.xwjnote3.presenter.NotePresenter;
import com.xwj.xwjnote3.utils.CommonUtils;
import com.xwj.xwjnote3.utils.ConstantUtils;
import com.xwj.xwjnote3.utils.NoteUtil;
import com.xwj.xwjnote3.utils.UserUtils;
import com.xwj.xwjnote3.view.NoteView;

import java.util.Date;


/**
 * bind
 * Created by xwjsd on 2015/12/3.
 */
public class NotePresenterImpl implements NotePresenter {

    private static final String TAG = NotePresenterImpl.class.getSimpleName();
    private NoteView mNoteView;
    private NoteModel mModel;
    private UserUtils mUserUtils;

    public NotePresenterImpl(Context context, NoteView noteView) {
        mNoteView = noteView;
        mModel = new NoteModelImpl((Context) noteView);
        mUserUtils = UserUtils.getInstance(context);
    }

    @Override
    public void bindDataByNote(Note note) {
        if (note == null) {
            mNoteView.setContentHint("请输入便签内容");
        } else {
            mNoteView.bind(note);
        }
    }

    /**
     * 保存note到数据库并关闭当前Activity.
     */
    @Override
    public void save(String action, Note oldNote, boolean isSave) {
        if (ConstantUtils.ACTION_NEW.equals(action)) {
            if (isSave) {
                Log.e(TAG, "ACTION_NEW");
                Note note = new Note();
                if (!TextUtils.isEmpty(mNoteView.getNoteContent())) {
                    note.setContent(mNoteView.getNoteContent());
                    note.setTitle(mNoteView.getNoteTitle());
                    note.setFavorite(false);
                    note.setNoteType(NoteUtil.NOTE_TYPE_NORMAL);
                    note.setCreateTime(new Date());
                    note.setLastModifiedTime(new Date());
                    note.setId(CommonUtils.getGuid());
                    note.setHasSync(false);
                    if (mUserUtils.isLogin()) {
                        note.setUserAuth(mUserUtils.getUserName());
                    } else {
                        note.setUserAuth("");
                    }
                    mModel.addNote(note);
                }
            }
        } else if (ConstantUtils.ACTION_EDIT.equals(action)) {
            if (isSave) {
                Log.e(TAG, "ACTION_EDIT");
                Note note = oldNote;
                if (!TextUtils.isEmpty(mNoteView.getNoteContent())) {
                    note.setContent(mNoteView.getNoteContent());
                    note.setTitle(mNoteView.getNoteTitle());
                    note.setFavorite(false);
                    note.setNoteType(NoteUtil.NOTE_TYPE_NORMAL);
                    note.setLastModifiedTime(new Date());
                    note.setId(note.getId());
                    note.setHasSync(false);
                    if (mUserUtils.isLogin()) {
                        note.setUserAuth(mUserUtils.getUserName());
                    } else {
                        note.setUserAuth("");
                    }
                    mModel.updateNote(note);
                }
            }
        }
        mNoteView.finishActivity();
    }
}
