package io.github.samsalmag.foodbankandroid.model

data class Ingredient (
    var name: String,
    var quantity: Int,
    var unit: String
) {
    constructor(): this("", 0, "")

    fun canStack(other: Ingredient): Boolean {
        return name == other.name && unit == other.unit
    }
}
