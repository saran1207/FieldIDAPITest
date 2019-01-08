package com.n4systems.util.persistence.image;

import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.exceptions.FileProcessingException;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.reporting.PathHandler;
import com.n4systems.util.ServiceLocator;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileSystemBrandingLogoFileProcessor {
    private S3Service s3Service = ServiceLocator.getS3Service();
    private InternalOrg internalOrg;
    private static final Logger logger = Logger.getLogger(FileSystemBrandingLogoFileProcessor.class);

    public FileSystemBrandingLogoFileProcessor(InternalOrg internalOrg) {
        this.internalOrg = internalOrg;
    }


    public void process(UploadedImage internalOrgLogoImage) throws FileProcessingException {
        if (internalOrgLogoImage.isRemoveImage()) {
            removeExistingInternalOrgLogoImage();
            internalOrgLogoImage.getImage().delete();
        }
        if (internalOrgLogoImage.isNewImage()) {
            putNewImageInPlace(internalOrgLogoImage);
        }
    }


    private void putNewImageInPlace(UploadedImage internalOrgLogoImage) {
        putNewImageForBrandingLogo(tempFile(internalOrgLogoImage));
    }

    private void putNewImageForBrandingLogo(File internalOrgLogoImageFile) {

        try {
            byte[] brandingLogoImageData = FileUtils.readFileToByteArray(internalOrgLogoImageFile);
            String contentType = new MimetypesFileTypeMap().getContentType(internalOrgLogoImageFile);
            s3Service.uploadBrandingLogo(contentType, brandingLogoImageData);
        }
        catch(FileNotFoundException e) {
            logger.warn("Unable to read from temp branding logo Image file at: " + internalOrgLogoImageFile, e);
            throw new FileAttachmentException("Unable to read from temp branding logo Image file at: " + internalOrgLogoImageFile, e);
        }
        catch(IOException e) {
            logger.warn("Unable to upload branding logo Image file to S3", e);
            throw new FileAttachmentException("Unable to upload branding logo Image file to S3", e);
        }
    }

    private File tempFile(UploadedImage internalOrgLogoImage) {
        return new File(PathHandler.getTempRoot().getAbsolutePath() + '/' + internalOrgLogoImage.getUploadDirectory());
    }

    private void removeExistingInternalOrgLogoImage() {
        if (s3Service.isBrandingLogoExists(internalOrg.getId(), internalOrg.isPrimary())) {
            s3Service.removeBrandingLogo();
        }
    }
}
