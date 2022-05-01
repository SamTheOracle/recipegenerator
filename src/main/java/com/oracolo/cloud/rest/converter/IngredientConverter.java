package com.oracolo.cloud.rest.converter;

import com.oracolo.cloud.entities.Ingredient;
import com.oracolo.cloud.entities.Metadata;
import com.oracolo.cloud.rest.dto.IngredientDto;

import javax.enterprise.context.ApplicationScoped;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class IngredientConverter {

    public IngredientDto toIngredient(Ingredient ingredient) {
        return new IngredientDto(ingredient.getName());
    }

    public List<IngredientDto> to(Collection<Ingredient> ingredients) {
        return ingredients.stream().map(this::toIngredient).collect(Collectors.toUnmodifiableList());
    }

    public Set<Ingredient> fromIngredients(Collection<IngredientDto> ingredientDtos) {
        Collection<IngredientDto> finalIngredientDtos = Objects.requireNonNullElse(ingredientDtos, new ArrayList<>());
        return finalIngredientDtos.stream().map(this::fromIngredient).collect(Collectors.toCollection(HashSet::new));
    }

    public Ingredient fromIngredient(IngredientDto ingredientDto) {
        Ingredient ingredient = new Ingredient().setName(ingredientDto.name);
        ingredient.setMetadata(new Metadata().setInsertDate(Instant.now()));
        return ingredient;
    }
}
