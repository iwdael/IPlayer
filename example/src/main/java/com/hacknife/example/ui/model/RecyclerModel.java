package com.hacknife.example.ui.model;

import com.hacknife.example.constant.Constant;
import com.hacknife.example.ui.base.BaseModel;
import com.hacknife.example.ui.model.i.IRecyclerModel;
import com.hacknife.example.ui.viewmodel.i.IRecyclerViewModel;
import com.hacknife.iplayer.DataSource;

import java.util.ArrayList;
import java.util.List;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
public class RecyclerModel extends BaseModel<IRecyclerViewModel> implements IRecyclerModel {

    public RecyclerModel(IRecyclerViewModel viewmodel) {
        super(viewmodel);
    }
    @Override
    public void loadDataSource() {
        List<DataSource> dataSources = new ArrayList<>(Constant.img.length);
        int count = Constant.img.length;
        for (int i = 0; i < count; i++) {
            dataSources.add(new DataSource(Constant.url[i], Constant.title[i], Constant.img[i]));
        }
        viewModel.callbackDataSource(dataSources);
    }
}