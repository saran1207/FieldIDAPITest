package com.n4systems.model.asset;

import com.n4systems.model.Asset;
import com.n4systems.model.Attachment;
import com.n4systems.model.api.Note;
import com.n4systems.model.api.Saveable;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.model.security.SecurityDefiner;

import javax.persistence.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

@Entity
@Table(name = "assetattachments")
public class AssetAttachment extends EntityWithTenant implements Saveable,
		Attachment {
	private static final long serialVersionUID = 1L;

	public static SecurityDefiner createSecurityDefiner() {
		return new SecurityDefiner(SecurityDefiner.DEFAULT_TENANT_PATH, "asset." + SecurityDefiner.DEFAULT_OWNER_PATH, null, null);
	}

	@ManyToOne
	@JoinColumn(name = "asset_id")
	private Asset asset;
	private Note note = new Note();
	private String mobileId;

	@Transient
	private byte[] data;

    @Transient
    private boolean uploadInProgress;

	public AssetAttachment() {
	}

	public AssetAttachment(Note note) {
		this.note = note;
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
	
	private void ensureMobileIdIsSet() {
		if (mobileId == null) {
			mobileId = UUID.randomUUID().toString();
		}
	}

	public Asset getAsset() {
		return asset;
	}

	public void setAsset(Asset asset) {
		this.asset = asset;
	}

	public Note getNote() {
		return note;
	}

	public void setNote(Note note) {
		this.note = note;
	}

	public String getComments() {
		return note.getComments();
	}

    //the attachment Filename field is overloaded to house full path instead of just the filename
	public String getFileName() {
		return note.getFileName();
	}

	public void setComments(String comment) {
		note.setComments(comment);
	}

	public void setFileName(String fileName) {
		note.setFileName(fileName);
	}

	public boolean isImage() {
		return note.isImage();
	}

    public boolean isRemote() {
        //the local files only contain the filename, whereas the files on s3 have a full path
        return getFileName().indexOf('/') != -1;
    }

	@Override
	public boolean hasAttachedFile() {
		return note.hasAttachedFile();
	}

	public String getMobileId() {
		return mobileId;
	}

	public void setMobileId(String mobileId) {
		this.mobileId = mobileId;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

    public boolean isUploadInProgress() {
        return uploadInProgress;
    }

    public void setUploadInProgress(boolean uploadInProgress) {
        this.uploadInProgress = uploadInProgress;
    }
}
