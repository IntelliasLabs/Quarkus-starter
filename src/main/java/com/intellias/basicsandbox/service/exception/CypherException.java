package com.intellias.basicsandbox.service.exception;

public class CypherException extends RuntimeException {

    public CypherException(String message, Exception e) {
        super(message, e);
    }

}
