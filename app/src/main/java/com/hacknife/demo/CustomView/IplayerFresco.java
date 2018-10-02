package com.hacknife.demo.CustomView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.hacknife.iplayer.DataSource;
import com.hacknife.iplayer.Iplayer;
import com.hacknife.demo.R;

//import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Just replace thumb from ImageView to SimpleDraweeView
 * Created by Nathen
 * On 2016/05/01 22:59
 */
public class IplayerFresco extends Iplayer {
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
        bottomProgressBar = findViewById(R.id.bottom_progress);
        titleTextView = findViewById(R.id.title);
        backButton = findViewById(R.id.back);
//        thumbImageView = findViewById(R.id.thumb);
        loadingProgressBar = findViewById(R.id.loading);
        tinyBackImageView = findViewById(R.id.back_tiny);

//        thumbImageView.setOnClickListener(this);
        backButton.setOnClickListener(this);
        tinyBackImageView.setOnClickListener(this);

    }

    @Override
    public void setUp(DataSource jzDataSource, int screen) {
        super.setUp(jzDataSource, screen);
        titleTextView.setText(jzDataSource.title);
        if (currentScreen == SCREEN_WINDOW_FULLSCREEN) {
            fullscreenButton.setImageResource(R.drawable.jz_shrink);
            backButton.setVisibility(View.VISIBLE);
            tinyBackImageView.setVisibility(View.INVISIBLE);
        } else if (currentScreen == SCREEN_WINDOW_LIST) {
            fullscreenButton.setImageResource(R.drawable.jz_enlarge);
            backButton.setVisibility(View.GONE);
            tinyBackImageView.setVisibility(View.INVISIBLE);
        } else if (currentScreen == SCREEN_WINDOW_TINY) {
            tinyBackImageView.setVisibility(View.VISIBLE);
            setAllControlsVisiblity(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
                    View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_standard_fresco;
    }


}
