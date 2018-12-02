package com.hacknife.example.ui.viewmodel;

import com.hacknife.example.ui.SecondPlayerActivityBriefnessor;

import com.hacknife.example.ui.base.BaseViewModel;
import com.hacknife.example.ui.model.SecondPlayerModel;
import com.hacknife.example.ui.view.ISecondPlayerView;
import com.hacknife.example.ui.viewmodel.i.ISecondPlayerViewModel;
import com.hacknife.example.ui.model.i.ISecondPlayerModel;


/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
public class SecondPlayerViewModel extends BaseViewModel<ISecondPlayerView, ISecondPlayerModel, SecondPlayerActivityBriefnessor> implements ISecondPlayerViewModel {

    public SecondPlayerViewModel(ISecondPlayerView view, SecondPlayerActivityBriefnessor briefnessor) {
        super(view, briefnessor);
    }

    @Override
    protected ISecondPlayerModel createModel() {
        return new SecondPlayerModel(this);
    }


}