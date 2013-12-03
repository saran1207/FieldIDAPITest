package com.n4systems.model.attachment;

import com.google.common.base.Preconditions;
import com.n4systems.model.Tenant;
import com.n4systems.model.parents.EntityWithTenant;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

import javax.persistence.*;

@Entity
@Table(name = "s3_attachments")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public abstract class AbstractS3Attachment extends EntityWithTenant implements S3Attachment {

    public enum Type {
        PLACE,THING,PERSON   // EVENT, etc...?
    }

    @Column(nullable = false)
    private String path;

    @Column(nullable = true)
    private String comments;

    @Column(name = "md5_sum", nullable=false)
    private String md5sum;

    @Column(name="content_type", nullable=false)
    private String contentType;

    @Column(insertable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private Type type;

    protected @Transient String tempPath;
    protected @Transient String fileName;
    protected @Transient byte[] bytes;


    @Deprecated // for hibernate only.
    public AbstractS3Attachment() {
    }

    public AbstractS3Attachment(Type type, Tenant tenant) {
        super(tenant);
        this.type = type;
    }

    public AbstractS3Attachment withContent(String fileName, String contentType, byte[] bytes) {
        Preconditions.checkArgument(StringUtils.isNotBlank(fileName)
                && StringUtils.isNotBlank(contentType)
                && bytes!=null && bytes.length>0,
                    "you must specify all non-null args when creating a new attachment. ");
        this.fileName = fileName;
        this.contentType = contentType;
        this.bytes = bytes;
        this.md5sum = DigestUtils.md5Hex(bytes);
        setPath(TENANT_PREFIX + getTenantId() + getRelativePath());
        return this;
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

    public String getMd5sum() {
        return md5sum;
    }

    public void setMd5sum(String md5sum) {
        this.md5sum = md5sum;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTempPath() {
        return tempPath;
    }

    public AbstractS3Attachment withTempFileName(String tempFileName) {
        tempPath = TENANT_PREFIX + getTenantId() + getRelativeTempPath(tempFileName);
        return this;
    }

    protected abstract String getRelativeTempPath(String fileName);

    protected abstract String getRelativePath();

    public abstract Long getTenantId();

}
