package io.github.samsalmag.foodbankandroid.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.samsalmag.foodbankandroid.databinding.FragmentRandomDishBinding
import io.github.samsalmag.foodbankandroid.service.DishService
import io.github.samsalmag.foodbankandroid.model.Dish
import io.github.samsalmag.foodbankandroid.retrofit.RetrofitService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.util.logging.Level
import java.util.logging.Logger

class RandomDishFragment : Fragment() {

    private val LOGGER = Logger.getLogger(RandomDishFragment::class.java.name)

    private var _binding: FragmentRandomDishBinding? = null
    private val binding get() = _binding!!  // This property is only valid between onCreateView() and onDestroyView()

    private lateinit var dishes: List<Dish>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRandomDishBinding.inflate(layoutInflater, container, false)

        loadDishes()

        binding.fragmentRandomDishButtonGetDish.setOnClickListener {
            binding.fragmentRandomDishTextViewDishName.visibility = View.VISIBLE
            if (dishes.isNotEmpty()) {
                val randomDish = dishes.random()
                binding.fragmentRandomDishTextViewDishName.text = randomDish.name
            }
            else binding.fragmentRandomDishTextViewDishName.text = "Could not find any dishes \uD83D\uDE22"
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadDishes() {
        dishes = emptyList()
        lifecycleScope.launch() {
            dishes = fetchDishes()
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
                else throw Exception("Response was not successful. Error code: ${response.code()}, Message: ${response.message()}")
            }
            catch (e: Exception) {
                LOGGER.log(Level.SEVERE,"Failed to fetch dishes", e)
                emptyList()
            }
        }
    }
}
