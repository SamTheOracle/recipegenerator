package com.oracolo.cloud.exception.mapper;

import com.oracolo.cloud.exception.ErrorCode;
import com.oracolo.cloud.exception.RecipeNotFoundException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class RecipeNotFoundExceptionMapper extends BaseMapper implements ExceptionMapper<RecipeNotFoundException> {
    @Override
    public Response toResponse(RecipeNotFoundException exception) {
        return response(Response.Status.NOT_FOUND.getStatusCode(), ErrorCode.RECIPE_NOT_FOUND, exception.getMessage());
    }
}
