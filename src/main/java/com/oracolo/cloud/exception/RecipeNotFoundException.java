package com.oracolo.cloud.exception;

public class RecipeNotFoundException extends RuntimeException{

    public RecipeNotFoundException(){
        super("Recipe not found");
    }
}
