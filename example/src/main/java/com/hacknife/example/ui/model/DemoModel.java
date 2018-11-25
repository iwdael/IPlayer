package com.hacknife.example.ui.model;

import com.hacknife.example.ui.base.BaseModel;
import com.hacknife.example.ui.model.i.IDemoModel;
import com.hacknife.example.ui.viewmodel.i.IDemoViewModel;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
public class DemoModel extends BaseModel<IDemoViewModel> implements IDemoModel {

    public DemoModel(IDemoViewModel viewmodel) {
        super(viewmodel);
    }
}