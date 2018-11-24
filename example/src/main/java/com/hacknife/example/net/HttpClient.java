package com.hacknife.example.net;


import com.hacknife.example.api.ApiConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : IMusic
 */
public class HttpClient {
    private static HttpClient sHttpClitent;
    private final Retrofit retrofit;

    private static HttpClient get() {
        if (sHttpClitent == null) {
            synchronized (HttpClient.class) {
                if (sHttpClitent == null) {
                    sHttpClitent = new HttpClient();
                }
            }
        }
        return sHttpClitent;
    }

    private HttpClient() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient okClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(new HttpInterceptor())
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(ApiConfig.baseUrl)
                .client(okClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

    public static <T> T create(Class<T> clazz) {
        return get().retrofit.create(clazz);
    }


}
