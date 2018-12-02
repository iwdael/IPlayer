package com.hacknife.example.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : IPlayer
 */
public class FragmentAdapter extends FragmentPagerAdapter {
    List<Fragment> fragments;

    public FragmentAdapter(FragmentManager fm) {
        super(fm);
        fragments = new ArrayList<>();
    }

    public void bindData(Fragment... fragments) {
        this.fragments.clear();
        this.fragments.addAll(Arrays.asList(fragments));
    }

    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return "第" + position + "页";
    }
}
