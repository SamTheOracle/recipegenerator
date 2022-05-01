package com.oracolo.cloud.dao;

import com.oracolo.cloud.bl.helpers.FetchParam;
import com.oracolo.cloud.entities.Ingredient;
import com.oracolo.cloud.entities.Ingredient_;
import com.oracolo.cloud.entities.Recipe;
import com.oracolo.cloud.entities.Recipe_;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class IngredientDao extends BaseDao<Ingredient> {

    public Optional<Ingredient> getByName(String name) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Ingredient> cq = cb.createQuery(Ingredient.class);
        Root<Ingredient> root = cq.from(Ingredient.class);
        Predicate namePredicate = cb.equal(root.get(Ingredient_.name),name);
        return entityManager.createQuery(cq.where(namePredicate)).getResultStream().findAny();
    }
}
