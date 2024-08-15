package io.github.samsalmag.foodbankandroid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.samsalmag.foodbankandroid.R
import com.samsalmag.foodbankandroid.databinding.FragmentDishesBinding
import io.github.samsalmag.foodbankandroid.model.Dish
import io.github.samsalmag.foodbankandroid.retrofit.RetrofitService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.logging.Level
import java.util.logging.Logger

class DishesFragment : Fragment() {
    private val LOGGER = Logger.getLogger(DishesFragment::class.java.name)

    private var _binding: FragmentDishesBinding? = null
    private val binding get() = _binding!!  // This property is only valid between onCreateView() and onDestroyView()

    private lateinit var dishRecyclerView: RecyclerView
    private lateinit var dishAdapter: DishAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentDishesBinding.inflate(layoutInflater, container, false)

        initButtonAddNewDish()
        initRecyclerView()

        return binding.root
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

        lifecycleScope.launch {
            // Fetch dishes from API and update the adapter
            val dishes = fetchDishes()
            dishAdapter.updateDishes(dishes)
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
                    LOGGER.severe("Failed to fetch dishes. Error code: ${response.code()}, Message: ${response.message()}")
                    emptyList()
                }
            }
            catch (e: Exception) {
                LOGGER.log(Level.SEVERE,"Failed to fetch dishes", e)
                emptyList()
            }
        }
    }
}
