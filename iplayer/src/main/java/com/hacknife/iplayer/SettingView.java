package com.hacknife.iplayer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : iplayer
 */
public class SettingView extends FrameLayout implements View.OnClickListener {
    TextView rotate90;
    TextView rotate180;
    TextView rotate270;
    TextView size4_3;
    TextView size16_9;
    TextView sizeFull;

    public SettingView(@NonNull Context context) {
        this(context, null);
    }

    public SettingView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = View.inflate(context, R.layout.iplayer_layout_setting, null);
        addView(view);
        rotate90 = view.findViewById(R.id.iplayer_tv_rotate_90);
        rotate180 = view.findViewById(R.id.iplayer_tv_rotate_180);
        rotate270 = view.findViewById(R.id.iplayer_tv_rotate_270);
        size4_3 = view.findViewById(R.id.iplayer_tv_size_4_3);
        size16_9 = view.findViewById(R.id.iplayer_tv_size_16_9);
        sizeFull = view.findViewById(R.id.iplayer_tv_size_full);

        rotate90.setOnClickListener(this);
        rotate180.setOnClickListener(this);
        rotate270.setOnClickListener(this);
        size4_3.setOnClickListener(this);
        size16_9.setOnClickListener(this);
        sizeFull.setOnClickListener(this);
    }

    public void resetRotate() {
        rotate90.setTextColor(getResources().getColor(R.color.iplayer_setting_text_color));
        rotate180.setTextColor(getResources().getColor(R.color.iplayer_setting_text_color));
        rotate270.setTextColor(getResources().getColor(R.color.iplayer_setting_text_color));
    }

    public void resetSize() {
        size4_3.setTextColor(getResources().getColor(R.color.iplayer_setting_text_color));
        size16_9.setTextColor(getResources().getColor(R.color.iplayer_setting_text_color));
        sizeFull.setTextColor(getResources().getColor(R.color.iplayer_setting_text_color));
    }

    @Override
    public void onClick(View view) {
        if (onSettingListener == null) return;
        if (view.getId() == R.id.iplayer_tv_rotate_90) {
            resetRotate();
            rotate90.setTextColor(getResources().getColor(R.color.iplayer_setting_text_color_selected));
            onSettingListener.onRatate(90);
        }
        if (view.getId() == R.id.iplayer_tv_rotate_180) {
            resetRotate();
            rotate180.setTextColor(getResources().getColor(R.color.iplayer_setting_text_color_selected));
            onSettingListener.onRatate(180);
        }
        if (view.getId() == R.id.iplayer_tv_rotate_270) {
            resetRotate();
            rotate270.setTextColor(getResources().getColor(R.color.iplayer_setting_text_color_selected));
            onSettingListener.onRatate(270);
        }
        if (view.getId() == R.id.iplayer_tv_size_4_3) {
            resetSize();
            size4_3.setTextColor(getResources().getColor(R.color.iplayer_setting_text_color_selected));
            onSettingListener.onSize(1);
        }
        if (view.getId() == R.id.iplayer_tv_size_16_9) {
            resetSize();
            size16_9.setTextColor(getResources().getColor(R.color.iplayer_setting_text_color_selected));
            onSettingListener.onSize(2);
        }
        if (view.getId() == R.id.iplayer_tv_size_full) {
            resetSize();
            sizeFull.setTextColor(getResources().getColor(R.color.iplayer_setting_text_color_selected));
            onSettingListener.onSize(3);
        }
    }

    OnSettingListener onSettingListener;

    public void setOnSettingListener(OnSettingListener onSettingListener) {
        this.onSettingListener = onSettingListener;
    }

    public interface OnSettingListener {
        void onRatate(int angle);

        void onSize(int size);
    }
}
