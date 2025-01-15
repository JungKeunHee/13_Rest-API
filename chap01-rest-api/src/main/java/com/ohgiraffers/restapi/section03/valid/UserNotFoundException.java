package com.ohgiraffers.restapi.section03.valid;

public class UserNotFoundException extends Throwable {

    public UserNotFoundException(String message) {
        super(message);
    }
}
