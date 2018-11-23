package com.hacknife.example.ui.model;

import com.hacknife.example.bean.VideoSource;
import com.hacknife.example.ui.base.BaseModel;
import com.hacknife.example.ui.model.i.IExampleModel;
import com.hacknife.example.ui.viewmodel.i.IExampleViewModel;
import com.hacknife.iplayer.DataSource;

import java.util.ArrayList;
import java.util.List;

import static com.hacknife.example.constant.Constant.img;
import static com.hacknife.example.constant.Constant.title;
import static com.hacknife.example.constant.Constant.url;

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

    @Override
    public void loadVideo() {
        List<VideoSource> dataSources=new ArrayList<>(title.length);
        for (int i = 0; i < title.length; i++) {
            dataSources.add(new VideoSource(url[i],title[i],img[i]));
        }
        viewModel.callBackVideo(dataSources);
    }
}