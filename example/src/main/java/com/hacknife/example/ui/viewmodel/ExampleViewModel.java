package com.hacknife.example.ui.viewmodel;

import com.hacknife.example.bean.JsonRootBean;
import com.hacknife.example.bean.VideoList;
import com.hacknife.example.bean.VideoSource;
import com.hacknife.example.ui.ExampleActivityBriefnessor;

import com.hacknife.example.ui.base.BaseViewModel;
import com.hacknife.example.ui.model.ExampleModel;
import com.hacknife.example.ui.view.IExampleView;
import com.hacknife.example.ui.viewmodel.i.IExampleViewModel;
import com.hacknife.example.ui.model.i.IExampleModel;

import java.util.ArrayList;
import java.util.List;


/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : MVVM
 */
public class ExampleViewModel extends BaseViewModel<IExampleView, IExampleModel, ExampleActivityBriefnessor> implements IExampleViewModel {

    public ExampleViewModel(IExampleView view, ExampleActivityBriefnessor briefnessor) {
        super(view, briefnessor);
    }



    public void loadVideo(int refresh) {
        model.loadVideo(refresh);
    }

    @Override
    public void callbackVideoList(JsonRootBean jsonRootBean, int refresh) {
        List<VideoList> lists = jsonRootBean.getList();
        List<VideoSource> data = new ArrayList<>(lists.size());
        for (VideoList list : lists) {
            data.add(new VideoSource(list.getVideo().getOrigin_video_url(), list.getTitle(), list.getVideo().getThumbnail_url()));
        }
        view.callbackVideo(data,refresh);
    }

    @Override
    protected IExampleModel createModel() {
        return new ExampleModel(this);
    }


}