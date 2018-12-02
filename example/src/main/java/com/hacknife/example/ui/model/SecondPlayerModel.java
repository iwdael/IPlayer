package com.hacknife.example.ui.model;

import com.hacknife.example.ui.base.BaseModel;
import com.hacknife.example.ui.model.i.ISecondPlayerModel;
import com.hacknife.example.ui.viewmodel.i.ISecondPlayerViewModel;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
public class SecondPlayerModel extends BaseModel<ISecondPlayerViewModel> implements ISecondPlayerModel {

    public SecondPlayerModel(ISecondPlayerViewModel viewmodel) {
        super(viewmodel);
    }
}