package ru.vitasoft.exception;

public class ExistStorageException extends StorageException {
    public ExistStorageException(String uuid) {
        super("Error: resume with uuid: " + uuid + " already exist!", uuid);
    }
}
