package com.nwtkts.uber.exception;

public class ClientRideAlreadyRatedException extends RuntimeException{

    public ClientRideAlreadyRatedException() {}

    public ClientRideAlreadyRatedException(String message) {
        super(message);
    }

}
