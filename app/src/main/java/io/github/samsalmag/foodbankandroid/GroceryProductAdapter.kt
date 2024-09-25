package io.github.samsalmag.foodbankandroid

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.samsalmag.foodbankandroid.R
import io.github.samsalmag.foodbankandroid.model.GroceryProduct
import java.util.logging.Logger

class GroceryProductAdapter(private val groceryProducts: MutableList<GroceryProduct>) : RecyclerView.Adapter<GroceryProductAdapter.GroceryItemViewHolder>() {

    class GroceryItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ingredientName: TextView = itemView.findViewById(R.id.textView_ingredientName)
        val ingredientQuantity: TextView = itemView.findViewById(R.id.textView_ingredientQuantity)
        val ingredientUnit: TextView = itemView.findViewById(R.id.textView_ingredientUnit)
        val groceryComplete: CheckBox = itemView.findViewById(R.id.checkBox_ingredientComplete)
    }

    private val LOGGER = Logger.getLogger(GroceryProductAdapter::class.java.name)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroceryItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_grocery_product, parent, false)
        return GroceryItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: GroceryItemViewHolder, position: Int) {
        val groceryProduct = groceryProducts[position]
        holder.ingredientName.text = groceryProduct.ingredient.name
        holder.ingredientQuantity.text = groceryProduct.ingredient.quantity.toString()
        holder.ingredientUnit.text = groceryProduct.ingredient.unit
        holder.groceryComplete.isChecked = groceryProduct.isComplete

        holder.groceryComplete.setOnCheckedChangeListener { _, isChecked ->
            groceryProduct.isComplete = isChecked
            LOGGER.info("Ingredient ${groceryProduct.ingredient.name} is complete: $isChecked")

            groceryProducts[position] = groceryProduct
            SharedPreferencesUtil.saveGroceryList(holder.itemView.context, groceryProducts)
        }
    }

    override fun getItemCount(): Int {
        return groceryProducts.size
    }

    fun updateProducts(newProducts: List<GroceryProduct>) {
        groceryProducts.clear()
        groceryProducts.addAll(newProducts)
        notifyDataSetChanged()
    }
}
