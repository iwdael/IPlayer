package com.hacknife.example.ui.injector.components;

import com.hacknife.example.ui.injector.modules.ExampleModule;
import com.hacknife.example.ui.ExampleActivity;

import dagger.Component;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
@Component(modules = ExampleModule.class)
public interface ExampleActivityComponent {
    void inject(ExampleActivity activity);
}