package com.xwj.xwjnote3.ui;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * item的装饰类
 * Created by xwjsd on 2015/12/3.
 */
public class MyItemDecoration extends RecyclerView.ItemDecoration {
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = outRect.left + 10;
        outRect.top = outRect.top + 10;
        outRect.right = outRect.right + 10;
        outRect.bottom = outRect.bottom + 10;
    }
}
