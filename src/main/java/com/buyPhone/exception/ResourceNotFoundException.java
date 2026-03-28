package com.buyPhone.exception;


import java.util.UUID;

public class ResourceNotFoundException extends AppException {

    public ResourceNotFoundException(String resource, String id) {
        super(resource + " not found with id: " + id);
    }

}
