package com.hacknife.example.ui.model;

import com.hacknife.example.api.Api;
import com.hacknife.example.bean.JsonRootBean;
import com.hacknife.example.net.HttpClient;
import com.hacknife.example.ui.base.BaseModel;
import com.hacknife.example.ui.model.i.IExampleModel;
import com.hacknife.example.ui.viewmodel.i.IExampleViewModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

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
    public void loadVideo(final int refresh) {
        HttpClient.create(Api.class)
                .videoList(
                        "TJLR2ROMDdSYkgyNHZnUy1uakZjUlczWjFaQktnajhzSTVTdnVFazJWMWR1Q0JjQUFBQUFBJCQAAAAAAAAAAAEAAAAyLikx83vRxcWuyfEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAF0r-VtdK~lbZ",
                        "wappc_1543054584036_494",
                        "2",
                        "9.8.8.13",
                        "000000000000000",
                        "250CDB0D377D3E290922959DCFB9B995|0",
                        "250CDB0D377D3E290922959DCFB9B995|0",
                        "",
                        "1014610n",
                        "1",
                        "vivo X20A",
                        "1",
                        "2",
                        "3CEFC58A17DA2273D48BA670E6D5D28B",
                        "1",
                        "1",
                        "1",
                        "911",
                        "261",
                        "1",
                        "personalize_page",
                        "55d84e89fa8dfbb7178ea83f564ca6180d3cab31521d802a0b59331395eaf4a1",
                        "5922326240",
                        "1543056517577",
                        "[{\"tid\":\"5922326240\",\"duration\":2},{\"tid\":\"5825709772\",\"duration\":8},{\"tid\":\"5814780009\",\"duration\":0},{\"tid\":\"5793344693\",\"duration\":0},{\"tid\":\"5676004897\",\"duration\":1},{\"tid\":\"5738278946\",\"duration\":0},{\"tid\":\"5810813951\",\"duration\":0},{\"tid\":\"5836613662\",\"duration\":0}]",
                        "114200#6#ui_tag_167,msd_normal,tw_tag_18,video_tag_126,live_tag_156"

                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<JsonRootBean>() {
                    @Override
                    public void accept(JsonRootBean jsonRootBean) throws Exception {
                        viewModel.callbackVideoList(jsonRootBean,refresh);
                    }
                });
    }
}