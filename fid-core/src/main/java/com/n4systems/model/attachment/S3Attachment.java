package com.n4systems.model.attachment;

public interface S3Attachment {

    public final static String TENANT_PREFIX = "tenants/";

    String getPath();
    String getTempPath();
    String getComments();
    String getContentType();
    byte[] getBytes();
}
