package com.n4systems.model.attachment;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.n4systems.model.Tenant;
import com.n4systems.model.parents.EntityWithTenant;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "s3_attachments")    // TODO : change this table name to 'attachments' or 'blobs' or 'data-files' or ????
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public abstract class AbstractAttachment extends EntityWithTenant implements Attachment {

    @Column(nullable = false)
    private String path;

    @Column(nullable = true)
    private String comments;

    @Column(name = "md5_sum", nullable=false)
    private String md5sum;

    @Column(name="content_type", nullable=false)
    private String contentType;

    @Enumerated(EnumType.STRING)
    @Column(insertable = false, updatable = false)
    private Type type = Type.LOCAL_FILE;

    @Column(name="file_name")
    private String fileName;

    @Column(name="meta_inf")
    private String metaInf;

    protected @Transient State state = State.UPLOADED;
    protected @Transient String guid;
    protected @Transient byte[] bytes;
    protected @Transient List<String> subdirectories = Lists.newArrayList();
    protected @Transient Map<String, String> meta = Maps.newHashMap();
    protected @Transient String prefix = "";


    @Deprecated // for hibernate only.
    public AbstractAttachment() {
    }

    protected AbstractAttachment(Type type, Tenant tenant) {
        super(tenant);
        setType(type);
    }

    public <T extends AbstractAttachment> T withContent(String fileName, String contentType, byte[] bytes) {
        // TODO : maybe add GUID or timestamp prefix to filename to avoid collision?
        Preconditions.checkArgument(StringUtils.isNotBlank(fileName)
                && StringUtils.isNotBlank(contentType)
                && bytes!=null && bytes.length>0,
                    "you must specify all non-null args when creating a new attachment. ");
        this.fileName = fileName;
        this.contentType = contentType;
        this.bytes = bytes;
        this.md5sum = DigestUtils.md5Hex(bytes);
        this.state = State.VOID;
        calculatePaths();
        return (T) this;
    }

    public <T extends AbstractAttachment> T withMetaInf(String... inf) {
        meta.put("basic",Joiner.on("/").join(inf));
        generateMetaInf();
        return (T) this;
    }

    public <T extends AbstractAttachment> T addMetaInf(String key, Object value) {
        Preconditions.checkNotNull(key,"meta info key must not be null");
        Preconditions.checkNotNull(value,"meta info value must not be null");
        meta.put(key, value.toString());
        generateMetaInf();
        return (T) this;
    }

    public <T extends AbstractAttachment> T withSubdirectories(String... dirs) {
        // set path again because subdirectories have changed.  don't do this after you saved entity!
        Preconditions.checkState(getId()==null);
        subdirectories.addAll(Lists.newArrayList(dirs));
        calculatePaths();
        return (T) this;
    }

    public <T extends AbstractAttachment> T withPrefix(String prefix) {
        this.prefix = prefix;
        return (T) this;
    }


    public <T extends AbstractAttachment> T setSubdirectories(String... dirs) {
        subdirectories = Lists.newArrayList();
        withSubdirectories(dirs);
        return (T) this;
    }

    private void calculatePaths() {
        this.path = getSubdirectoryAndFilename();
    }

    protected String getSubdirectoryAndFilename(String... optionalDirs) {
        Preconditions.checkNotNull(guid,"you must give the attachment a name (guid) before using.  " + getFileName());
        List<String> path = Lists.newArrayList(optionalDirs);
        path.addAll(subdirectories);
        path.add(guid);
        return prefix + Joiner.on("/").skipNulls().join(path);
    }

    private void generateMetaInf() {
        this.metaInf = Joiner.on("&").withKeyValueSeparator("=").join(meta);
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
        Preconditions.checkArgument(type!=null);
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    protected Long getTenantId() {
        return getTenant().getId();
    }

    public State getState() {
        return state;
    }

    @Override
    public void setState(State state) {
        this.state = state;
    }

    public <T extends AbstractAttachment> T named(String guid) {
        this.guid = guid;
        return (T) this;
    }

    public String getMetaInf() {
        return this.metaInf;
    }

    public void setMetaInf(String inf) {
        this.metaInf = inf;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
