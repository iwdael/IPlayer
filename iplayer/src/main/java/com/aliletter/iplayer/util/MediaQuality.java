package com.aliletter.iplayer.util;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author: aliletter
 * Github: http://github.com/aliletter
 * Data: 2017/11/16.
 */

public class MediaQuality implements Parcelable {
    public final static int SD = 1;
    public final static int HD = 2;
    public final static int ULTRA = 3;
    public final static int BLU = 4;
    public final static int _1080 = 5;
    public Integer type;
    public String url;

    public MediaQuality(Integer type, String url) {
        this.type = type;
        this.url = url;
    }

    protected MediaQuality(Parcel in) {
        url = in.readString();
    }

    public static final Creator<MediaQuality> CREATOR = new Creator<MediaQuality>() {
        @Override
        public MediaQuality createFromParcel(Parcel in) {
            return new MediaQuality(in.readInt(), in.readString());
        }

        @Override
        public MediaQuality[] newArray(int size) {
            return new MediaQuality[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(type);
        parcel.writeString(url);
    }
}
