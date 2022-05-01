package com.oracolo.cloud.bl;

import com.oracolo.cloud.bl.helpers.FetchParam;
import com.oracolo.cloud.dao.RecipeDao;
import com.oracolo.cloud.entities.*;
import com.oracolo.cloud.exception.BadImageException;
import com.oracolo.cloud.exception.ImageTooBigException;
import com.oracolo.cloud.exception.TooManyIngredientsException;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class RecipeService {

    @Inject
    RecipeDao recipeDao;

    @Inject
    IngredientService ingredientService;

    @Inject
    RecipeIngredientService recipeIngredientService;

    @Inject
    @ConfigProperty(name = "IMAGE_MAX_SIZE", defaultValue = "5242880")
    long imageMaxSize;

    @Inject
    @ConfigProperty(name = "MAX_INGREDIENTS_PER_RECIPE", defaultValue = "50")
    int maxIngredientsPerRecipe;

    /**
     * Safe insert of {@link Recipe} in database, checking constraints first
     *
     * @throws ImageTooBigException        if image exceeds given max dimension
     * @throws TooManyIngredientsException if ingredients size exceeds given dimension
     * @throws BadImageException           if any error occurs during image read
     */
    @Transactional
    public Recipe checkedInsert(Recipe recipe, Collection<Ingredient> ingredients) {

        byte[] image = recipe.getImage() == null ? new byte[0] : recipe.getImage();

        if (image.length > imageMaxSize) {
            throw new ImageTooBigException();
        }
        if (ingredients.size() > maxIngredientsPerRecipe) {
            throw new TooManyIngredientsException();
        }
        recipeDao.insert(recipe);
        Set<Ingredient> persistedIngredients = ingredientService.insert(ingredients);
        Set<RecipeIngredient> recipeIngredients = persistedIngredients.stream().map(ingredient -> new RecipeIngredient(new RecipeIngredientId(recipe,ingredient))).collect(Collectors.toSet());
        recipeIngredientService.insert(recipeIngredients);
        recipe.setRecipeIngredients(recipeIngredients);
        recipeDao.update(recipe);
        return recipe;
    }

    public List<Recipe> fetchRecipes(Map<FetchParam, List<String>> fetchParams) {
        if(fetchParams.isEmpty()){
            return recipeDao.fetchAll();
        }
        return recipeDao.fetchByParam(fetchParams);
    }
    @Transactional
    public Recipe update(Recipe recipe, Set<Ingredient> ingredients) {
        recipeIngredientService.delete(recipe.getRecipeIngredients());
        Set<Ingredient> persistedIngredients = ingredientService.insert(ingredients);
        Set<RecipeIngredient> recipeIngredients = persistedIngredients.stream().map(ingredient -> new RecipeIngredient(new RecipeIngredientId(recipe,ingredient))).collect(Collectors.toSet());
        recipeIngredientService.insert(recipeIngredients);
        recipe.setRecipeIngredients(recipeIngredients);
        recipeDao.update(recipe);
        return recipe;
    }

    @Transactional
    public void delete(Recipe recipe) {
        recipeDao.delete(recipe);
    }

    public Optional<Recipe> getById(Integer recipeId) {
        return recipeDao.getById(recipeId, Recipe.class);
    }
}
