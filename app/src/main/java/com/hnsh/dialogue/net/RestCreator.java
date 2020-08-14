package com.hnsh.dialogue.net;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.hnsh.dialogue.constants.BIZConstants;
import com.hnsh.dialogue.rx.service.RxDeviceInfoService;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public final class RestCreator {


    public static RxDeviceInfoService getRxRestService() {

        return RestServiceHolder.RX_REST_SERVICE;
    }

    private static final class RetrofitHolder {

        //  private static final String BASE_URL = (String) Latte.getConfigurations().get(ConfigType.API_HOST.name());

        private static final Retrofit RETROFIT_CLIENT = new Retrofit.Builder()
                .baseUrl(BIZConstants.Dmonkey.URL_BASE)
                .client(OkhttpHolder.OK_HTTP_CLIENT)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES)
                        .create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
                .build();
    }

    private static final class OkhttpHolder {

        private static final int TIMEOUT = 60;

        private static final OkHttpClient.Builder OK_HTTP_CLIENT_BUILDER = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS).addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));

        private static final OkHttpClient OK_HTTP_CLIENT = OK_HTTP_CLIENT_BUILDER.build();
    }

    private static final class RestServiceHolder {

        private static final RxDeviceInfoService RX_REST_SERVICE = RetrofitHolder.RETROFIT_CLIENT.create(RxDeviceInfoService.class);
    }
}
