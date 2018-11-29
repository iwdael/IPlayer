package com.hacknife.example.ui.injector.components;

import com.hacknife.example.ui.injector.modules.RecyclerViewMultiHolderModule;
import com.hacknife.example.ui.RecyclerViewMultiHolderActivity;

import dagger.Component;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
@Component(modules = RecyclerViewMultiHolderModule.class)
public interface RecyclerViewMultiHolderActivityComponent {
    void inject(RecyclerViewMultiHolderActivity activity);
}