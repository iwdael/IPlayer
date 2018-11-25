package com.hacknife.example.ui.base;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.hacknife.briefness.Briefness;
import com.hacknife.briefness.Briefnessor;
import com.hacknife.example.R;
import com.hacknife.immersive.Immersive;

import javax.inject.Inject;


/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : briefness
 */
public abstract class BaseActivity<T extends IBaseViewModel, B extends Briefnessor> extends AppCompatActivity implements IBaseView {
    @Inject
    protected T viewModel;
    protected B briefnessor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        briefnessor = (B) Briefness.bind(this);
        injector();
        briefnessor.bindViewModel(viewModel);
        initView();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (viewModel != null)
            viewModel.dettachView();
    }

    protected abstract void injector();

    protected void initView() {
    }

    @Override
    public Context applicationContext() {
        return getApplication();
    }

    @Override
    public Activity context() {
        return this;
    }
}
