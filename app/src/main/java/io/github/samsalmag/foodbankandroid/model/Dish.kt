package io.github.samsalmag.foodbankandroid.model

import com.fasterxml.jackson.databind.annotation.JsonSerialize

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import io.github.samsalmag.foodbankandroid.jackson.LocalDateTimeDeserializer
import io.github.samsalmag.foodbankandroid.jackson.LocalDateTimeSerializer

import java.time.LocalDateTime

data class Dish (
    val id: String,
    var name: String,
    var categories: MutableList<String>,
    val ingredients: MutableList<Ingredient>,
    val instructions: String,

    @JsonSerialize(using = LocalDateTimeSerializer::class)
    @JsonDeserialize(using = LocalDateTimeDeserializer::class)
    val creationTime: LocalDateTime,

    @JsonSerialize(using = LocalDateTimeSerializer::class)
    @JsonDeserialize(using = LocalDateTimeDeserializer::class)
    var latestModTime: LocalDateTime
) {
    constructor(): this("", "", mutableListOf(), mutableListOf(), "", LocalDateTime.now(), LocalDateTime.now())
}
