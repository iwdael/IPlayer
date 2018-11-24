package com.hacknife.example.net;

import android.os.Build;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by hzwangchenyan on 2017/3/30.
 */
public class HttpInterceptor implements Interceptor {
    private static final String UA = "User-Agent";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request()
                .newBuilder()
                .addHeader("User-Agent", "bdtb for Android 9.8.8.13")
                .addHeader("Cookie", "ka=open")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Charset", "UTF-8")
                .addHeader("client_logid", "1543055518570")
                .addHeader("client_user_token", "824782386")
                .addHeader("cuid", "250CDB0D377D3E290922959DCFB9B995|0")
                .addHeader("cuid_galaxy2", "250CDB0D377D3E290922959DCFB9B995|0")
                .addHeader("cuid_gid", "")
                .addHeader("Accept-Encoding", "")
                .addHeader("Host", "c.tieba.baidu.com")
                .build();
        return chain.proceed(request);
    }

    private String makeUA() {
        return Build.BRAND + "/" + Build.MODEL + "/" + Build.VERSION.RELEASE;
    }
}
