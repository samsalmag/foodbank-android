package io.github.samsalmag.foodbankandroid.retrofit

import io.github.samsalmag.foodbankandroid.model.Dish
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface FoodbankApi {

    companion object {
        const val BASE_URL = "/api/v1"
    }

    @GET("/")
    fun default(): Call<Void>

    // DISH
    @GET("$BASE_URL/dish/all")
    fun dishes(): Call<List<Dish>>

    @POST("$BASE_URL/dish/add")
    fun addDish(@Body dish: Dish): Call<Dish>
}
