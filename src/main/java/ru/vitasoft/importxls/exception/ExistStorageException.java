package ru.vitasoft.importxls.exception;

public class ExistStorageException extends StorageException {
    public ExistStorageException(String uuid) {
        super("Error: resume with uuid: " + uuid + " already exist!", uuid);
    }
}
