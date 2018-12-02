package com.hacknife.example.ui.viewmodel;

import com.hacknife.example.ui.ChangeEngineActivityBriefnessor;

import com.hacknife.example.ui.base.BaseViewModel;
import com.hacknife.example.ui.model.ChangeEngineModel;
import com.hacknife.example.ui.view.IChangeEngineView;
import com.hacknife.example.ui.viewmodel.i.IChangeEngineViewModel;
import com.hacknife.example.ui.model.i.IChangeEngineModel;


/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
public class ChangeEngineViewModel extends BaseViewModel<IChangeEngineView, IChangeEngineModel, ChangeEngineActivityBriefnessor> implements IChangeEngineViewModel {

    public ChangeEngineViewModel(IChangeEngineView view, ChangeEngineActivityBriefnessor briefnessor) {
        super(view, briefnessor);
    }

    @Override
    protected IChangeEngineModel createModel() {
        return new ChangeEngineModel(this);
    }


}