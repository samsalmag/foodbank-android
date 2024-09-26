package io.github.samsalmag.foodbankandroid.model

enum class DishCategory(private val categoryString: String) {

    // CUISINE TYPES
    MEXICAN("Mexican"),
    ITALIAN("Italian"),
    INDIAN("Indian"),
    THAI("Thai"),
    JAPANESE("Japanese"),
    AMERICAN("American"),
    PERSIAN("Persian"),
    MIDDLE_EASTERN("Middle Eastern"),

    // FOOD CATEGORIES
    BEEF("Beef"),
    CHICKEN("Chicken"),
    PORK("Pork"),
    FISH("Fish"),
    SEAFOOD("Seafood"),

    // DIETARY PREFERENCES
    VEGETARIAN("Vegetarian"),
    VEGAN("Vegan"),
    GLUTEN_FREE("Gluten free"),
    DAIRY_FREE("Dairy free");

    override fun toString(): String {
        return categoryString
    }
}
