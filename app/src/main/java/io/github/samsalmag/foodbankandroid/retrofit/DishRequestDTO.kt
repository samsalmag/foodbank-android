package io.github.samsalmag.foodbankandroid.retrofit

import io.github.samsalmag.foodbankandroid.model.Ingredient

class DishRequestDTO (

    var name: String,
    var categories: List<String>,
    val ingredients: List<Ingredient>,
    val instructions: String,

)
