package io.github.samsalmag.foodbankandroid.model

import com.fasterxml.jackson.databind.annotation.JsonSerialize

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import io.github.samsalmag.foodbankandroid.jackson.LocalDateTimeDeserializer
import io.github.samsalmag.foodbankandroid.jackson.LocalDateTimeSerializer


import java.time.LocalDateTime
import java.util.ArrayList

data class Dish (
    var id: String? = null,
    var name: String? = null,
    var category: String = "Unknown",
    val ingredients: MutableList<String> = mutableListOf(),
    val instructions: MutableList<String> = mutableListOf(),

    @JsonSerialize(using = LocalDateTimeSerializer::class)
    @JsonDeserialize(using = LocalDateTimeDeserializer::class)
    val creationTime: LocalDateTime = LocalDateTime.now()
)
