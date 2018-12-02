package com.hacknife.example.ui.model;

import com.hacknife.example.ui.base.BaseModel;
import com.hacknife.example.ui.model.i.IListVieFragmentModel;
import com.hacknife.example.ui.viewmodel.i.IListVieFragmentViewModel;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
public class ListVieFragmentModel extends BaseModel<IListVieFragmentViewModel> implements IListVieFragmentModel {

    public ListVieFragmentModel(IListVieFragmentViewModel viewmodel) {
        super(viewmodel);
    }
}