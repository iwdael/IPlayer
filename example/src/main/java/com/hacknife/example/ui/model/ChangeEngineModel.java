package com.hacknife.example.ui.model;

import com.hacknife.example.ui.base.BaseModel;
import com.hacknife.example.ui.model.i.IChangeEngineModel;
import com.hacknife.example.ui.viewmodel.i.IChangeEngineViewModel;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
public class ChangeEngineModel extends BaseModel<IChangeEngineViewModel> implements IChangeEngineModel {

    public ChangeEngineModel(IChangeEngineViewModel viewmodel) {
        super(viewmodel);
    }
}