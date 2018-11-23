package com.hacknife.example.ui.model;

import com.hacknife.example.ui.base.BaseModel;
import com.hacknife.example.ui.model.i.IExampleModel;
import com.hacknife.example.ui.viewmodel.i.IExampleViewModel;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
public class ExampleModel extends BaseModel<IExampleViewModel> implements IExampleModel {

    public ExampleModel(IExampleViewModel viewmodel) {
        super(viewmodel);
    }
}