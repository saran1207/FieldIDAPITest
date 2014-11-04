package com.n4systems.exceptions.loto;

public class AnnotatedImageGenerationException extends Exception {

    public AnnotatedImageGenerationException() {
    }

    public AnnotatedImageGenerationException(String message) {
        super(message);
    }

    public AnnotatedImageGenerationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AnnotatedImageGenerationException(Throwable cause) {
        super(cause);
    }

    public AnnotatedImageGenerationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
