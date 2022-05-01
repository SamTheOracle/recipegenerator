package com.oracolo.cloud.bl;

import com.oracolo.cloud.dao.RecipeIngredientDao;
import com.oracolo.cloud.entities.RecipeIngredient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Collection;
import java.util.Set;

@ApplicationScoped
public class RecipeIngredientService {

    @Inject
    RecipeIngredientDao recipeIngredientDao;

    public void insert(Collection<RecipeIngredient> recipeIngredients){
        recipeIngredients.forEach(recipeIngredientDao::insert);
    }

    public void delete(Set<RecipeIngredient> recipeIngredients) {
        recipeIngredientDao.delete(recipeIngredients);
    }
}
