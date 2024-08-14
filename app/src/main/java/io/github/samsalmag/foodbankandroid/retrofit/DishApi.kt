package io.github.samsalmag.foodbankandroid.retrofit

import io.github.samsalmag.foodbankandroid.model.Dish
import retrofit2.http.GET

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface DishApi {

    companion object {
        const val baseUrl = "/api/v1/dish"
    }

    @GET(baseUrl + "/all")
    fun dishes(): Call<List<Dish>>

    @POST(baseUrl + "/add")
    fun addDish(@Body dish: Dish): Call<Dish>
}
