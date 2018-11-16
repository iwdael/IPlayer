package com.hacknife.demo.CustomView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.hacknife.iplayer.Iplayer;

/**
 * Created by Nathen on 2016/11/6.
 */

public class IplayerShowTextureViewAfterAutoComplete extends Iplayer {
    public IplayerShowTextureViewAfterAutoComplete(Context context) {
        super(context);
    }

    public IplayerShowTextureViewAfterAutoComplete(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onAutoCompletion() {
        super.onAutoCompletion();
        iv_thumb.setVisibility(View.GONE);
    }

}
