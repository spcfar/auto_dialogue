package com.hnsh.dialogue.rx.service;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RxWeatherService {

    @GET("api/?version=v9")
    Observable<ResponseBody> getWeather(@Query("city") String city, @Query("appid") String appid, @Query("appsecret") String appsecret);
}
