package com.xwj.xwjnote3.interactor;

import com.xwj.xwjnote3.model.Note;

import java.util.List;


/**
 * 查询数据完成的监听器接口。
 * Created by xwjsd on 2015/12/3.
 */
public interface OnFinishListener {
    public void onFinish(List<Note> list);
}
