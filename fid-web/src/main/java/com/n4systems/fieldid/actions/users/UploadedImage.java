package com.n4systems.fieldid.actions.users;

import com.n4systems.util.StringUtils;

import java.io.File;
import java.io.Serializable;

public class UploadedImage implements Serializable {
	private File currentImage;
	private String uploadDirectory;
	private boolean removeImage = false;
	private boolean newImage = false;
	
	
	public boolean isRemoveImage() {
		return removeImage;
	}
	public void setRemoveImage(boolean removeImage) {
		this.removeImage = removeImage;
	}
	public boolean isNewImage() {
		return newImage;
	}
	public void setNewImage(boolean newImage) {
		this.newImage = newImage;
	}
	public File getImage() {
		return currentImage;
	}
	public void setImage(File image) {
		this.currentImage = image;
	}
	public String getUploadDirectory() {
		return uploadDirectory;
	}
	public void setUploadDirectory(String uploadDirectory) {
		this.uploadDirectory = uploadDirectory;
	}
	public boolean isExistingImage() {
		return currentImage != null || StringUtils.isNotEmpty(uploadDirectory);
	}
	
	
	
}