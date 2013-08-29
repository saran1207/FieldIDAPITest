package com.n4systems.model.downloadlink;

import com.n4systems.model.api.HasUser;
import com.n4systems.model.api.NamedEntity;
import com.n4systems.model.api.Saveable;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.model.security.SecurityDefiner;
import com.n4systems.model.user.User;
import com.n4systems.util.mail.MailMessage;

import javax.persistence.*;
import java.io.File;
import java.util.UUID;

@Entity
@Table(name = "downloads")
public class DownloadLink extends EntityWithTenant implements HasUser, Saveable, NamedEntity {
	private static final long serialVersionUID = 1L;
	private static final String DOWNLOAD_FILE_EXT = "dl";
	public static SecurityDefiner createSecurityDefiner() {
		return new SecurityDefiner(DownloadLink.class);
	}
	
	@Column(nullable = false)
	private String name;

	@Column(nullable = true, unique = true)
	private String downloadId;
	
	@Column(name="contenttype", nullable = false)
	@Enumerated(EnumType.STRING)
	private ContentType contentType;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private DownloadState state = DownloadState.REQUESTED;

	@ManyToOne(fetch=FetchType.EAGER, optional=false)
	@JoinColumn(name="user_id", nullable=false)
	private User user;
	
	@Override
	protected void onCreate() {
		super.onCreate();
		generateDownloadId();
	}

	@Override
	protected void onUpdate() {
		super.onUpdate();
		generateDownloadId();
	}
	
	private void generateDownloadId() {
		if (downloadId == null) {
			String randomUuid= UUID.randomUUID().toString();
			downloadId = randomUuid.substring(0,randomUuid.indexOf("-", 10));
		}
	}
	
	@Override
	public String toString() {
		return String.format("%s (%d) {%s}", name, getId(), user);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ContentType getContentType() {
		return contentType;
	}

	public void setContentType(ContentType contentType) {
		this.contentType = contentType;
	}

	public DownloadState getState() {
		return state;
	}

	public void setState(DownloadState state) {
		this.state = state;
	}

	@Override
	public User getUser() {
		return user;
	}

	@Override
	public void setUser(User user) {
		this.user = user;
	}
	
	public String prepareFileName() {
		if (name == null || contentType == null) {
			throw new IllegalStateException("name/contentType cannot be null");
		}
		return contentType.prepareFileName(name);
	}
	
	public File getFile() {
		return getFile(true);
	}
	
	public File getFile(boolean createParents) {
		if (user == null) {
			throw new IllegalStateException("user cannot be null");
		}
		
		File downloadFile = new File(user.getPrivateDir(), createFilesystemName());
		
		if (createParents) {
			ensureParentDirectoriesExist(downloadFile);
		}
		
		return downloadFile;
	}
	
	private String createFilesystemName() {
		if (getId() == null) {
			throw new IllegalStateException("id cannot be null");
		}
		return String.format("%06d.%s", getId(), DOWNLOAD_FILE_EXT);
	}

	private void ensureParentDirectoriesExist(File downloadFile) {
		File parentDir = downloadFile.getParentFile();
		
		if (!parentDir.isDirectory() && !parentDir.mkdirs()) {
			throw new SecurityException("Could not create directory [" + parentDir.toString() + "]");
		}
	}
	
	public MailMessage generateMailMessage(String body) {
		return new MailMessage(getName(), body, getUser().getEmailAddress());
	}
	
	public boolean isDownloaded(){
		return state.equals(DownloadState.DOWNLOADED);
	}

	public String getDownloadId() {
		return downloadId;
	}

}
