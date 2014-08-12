package com.n4systems.fieldid.actions.users;

import java.io.File;
import java.io.IOException;

import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.model.user.User;
import com.n4systems.util.ServiceLocator;
import org.apache.commons.io.FileUtils;

import com.n4systems.exceptions.FileProcessingException;
import com.n4systems.reporting.PathHandler;

public class FileSystemUserSignatureFileProcessor {
    private S3Service s3Service = ServiceLocator.getS3Service();
    private User user;


	public FileSystemUserSignatureFileProcessor(User user) {
        this.user = user;
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
        s3Service.uploadUserSignature(tempFile(signature), user);
	}

	private File tempFile(UploadedImage signature) {
		return new File(PathHandler.getTempRoot().getAbsolutePath() + '/' + signature.getUploadDirectory());
	}

    private void removeExistingSignature() {
        if (s3Service.userSignatureExists(user)) {
            s3Service.removeUserSignature(user);
        }
    }
}
