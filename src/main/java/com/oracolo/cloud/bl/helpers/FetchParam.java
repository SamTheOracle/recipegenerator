package com.oracolo.cloud.bl.helpers;

import java.util.Arrays;
import java.util.Optional;

public enum FetchParam {

    RECIPE_NAME("recipeName"), INGREDIENT_NAME("ingredientName");

    private final String query;

    FetchParam(String query) {
        this.query = query;
    }

    public String query() {
        return query;
    }

    public static Optional<FetchParam> from(String someQuery){
        return Arrays.stream(values()).filter(fetchParam -> fetchParam.query.equals(someQuery) || fetchParam.name().equals(someQuery)).findAny();
    }
}
