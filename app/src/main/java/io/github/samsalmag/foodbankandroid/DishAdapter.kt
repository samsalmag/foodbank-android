package io.github.samsalmag.foodbankandroid

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.samsalmag.foodbankandroid.R
import io.github.samsalmag.foodbankandroid.model.Dish
import io.github.samsalmag.foodbankandroid.model.GroceryProduct
import java.util.logging.Logger

class DishAdapter(private val dishes: MutableList<Dish>) : RecyclerView.Adapter<DishAdapter.DishViewHolder>() {

    class DishViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewDishName: TextView = itemView.findViewById(R.id.textView_dishName)
        val textViewDishCategories: TextView = itemView.findViewById(R.id.textView_dishCategories)
        val buttonAddToGroceryList: MaterialButton = itemView.findViewById(R.id.button_addToGroceryList)
    }

    private val LOGGER = Logger.getLogger(DishAdapter::class.java.name)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_dish, parent, false)
        return DishViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DishViewHolder, position: Int) {
        val dish = dishes[position]
        holder.textViewDishName.text = dish.name
        holder.textViewDishCategories.text = dish.categories
            .toString().substring(1, dish.categories.toString().length - 1)   // Remove list brackets

        // Alternate background color
        if (position % 2 == 0)
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.recycler_view_even_item))
        else
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.recycler_view_odd_item))

        // Handle button click event
        holder.buttonAddToGroceryList.setOnClickListener {
            addDishIngredientsToGroceryList(dish, holder)
        }
    }

    override fun getItemCount(): Int {
        return dishes.size
    }

    fun updateDishes(newDishes: List<Dish>) {
        dishes.clear()
        dishes.addAll(newDishes)
        notifyDataSetChanged()
    }

    private fun addDishIngredientsToGroceryList(dish: Dish, holder: DishViewHolder) {
        val ingredientsList = dish.ingredients
        LOGGER.info("Added ingredients: [$ingredientsList] to grocery list!")

        // Get the current grocery list
        val groceryProducts = mutableListOf<GroceryProduct>()
        groceryProducts.addAll(SharedPreferencesUtil.getGroceryList(holder.itemView.context))

        for (ingredient in ingredientsList) {
            var ingredientStacked = false

            // Check if ingredient is already in grocery list, and if so, stack them
            for (product in groceryProducts) {
                if (product.ingredient.canStack(ingredient)) {
                    product.ingredient.quantity += ingredient.quantity  // Stack the ingredients
                    product.isComplete = false  // Reset the complete flag, since quantity might have changed
                    ingredientStacked = true
                    break
                }
            }

            // If ingredient did not get stacked, then add new product to grocery list
            if (!ingredientStacked) {
                val newGroceryProduct = GroceryProduct(ingredient, false)
                groceryProducts.add(newGroceryProduct)
            }
        }
        SharedPreferencesUtil.saveGroceryList(holder.itemView.context, groceryProducts)
    }
}
