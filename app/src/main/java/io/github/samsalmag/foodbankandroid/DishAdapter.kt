package io.github.samsalmag.foodbankandroid

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.samsalmag.foodbankandroid.R
import io.github.samsalmag.foodbankandroid.model.Dish

class DishAdapter(private val dishes: MutableList<Dish>) : RecyclerView.Adapter<DishAdapter.DishViewHolder>() {

    class DishViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewDishName: TextView = itemView.findViewById(R.id.dishName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_dish, parent, false)
        return DishViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DishViewHolder, position: Int) {
        val dish = dishes[position]
        holder.textViewDishName.text = dish.name
    }

    override fun getItemCount(): Int {
        return dishes.size
    }

    fun updateDishes(newDishes: List<Dish>) {
        dishes.clear()
        dishes.addAll(newDishes)
        notifyDataSetChanged()
    }
}
