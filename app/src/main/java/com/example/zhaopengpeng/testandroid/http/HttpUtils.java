package com.example.zhaopengpeng.testandroid.http;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/*****************************************
 * 文  件： 
 * 描  述： 
 * 作  者： zhaopengpeng 
 * 日  期： 2017/11/25
 *****************************************/

public class HttpUtils {
    public static final String BASE_URL = "https://api.shanbay.com/";



    public static ApiService getApiService(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(ApiService.class);
    }
}
