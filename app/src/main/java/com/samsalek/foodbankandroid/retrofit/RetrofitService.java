package com.samsalek.foodbankandroid.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

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
                        .addConverterFactory(JacksonConverterFactory.create())
                        .build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
