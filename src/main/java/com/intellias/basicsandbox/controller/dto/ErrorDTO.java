package com.intellias.basicsandbox.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.ws.rs.core.Response;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDTO {
    private Response.Status status;
    private String message;
}

