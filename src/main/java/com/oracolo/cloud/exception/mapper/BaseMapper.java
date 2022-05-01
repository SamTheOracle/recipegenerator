package com.oracolo.cloud.exception.mapper;

import com.oracolo.cloud.rest.dto.ErrorDto;
import com.oracolo.cloud.exception.ErrorCode;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public abstract class BaseMapper {

    protected static Response response(int status, ErrorCode errorCode, String message){
        ErrorDto errorDto = new ErrorDto();
        errorDto.errorCode = errorCode;
        errorDto.message = message;
        return Response.status(status).entity(errorDto).type(MediaType.APPLICATION_JSON).build();
    }
}
