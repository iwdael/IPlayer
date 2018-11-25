package com.hacknife.example.ui.injector.components;

import com.hacknife.example.ui.injector.modules.DemoModule;
import com.hacknife.example.ui.DemoActivity;

import dagger.Component;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
@Component(modules = DemoModule.class)
public interface DemoActivityComponent {
    void inject(DemoActivity activity);
}