package com.hacknife.example.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : IPlayer
 */
public class RecyclerView extends android.support.v7.widget.RecyclerView {

    private LinearLayoutManager layout;
    private int heightPixels;

    public RecyclerView(Context context) {
        super(context);
        init(context);
    }


    public RecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        heightPixels = context.getResources().getDisplayMetrics().heightPixels;
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        this.layout = (LinearLayoutManager) layout;
        super.setLayoutManager(layout);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_UP) {
            int index = layout.findFirstVisibleItemPosition();
            Log.v("TAG", "----index--->>" + index);
            int top = getChildAt(index).getTop();
            Log.v("TAG", "----top--->>" + top);
            if (top < 0) {
                if (top + heightPixels < heightPixels / 2) {
                    smoothScrollToPosition(index + 1);
                } else {
                    smoothScrollToPosition(index);
                }
            } else {
                if (heightPixels - top > heightPixels / 2) {
                    smoothScrollToPosition(index + 1);
                } else {
                    smoothScrollToPosition(index);
                }

            }

        }
        return super.onTouchEvent(e);
    }
}
