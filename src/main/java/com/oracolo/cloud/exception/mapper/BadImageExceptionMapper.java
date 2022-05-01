package com.oracolo.cloud.exception.mapper;

import com.oracolo.cloud.exception.ErrorCode;
import com.oracolo.cloud.exception.ImageTooBigException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class BadImageExceptionMapper extends BaseMapper implements ExceptionMapper<ImageTooBigException> {
    @Override
    public Response toResponse(ImageTooBigException exception) {
        return response(Response.Status.BAD_REQUEST.getStatusCode(), ErrorCode.IMAGE_TOO_BIG, exception.getMessage());
    }
}
