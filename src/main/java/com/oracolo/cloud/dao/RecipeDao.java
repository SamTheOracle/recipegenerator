package com.oracolo.cloud.dao;

import com.oracolo.cloud.bl.helpers.FetchParam;
import com.oracolo.cloud.entities.*;
import com.oracolo.cloud.entities.Ingredient_;
import com.oracolo.cloud.entities.RecipeIngredientId_;
import com.oracolo.cloud.entities.RecipeIngredient_;
import com.oracolo.cloud.entities.Recipe_;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class RecipeDao extends BaseDao<Recipe> {
    public List<Recipe> fetchAll() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Recipe> cq = cb.createQuery(Recipe.class);
        Root<Recipe> root = cq.from(Recipe.class);
        return entityManager.createQuery(cq.select(root)).getResultList();
    }

    public List<Recipe> fetchByParam(Map<FetchParam, List<String>> fetchParams) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Recipe> cq = cb.createQuery(Recipe.class);
        Root<Recipe> root = cq.from(Recipe.class);
        Join<Recipe, RecipeIngredient> join = root.join(Recipe_.recipeIngredients);
        List<Predicate> predicates = new ArrayList<>();
        for (Map.Entry<FetchParam, List<String>> entry : fetchParams.entrySet()) {
            switch (entry.getKey()) {
                case RECIPE_NAME:
                    predicates.add(cb.like(root.get(Recipe_.name), entry.getValue().stream().map(recipeName -> "%" + recipeName + "%")
                            .findAny().orElse("%%")));
                    break;
                case INGREDIENT_NAME:
                    Predicate[] ingredientPredicates = entry.getValue().stream().map(ingredientName -> cb.like(join.get(RecipeIngredient_.recipeIngredientId).get(RecipeIngredientId_.ingredient).get(Ingredient_.name), "%" + ingredientName + "%"))
                            .toArray(Predicate[]::new);
                    if (ingredientPredicates.length != 0) {
                        Predicate finalIngredientPredicate = cb.or(ingredientPredicates);
                        predicates.add(finalIngredientPredicate);
                    }
            }
        }
        return entityManager.createQuery(cq.where(predicates.toArray(Predicate[]::new))).getResultList();
    }
}
