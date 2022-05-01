package com.oracolo.cloud.bl;

import com.oracolo.cloud.dao.IngredientDao;
import com.oracolo.cloud.entities.Ingredient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class IngredientService {

    @Inject
    IngredientDao ingredientDao;

    public Ingredient insert(Ingredient ingredient){
        Optional<Ingredient> ingredientOptional = ingredientDao.getByName(ingredient.getName());
        if(ingredientOptional.isEmpty()){
            ingredientDao.insert(ingredient);
            return ingredient;
        }
        return ingredientOptional.get();
    }
    public Set<Ingredient> insert(Collection<Ingredient> ingredients){
        return ingredients.stream().map(this::insert).collect(Collectors.toUnmodifiableSet());
    }
}
