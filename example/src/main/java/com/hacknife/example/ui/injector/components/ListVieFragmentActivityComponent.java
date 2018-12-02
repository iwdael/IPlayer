package com.hacknife.example.ui.injector.components;

import com.hacknife.example.ui.injector.modules.FragmentAdapterModule;
import com.hacknife.example.ui.injector.modules.ListVieFragmentModule;
import com.hacknife.example.ui.ListVieFragmentActivity;

import dagger.Component;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
@Component(modules = {ListVieFragmentModule.class, FragmentAdapterModule.class})
public interface ListVieFragmentActivityComponent {
    void inject(ListVieFragmentActivity activity);
}