package com.xwj.xwjnote3.interactor.impl;

import com.xwj.xwjnote3.interactor.FindNoteItemInteractor;
import com.xwj.xwjnote3.interactor.OnFinishListener;
import com.xwj.xwjnote3.model.Note;

import java.util.ArrayList;


/**
 * Created by xwjsd on 2015/12/3.
 */
public class FindNoteItemInteractorImpl implements FindNoteItemInteractor {


    @Override
    public void findNoteItems(OnFinishListener listener) {

        listener.onFinish(new ArrayList<Note>());
    }

    /*private List<Note> generateList() {
        List<Note> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add("item" + i);
        }
        return list;
    }*/
}
