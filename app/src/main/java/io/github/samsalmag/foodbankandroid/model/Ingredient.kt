package io.github.samsalmag.foodbankandroid.model

data class Ingredient (
    var name: String,
    var amount: Int,
    var unit: String
) {
    constructor(): this("", 0, "")
}
