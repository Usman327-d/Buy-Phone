package com.buyPhone.exception;


public class ResourceNotFoundException extends AppException {

    public ResourceNotFoundException(String resource, String id) {
        super(resource + " not found with id: " + id);
    }

}
