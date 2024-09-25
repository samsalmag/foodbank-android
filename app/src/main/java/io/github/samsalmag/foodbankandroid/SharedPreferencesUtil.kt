package io.github.samsalmag.foodbankandroid

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.github.samsalmag.foodbankandroid.model.GroceryProduct


class SharedPreferencesUtil {
    companion object {
        private const val DEFAULT_PREFS = "default"
        private const val GROCERY_LIST_KEY = "grocery_list"

        private val gson = Gson()

        fun saveGroceryList(context: Context, groceryList: List<GroceryProduct>) {
            saveList(context, GROCERY_LIST_KEY, groceryList)
        }

        fun getGroceryList(context: Context): List<GroceryProduct> {
            return getList(context, GROCERY_LIST_KEY, GroceryProduct::class.java)
        }

        fun clearGroceryList(context: Context) {
            saveList(context, GROCERY_LIST_KEY, emptyList())
        }

        private fun saveList(context: Context, key: String, data: List<Any>) {
            val sharedPreferences = context.getSharedPreferences(DEFAULT_PREFS, Context.MODE_PRIVATE)

            // Convert the data to JSON, then save it
            val jsonData = gson.toJson(data)
            val prefsEditor = sharedPreferences.edit()
            prefsEditor.putString(key, jsonData)
            prefsEditor.apply()
        }

        private fun <T> getList(context: Context, key: String, clazz: Class<T>): List<T> {
            val sharedPreferences = context.getSharedPreferences(DEFAULT_PREFS, Context.MODE_PRIVATE)

            // Get the data as JSON string, since data is saved as JSON
            // Then convert JSON string to parameterized list
            val jsonData: String = sharedPreferences.getString(key, null) ?: return emptyList()
            val type = TypeToken.getParameterized(List::class.java, clazz).type

            return gson.fromJson(jsonData, type)
        }
    }
}
