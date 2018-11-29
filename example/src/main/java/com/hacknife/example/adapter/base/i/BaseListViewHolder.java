package com.hacknife.example.adapter.base.i;

import android.view.View;

import com.hacknife.briefness.Briefness;
import com.hacknife.briefness.Briefnessor;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : IPlayer
 */
public abstract class BaseListViewHolder<T, B extends Briefnessor> {
    protected View view;
    protected B briefnessor;

    public BaseListViewHolder(View view) {
        this.view = view;
        briefnessor = (B) Briefness.bind(this, view);
    }

    protected abstract void bindData(T t);

    public View getView() {
        return view;
    }
}
