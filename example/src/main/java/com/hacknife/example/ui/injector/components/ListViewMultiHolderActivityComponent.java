package com.hacknife.example.ui.injector.components;

import com.hacknife.example.ui.injector.modules.ListViewMultiHolderModule;
import com.hacknife.example.ui.ListViewMultiHolderActivity;

import dagger.Component;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
@Component(modules = ListViewMultiHolderModule.class)
public interface ListViewMultiHolderActivityComponent {
    void inject(ListViewMultiHolderActivity activity);
}