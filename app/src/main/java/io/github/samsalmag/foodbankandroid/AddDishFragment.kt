package io.github.samsalmag.foodbankandroid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.samsalmag.foodbankandroid.databinding.FragmentAddDishBinding
import io.github.samsalmag.foodbankandroid.model.Dish
import io.github.samsalmag.foodbankandroid.retrofit.DishApi
import io.github.samsalmag.foodbankandroid.retrofit.RetrofitService
import io.github.samsalmag.foodbankandroid.retrofit.RetrofitUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.logging.Level
import java.util.logging.Logger

class AddDishFragment : Fragment() {
    private val LOGGER = Logger.getLogger(AddDishFragment::class.java.name)

    private var _binding: FragmentAddDishBinding? = null
    private val binding get() = _binding!!  // This property is only valid between onCreateView() and onDestroyView()

    private lateinit var dishApi: DishApi

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddDishBinding.inflate(layoutInflater, container, false)

        initButtonBack()
        initButtonAddDish()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dishApi = RetrofitService().get().create(DishApi::class.java)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null     // Avoid memory leaks
    }

    private fun initButtonBack() {
        val buttonBack = binding.buttonBack
        buttonBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun initButtonAddDish() {
        val nameInputEditText = binding.textFieldDishName
        val addDishButton = binding.buttonAddDish

        addDishButton.setOnClickListener {
            val dish = Dish()
            val dishName = nameInputEditText.text.toString()
            dish.name = dishName
            postDish(dish)
        }
    }

    private fun postDish(dish: Dish) {
        dishApi.addDish(dish).enqueue(object : Callback<Dish> {

            override fun onResponse(call: Call<Dish>, response: Response<Dish>) {
                LOGGER.info(RetrofitUtil.requestToString(response))     // Log the request we send to the API

                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Dish added successfully!", Toast.LENGTH_LONG).show()
                }
                else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                    try {
                        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
                    }
                    catch (e: IOException) {
                        LOGGER.log(Level.SEVERE, "Error when reading error body", e)
                    }
                }
            }

            override fun onFailure(call: Call<Dish>, t: Throwable) {
                Toast.makeText(requireContext(), "Request failed!", Toast.LENGTH_LONG).show()
                LOGGER.log(Level.SEVERE, "Error when adding dish", t)
            }
        })
    }
}
