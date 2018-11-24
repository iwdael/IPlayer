package com.hacknife.example.api;

import com.hacknife.example.bean.JsonRootBean;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : IPlayer
 */
public interface Api {
    @FormUrlEncoded
    @POST(ApiConfig.VIDEO_LIST)
    Observable<JsonRootBean> videoList(
            @Field("BDUSS") String BDUSS,
            @Field("_client_id") String _client_id,
            @Field("_client_type") String _client_type,
            @Field("_client_version") String _client_version,
            @Field("_phone_imei") String _phone_imei,
            @Field("cuid") String cuid,
            @Field("cuid_galaxy2") String cuid_galaxy2,
            @Field("cuid_gid") String cuid_gid,
            @Field("from") String from,
            @Field("is_vertical") String is_vertical,
            @Field("model") String model,
            @Field("net_type") String net_type,
            @Field("pn") String pn,
            @Field("sign") String sign,
            @Field("stErrorNums") String stErrorNums,
            @Field("stMethod") String stMethod,
            @Field("stMode") String stMode,
            @Field("stSize") String stSize,
            @Field("stTime") String stTime,
            @Field("stTimesNum") String stTimesNum,
            @Field("st_type") String st_type,
            @Field("stoken") String stoken,
            @Field("tid") String tid,
            @Field("timestamp") String timestamp,
            @Field("user_view_data") String user_view_data,
            @Field("yuelaou_locate") String yuelaou_locate
    );
}
