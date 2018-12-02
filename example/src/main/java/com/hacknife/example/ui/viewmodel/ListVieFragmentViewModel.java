package com.hacknife.example.ui.viewmodel;

import com.hacknife.example.ui.ListVieFragmentActivityBriefnessor;

import com.hacknife.example.ui.base.BaseViewModel;
import com.hacknife.example.ui.model.ListVieFragmentModel;
import com.hacknife.example.ui.view.IListVieFragmentView;
import com.hacknife.example.ui.viewmodel.i.IListVieFragmentViewModel;
import com.hacknife.example.ui.model.i.IListVieFragmentModel;


/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
public class ListVieFragmentViewModel extends BaseViewModel<IListVieFragmentView, IListVieFragmentModel, ListVieFragmentActivityBriefnessor> implements IListVieFragmentViewModel {

    public ListVieFragmentViewModel(IListVieFragmentView view, ListVieFragmentActivityBriefnessor briefnessor) {
        super(view, briefnessor);
    }

    @Override
    protected IListVieFragmentModel createModel() {
        return new ListVieFragmentModel(this);
    }


}