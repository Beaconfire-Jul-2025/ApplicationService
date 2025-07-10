package org.beaconfire.application.exception;

public class ApplicationAlreadyExistsException extends RuntimeException {
    public ApplicationAlreadyExistsException(String employeeId) {
        super("Application already exists for employee: " + employeeId);
    }
}
