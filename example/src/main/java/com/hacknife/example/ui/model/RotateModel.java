package com.hacknife.example.ui.model;

import com.hacknife.example.ui.base.BaseModel;
import com.hacknife.example.ui.model.i.IRotateModel;
import com.hacknife.example.ui.viewmodel.i.IRotateViewModel;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
public class RotateModel extends BaseModel<IRotateViewModel> implements IRotateModel {

    public RotateModel(IRotateViewModel viewmodel) {
        super(viewmodel);
    }
}