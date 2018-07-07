package com.saleshero.sorter.exception;

public class FileManagementException extends Throwable {
    public FileManagementException(String message, Exception e){
        super(message,e);
    }

    public FileManagementException(String message) {
        super(message);
    }
}
