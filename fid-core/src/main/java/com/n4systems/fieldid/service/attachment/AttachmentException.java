package com.n4systems.fieldid.service.attachment;

public class AttachmentException extends RuntimeException {

    public AttachmentException(Throwable cause) {
        super(cause);
    }

    public AttachmentException(String message, Throwable cause) {
        super(message, cause);
    }
}
