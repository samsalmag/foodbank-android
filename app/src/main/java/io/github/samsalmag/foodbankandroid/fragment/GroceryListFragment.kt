package io.github.samsalmag.foodbankandroid.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.samsalmag.foodbankandroid.R
import com.samsalmag.foodbankandroid.databinding.FragmentGroceryListBinding
import io.github.samsalmag.foodbankandroid.adapter.GroceryProductAdapter
import io.github.samsalmag.foodbankandroid.util.SharedPreferencesUtil
import java.util.logging.Logger

class GroceryListFragment : Fragment(), GroceryProductAdapter.CheckboxClickListener {
    private val LOGGER = Logger.getLogger(GroceryListFragment::class.java.name)

    private var _binding: FragmentGroceryListBinding? = null
    private val binding get() = _binding!!  // This property is only valid between onCreateView() and onDestroyView()

    private lateinit var groceryProductsRecyclerView: RecyclerView
    private lateinit var groceryProductAdapter: GroceryProductAdapter

    private var remainingGroceryProducts: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentGroceryListBinding.inflate(layoutInflater, container, false)

        initRecyclerView()
        initButtonClearGroceryList()
        initTextViewRemainingGroceryProducts()
        loadGroceryProducts()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecyclerView() {
        groceryProductsRecyclerView = binding.recyclerViewGroceryProductsList
        groceryProductsRecyclerView.layoutManager = LinearLayoutManager(context)

        groceryProductAdapter = GroceryProductAdapter(mutableListOf(), this)  // Initialize with an empty list
        groceryProductsRecyclerView.adapter = groceryProductAdapter
    }

    private fun initTextViewRemainingGroceryProducts() {
        val groceryProducts = SharedPreferencesUtil.getGroceryList(requireContext())
        val completedGroceryProducts = groceryProducts.count() { it.isComplete }
        remainingGroceryProducts = groceryProducts.size - completedGroceryProducts
        updateTextViewRemainingGroceryProducts()
    }

    private fun initButtonClearGroceryList() {
        binding.buttonClearGroceryList.setOnClickListener {
            SharedPreferencesUtil.clearGroceryList(requireContext())
            groceryProductAdapter.updateProducts(emptyList())

            // Reset remaining grocery products
            remainingGroceryProducts = 0
            updateTextViewRemainingGroceryProducts()
        }
    }

    private fun loadGroceryProducts() {
        val groceryProducts = SharedPreferencesUtil.getGroceryList(requireContext())
        groceryProductAdapter.updateProducts(groceryProducts)
    }

    override fun onCheckboxClick(position: Int, isChecked: Boolean) {
        remainingGroceryProducts = if (isChecked) remainingGroceryProducts - 1 else remainingGroceryProducts + 1
        updateTextViewRemainingGroceryProducts()
    }

    private fun updateTextViewRemainingGroceryProducts() {
        if (remainingGroceryProducts == 0) {
            binding.textViewRemainingGroceryProducts.text = "All grocery products done! \uD83D\uDE03"
            binding.textViewRemainingGroceryProducts.setTextColor(ContextCompat.getColor(requireContext(), R.color.grocery_list_complete))
        } else {
            var productText = "product"
            if (remainingGroceryProducts > 1)
                productText = "products"

            binding.textViewRemainingGroceryProducts.text = "$remainingGroceryProducts grocery $productText left! \uD83D\uDE2C"
            binding.textViewRemainingGroceryProducts.setTextColor(ContextCompat.getColor(requireContext(), R.color.grocery_list_incomplete))
        }
    }
}
