package com.n4systems.fieldid.actions.helpers;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


import com.n4systems.ejb.ConfigManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.Inspection;
import com.n4systems.model.api.HasFileAttachments;
import com.n4systems.model.product.AssetAttachment;
import com.n4systems.model.user.User;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;

@SuppressWarnings("serial")
abstract public class UploadAttachmentSupport extends AbstractCrud {

	protected ConfigManager configManager;
	
	private List<AssetAttachment> uploadedFiles = new ArrayList<AssetAttachment>();
	private List<AssetAttachment> attachments = new ArrayList<AssetAttachment>();

	public UploadAttachmentSupport(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}
	
	public List<AssetAttachment> getUploadedFiles() {
		return uploadedFiles;
	}

	@Validations(
		customValidators = {
			@CustomValidator( type="uploadAttachmentValidator", message="", key="error.filenameused" ),
			@CustomValidator( type="uploadAttachmentSecurityValidator", message="", key="error.invalidfile" ),
			@CustomValidator( type="uploadAttachmentLimitValidator", message="", key="error.filelimit" )
		}
	)
	public void setUploadedFiles( List<AssetAttachment> uploadedFiles ) {
		this.uploadedFiles = uploadedFiles;
	}
	
	public List<AssetAttachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<AssetAttachment> attachments) {
		this.attachments = attachments;
	}
	
	public void setAttachments(Set<AssetAttachment> attachments) {
		this.attachments.clear();
		this.attachments.addAll(attachments);
	}
	
	public Integer getFileUploadMax() {
		return ConfigContext.getCurrentContext().getInteger(ConfigEntry.PRODUCT_FILE_UPLOAD_MAX);
	}
	
	public String getFileName(String file) {
		return (new File(file)).getName();
	}

	/**
	 * Used to prepare the attachment lists from beans implementing {@link HasFileAttachments} (namely 
	 * {@link Inspection} and {@link com.n4systems.model.AssetType}), post file upload and prior to update<p />
	 * Preparation consists of 2 parts:<ol>
	 * <li>Any attachment <b>not</b> found in the {@link #getAttachments() attachments} list 
	 * will be removed from <tt>hasAttachments</tt> {@link HasFileAttachments#getAttachments() attachments} list.
	 * The match performed is by <tt>id</tt>.</li>
	 * <li>Attachments which remain attached will have their {@link FileAttachment#getComments() comments} and
	 * {@link FileAttachment#getModifiedBy() modifiedBy} user updated provided the <tt>comments</tt> have changed.</li>
	 * <p/>
	 * @param hasAttachments	A bean implementing {@link HasFileAttachments} to update and clean inspections on
	 * @param modifiedBy		The {@link User} to set as the modifiedBy user if comments on the attachment have changed. 
	 */
	protected void updateAttachmentList(HasFileAttachments hasAttachments, User modifiedBy) {
		// Create a map of attachment ids to their comments so we know which ones are still attached and 
		// can update the comments
		String comment = null;
		Map<Long, String> updatedAttachments = new HashMap<Long, String>();
		for(AssetAttachment attachment: attachments) {
			// null out empty comments
			comment = (attachment.getNote().getComments() != null && attachment.getNote().getComments().trim().length() > 0) ? attachment.getNote().getComments().trim() : null;
			updatedAttachments.put(attachment.getId(), comment);
		}
		
		// Construct a list of attachments that should no longer be attached
		List<FileAttachment> removedAttachments = new ArrayList<FileAttachment>();
		for(FileAttachment oldAttachment: hasAttachments.getAttachments()) {
			// if this attachment is not in our map of attachments
			if(!updatedAttachments.containsKey(oldAttachment.getId())) {
				removedAttachments.add(oldAttachment);
			} else {
				// update the comments and the modified by
				oldAttachment.setModifiedBy(modifiedBy);
				oldAttachment.setComments(updatedAttachments.get(oldAttachment.getId()));
			}
		}
		
		// now we can remove all unattached attachments
		hasAttachments.getAttachments().removeAll(removedAttachments);
	}
	
	
}
