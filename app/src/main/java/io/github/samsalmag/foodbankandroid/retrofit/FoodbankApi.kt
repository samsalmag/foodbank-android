package io.github.samsalmag.foodbankandroid.retrofit

import io.github.samsalmag.foodbankandroid.model.Dish
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface FoodbankApi {

    companion object {
        const val BASE_URL = "/api/v1/dish"
    }

    @GET("$BASE_URL/")
    fun default(): Call<Void>

    // DISH
    @GET("$BASE_URL/all")
    fun dishes(): Call<List<Dish>>

    @POST("$BASE_URL/")
    fun addDish(@Body dishRequestDTO: DishRequestDTO): Call<Dish>
}
