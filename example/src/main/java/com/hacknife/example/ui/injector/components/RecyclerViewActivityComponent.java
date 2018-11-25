package com.hacknife.example.ui.injector.components;

import com.hacknife.example.ui.injector.modules.RecyclerViewModule;
import com.hacknife.example.ui.RecyclerViewActivity;

import dagger.Component;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
@Component(modules = RecyclerViewModule.class)
public interface RecyclerViewActivityComponent {
    void inject(RecyclerViewActivity activity);
}