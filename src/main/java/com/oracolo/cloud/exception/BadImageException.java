package com.oracolo.cloud.exception;

public class BadImageException extends RuntimeException{

    public BadImageException(){
        super("Error during reading of image");
    }
}
