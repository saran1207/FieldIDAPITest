package com.n4systems.model;

import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.model.user.User;
import com.n4systems.util.ContentTypeUtil;
import org.springframework.util.StringUtils;

import javax.activation.FileTypeMap;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "fileattachments")
public class FileAttachment extends EntityWithTenant implements Attachment {
	private static final long serialVersionUID = 1L;

	@Column(length=255)
	private String fileName;

	private String comments;
    private String mobileId;
	
	public FileAttachment() {}

	public FileAttachment(Tenant tenant, User modifiedBy, String fileName) {
		super(tenant);
		setModifiedBy(modifiedBy);
		setFileName(fileName);
	}

    @Override
    protected void onCreate() {
        super.onCreate();
        ensureMobileIdIsSet();
    }

    @Override
    protected void onUpdate() {
        super.onUpdate();
        ensureMobileIdIsSet();
    }

    public void ensureMobileIdIsSet() {
        if (getMobileId() == null || getMobileId().length() < 1) {
            setMobileId(UUID.randomUUID().toString());
        }
    }

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

    public String getDisplayFileName() {
        return getFileName().substring(getFileName().lastIndexOf('/') + 1);
    }

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

    public String getMobileId() {
        return mobileId;
    }

    public void setMobileId(String mobileId) {
        this.mobileId = mobileId;
    }
	
	/**
	 * Tests if the filename has a content type starting with <code>'image/'</code>. The
	 * content type is queried from {@link FileTypeMap#getContentType(String)}.
	 * 
	 * @param fileName	String file name, including extension.
	 * @return			<code>true</code> if content type starts with <code>'image/'</code>.
	 */
	public boolean isImage() {
		if (hasAttachedFile()) {
			String contentType = getContentType(fileName);
			return contentType.startsWith("image/");
		}
		return false;
	}

    public boolean isRemote() {
        //the local files only contain the filename, whereas the files on s3 have a full path
        //files in temp folder will have one /, so we are counting if there is more than 1
        return StringUtils.countOccurrencesOf(getFileName(), "/") > 1;
    }

	protected String getContentType(String fileName) {
		return ContentTypeUtil.getContentType(fileName);
	}

	public boolean hasAttachedFile() {
		return fileName != null && !fileName.isEmpty();
	}

}
