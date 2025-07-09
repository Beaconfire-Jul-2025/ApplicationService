package org.beaconfire.application.exception;


public class ApplicationAlreadyExistsException extends RuntimeException {

    public ApplicationAlreadyExistsException(String userId) {
        super("An application already exists for userId: " + userId);
    }
}
