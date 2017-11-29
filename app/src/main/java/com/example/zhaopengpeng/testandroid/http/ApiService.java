package com.example.zhaopengpeng.testandroid.http;

import com.example.zhaopengpeng.testandroid.bean.ResponseBean;
import com.example.zhaopengpeng.testandroid.bean.WordBean;

import io.reactivex.Observable;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Query;

/*****************************************
 * 文  件： 
 * 描  述： 
 * 作  者： zhaopengpeng 
 * 日  期： 2017/11/25
 *****************************************/

public interface ApiService {

    @GET("bdc/search")
    Observable<ResponseBean<WordBean>> getWordDetail(@Query("word") String word);
}
