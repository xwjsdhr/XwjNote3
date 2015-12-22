package com.xwj.xwjnote3.view;

import com.xwj.xwjnote3.model.MyUser;
import com.xwj.xwjnote3.model.Note;

import java.util.List;


/**
 * View接口类.
 * Created by xwjsd on 2015/12/3.
 */
public interface NoteListView {

    public void showProgress();

    public void hideProgress();

    public void setList(List<Note> list);

    public void switchRecyclerView(List<Note> list);

    public void closeDrawer();

    public void openDrawer();

    void showMessage(String msg);

    void setLayoutManager(int index);

    // void setLayoutManager(String str);
    void showChooseLayoutManagerDialog();


    void showFloatActionButton();

    void hideFloatActionButton();

    void switchFabIcon();

    void setUserInfo(MyUser userInfo);

    void showSwipeProgress();

    void hideSwipeProgress();
}
