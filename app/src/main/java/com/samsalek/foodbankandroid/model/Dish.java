package com.samsalek.foodbankandroid.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.samsalek.foodbankandroid.jackson.LocalDateTimeDeserializer;
import com.samsalek.foodbankandroid.jackson.LocalDateTimeSerializer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dish {

    private String id;

    private String name;
    private String category = "Unknown";
    private List<String> ingredients = new ArrayList<>();
    private List<String> instructions = new ArrayList<>();

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime creationTime = LocalDateTime.now();
}
