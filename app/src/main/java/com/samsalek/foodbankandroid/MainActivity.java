package com.samsalek.foodbankandroid;

import android.os.Bundle;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.AppCompatActivity;

import com.samsalek.foodbankandroid.model.Dish;
import com.samsalek.foodbankandroid.retrofit.DishApi;
import com.samsalek.foodbankandroid.retrofit.RetrofitService;

import android.widget.Toast;

import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private final Logger LOGGER = Logger.getLogger(MainActivity.class.getName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();
    }

    private void initComponents() {
        TextInputEditText nameInputEditText = findViewById(R.id.textFieldName);
        MaterialButton addDishButton = findViewById(R.id.addDishButton);

        RetrofitService retrofitService =  new RetrofitService();
        DishApi dishApi = retrofitService.getRetrofit().create(DishApi.class);

        addDishButton.setOnClickListener(view -> {
            String dishName = String.valueOf(nameInputEditText.getText());

            Dish dish = new Dish(dishName);

            dishApi.addDish(dish)
                    .enqueue(new Callback<Dish>() {
                        @Override
                        public void onResponse(Call<Dish> call, Response<Dish> response) {
                            Toast.makeText(MainActivity.this, "Successfully added dish!", Toast.LENGTH_SHORT).show();
                            LOGGER.info("HERE: " + response.body());
                        }

                        @Override
                        public void onFailure(Call<Dish> call, Throwable t) {
                            Toast.makeText(MainActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                           LOGGER.log(Level.SEVERE, "Error when adding dish", t);
                        }
                    });
        });
    }
}