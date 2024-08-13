package io.github.samsalmag.foodbankandroid

import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle

import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.button.MaterialButton

import android.widget.Toast
import com.samsalmag.foodbankandroid.R
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

class MainActivity : AppCompatActivity() {
    private val LOGGER = Logger.getLogger(MainActivity::class.java.name)
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initComponents()
    }

    private fun initComponents() {
        val nameInputEditText = findViewById<TextInputEditText>(R.id.textFieldName)
        val addDishButton = findViewById<MaterialButton>(R.id.addDishButton)
        val retrofitService = RetrofitService()
        val dishApi = retrofitService.get().create(DishApi::class.java)

        addDishButton.setOnClickListener {
            val dishName = nameInputEditText.text.toString()
            val dish = Dish()
            dish.name = dishName
            
            dishApi.addDish(dish).enqueue(object : Callback<Dish> {

                    override fun onResponse(call: Call<Dish>, response: Response<Dish>) {
                        LOGGER.info(RetrofitUtil.requestToString(response))

                        if (response.isSuccessful) {
                            Toast.makeText(this@MainActivity, "Dish added successfully!", Toast.LENGTH_LONG).show()
                        }
                        else {
                            if (response.errorBody() != null) {
                                try {
                                    Toast.makeText(this@MainActivity, response.errorBody()!!.string(), Toast.LENGTH_LONG).show()
                                } catch (e: IOException) {
                                    e.printStackTrace()
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<Dish?>, t: Throwable) {
                        Toast.makeText(this@MainActivity, "Failed!", Toast.LENGTH_SHORT).show()
                        LOGGER.log(Level.SEVERE, "Error when adding dish", t)
                    }
                })
        }
    }
}
