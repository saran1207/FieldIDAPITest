package com.n4systems.model.attachment;

public interface S3Attachment {

    public final static String TENANT_PREFIX = "tenants/";

    String getFileName();
    String getComments();
    String getContentType();
    String getTempFileName();
    String getMd5sum();
    byte[] getBytes();
    String getTempPath();
    void setTempFileName(String uuid);
}
