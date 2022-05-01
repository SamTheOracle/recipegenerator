package com.oracolo.cloud.rest.dto;


import java.util.List;

public class RecipeDto {

    public Integer id;

    public String name;

    public List<IngredientDto> ingredients;

    public String imageUrl;

    public RecipeDto() {
    }

    public RecipeDto(Integer id, String name, List<IngredientDto> ingredients, String imageUrl) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.imageUrl = imageUrl;
    }
}
