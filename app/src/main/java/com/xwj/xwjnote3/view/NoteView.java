package com.xwj.xwjnote3.view;


import com.xwj.xwjnote3.model.Note;

/**
 * Created by xwjsd on 2015/12/3.
 */
public interface NoteView {
    void bind(Note note);

    void setContentHint(String hint);

    String getNoteContent();

    String getNoteTitle();

    void finishActivity();

    void pickImage();

}
