package com.oracolo.cloud.exception;

public class TooManyIngredientsException extends RuntimeException{

    public TooManyIngredientsException(){
        super("Too many ingredients");
    }
}
