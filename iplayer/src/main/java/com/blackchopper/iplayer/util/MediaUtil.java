package com.blackchopper.iplayer.util;

/**
 * author  : Black Chopper
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/BlackChopper
 * project : IPlayer
 */
public class MediaUtil {
    private final static int HOUR = 3600 * 1000;
    private final static int MINUTE = 60 * 1000;
    private final static int SECOND = 1000;

    public static String duration2Time(int time) {
        StringBuilder builder = new StringBuilder();
        if (time > HOUR) {
            int hour = time / HOUR;
            builder.append("0").append(String.valueOf(hour)).append(":");

            int minute = (time % HOUR) / MINUTE;
            if (minute < 10) {
                builder.append("0").append(String.valueOf(minute));
            } else {
                builder.append(String.valueOf(minute));
            }
        } else {
            int minute = time / MINUTE;
            if (minute < 10) {
                builder.append("0").append(String.valueOf(minute));
            } else {
                builder.append(String.valueOf(minute));
            }
            builder.append(":");

            int second = (time % MINUTE) / SECOND;
            if (second < 10) {
                builder.append("0").append(String.valueOf(second));
            } else {
                builder.append(String.valueOf(second));
            }
        }
        return builder.toString();
    }
}
