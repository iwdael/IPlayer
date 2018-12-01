package com.hacknife.example.ui.injector.modules;

import android.support.v4.app.FragmentManager;

import com.hacknife.example.adapter.FragmentAdapter;
import com.hacknife.example.ui.DemoActivityBriefnessor;
import com.hacknife.example.ui.view.IDemoView;
import com.hacknife.example.ui.viewmodel.DemoViewModel;
import com.hacknife.example.ui.viewmodel.i.IDemoViewModel;

import dagger.Module;
import dagger.Provides;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : IPlayer
 */
@Module
public class FragmentAdapterModule {

    private final FragmentManager fm;

    public FragmentAdapterModule(FragmentManager fm) {
        this.fm = fm;
    }

    @Provides
    FragmentAdapter provideViewModel() {
        return new FragmentAdapter(fm);
    }
}


