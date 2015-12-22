package com.xwj.xwjnote3.presenter;


import com.xwj.xwjnote3.model.Note;

/**
 * Created by xwjsd on 2015/12/3.
 */
public interface NotePresenter {

    void bindDataByNote(Note note);

    void save(String action, Note mNote, boolean b);
}
