package com.hacknife.example.ui.viewmodel;

import com.hacknife.example.ui.DemoActivityBriefnessor;

import com.hacknife.example.ui.base.BaseViewModel;
import com.hacknife.example.ui.model.DemoModel;
import com.hacknife.example.ui.view.IDemoView;
import com.hacknife.example.ui.viewmodel.i.IDemoViewModel;
import com.hacknife.example.ui.model.i.IDemoModel;


/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
public class DemoViewModel extends BaseViewModel<IDemoView, IDemoModel, DemoActivityBriefnessor> implements IDemoViewModel {

    public DemoViewModel(IDemoView view, DemoActivityBriefnessor briefnessor) {
        super(view, briefnessor);
    }

    @Override
    protected IDemoModel createModel() {
        return new DemoModel(this);
    }


}