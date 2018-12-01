package com.hacknife.example.ui.viewmodel;

import com.hacknife.example.ui.RecyclerFragmentActivityBriefnessor;

import com.hacknife.example.ui.base.BaseViewModel;
import com.hacknife.example.ui.model.RecyclerFragmentModel;
import com.hacknife.example.ui.view.IRecyclerFragmentView;
import com.hacknife.example.ui.viewmodel.i.IRecyclerFragmentViewModel;
import com.hacknife.example.ui.model.i.IRecyclerFragmentModel;


/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
public class RecyclerFragmentViewModel extends BaseViewModel<IRecyclerFragmentView, IRecyclerFragmentModel, RecyclerFragmentActivityBriefnessor> implements IRecyclerFragmentViewModel {

    public RecyclerFragmentViewModel(IRecyclerFragmentView view, RecyclerFragmentActivityBriefnessor briefnessor) {
        super(view, briefnessor);
    }

    @Override
    protected IRecyclerFragmentModel createModel() {
        return new RecyclerFragmentModel(this);
    }


}