package com.n4systems.model.attachment;

public interface Attachment {

    enum State {VOID, LIMBO, UPLOADED };
    enum Type {LOCAL_FILE, S3_IMAGE, S3_FILE};

    public Type getType();
    void setState(State state);
    State getState();
    String getPath();
    String getTempPath();
    String getComments();
    String getContentType();
    byte[] getBytes();

}
