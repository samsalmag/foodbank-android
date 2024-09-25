package io.github.samsalmag.foodbankandroid.model

enum class IngredientUnit(private val unitString: String) {
    // Weight units
    GRAM("g"),
    KILOGRAM("kg"),

    // Volume units
    MILLILITER("ml"),
    CENTILITER("cl"),
    DECILITER("dl"),
    LITER("l"),
    TABLESPOON("tbsp"),
    TEASPOON("tsp"),

    // Other units
    PIECE("pc");

    override fun toString(): String {
        return unitString
    }
}
