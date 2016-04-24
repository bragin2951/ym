package com.example.ym;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Url;

//интерфейс для подключения и получения файла
public interface MyInterface {
    @GET
    Call<ResponseBody> download(@Url String fileUrl);

    Retrofit retrofit =
            new Retrofit.Builder()
                    .baseUrl("http://download.cdn.yandex.net/mobilization-2016/")
                    .build();
}