package com.hacknife.example.ui.injector.components;

import com.hacknife.example.ui.injector.modules.ChangeEngineModule;
import com.hacknife.example.ui.ChangeEngineActivity;

import dagger.Component;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
@Component(modules = ChangeEngineModule.class)
public interface ChangeEngineActivityComponent {
    void inject(ChangeEngineActivity activity);
}