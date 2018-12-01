package com.hacknife.example.ui.injector.components;

import com.hacknife.example.ui.injector.modules.FragmentAdapterModule;
import com.hacknife.example.ui.injector.modules.RecyclerFragmentModule;
import com.hacknife.example.ui.RecyclerFragmentActivity;

import dagger.Component;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
@Component(modules = {RecyclerFragmentModule.class, FragmentAdapterModule.class})
public interface RecyclerFragmentActivityComponent {
    void inject(RecyclerFragmentActivity activity);
}