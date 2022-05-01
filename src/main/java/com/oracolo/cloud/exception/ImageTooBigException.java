package com.oracolo.cloud.exception;

public class ImageTooBigException extends RuntimeException{

    public ImageTooBigException(){
        super("Provided image is way too big");
    }
}
