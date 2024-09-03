package io.github.samsalmag.foodbankandroid

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.samsalmag.foodbankandroid.R
import com.samsalmag.foodbankandroid.databinding.FragmentAddDishBinding
import io.github.samsalmag.foodbankandroid.model.Dish
import io.github.samsalmag.foodbankandroid.retrofit.FoodbankApi
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
    private val binding get() = _binding!!      // This property is only valid between onCreateView() and onDestroyView()

    private lateinit var foodbankApi: FoodbankApi

    private var ingredientCount = 1
    private val maxIngredientCount = 20

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddDishBinding.inflate(layoutInflater, container, false)

        initButtonBack()
        initButtonAddDish()
        initButtonAddIngredient()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        foodbankApi = RetrofitService().foodbankApi
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

            dish.ingredients.addAll(getIngredientList())

            postDish(dish)
        }
    }

    private fun initButtonAddIngredient() {
        val addIngredientButton = binding.buttonAddIngredient
        addIngredientButton.setOnClickListener {
            addIngredient()
        }
    }

    private fun postDish(dish: Dish) {
        foodbankApi.addDish(dish).enqueue(object : Callback<Dish> {

            override fun onResponse(call: Call<Dish>, response: Response<Dish>) {
                LOGGER.info(RetrofitUtil.requestToString(response))     // Log the request we send to the API

                if (response.isSuccessful) {
                    // Toast.makeText(requireContext(), "Dish added successfully!", Toast.LENGTH_LONG).show()
                    showToast("Dish added successfully!")
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

    private fun addIngredient() {
        if (ingredientCount < maxIngredientCount) {
            ingredientCount++

            val newTextField = TextInputEditText(requireContext())

            val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            layoutParams.setMargins(0, 0, 0, 20)
            newTextField.hint = "Ingredient $ingredientCount"
            newTextField.minHeight = 50.asDp()

            binding.layoutIngredientContainer.addView(newTextField, 0)

            if (ingredientCount == maxIngredientCount) {
                binding.buttonAddIngredient.isEnabled = false
            }
        }
    }

    private fun getIngredientList(): List<String> {
        val ingredientList = mutableListOf<String>()

        for (view in binding.layoutIngredientContainer.children) {
            if (view is TextInputEditText) {
                ingredientList.add(view.text.toString())
            }
        }

        return ingredientList
    }

    private fun showToast(message: String) {
        val toast = Toast.makeText(requireContext(), message, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.TOP, 0, 200)
        toast.show()
    }

    // Helper function to set the integer's unit as dp (converts dp to px)
    private fun Int.asDp(): Int {
        val density = resources.displayMetrics.density
        return (this * density).toInt()
    }
}
