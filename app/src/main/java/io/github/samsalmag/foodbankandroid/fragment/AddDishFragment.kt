package io.github.samsalmag.foodbankandroid.fragment

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.samsalmag.foodbankandroid.R
import com.samsalmag.foodbankandroid.databinding.FragmentAddDishBinding
import io.github.samsalmag.foodbankandroid.model.Dish
import io.github.samsalmag.foodbankandroid.model.DishCategory
import io.github.samsalmag.foodbankandroid.model.Ingredient
import io.github.samsalmag.foodbankandroid.model.IngredientUnit
import io.github.samsalmag.foodbankandroid.retrofit.DishRequestDTO
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

    private var ingredientCount = 0
    private val maxIngredientCount = 20

    private var categoryCount = 0
    private val maxCategoryCount = 5

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddDishBinding.inflate(layoutInflater, container, false)

        initButtonBack()
        initButtonAddDish()
        initButtonAddCategory()
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

            val dishName = nameInputEditText.text.toString()

            val dishRequestDTO = DishRequestDTO(dishName, getCategoryList(), getIngredientList(), "TEMP")

            postDish(dishRequestDTO)
        }
    }

    private fun initButtonAddCategory() {
        val addCategoryButton = binding.buttonAddCategory
        addCategoryButton.setOnClickListener {
            addCategory()
        }
    }

    private fun initButtonAddIngredient() {
        val addIngredientButton = binding.buttonAddIngredient
        addIngredientButton.setOnClickListener {
            addIngredient()
        }
    }

    private fun postDish(dishRequestDTO: DishRequestDTO) {
        foodbankApi.addDish(dishRequestDTO).enqueue(object : Callback<Dish> {

            override fun onResponse(call: Call<Dish>, response: Response<Dish>) {
                LOGGER.info(RetrofitUtil.requestToString(response))     // Log the request we send to the API

                if (response.isSuccessful) {
                    // Toast.makeText(requireContext(), "Dish added successfully!", Toast.LENGTH_LONG).show()
                    showToast("Dish added successfully!")
                    requireActivity().supportFragmentManager.popBackStack()     // View is closed after successful addition
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

            val inflater = LayoutInflater.from(context)
            val ingredientItemView = inflater.inflate(R.layout.item_ingredient, binding.layoutIngredientContainer, false)

            ingredientItemView.findViewById<TextInputEditText>(R.id.textInput_ingredientName).hint = "Ingredient $ingredientCount"
            val unitDropdown = ingredientItemView.findViewById<Spinner>(R.id.dropdown_ingredientUnit)
            val options = IngredientUnit.values().map { it.toString() }
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, options)
            unitDropdown.adapter = adapter

            binding.layoutIngredientContainer.addView(ingredientItemView, 0)

            if (ingredientCount == maxIngredientCount) {
                binding.buttonAddIngredient.isEnabled = false
            }
        }
    }

    private fun addCategory() {
        if (categoryCount < maxCategoryCount) {
            categoryCount++

            val newDropdown = Spinner(requireContext())
            val options = DishCategory.values().map { it.toString() }.sorted()
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, options)
            newDropdown.adapter = adapter

            binding.layoutCategoryContainer.addView(newDropdown, 0)

            if (categoryCount == maxCategoryCount) {
                binding.buttonAddCategory.isEnabled = false
            }
        }
    }

    private fun getCategoryList(): List<String> {
        val categoryList = mutableListOf<String>()
        for (view in binding.layoutCategoryContainer.children) {
            if (view is Spinner) {
                val category = view.selectedItem.toString()
                // Only add the category if it's not already in the list
                if (!categoryList.contains(category))
                    categoryList.add(category)
            }
        }
        return categoryList
    }

    private fun getIngredientList(): List<Ingredient> {
        val ingredientList = mutableListOf<Ingredient>()
        for (view in binding.layoutIngredientContainer.children) {
            if (view is LinearLayout) {
                var name = "ERROR"
                var quantity = -1
                var unit = "ERROR"

                // QUANTITY
                if (view.getChildAt(0) is TextInputEditText)
                    quantity = (view.getChildAt(0) as TextInputEditText).text.toString().toIntOrNull() ?: 0

                // UNIT
                if (view.getChildAt(1) is Spinner)
                    unit = (view.getChildAt(1) as Spinner).selectedItem.toString()

                // NAME
                if (view.getChildAt(2) is TextInputEditText) {
                    val input = (view.getChildAt(2) as TextInputEditText).text.toString()
                    name = if (!input.isNullOrEmpty()) input else ""
                }

                // Only add ingredient if its name is not empty
                if (name.isNotEmpty()) ingredientList.add(Ingredient(name, quantity, unit))
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
