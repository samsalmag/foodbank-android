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
) : RecyclerView.Adapter<GroceryProductAdapter.GroceryProductViewHolder>() {

    class GroceryProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroceryProductViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_grocery_product, parent, false)
        return GroceryProductViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: GroceryProductViewHolder, position: Int) {
        val groceryProduct = groceryProducts[position]
        holder.ingredientName.text = groceryProduct.ingredient.name
        holder.ingredientQuantity.text = groceryProduct.ingredient.quantity.toString()
        holder.ingredientUnit.text = groceryProduct.ingredient.unit
        holder.groceryComplete.isChecked = groceryProduct.isComplete

        initViewAppearance(holder, position)
        initCheckboxIngredientComplete(holder, position)
    }

    override fun getItemCount(): Int {
        return groceryProducts.size
    }

    fun updateProducts(newProducts: List<GroceryProduct>) {
        groceryProducts.clear()
        groceryProducts.addAll(newProducts)
        notifyDataSetChanged()
    }

    /**
     * Initializes the view's appearance by setting the background color and 'completion' overlay visibility.
     *
     * @param holder The ViewHolder for the grocery product.
     * @param position The position of the grocery product in the list.
     */
    private fun initViewAppearance(holder: GroceryProductViewHolder, position: Int) {
        // Alternate background color
        if (position % 2 == 0)
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.recycler_view_even_item))
        else
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.recycler_view_odd_item))

        // Init overlay visibility
       updateCompletionOverlay(holder, holder.groceryComplete.isChecked)
    }

    private fun initCheckboxIngredientComplete(holder: GroceryProductViewHolder, position: Int) {
        // Handle checkbox click event
        holder.groceryComplete.setOnCheckedChangeListener { _, isChecked ->
            val groceryProduct = groceryProducts[position]
            groceryProduct.isComplete = isChecked   // Update the 'isComplete' flag
            SharedPreferencesUtil.saveGroceryList(holder.itemView.context, groceryProducts)  // Save the updated list

            updateCompletionOverlay(holder, isChecked)
            checkboxClickListener?.onCheckboxClick(position, isChecked) // If a listener is set, notify it
        }
    }

    private fun updateCompletionOverlay(holder: GroceryProductViewHolder, isChecked: Boolean) {
        holder.completeOverlay.visibility = if (isChecked) View.VISIBLE else View.GONE
    }
}
