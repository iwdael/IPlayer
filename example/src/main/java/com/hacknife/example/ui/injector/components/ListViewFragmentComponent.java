package com.hacknife.example.ui.injector.components;

import com.hacknife.example.ui.injector.modules.ListViewFragModule;
import com.hacknife.example.ui.ListViewFragment;

import dagger.Component;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
@Component(modules = ListViewFragModule.class)
public interface ListViewFragmentComponent {
    void inject(ListViewFragment activity);
}