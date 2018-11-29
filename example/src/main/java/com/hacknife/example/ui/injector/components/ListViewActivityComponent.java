package com.hacknife.example.ui.injector.components;

import com.hacknife.example.ui.injector.modules.ListViewModule;
import com.hacknife.example.ui.ListViewActivity;

import dagger.Component;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
@Component(modules = ListViewModule.class)
public interface ListViewActivityComponent {
    void inject(ListViewActivity activity);
}