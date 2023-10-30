package com.intellias.basicsandbox.controller.exception;

import com.intellias.basicsandbox.controller.dto.ErrorDTO;
import com.intellias.basicsandbox.service.exception.ItemNotFoundException;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class RestExceptionHandler implements ExceptionMapper<ItemNotFoundException> {

    @Override
    public Response toResponse(ItemNotFoundException ex) {
        ErrorDTO error = new ErrorDTO(Response.Status.NOT_FOUND, ex.getMessage());
        return Response.status(Response.Status.NOT_FOUND).entity(error).build();
    }
}
