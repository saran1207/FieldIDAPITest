package com.n4systems.util.persistence.image;

import com.n4systems.exceptions.FileProcessingException;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.orgs.SecondaryOrg;
import com.n4systems.reporting.PathHandler;
import com.n4systems.util.ServiceLocator;

import java.io.File;

public class FileSystemInternalOrgReportFileProcessor {
    private S3Service s3Service = ServiceLocator.getS3Service();
    private InternalOrg internalOrg;


    public FileSystemInternalOrgReportFileProcessor(InternalOrg internalOrg) {
        this.internalOrg = internalOrg;
    }


    public void process(UploadedImage internalOrgLogoImage) throws FileProcessingException {
        if (internalOrgLogoImage.isRemoveImage()) {
            removeExistingInternalOrgLogoImage();
        }
        if (internalOrgLogoImage.isNewImage()) {
            putNewImageInPlace(internalOrgLogoImage);
        }
    }


    private void putNewImageInPlace(UploadedImage internalOrgLogoImage) {
        s3Service.uploadInternalOrgLogoImage(tempFile(internalOrgLogoImage), internalOrg);
    }

    private File tempFile(UploadedImage internalOrgLogoImage) {
        return new File(PathHandler.getTempRoot().getAbsolutePath() + '/' + internalOrgLogoImage.getUploadDirectory());
    }

    private void removeExistingInternalOrgLogoImage() {
        if (s3Service.isCertificateLogoExists(internalOrg.getId(), internalOrg.isPrimary())) {
            s3Service.removeInternalOrgLogoImage(internalOrg);
        }
    }
}
