package io.github.samsalmag.foodbankandroid.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.samsalmag.foodbankandroid.R
import io.github.samsalmag.foodbankandroid.util.SharedPreferencesUtil
import io.github.samsalmag.foodbankandroid.model.GroceryProduct
import java.util.logging.Logger

class GroceryProductAdapter(
    private val groceryProducts: MutableList<GroceryProduct>,
    private var checkboxClickListener: CheckboxClickListener? = null
) : RecyclerView.Adapter<GroceryProductAdapter.GroceryItemViewHolder>() {

    class GroceryItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ingredientName: TextView = itemView.findViewById(R.id.textView_ingredientName)
        val ingredientQuantity: TextView = itemView.findViewById(R.id.textView_ingredientQuantity)
        val ingredientUnit: TextView = itemView.findViewById(R.id.textView_ingredientUnit)
        val groceryComplete: CheckBox = itemView.findViewById(R.id.checkBox_ingredientComplete)
        val completeOverlay: View = itemView.findViewById(R.id.view_completeOverlay)
    }

    interface CheckboxClickListener {
        fun onCheckboxClick(position: Int, isChecked: Boolean)
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

        // Alternate background color
        if (position % 2 == 0)
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.recycler_view_even_item))
        else
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.recycler_view_odd_item))

        // Init overlay visibility
        holder.completeOverlay.visibility = if (holder.groceryComplete.isChecked) View.VISIBLE else View.GONE

        // Handle checkbox click event
        holder.groceryComplete.setOnCheckedChangeListener { _, isChecked ->
            groceryProduct.isComplete = isChecked
            groceryProducts[position] = groceryProduct  // Update the grocery product in the list
            SharedPreferencesUtil.saveGroceryList(holder.itemView.context, groceryProducts)  // Save the updated list

            holder.completeOverlay.visibility = if (isChecked) View.VISIBLE else View.GONE  // Update overlay visibility
            checkboxClickListener?.onCheckboxClick(position, isChecked) // If a listener is set, notify it
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
