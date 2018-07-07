package com.saleshero.sorter.exception;

public class SorterException extends Throwable {
    public SorterException(String message, Exception e){
        super(message,e);
    }

    public SorterException(String message) {
        super(message);
    }
}
