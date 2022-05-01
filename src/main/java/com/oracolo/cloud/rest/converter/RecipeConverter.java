package com.oracolo.cloud.rest.converter;

import com.oracolo.cloud.entities.RecipeIngredient;
import com.oracolo.cloud.exception.BadImageException;
import com.oracolo.cloud.rest.dto.RecipeDto;
import com.oracolo.cloud.rest.dto.RecipeFormDto;
import com.oracolo.cloud.entities.Ingredient;
import com.oracolo.cloud.entities.Recipe;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.*;
import java.nio.file.Files;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class RecipeConverter {

    @Inject
    IngredientConverter ingredientConverter;

    public RecipeDto toRecipe(Recipe recipe) {
       return new RecipeDto(recipe.getId(),recipe.getName(),ingredientConverter.to(recipe.getRecipeIngredients()
               .stream()
               .map(recipeIngredient -> recipeIngredient.getRecipeIngredientId().getIngredient())
               .collect(Collectors.toUnmodifiableSet())), "/recipes/"+recipe.getId()+"/image");
    }

    public List<RecipeDto> toRecipes(Collection<Recipe> recipes) {
        return recipes.stream().map(this::toRecipe).collect(Collectors.toUnmodifiableList());
    }



    public Recipe fromRecipe(RecipeFormDto recipeFormDto) {
        try{
            Recipe recipe = new Recipe();
            File fileImage = recipeFormDto.image;
            if(fileImage!=null){
                InputStream image = new ByteArrayInputStream(Files.readAllBytes(fileImage.toPath()));
                recipe.setImage(image.readAllBytes());
            }
            return recipe.setName(recipeFormDto.name);
        }catch (IOException e){
            throw new BadImageException();
        }

    }
}
