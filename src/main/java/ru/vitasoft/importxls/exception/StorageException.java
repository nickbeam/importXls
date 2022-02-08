package ru.vitasoft.importxls.exception;

public class StorageException extends RuntimeException {
    public StorageException(Exception e) {
        super(e.getMessage(), e);
    }
}
