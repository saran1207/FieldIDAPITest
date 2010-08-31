package com.n4systems.fieldid.actions.users;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.n4systems.exceptions.FileProcessingException;
import com.n4systems.reporting.PathHandler;

public class FileSystemUserSignatureFileProcessor {
	private final File userImagePath;
	

	public FileSystemUserSignatureFileProcessor(File userImagePath) {
		super();
		this.userImagePath = userImagePath;
	}


	public void process(UploadedImage signature) throws FileProcessingException {
		if (signature.isRemoveImage()) {
			removeExistingSignature();
		}
		
		if (signature.isNewImage()) {
			putNewImageInPlace(signature);
		}
	}


	private void putNewImageInPlace(UploadedImage signature) {
		removeExistingSignature();
		ensureDirectoryIsAvailable();
		copyTempImageToSignatureLocation(signature);
	}


	private void copyTempImageToSignatureLocation(UploadedImage signature) {
		try {
			FileUtils.copyFile(tempFile(signature), userImagePath);
		} catch (IOException e) {
			throw new FileProcessingException(e);
		}
	}


	private File tempFile(UploadedImage signature) {
		return new File(PathHandler.getTempRoot().getAbsolutePath() + '/' + signature.getUploadDirectory());
	}


	private void ensureDirectoryIsAvailable() {
		if (!userImagePath.getParentFile().exists()) {
			userImagePath.getParentFile().mkdirs();
		}
	}


	private void removeExistingSignature() {
		if (userImagePath.exists()) {
			userImagePath.delete();
		}
	}

}
