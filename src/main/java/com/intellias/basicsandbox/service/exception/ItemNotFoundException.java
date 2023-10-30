package com.intellias.basicsandbox.service.exception;

import java.util.UUID;

public class ItemNotFoundException extends RuntimeException {

    public ItemNotFoundException(UUID id) {
        super("An item with ID " + id + " not found.");
    }

}
