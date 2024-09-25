package io.github.samsalmag.foodbankandroid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.samsalmag.foodbankandroid.databinding.FragmentGroceryListBinding
import java.util.logging.Logger

class GroceryListFragment : Fragment() {
    private val LOGGER = Logger.getLogger(GroceryListFragment::class.java.name)

    private var _binding: FragmentGroceryListBinding? = null
    private val binding get() = _binding!!  // This property is only valid between onCreateView() and onDestroyView()

    private lateinit var groceryProductsRecyclerView: RecyclerView
    private lateinit var groceryProductAdapter: GroceryProductAdapter

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

        groceryProductAdapter = GroceryProductAdapter(mutableListOf())  // Initialize with an empty list
        groceryProductsRecyclerView.adapter = groceryProductAdapter
    }

    private fun initTextViewRemainingGroceryProducts() {
        val remainingGroceryProducts = SharedPreferencesUtil.getGroceryList(requireContext()).size
        binding.textViewRemainingGroceryProducts.text = "$remainingGroceryProducts Grocery products left!"
    }

    private fun initButtonClearGroceryList() {
        binding.buttonClearGroceryList.setOnClickListener {
            SharedPreferencesUtil.clearGroceryList(requireContext())
            groceryProductAdapter.updateProducts(emptyList())
        }
    }

    private fun loadGroceryProducts() {
        val groceryProducts = SharedPreferencesUtil.getGroceryList(requireContext())
        groceryProductAdapter.updateProducts(groceryProducts)
    }
}
