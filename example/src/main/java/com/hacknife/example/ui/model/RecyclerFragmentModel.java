package com.hacknife.example.ui.model;

import com.hacknife.example.ui.base.BaseModel;
import com.hacknife.example.ui.model.i.IRecyclerFragmentModel;
import com.hacknife.example.ui.viewmodel.i.IRecyclerFragmentViewModel;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
public class RecyclerFragmentModel extends BaseModel<IRecyclerFragmentViewModel> implements IRecyclerFragmentModel {

    public RecyclerFragmentModel(IRecyclerFragmentViewModel viewmodel) {
        super(viewmodel);
    }
}