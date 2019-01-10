package com.n4systems.util.persistence.image;

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

public class FileSystemInternalOrgReportFileProcessor {
    private S3Service s3Service = ServiceLocator.getS3Service();
    private InternalOrg internalOrg;
    private static final Logger logger = Logger.getLogger(FileSystemInternalOrgReportFileProcessor.class);

    public FileSystemInternalOrgReportFileProcessor(InternalOrg internalOrg) {
        this.internalOrg = internalOrg;
    }


    public void process(UploadedImage internalOrgLogoImage) throws FileProcessingException,FileNotFoundException,IOException {
        if (internalOrgLogoImage.isRemoveImage()) {
            removeExistingInternalOrgLogoImage();
            internalOrgLogoImage.getImage().delete();
        }
        if (internalOrgLogoImage.isNewImage()) {
            putNewImageInPlace(internalOrgLogoImage);
        }
    }


    private void putNewImageInPlace(UploadedImage internalOrgLogoImage) throws FileNotFoundException,IOException {
        s3Service.uploadInternalOrgLogoImage(tempFile(internalOrgLogoImage), internalOrg);
        if (internalOrg.isPrimary()) {
            putNewImageForPrimaryOrg(tempFile(internalOrgLogoImage), internalOrg);
        }
        else
            putNewImageForSecondaryOrg(tempFile(internalOrgLogoImage), internalOrg);
    }

    private void putNewImageForPrimaryOrg(File internalOrgLogoImageFile, InternalOrg internalOrg) throws FileNotFoundException,IOException {

        try {
            byte[] primaryOrgLogoImageData = FileUtils.readFileToByteArray(internalOrgLogoImageFile);
            String contentType = new MimetypesFileTypeMap().getContentType(internalOrgLogoImageFile);
            s3Service.uploadPrimaryOrgCertificateLogo(contentType, primaryOrgLogoImageData);
        } catch (FileNotFoundException e) {
            logger.error("Unable to read from temp primary Org logo Image file at: " + internalOrgLogoImageFile, e);
            throw new FileNotFoundException("Unable to read from temp primary Org logo Image file");
        } catch (IOException e) {
            logger.error("Unable to upload primary Org logo Image file to S3", e);
            throw new IOException("Unable to upload primary Org logo Image file to server", e);
        }
    }


    private void putNewImageForSecondaryOrg(File internalOrgLogoImageFile, InternalOrg internalOrg) throws FileNotFoundException,IOException {

        try {
            byte[] secondaryOrgLogoImageData = FileUtils.readFileToByteArray(internalOrgLogoImageFile);
            String contentType = new MimetypesFileTypeMap().getContentType(internalOrgLogoImageFile);
            s3Service.uploadSecondaryOrgCertificateLogo(internalOrg.getId(), contentType, secondaryOrgLogoImageData);
        }
        catch(FileNotFoundException e) {
            logger.error("Unable to read from temp secondary Org logo Image file at: " + internalOrgLogoImageFile, e);
            throw new FileNotFoundException("Unable to read from temp secondary Org logo Image file");
        }
        catch(IOException e) {
            logger.error("Unable to upload secondary Org logo Image file to S3", e);
            throw new IOException("Unable to upload secondary Org logo Image file to server", e);
        }
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
