package com.n4systems.model.common;

public interface S3Image {

    String getS3Path();
    Long getId();

    String getTempFileName();
    void setTempFileName(String tempFileName);

    String getContentType();
    void setContentType(String contentType);

}
