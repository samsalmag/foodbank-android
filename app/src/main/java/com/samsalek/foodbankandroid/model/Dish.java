package com.samsalek.foodbankandroid.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Dish {

    private String id;

    private String name;
    private String category = "Unknown";
    private List<String> ingredients = new ArrayList<>();
    private List<String> instructions = new ArrayList<>();
    private LocalDateTime creationTime = LocalDateTime.now();

    public Dish(String name) {
        this.name = name;
    }

    public Dish(String name, String category, List<String> ingredients, List<String> instructions) {
        this.name = name;
        this.category = category;
        this.ingredients = ingredients;
        this.instructions = instructions;
    }
}
