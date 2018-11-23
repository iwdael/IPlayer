package com.hacknife.example.ui;


import com.hacknife.example.R;
import com.hacknife.example.ui.base.BaseActivity;
import com.hacknife.example.ui.injector.modules.ExampleModule;

import android.os.Bundle;


import com.hacknife.example.ui.view.IExampleView;
import com.hacknife.example.ui.viewmodel.ExampleViewModel;
import com.hacknife.briefness.BindLayout;
import com.hacknife.example.ui.injector.components.DaggerExampleActivityComponent;
import com.hacknife.example.ui.viewmodel.i.IExampleViewModel;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
@BindLayout(R.layout.activity_example)
public class ExampleActivity extends BaseActivity<IExampleViewModel, ExampleActivityBriefnessor> implements IExampleView {

    @Override
    protected void injector() {
        DaggerExampleActivityComponent.builder()
                .exampleModule(new ExampleModule(this, briefnessor))
                .build().inject(this);
    }
}
