package com.hacknife.example.ui.injector.components;

import com.hacknife.example.ui.injector.modules.RecyclerModule;
import com.hacknife.example.ui.RecyclerFragment;

import dagger.Component;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
@Component(modules = RecyclerModule.class)
public interface RecyclerFragmentComponent {
    void inject(RecyclerFragment activity);
}