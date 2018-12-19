package com.n4systems.util.persistence.image;

import com.n4systems.exceptions.FileProcessingException;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.model.orgs.SecondaryOrg;
import com.n4systems.model.user.User;
import com.n4systems.reporting.PathHandler;
import com.n4systems.util.ServiceLocator;

import java.io.File;

public class FileSystemSecondaryOrgReportFileProcessor {
    private S3Service s3Service = ServiceLocator.getS3Service();
    private SecondaryOrg secondaryOrg;


	public FileSystemSecondaryOrgReportFileProcessor(SecondaryOrg secondaryOrg) {
        this.secondaryOrg = secondaryOrg;
	}


	public void process(UploadedImage secondaryOrgLogoImage) throws FileProcessingException {
        if (secondaryOrgLogoImage.isRemoveImage()) {
            removeExistingSignature();
        }
		if (secondaryOrgLogoImage.isNewImage()) {
			putNewImageInPlace(secondaryOrgLogoImage);
		}
	}


	private void putNewImageInPlace(UploadedImage secondaryOrgLogoImage) {
        s3Service.uploadSecondaryOrgLogoImage(tempFile(secondaryOrgLogoImage), secondaryOrg);
	}

	private File tempFile(UploadedImage secondaryOrgLogoImage) {
		return new File(PathHandler.getTempRoot().getAbsolutePath() + '/' + secondaryOrgLogoImage.getUploadDirectory());
	}

    private void removeExistingSignature() {
        if (s3Service.secondaryOrgCertificateLogoExists(secondaryOrg.getId())) {
            s3Service.removeSecondaryOrgLogoImage(secondaryOrg);
        }
    }
}
