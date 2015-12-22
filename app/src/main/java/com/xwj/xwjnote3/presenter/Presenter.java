package com.xwj.xwjnote3.presenter;

import android.view.MenuItem;

import com.xwj.xwjnote3.model.Note;

import java.util.List;


/**
 * Created by xwjsd on 2015/12/3.
 */
public interface Presenter {

    public void showProgress();

    void onResume();

    public void hideProgress();

    public void closeDrawer();

    public void openDrawer();

    void switchList(int checkId);

    void switchAction(MenuItem id);

    void searchByContent(String query, boolean b);

    void moveToTrash(Note note);

    void deleteNote(Note note);

    void restoreNote(Note note);

    void shareNote(Note note);

    void favoriteNote(Note note, boolean isFavorite);

    void clearTrashBin();

    void syncNote(List<Note> list);

    void getBinSize();

    void startUserInfoActivity();
}
