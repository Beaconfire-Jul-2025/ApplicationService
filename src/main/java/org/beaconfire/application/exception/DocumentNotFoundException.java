package org.beaconfire.application.exception;

public class DocumentNotFoundException extends RuntimeException {
    public DocumentNotFoundException(Long id) {
        super("Document not found with id: " + id);
    }
}
