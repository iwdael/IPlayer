package com.hacknife.example.ui.viewmodel;

import com.hacknife.example.ui.ExampleActivityBriefnessor;

import com.hacknife.example.ui.base.BaseViewModel;
import com.hacknife.example.ui.model.ExampleModel;
import com.hacknife.example.ui.view.IExampleView;
import com.hacknife.example.ui.viewmodel.i.IExampleViewModel;
import com.hacknife.example.ui.model.i.IExampleModel;


/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
public class ExampleViewModel extends BaseViewModel<IExampleView, IExampleModel, ExampleActivityBriefnessor> implements IExampleViewModel {

    public ExampleViewModel(IExampleView view, ExampleActivityBriefnessor briefnessor) {
        super(view, briefnessor);
    }

    @Override
    protected IExampleModel createModel() {
        return new ExampleModel(this);
    }


}