package com.xwj.xwjnote3.interactor.impl;

import android.content.Context;

import com.xwj.xwjnote3.interactor.OnFinishListener;
import com.xwj.xwjnote3.interactor.SwitchListInteractor;
import com.xwj.xwjnote3.model.Note;
import com.xwj.xwjnote3.model.NoteModel;
import com.xwj.xwjnote3.model.impl.NoteModelImpl;
import com.xwj.xwjnote3.utils.NoteUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * 切换列表交互的实现类。
 * Created by xwjsd on 2015/12/3.
 */
public class SwitchListInteractorImpl implements SwitchListInteractor {

    private Context mContext;
    private NoteModel mModel;

    public SwitchListInteractorImpl(Context context) {
        this.mContext = context;
        mModel = new NoteModelImpl(context);
    }

    @Override
    public void switchList(int checkId, OnFinishListener listener) {
        List<Note> list = new ArrayList<>();
        if (checkId == 0) {
            list.addAll(mModel.getAllNotes());
        } else if (checkId == 1) {
            //none
            list.addAll(mModel.getAllFavoriteNotes());
        } else if (checkId == 2) {
            list.addAll(mModel.getNotes(NoteUtil.NOTE_TYPE_TRASH));
        }
        listener.onFinish(list);
    }
}
