package com.oracolo.cloud.exception.mapper;

import com.oracolo.cloud.exception.ErrorCode;
import com.oracolo.cloud.exception.ImageTooBigException;
import com.oracolo.cloud.exception.TooManyIngredientsException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class TooManyIngredientsExceptionMapper extends BaseMapper implements ExceptionMapper<TooManyIngredientsException> {
    @Override
    public Response toResponse(TooManyIngredientsException exception) {
        return response(Response.Status.BAD_REQUEST.getStatusCode(), ErrorCode.TOO_MANY_INGREDIENTS, exception.getMessage());
    }
}
