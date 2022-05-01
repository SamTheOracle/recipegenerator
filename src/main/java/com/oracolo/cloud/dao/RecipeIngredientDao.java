package com.oracolo.cloud.dao;

import com.oracolo.cloud.entities.Ingredient;
import com.oracolo.cloud.entities.Ingredient_;
import com.oracolo.cloud.entities.RecipeIngredient;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Optional;

@ApplicationScoped
public class RecipeIngredientDao extends BaseDao<RecipeIngredient> {
}
