package com.n4systems.model.attachment;

import com.n4systems.model.parents.EntityWithTenant;
import org.apache.commons.codec.digest.DigestUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "s3_attachment")
public abstract class AbstractS3Attachment extends EntityWithTenant implements S3Attachment {

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = true)
    private String comments;

    @Column(name = "md5sum")
    private String md5sum;

    @Column(nullable=false)
    private String contentType;

    protected @Transient String tempFileName;

    private @Transient byte[] bytes;


    public AbstractS3Attachment() {

    }

    public AbstractS3Attachment(String fileName, String contentType, byte[] bytes) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.bytes = bytes;
        this.md5sum = DigestUtils.md5Hex(bytes);
    }


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getTempFileName() {
        return tempFileName;
    }

    public String getMd5sum() {
        return md5sum;
    }

    public void setMd5sum(String md5sum) {
        this.md5sum = md5sum;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setTempFileName(String tempFileName) {
        this.tempFileName = tempFileName;
    }

    public String getPath() {
        return TENANT_PREFIX + getTenantId() + getRelativePath();
    }

    public String getTempPath() {
        return TENANT_PREFIX + getTenantId() + getRelativeTempPath();
    }

    protected abstract String getRelativeTempPath();

    protected abstract String getRelativePath();

    public abstract Long getTenantId();

}
