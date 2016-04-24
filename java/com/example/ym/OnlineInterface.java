package com.example.ym;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

//интерфейс для подключения и получения данных онлайн
public interface OnlineInterface {
    @GET("/artists.json")
    Call<ArrayList<Artist>> onlineReport();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://download.cdn.yandex.net/mobilization-2016/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
