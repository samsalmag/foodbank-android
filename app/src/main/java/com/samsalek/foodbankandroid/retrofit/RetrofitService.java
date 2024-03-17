package com.samsalek.foodbankandroid.retrofit;

import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {

    private Retrofit retrofit;

    private final String ip = "192.168.1.20";
    private final String port = "8080";

    public RetrofitService() {
        init();
    }

    private void init() {
        retrofit = new Retrofit.Builder()
                        .baseUrl("http://" + ip + ":" + port)
                        .addConverterFactory(GsonConverterFactory.create(new Gson()))
                        .build();

    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
