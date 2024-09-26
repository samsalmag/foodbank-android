package io.github.samsalmag.foodbankandroid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.samsalmag.foodbankandroid.R
import com.samsalmag.foodbankandroid.databinding.FragmentDishesBinding
import io.github.samsalmag.foodbankandroid.model.Dish
import io.github.samsalmag.foodbankandroid.retrofit.RetrofitService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.util.logging.Level
import java.util.logging.Logger

class DishesFragment : Fragment() {
    private val LOGGER = Logger.getLogger(DishesFragment::class.java.name)

    private var _binding: FragmentDishesBinding? = null
    private val binding get() = _binding!!  // This property is only valid between onCreateView() and onDestroyView()

    private lateinit var dishRecyclerView: RecyclerView
    private lateinit var dishAdapter: DishAdapter

    private lateinit var loadingIndicator: CircularProgressIndicator
    private lateinit var buttonRetryLoadDishes: MaterialButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDishesBinding.inflate(layoutInflater, container, false)

        initButtonAddNewDish()
        initRecyclerView()
        initLoadingIndicator()
        initLayoutLoadDishesError()

        loadDishes()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null     // Avoid memory leaks
    }

    private fun initButtonAddNewDish() {
        val buttonAddNewDish = binding.buttonNewDishView
        buttonAddNewDish.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragment_container, AddDishFragment()).addToBackStack(null).commit()
        }
    }

    private fun initRecyclerView() {
        dishRecyclerView = binding.recyclerViewDishesList
        dishRecyclerView.layoutManager = LinearLayoutManager(context)

        dishAdapter = DishAdapter(mutableListOf())   // Initialize with an empty list
        dishRecyclerView.adapter = dishAdapter
    }

    private fun initLoadingIndicator() {
        loadingIndicator = binding.progressIndicatorDishesList
    }

    private fun initLayoutLoadDishesError() {
        buttonRetryLoadDishes = binding.buttonRetryLoadDishes
        buttonRetryLoadDishes.setOnClickListener {
            loadDishes()
        }
    }

    private fun loadDishes() {
        showLoadingIndicator()
        lifecycleScope.launch {
            try {
                // Fetch dishes from API and update the adapter
                val dishes = fetchDishes()

                if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                    dishAdapter.updateDishes(dishes)
                    loadingIndicator.visibility = View.GONE
                }
            }
            catch (e: Exception) {
                if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED))
                    showLayoutLoadDishesError()
            }
        }


    }

    private suspend fun fetchDishes(): List<Dish> {
        return withContext(Dispatchers.IO) {
            try {
                val response: Response<List<Dish>> = RetrofitService().foodbankApi.dishes().execute()

                if (response.isSuccessful) {
                    LOGGER.info("Dishes fetched successfully: ${response.body()?.size} dishes in total")
                    response.body() ?: emptyList()
                }
                else {
                    throw Exception("Failed to fetch dishes. Error code: ${response.code()}, Message: ${response.message()}")
                }
            }
            catch (e: Exception) {
                LOGGER.log(Level.SEVERE,"Failed to fetch dishes", e)
                throw e
            }
        }
    }

    private fun showLoadingIndicator() {
        loadingIndicator.visibility = View.VISIBLE
        binding.layoutLoadDishesError.visibility = View.GONE
    }

    private fun showLayoutLoadDishesError() {
        loadingIndicator.visibility = View.GONE
        binding.layoutLoadDishesError.visibility = View.VISIBLE
    }
}
