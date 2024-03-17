package com.samsalek.foodbankandroid.retrofit;

import com.samsalek.foodbankandroid.model.Dish;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface DishApi {

    String baseUrl = "/api/v1/dish";

    @GET(baseUrl + "/all")
    Call<List<Dish>> getDishes();

    @POST(baseUrl + "/add")
    Call<Dish> addDish(@Body Dish dish);
}
