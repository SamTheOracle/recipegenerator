package com.oracolo.cloud.rest.dto;

import java.util.Objects;

public class IngredientDto {

    public String name;

    public IngredientDto(){

    }
    public IngredientDto(String name){
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IngredientDto that = (IngredientDto) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "IngredientDto{" +
                "name='" + name + '\'' +
                '}';
    }
}
