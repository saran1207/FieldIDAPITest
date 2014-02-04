package com.n4systems.model.attachment;

public interface Attachment {

    enum State {VOID, LIMBO, UPLOADED };
    enum Type {LOCAL_FILE, S3_FILE};

    public Type getType();
    void setState(State state);
    State getState();
    String getPath();
    String getComments();
    String getContentType();
    byte[] getBytes();
    // getFileName();   use GUID as path filename part.

    // instead of db...
    String getFileName();
    //  PATH | MD5 | CONTENTTYPE | COMMENTS | TYPE

    // use...

    //  KEY     | FILENAME     | CONTENTTYPE | COMMENTS | TYPE
    //   guid       orgi.jpg      image/jpg     hello     S3_IMAGE

}
