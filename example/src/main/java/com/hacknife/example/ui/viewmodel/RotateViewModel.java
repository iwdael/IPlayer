package com.hacknife.example.ui.viewmodel;

import com.hacknife.example.ui.RotateActivityBriefnessor;

import com.hacknife.example.ui.base.BaseViewModel;
import com.hacknife.example.ui.model.RotateModel;
import com.hacknife.example.ui.view.IRotateView;
import com.hacknife.example.ui.viewmodel.i.IRotateViewModel;
import com.hacknife.example.ui.model.i.IRotateModel;


/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
public class RotateViewModel extends BaseViewModel<IRotateView, IRotateModel, RotateActivityBriefnessor> implements IRotateViewModel {

    public RotateViewModel(IRotateView view, RotateActivityBriefnessor briefnessor) {
        super(view, briefnessor);
    }

    @Override
    protected IRotateModel createModel() {
        return new RotateModel(this);
    }


}