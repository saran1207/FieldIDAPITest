package com.n4systems.model.asset;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.n4systems.model.Asset;
import com.n4systems.model.Attachment;
import com.n4systems.model.api.Note;
import com.n4systems.model.api.Saveable;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.model.security.SecurityDefiner;

@Entity
@Table(name = "assetattachments")
public class AssetAttachment extends EntityWithTenant implements Saveable, Attachment {
	private static final long serialVersionUID = 1L;

	public static SecurityDefiner createSecurityDefiner() {
		return new SecurityDefiner(SecurityDefiner.DEFAULT_TENANT_PATH, "asset." + SecurityDefiner.DEFAULT_OWNER_PATH, null, null);
	}
	
	@ManyToOne
    @JoinColumn(name="product_id")
	private Asset asset;
	private Note note = new Note();
	
	public AssetAttachment() {
	}
	
	public AssetAttachment(Note note) {
		this.note = note;
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

	@Override
	public boolean hasAttachedFile() {
		return note.hasAttachedFile();
	}

	
}
