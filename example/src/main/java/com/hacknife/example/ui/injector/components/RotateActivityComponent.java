package com.hacknife.example.ui.injector.components;

import com.hacknife.example.ui.injector.modules.RotateModule;
import com.hacknife.example.ui.RotateActivity;

import dagger.Component;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
@Component(modules = RotateModule.class)
public interface RotateActivityComponent {
    void inject(RotateActivity activity);
}