package com.hacknife.demo.CustomView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.hacknife.iplayer.DataSource;
import com.hacknife.iplayer.IPlayer;
import com.hacknife.demo.R;

//import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Just replace thumb from ImageView to SimpleDraweeView
 * Created by Nathen
 * On 2016/05/01 22:59
 */
public class IplayerFresco extends IPlayer {
    //    public SimpleDraweeView thumbImageView;

    public IplayerFresco(Context context) {
        super(context);
    }

    public IplayerFresco(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void init(Context context) {
        super.init(context);
        pro_bottom = findViewById(R.id.pro_bottom);
        tv_title = findViewById(R.id.tv_title);
        iv_back = findViewById(R.id.iv_back);
//        thumbImageView = findViewById(R.id.thumb);
        pro_loading = findViewById(R.id.pro_loading);
        iv_back_tiny = findViewById(R.id.iv_back_tiny);

//        thumbImageView.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        iv_back_tiny.setOnClickListener(this);

    }

    @Override
    public void setDataSource(DataSource jzDataSource, int screen) {
        super.setDataSource(jzDataSource, screen);
        tv_title.setText(jzDataSource.title);
        if (currentScreen == SCREEN_WINDOW_FULLSCREEN) {
            iv_fullscreen.setImageResource(R.drawable.iplayer_shrink);
            iv_back.setVisibility(View.VISIBLE);
            iv_back_tiny.setVisibility(View.INVISIBLE);
        } else if (currentScreen == SCREEN_WINDOW_LIST) {
            iv_fullscreen.setImageResource(R.drawable.iplayer_enlarge);
            iv_back.setVisibility(View.GONE);
            iv_back_tiny.setVisibility(View.INVISIBLE);
        } else if (currentScreen == SCREEN_WINDOW_TINY) {
            iv_back_tiny.setVisibility(View.VISIBLE);
            setAllControlsVisiblity(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
                    View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_standard_fresco;
    }


}
