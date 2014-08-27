package com.n4systems.fieldid.service.amazon;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.HttpMethod;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.google.common.base.Preconditions;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.images.ImageService;
import com.n4systems.fieldid.service.uuid.UUIDService;
import com.n4systems.fieldid.version.FieldIdVersion;
import com.n4systems.model.*;
import com.n4systems.model.asset.AssetAttachment;
import com.n4systems.model.criteriaresult.CriteriaResultImage;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.procedure.ProcedureDefinitionImage;
import com.n4systems.model.user.User;
import com.n4systems.reporting.PathHandler;
import com.n4systems.services.ConfigService;
import com.n4systems.services.signature.SignatureService;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.ContentTypeUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.activation.MimetypesFileTypeMap;
import java.io.*;
import java.net.URL;
import java.util.*;

import org.springframework.util.Assert;
import sun.misc.BASE64Encoder;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

@Transactional
public class S3Service extends FieldIdPersistenceService {

    private static final Logger logger = Logger.getLogger(S3Service.class);

    private static final int DEFAULT_EXPIRATION_DAYS = 1;

    private Integer expirationDays = null;
    public static final String TENANTS_PREFIX = "tenants/";
    public static final String DEFAULT_BRANDING_LOGO_PATH = "common/default_branding_logo.gif";
    public static final String BRANDING_LOGO_PATH = "/logos/branding_logo.gif";
    public static final String CUSTOMER_LOGO_PATH = "/logos/customer_logo_%d.gif";
    public static final String PRIMARY_CERTIFICATE_LOGO_PATH = "/logos/primary_certificate_logo.gif";
    public static final String SECONDARY_CERTIFICATE_LOGO_PATH = "/logos/secondary_certificate_logo_%d.gif";
    public static final String CRITERIA_RESULT_IMAGE_PATH_ORIG = "/events/%d/criteria_results/%d/criteria_images/%s";
    public static final String CRITERIA_RESULT_IMAGE_PATH_THUMB = "/events/%d/criteria_results/%d/criteria_images/%s.thumbnail";
    public static final String CRITERIA_RESULT_IMAGE_PATH_MEDIUM = "/events/%d/criteria_results/%d/criteria_images/%s.medium";

    public static final String USER_SIGNATURE_IMAGE_FILE_NAME = "signature.gif";
    public static final String USER_SIGNATURE_PATH = "/users/%d/" + USER_SIGNATURE_IMAGE_FILE_NAME;
    public static final String EVENT_SIGNATURE_IMAGE_FILE_NAME = "signature.png";
    public static final String EVENT_SIGNATURE_PATH = "/events/%d/criteria_results/%d/" + EVENT_SIGNATURE_IMAGE_FILE_NAME;

    public static final String CRITERIA_RESULT_IMAGE_TEMP = "/temp_criteria_result_images/%s";
    public static final String CRITERIA_RESULT_THUMB_IMAGE_TEMP = "/temp_criteria_result_images/%s.thumbnail";
    public static final String CRITERIA_RESULT_MEDIUM_IMAGE_TEMP = "/temp_criteria_result_images/%s.medium";

    public static final String ASSETTYPE_PROFILE_IMAGE_PATH = "/assettypes/%d/profile/%s";

    public static final String ASSET_PROFILE_IMAGE_PATH = "/assets/%d/profile/";
    public static final String ASSET_PROFILE_IMAGE_PATH_ORIG = "/assets/%d/profile/%s";
    public static final String ASSET_PROFILE_IMAGE_PATH_THUMB = "/assets/%d/profile/%s.thumbnail";
    public static final String ASSET_PROFILE_IMAGE_PATH_MEDIUM = "/assets/%d/profile/%s.medium";

    public static final String ASSET_ATTACHMENT_FOLDER = "/assets/%s/attachments/%s/";
    public static final String ASSET_ATTACHMENT_PATH = ASSET_ATTACHMENT_FOLDER + "%s";

    public static final String FILE_ATTACHMENT_FOLDER = "/file_attachments/%s/";
    public static final String FILE_ATTACHMENT_PATH = FILE_ATTACHMENT_FOLDER + "%s";

    public static final String CHART_FILE_NAME = "proof_test_chart.png";
    public static final String PROOF_TEST_FILE_NAME = "proof_test.pt";
    public static final String ASSET_PROOFTESTS_FOLDER = "/assets/%s/prooftests/%s/";
    public static final String ASSET_PROOFTESTS_FILE_PATH = ASSET_PROOFTESTS_FOLDER + PROOF_TEST_FILE_NAME;
    public static final String ASSET_PROOFTESTS_CHART_PATH = ASSET_PROOFTESTS_FOLDER + CHART_FILE_NAME;

    public static final String PROCEDURE_DEFINITION_IMAGE_TEMP = "/temp/procedure_definition_images/%s";
    public static final String PROCEDURE_DEFINITION_IMAGE_TEMP_MEDIUM = "/temp/procedure_definition_images/%s.medium";
    public static final String PROCEDURE_DEFINITION_IMAGE_TEMP_THUMB = "/temp/procedure_definition_images/%s.thumb";

    public static final String PROCEDURE_DEFINITION_IMAGE_PATH = "/assets/%d/procedure_definitions/%d/%s";
    public static final String PROCEDURE_DEFINITION_IMAGE_PATH_THUMB = "/assets/%d/procedure_definitions/%d/%s.thumbnail";
    public static final String PROCEDURE_DEFINITION_IMAGE_PATH_MEDIUM = "/assets/%d/procedure_definitions/%d/%s.medium";

    public static final String PLACE_ATTACHMENT_TEMP = "/temp/place_attachments/%s";
    public static final String PLACE_ATTACHMENT = "/place_attachments/%d/%s";
    public static final String PLACE_IMAGE = PLACE_ATTACHMENT;  // images are treated as a type of attachment - go in same folder.
    public static final String PLACE_IMAGE_MEDIUM = "/place_attachments/%d/%s.medium";
    public static final String PLACE_IMAGE_THUMBNAIL = "/place_attachments/%d/%s.thumbnail";

    public static final String THUMBNAIL_EXTENSION = ".thumbnail";
    public static final String MEDIUM_EXTENSION = ".medium";

    @Autowired private ConfigService configService;
    @Autowired private ImageService imageService;
    @Autowired private AmazonS3Client s3client;
    @Autowired private UUIDService uuidService;
    @Autowired private S3AttachmentHandler s3ImageAttachmentHandler;

    public URL getBrandingLogoURL() {
        return getBrandingLogoURL(null);
    }

    public URL getBrandingLogoURL(Long tenantId) {
        return generateResourceUrl(tenantId, BRANDING_LOGO_PATH);
    }

    public void uploadBrandingLogo(File file) {
        uploadResource(file, null, BRANDING_LOGO_PATH);
    }

    public void uploadDefaultBrandingLogo(Long tenantId) {
        copyObject(DEFAULT_BRANDING_LOGO_PATH, createResourcePath(tenantId, BRANDING_LOGO_PATH));
    }

    public URL getCustomerLogoURL(Long customerOrgId) {
        URL orgLogoUrl = generateResourceUrl(null, CUSTOMER_LOGO_PATH, customerOrgId);
        return orgLogoUrl;
    }

    public byte[] downloadCustomerLogo(Long customerOrgId) throws IOException {
        byte[] logoData = downloadResource(null, CUSTOMER_LOGO_PATH, customerOrgId);
        return logoData;
    }

    public boolean customerLogoExists(Long customerOrgId) {
        boolean exists = resourceExists(null, CUSTOMER_LOGO_PATH, customerOrgId);
        return exists;
    }

    public void uploadCustomerLogo(File file, Long customerOrgId) {
        uploadResource(file, null, CUSTOMER_LOGO_PATH, customerOrgId);
    }

    public void uploadCustomerLogo(Long customerOrgId, String contentType, byte[] bytes) {
        uploadResource(bytes, contentType, null, CUSTOMER_LOGO_PATH, customerOrgId);
    }

    public void removeCustomerLogo(Long customerOrgId) {
        removeResource(null, CUSTOMER_LOGO_PATH, customerOrgId);
    }

    public URL getPrimaryOrgCertificateLogoURL() {
        URL url = generateResourceUrl(null, PRIMARY_CERTIFICATE_LOGO_PATH);
        return url;
    }

    public URL getSecondaryOrgCertificateLogoURL(Long secondaryOrgId) {
        URL url = generateResourceUrl(null, SECONDARY_CERTIFICATE_LOGO_PATH, secondaryOrgId);
        return url;
    }

    public byte[] downloadInternalOrgCertificateLogo(InternalOrg org) throws IOException {
        byte[] logo = null;
        if (org.isSecondary() && secondaryOrgCertificateLogoExists(org.getId())) {
            logo = downloadSecondaryOrgCertificateLogo(org.getId());
        } else if (primaryOrgCertificateLogoExists()) {
            logo = downloadPrimaryOrgCertificateLogo();
        }
        return logo;
    }

    private byte[] downloadPrimaryOrgCertificateLogo() throws IOException {
        byte[] logoData = downloadResource(null, PRIMARY_CERTIFICATE_LOGO_PATH);
        return logoData;
    }

    private byte[] downloadSecondaryOrgCertificateLogo(Long secondaryOrgId) throws IOException {
        byte[] logoData = downloadResource(null, SECONDARY_CERTIFICATE_LOGO_PATH, secondaryOrgId);
        return logoData;
    }

    public void uploadPrimaryOrgCertificateLogo(File file) {
        uploadResource(file, null, PRIMARY_CERTIFICATE_LOGO_PATH);
    }

    public void uploadPrimaryOrgCertificateLogo(String contentType, byte[] bytes) {
        uploadResource(bytes, contentType, null, PRIMARY_CERTIFICATE_LOGO_PATH);
    }

    public void uploadSecondaryOrgCertificateLogo(File file, Long secondaryOrgId) {
        uploadResource(file, null, SECONDARY_CERTIFICATE_LOGO_PATH, secondaryOrgId);
    }

    public void uploadSecondaryOrgCertificateLogo(Long secondaryOrgId, String contentType, byte[] bytes) {
        uploadResource(bytes, contentType, null, SECONDARY_CERTIFICATE_LOGO_PATH, secondaryOrgId);
    }

    public void removePrimaryOrgCertificateLogo() {
        removeResource(null, PRIMARY_CERTIFICATE_LOGO_PATH);
    }

    public void removeSecondaryOrgCertificateLogo(Long secondaryOrgId) {
        removeResource(null, SECONDARY_CERTIFICATE_LOGO_PATH, secondaryOrgId);
    }

    public boolean primaryOrgCertificateLogoExists() {
        boolean exists = resourceExists(null, PRIMARY_CERTIFICATE_LOGO_PATH);
        return exists;
    }

    public boolean secondaryOrgCertificateLogoExists(Long secondaryOrgId) {
        boolean exists = resourceExists(null, SECONDARY_CERTIFICATE_LOGO_PATH, secondaryOrgId);
        return exists;
    }

    public URL getAssetProfileImageOriginalURL(Long assetId, String imageName) {
        URL assetProfileUrl = generateResourceUrl(null, ASSET_PROFILE_IMAGE_PATH_ORIG, assetId, imageName);
        return assetProfileUrl;
    }

    public URL getAssetProfileImageMediumURL(Long assetId, String imageName) {
        URL assetProfileUrl = generateResourceUrl(null, ASSET_PROFILE_IMAGE_PATH_MEDIUM, assetId, imageName);
        return assetProfileUrl;
    }

    public URL getAssetProfileImageThumbnailURL(Long assetId, String imageName) {
        URL assetProfileUrl = generateResourceUrl(null, ASSET_PROFILE_IMAGE_PATH_THUMB, assetId, imageName);
        return assetProfileUrl;
    }

    public byte[] downloadAssetProfileOriginalImage(Long assetId, String imageName) throws IOException {
        byte[] imageData = downloadResource(null, ASSET_PROFILE_IMAGE_PATH_ORIG, assetId, imageName);
        return imageData;
    }

    public File downloadAssetProfileImageFile(Long assetId, String imageName){
        File assetProfileImage = null;
        try {
            byte[] assetProfileImageBytes = downloadAssetProfileOriginalImage(assetId, imageName);
            assetProfileImage = PathHandler.getUserFile(getCurrentUser(), imageName);
            FileOutputStream assetProfileImageFos = new FileOutputStream(assetProfileImage);
            assetProfileImageFos.write(assetProfileImageBytes);
        }
        catch(FileNotFoundException e) {
            logger.warn("Unable to write to temp image file at: " + assetProfileImage, e);
        }
        catch(IOException e) {
            logger.warn("Unable to download asset profile image from S3: " + assetId, e);
        }
        return assetProfileImage;
    }

    public byte[] downloadAssetProfileMediumImage(Long assetId, String imageName) throws IOException {
        byte[] imageData = downloadResource(null, ASSET_PROFILE_IMAGE_PATH_MEDIUM, assetId, imageName);
        return imageData;
    }

    public byte[] downloadAssetProfileThumbnailImage(Long assetId, String imageName) throws IOException {
        byte[] imageData = downloadResource(null, ASSET_PROFILE_IMAGE_PATH_THUMB, assetId, imageName);
        return imageData;
    }

    public boolean assetProfileImageExists(Long assetId, String imageName) {
        boolean exists = resourceExists(null, ASSET_PROFILE_IMAGE_PATH_ORIG, assetId, imageName);
        return exists;
    }

    public boolean assetAttachmentExists(AssetAttachment assetAttachment){
        String fileName = assetAttachment.getFileName().substring(assetAttachment.getFileName().lastIndexOf('/') + 1);
        return assetAttachmentExists(assetAttachment.getAsset().getMobileGUID(), assetAttachment.getMobileId(), fileName);
    }

    public boolean assetAttachmentExists(String assetUuid, String assetAttachmentUuid, String assetAttachmentFilename){
        Assert.hasLength(assetUuid);
        Assert.hasLength(assetAttachmentUuid);
        Assert.hasLength(assetAttachmentFilename);
        boolean exists = resourceExists(null, ASSET_ATTACHMENT_PATH, assetUuid, assetAttachmentUuid, assetAttachmentFilename);
        return exists;
    }

    public boolean assetProofTestChartExists(ThingEventProofTest proofTest){
        if(proofTest == null){
            return false;
        }
        return assetProofTestChartExists(proofTest.getAsset().getMobileGUID(), proofTest.getThingEvent().getMobileGUID());
    }

    public boolean assetProofTestChartExists(String assetUuid, String eventUuid){
        Assert.hasLength(assetUuid);
        Assert.hasLength(eventUuid);
        boolean exists = resourceExists(null, ASSET_PROOFTESTS_CHART_PATH, assetUuid, eventUuid);
        return exists;
    }

    public boolean fileAttachmentExists(FileAttachment fileAttachment){
        String fileName = fileAttachment.getFileName().substring(fileAttachment.getFileName().lastIndexOf('/') + 1);
        return fileAttachmentExists(fileAttachment.getMobileId(), fileName);
    }

    public boolean fileAttachmentExists(String fileAttachmentUuid, String filename){
        Assert.hasLength(fileAttachmentUuid);
        Assert.hasLength(filename);
        boolean exists = resourceExists(null, FILE_ATTACHMENT_PATH, fileAttachmentUuid, filename);
        return exists;
    }

    public byte[] downloadProcedureDefinitionMediumImage(ProcedureDefinitionImage image) throws IOException {
        byte[] imageData = downloadResource(null, PROCEDURE_DEFINITION_IMAGE_PATH_MEDIUM,
                image.getProcedureDefinition().getAsset().getId(),
                image.getProcedureDefinition().getId(),
                image.getFileName());
        return imageData;
    }

    public void uploadAssetProfileImage(File file, Long assetId, String imageName) throws IOException {
        removePreviousAssetProfileImage(assetId);

        byte[] imageData = FileUtils.readFileToByteArray(file);
        String contentType = new MimetypesFileTypeMap().getContentType(file);
        byte[] thumbnailImage = imageService.generateThumbnail(imageData);
        byte[] mediumImage = imageService.generateMedium(imageData);

        uploadResource(file, null, ASSET_PROFILE_IMAGE_PATH_ORIG, assetId, imageName);
        uploadResource(thumbnailImage, contentType, null, ASSET_PROFILE_IMAGE_PATH_THUMB, assetId, imageName);
        uploadResource(mediumImage, contentType, null, ASSET_PROFILE_IMAGE_PATH_MEDIUM, assetId, imageName);
    }

    private void removePreviousAssetProfileImage(Long assetId) {
        List<S3ObjectSummary> objectSummaryList = getClient().listObjects(getBucket(), createResourcePath(null, ASSET_PROFILE_IMAGE_PATH, assetId)).getObjectSummaries();
        for(S3ObjectSummary summary: objectSummaryList) {
            if(summary.getSize() > 0) {
                deleteObject(summary.getKey());
            }
        }
    }

    public void removeAssetProfileImage(Long assetId, String imageName) {
        removeResource(null, ASSET_PROFILE_IMAGE_PATH_ORIG, assetId, imageName);
        removeResource(null, ASSET_PROFILE_IMAGE_PATH_MEDIUM, assetId, imageName);
        removeResource(null, ASSET_PROFILE_IMAGE_PATH_THUMB, assetId, imageName);
    }

    public ProcedureDefinitionImage uploadTempProcedureDefImage(ProcedureDefinitionImage image, String contentType, byte[] imageData) {
        String tempFileName = uploadTempImage(imageData, contentType, PROCEDURE_DEFINITION_IMAGE_TEMP, PROCEDURE_DEFINITION_IMAGE_TEMP_MEDIUM, PROCEDURE_DEFINITION_IMAGE_TEMP_THUMB);
        image.setTempFileName(tempFileName);
        image.setContentType(contentType);
        return image;
    }

    public void copyProcedureDefImageToTemp(ProcedureDefinitionImage from, ProcedureDefinitionImage to) {
        String tempFileName = uuidService.createUuid();
        to.setTempFileName(tempFileName);

        String contentType = getObjectMetadata(createResourcePath(from.getTenant().getId(),
                PROCEDURE_DEFINITION_IMAGE_PATH,
                from.getProcedureDefinition().getAsset().getId(),
                from.getProcedureDefinition().getId(),
                from.getFileName()).toString()).getContentType();
        to.setContentType(contentType);

        copyProcedureDefinitionImageToTemp(from, tempFileName, PROCEDURE_DEFINITION_IMAGE_PATH, PROCEDURE_DEFINITION_IMAGE_TEMP);
        copyProcedureDefinitionImageToTemp(from, tempFileName, PROCEDURE_DEFINITION_IMAGE_PATH_MEDIUM, PROCEDURE_DEFINITION_IMAGE_TEMP_MEDIUM);
        copyProcedureDefinitionImageToTemp(from, tempFileName, PROCEDURE_DEFINITION_IMAGE_PATH_THUMB, PROCEDURE_DEFINITION_IMAGE_TEMP_THUMB);

    }

    private void copyProcedureDefinitionImageToTemp(ProcedureDefinitionImage image, String tempFileName, String source, String dest) {
        Long tenantId = getCurrentTenant().getId();

        CopyObjectRequest copyObjectRequest = new CopyObjectRequest(getBucket(),
                createResourcePath(tenantId, source,
                        image.getProcedureDefinition().getAsset().getId(),
                        image.getProcedureDefinition().getId(),
                        image.getFileName()),
                getBucket(), createResourcePath(tenantId, dest, tempFileName));

        getClient().copyObject(copyObjectRequest);

    }

    public String uploadTempCriteriaResultImage(CriteriaResultImage criteriaResultImage, byte[] imageData) {
        String contentType = criteriaResultImage.getContentType();

        return uploadTempImage(imageData, contentType, CRITERIA_RESULT_IMAGE_TEMP, CRITERIA_RESULT_MEDIUM_IMAGE_TEMP, CRITERIA_RESULT_THUMB_IMAGE_TEMP);
    }

    private String uploadTempImage(byte[] imageData, String contentType, String imagePathTemplate, String mediumImagePathTemplate, String thumbImagePathTemplate) {
        Long tenantId = getCurrentTenant().getId();
        String uuid = uuidService.createUuid();

        byte[] mediumImage = imageService.generateMedium(imageData);
        byte[] thumbImage = imageService.generateThumbnail(imageData);

        uploadResource(imageData, contentType, tenantId,
                imagePathTemplate, uuid);
        uploadResource(mediumImage, contentType, tenantId,
                mediumImagePathTemplate, uuid);
        uploadResource(thumbImage, contentType, tenantId,
                thumbImagePathTemplate, uuid);

        return uuid;
    }

    public void finalizeProcedureDefinitionImageUpload(ProcedureDefinitionImage image) {
        if (image.getTempFileName()==null) {
            return;  // most likely it's already been finalized. (either that or things are fubar).
        }
        Long tenantId = image.getTenant().getId();
        String tempFileName = image.getTempFileName();

        copyTemporaryProcedureDefinitionImageToFinal(image, PROCEDURE_DEFINITION_IMAGE_TEMP, PROCEDURE_DEFINITION_IMAGE_PATH);
        copyTemporaryProcedureDefinitionImageToFinal(image, PROCEDURE_DEFINITION_IMAGE_TEMP_MEDIUM, PROCEDURE_DEFINITION_IMAGE_PATH_MEDIUM);
        copyTemporaryProcedureDefinitionImageToFinal(image, PROCEDURE_DEFINITION_IMAGE_TEMP_THUMB, PROCEDURE_DEFINITION_IMAGE_PATH_THUMB);

        removeResource(tenantId, PROCEDURE_DEFINITION_IMAGE_TEMP, tempFileName);
        removeResource(tenantId, PROCEDURE_DEFINITION_IMAGE_TEMP_MEDIUM, tempFileName);
        removeResource(tenantId, PROCEDURE_DEFINITION_IMAGE_TEMP_THUMB, tempFileName);

        image.setTempFileName(null);
    }

    private void copyTemporaryProcedureDefinitionImageToFinal(ProcedureDefinitionImage image, String source, String dest) {
        copyTemporaryImageToFinal(image.getTempFileName(), source, dest,
                image.getProcedureDefinition().getAsset().getId(),
                image.getProcedureDefinition().getId(),
                image.getFileName());
    }

    public void finalizeCriteriaResultImageUpload(CriteriaResultImage criteriaResultImage) {
        Long tenantId = criteriaResultImage.getCriteriaResult().getTenant().getId();
        String tempFileName = criteriaResultImage.getTempFileName();

        copyTemporaryCriteriaImageToFinal(criteriaResultImage, CRITERIA_RESULT_IMAGE_TEMP, CRITERIA_RESULT_IMAGE_PATH_ORIG);
        copyTemporaryCriteriaImageToFinal(criteriaResultImage, CRITERIA_RESULT_MEDIUM_IMAGE_TEMP, CRITERIA_RESULT_IMAGE_PATH_MEDIUM);
        copyTemporaryCriteriaImageToFinal(criteriaResultImage, CRITERIA_RESULT_THUMB_IMAGE_TEMP, CRITERIA_RESULT_IMAGE_PATH_THUMB);

        removeResource(tenantId, CRITERIA_RESULT_IMAGE_TEMP, tempFileName);
        removeResource(tenantId, CRITERIA_RESULT_MEDIUM_IMAGE_TEMP, tempFileName);
        removeResource(tenantId, CRITERIA_RESULT_THUMB_IMAGE_TEMP, tempFileName);
    }

    private void copyTemporaryCriteriaImageToFinal(CriteriaResultImage criteriaResultImage, String tempFileNameTemplate, String finalFileNameTemplate) {
        copyTemporaryImageToFinal(criteriaResultImage.getTempFileName(), tempFileNameTemplate, finalFileNameTemplate,
                criteriaResultImage.getCriteriaResult().getEvent().getId(),
                criteriaResultImage.getCriteriaResult().getId(),
                criteriaResultImage.getFileName());
    }

    private void copyTemporaryImageToFinal(String tempFileName, String tempFileNameTemplate, String finalFileNameTemplate, Object... pathArgs) {
        Long tenantId = getCurrentTenant().getId();

        CopyObjectRequest copyObjectRequest = new CopyObjectRequest(getBucket(),
                createResourcePath(tenantId, tempFileNameTemplate, tempFileName), getBucket(),
                createResourcePath(tenantId, finalFileNameTemplate,
                        pathArgs));

        getClient().copyObject(copyObjectRequest);
    }

    public void uploadCriteriaResultImage(CriteriaResultImage criteriaResultImage, byte[] imageData) {
        byte[] thumbnailImage = imageService.generateThumbnail(imageData);
        byte[] mediumImage = imageService.generateMedium(imageData);

        Long tenantId = criteriaResultImage.getCriteriaResult().getTenant().getId();

        uploadResource(
                thumbnailImage,
                criteriaResultImage.getContentType(),
                tenantId,
                CRITERIA_RESULT_IMAGE_PATH_THUMB,
                criteriaResultImage.getCriteriaResult().getEvent().getId(),
                criteriaResultImage.getCriteriaResult().getId(),
                criteriaResultImage.getFileName());

        uploadResource(
                mediumImage,
                criteriaResultImage.getContentType(),
                tenantId,
                CRITERIA_RESULT_IMAGE_PATH_MEDIUM,
                criteriaResultImage.getCriteriaResult().getEvent().getId(),
                criteriaResultImage.getCriteriaResult().getId(),
                criteriaResultImage.getFileName());

        uploadResource(
                imageData,
                criteriaResultImage.getContentType(),
                tenantId,
                CRITERIA_RESULT_IMAGE_PATH_ORIG,
                criteriaResultImage.getCriteriaResult().getEvent().getId(),
                criteriaResultImage.getCriteriaResult().getId(),
                criteriaResultImage.getFileName());
    }

    public URL getCriteriaResultImageMediumTempURL(CriteriaResultImage criteriaResultImage) {
        return generateResourceUrl(null, CRITERIA_RESULT_MEDIUM_IMAGE_TEMP,
                criteriaResultImage.getTempFileName());
    }

    public URL getCriteriaResultImageMediumURL(CriteriaResultImage criteriaResultImage) {
        return generateResourceUrl(null, CRITERIA_RESULT_IMAGE_PATH_MEDIUM,
                criteriaResultImage.getCriteriaResult().getEvent().getId(),
                criteriaResultImage.getCriteriaResult().getId(),
                criteriaResultImage.getFileName());
    }

    public URL getCriteriaResultImageThumbnailURL(CriteriaResultImage criteriaResultImage) {
        return generateResourceUrl(null, CRITERIA_RESULT_IMAGE_PATH_THUMB,
                criteriaResultImage.getCriteriaResult().getEvent().getId(),
                criteriaResultImage.getCriteriaResult().getId(),
                criteriaResultImage.getFileName());
    }

    public URL getCriteriaResultImageThumbnailURL(Long tenantId, CriteriaResultImage criteriaResultImage) {
        return generateResourceUrl(tenantId, CRITERIA_RESULT_IMAGE_PATH_THUMB,
                criteriaResultImage.getCriteriaResult().getEvent().getId(),
                criteriaResultImage.getCriteriaResult().getId(),
                criteriaResultImage.getFileName());
    }

    private URL getProcedureDefinitionImageURLImpl(ProcedureDefinitionImage procedureDefinitionImage, String tempPath, String path) {
        if (procedureDefinitionImage.getTempFileName()!=null) {
            return generateResourceUrl(procedureDefinitionImage.getTenant().getId(), tempPath ,
                    procedureDefinitionImage.getTempFileName());
        } else {
            return generateResourceUrl(procedureDefinitionImage.getTenant().getId(), path,
                    procedureDefinitionImage.getProcedureDefinition().getAsset().getId(),
                    procedureDefinitionImage.getProcedureDefinition().getId(),
                    procedureDefinitionImage.getFileName());
        }
    }

    public URL getProcedureDefinitionImageURL(ProcedureDefinitionImage procedureDefinitionImage) {
        return getProcedureDefinitionImageURLImpl(procedureDefinitionImage, PROCEDURE_DEFINITION_IMAGE_TEMP, PROCEDURE_DEFINITION_IMAGE_PATH);
    }

    public URL getProcedureDefinitionImageMediumURL(ProcedureDefinitionImage procedureDefinitionImage) {
        return getProcedureDefinitionImageURLImpl(procedureDefinitionImage, PROCEDURE_DEFINITION_IMAGE_TEMP_MEDIUM, PROCEDURE_DEFINITION_IMAGE_PATH_MEDIUM);
    }

    public URL getProcedureDefinitionImageThumbnailURL(ProcedureDefinitionImage procedureDefinitionImage) {
        return getProcedureDefinitionImageURLImpl(procedureDefinitionImage, PROCEDURE_DEFINITION_IMAGE_TEMP_THUMB, PROCEDURE_DEFINITION_IMAGE_PATH_THUMB);
    }

    public void removeProcedureDefinitionImages(ProcedureDefinition procedureDefinition) {
        for (ProcedureDefinitionImage image: procedureDefinition.getImages()) {
            removeProcedureDefinitionImage(image);
        }
        removeResource(procedureDefinition.getTenant().getId(),
                PROCEDURE_DEFINITION_IMAGE_PATH,
                procedureDefinition.getAsset().getId(),
                procedureDefinition.getId(),
                ""
        );
    }

    public void removeProcedureDefinitionImage(ProcedureDefinitionImage procedureDefinitionImage) {
        removeResource(procedureDefinitionImage.getProcedureDefinition().getTenant().getId(),
                PROCEDURE_DEFINITION_IMAGE_PATH,
                procedureDefinitionImage.getProcedureDefinition().getAsset().getId(),
                procedureDefinitionImage.getProcedureDefinition().getId(),
                procedureDefinitionImage.getFileName()
        );
        removeResource(procedureDefinitionImage.getProcedureDefinition().getTenant().getId(),
                PROCEDURE_DEFINITION_IMAGE_PATH_MEDIUM,
                procedureDefinitionImage.getProcedureDefinition().getAsset().getId(),
                procedureDefinitionImage.getProcedureDefinition().getId(),
                procedureDefinitionImage.getFileName()
        );
        removeResource(procedureDefinitionImage.getProcedureDefinition().getTenant().getId(),
                PROCEDURE_DEFINITION_IMAGE_PATH_THUMB,
                procedureDefinitionImage.getProcedureDefinition().getAsset().getId(),
                procedureDefinitionImage.getProcedureDefinition().getId(),
                procedureDefinitionImage.getFileName()
        );
    }


    public URL getPlaceAttachment(BaseOrg org, Attachment attachment) {
        return generateResourceUrl(org.getTenant().getId(), attachment.getFileName(), org.getId());
    }

    public byte[] downloadCriteriaResultImageMedium(CriteriaResultImage criteriaResultImage) throws IOException {
        return downloadResource(null, CRITERIA_RESULT_IMAGE_PATH_MEDIUM,
                criteriaResultImage.getCriteriaResult().getEvent().getId(),
                criteriaResultImage.getCriteriaResult().getId(),
                criteriaResultImage.getFileName());
    }

    public InputStream openCriteriaResultImageMedium(CriteriaResultImage criteriaResultImage) throws IOException {
        return new ByteArrayInputStream(downloadResource(null, CRITERIA_RESULT_IMAGE_PATH_MEDIUM,
                criteriaResultImage.getCriteriaResult().getEvent().getId(),
                criteriaResultImage.getCriteriaResult().getId(),
                criteriaResultImage.getFileName()));
    }

    public S3ImagePath uploadImage(byte[] data, String contentType, String path, Long tenantId) {
        return uploadResource(data, contentType, new S3ImagePath(path, tenantId));
    }

    private void uploadResource(byte[] data, String contentType, Long tenantId, String path, Object...pathArgs) {
        putObject(createResourcePath(tenantId, path, pathArgs), data, contentType);
    }

    private S3ImagePath uploadResource(byte[] data, String contentType, S3ImagePath imagePath) {
        // note that every time we upload an image, there are 3 different versions of it.
        putObject(imagePath.getOrigPath(), data, contentType);
        putObject(imagePath.getMediumPath(), imageService.generateMedium(data), contentType);
        putObject(imagePath.getThumbnailPath(), imageService.generateThumbnail(data), contentType);
        return imagePath;
    }

    private void uploadResource(File file, Long tenantId, String path, Object...pathArgs) {
        putObject(createResourcePath(tenantId, path, pathArgs), file);
    }

    private void removeResource(Long tenantId, String path, Object...pathArgs) {
        deleteObject(createResourcePath(tenantId, path, pathArgs));
    }

    private boolean resourceExists(Long tenantId, String path, Object...pathArgs) {
        try {
            ObjectMetadata metadata = getObjectMetadata(createResourcePath(tenantId, path, pathArgs));
            return metadata.getContentLength() > 0;
        } catch (AmazonS3Exception e) {
            return handleAmazonS3Exception(e, false);
        }
    }

    private byte[] downloadResource(Long tenantId, String path, Object...pathArgs) throws IOException {
        InputStream resourceInput = null;
        try {
            S3Object resource = getObject(createResourcePath(tenantId, path, pathArgs));
            resourceInput = resource.getObjectContent();
            byte[] resourceContent = IOUtils.toByteArray(resourceInput);
            return resourceContent;
        } catch (AmazonS3Exception e) {
            return handleAmazonS3Exception(e, (byte[]) null);
        } finally {
            IOUtils.closeQuietly(resourceInput);
        }
    }

    private InputStream openResourceStream(Long tenantId, String path, Object...pathArgs) throws IOException {
        try {
            S3Object resource = getObject(createResourcePath(tenantId, path, pathArgs));
            InputStream resourceInput = resource.getObjectContent();
            return resourceInput;
        } catch (AmazonS3Exception e) {
            return handleAmazonS3Exception(e, (InputStream) null);
        }
    }

    private URL generateResourceUrl(Long tenantId, String path, Object...pathArgs) {
        String fullResourcePath = createResourcePath(tenantId, path, pathArgs);
        return generateResourceUrl(fullResourcePath);
    }

    public URL generateResourceUrl(String fullResourcePath) {
        Date expires = new DateTime().plusDays(getExpiryInDays()).toDate();
        URL url = generatePresignedUrl(fullResourcePath, expires, HttpMethod.GET);
        return url;
    }


    private String createResourcePath(Long tenantId, String resourcePath, Object...pathArgs) {
        if (tenantId == null) {
            //tenantId = securityContext.getTenantSecurityFilter().getTenantId();
            tenantId = getCurrentTenant().getId();
        }
        String path = TENANTS_PREFIX + tenantId + String.format(resourcePath, pathArgs);

        return path;
    }

    private <T> T handleAmazonS3Exception(AmazonS3Exception e, T notFoundReturn) {
        if (e.getStatusCode() == 404) {
            return notFoundReturn;
        } else {
            throw e;
        }
    }

    private CopyObjectResult copyObject(String srcPath, String dstPath) {
        String bucket = getBucket();
        CopyObjectResult result = getClient().copyObject(bucket, srcPath, bucket, dstPath);
        return result;
    }

    private PutObjectResult putObject(String path, File file) {
        PutObjectResult result = getClient().putObject(getBucket(), path, file);
        return result;
    }

    private PutObjectResult putObject(String path, byte[] data, String contentType) {
        ObjectMetadata objectMeta = new ObjectMetadata();
        objectMeta.setContentLength(data.length);
        objectMeta.setContentType(contentType);

        PutObjectResult result = getClient().putObject(new PutObjectRequest(getBucket(), path, new ByteArrayInputStream(data), objectMeta));
        return result;
    }

    private void deleteObject(String path) {
        getClient().deleteObject(getBucket(), path);
    }

    private S3Object getObject(String path) {
        return getClient().getObject(getBucket(), path);
    }

    private ObjectMetadata getObjectMetadata(String path) {
        return getClient().getObjectMetadata(getBucket(), path);
    }

    private URL generatePresignedUrl(String path, Date expires, HttpMethod method) {
        URL url = null;
        try {
            url = getClient().generatePresignedUrl(getBucket(), path, expires, method);
        } catch(AmazonServiceException ase){
            //just ignore the exception, its probably caused by resource not existing
        }
        return url;
    }

    private AmazonS3Client getClient() {
        return s3client;
    }

    private String getBucket() {
        String bucket = configService.getString(ConfigEntry.AMAZON_S3_BUCKET);
        return bucket;
    }

    private String getAccessKey() {
        String accessKey = configService.getString(ConfigEntry.AMAZON_ACCESS_KEY_ID);
        return accessKey;
    }

    private String getSecretKey() {
        String secretKey = configService.getString(ConfigEntry.AMAZON_SECRET_ACCESS_KEY);
        return secretKey;
    }

    protected String getUploadTimeoutMilliseconds() {
        String uploadTimeoutMilliseconds = configService.getString(ConfigEntry.AMAZON_S3_UPLOAD_TIMEOUT_MILLISECONDS);
        return uploadTimeoutMilliseconds;
    }

    public String getUploadMaxFileSizeBytes() {
        String uploadMaxFileSizeBytes = configService.getString(ConfigEntry.AMAZON_S3_UPLOAD_MAX_FILE_SIZE_BYTES);
        return uploadMaxFileSizeBytes;
    }

    protected int getExpiryInDays() {
        return expirationDays==null ? DEFAULT_EXPIRATION_DAYS : expirationDays;
    }

    public void setExpiryInDays(int days) {
        expirationDays = days;
    }

    public void resetExpiryInDays() {
        expirationDays = null;
    }

    protected String getBucketHostname() {
        String bucketHostname = configService.getString(ConfigEntry.AMAZON_S3_BUCKET) + "." + configService.getString(ConfigEntry.AMAZON_S3_SERVER_HOSTNAME);
        return bucketHostname;
    }

    public String getAssetTypeProfileImagePath(AssetType assetType){
        String assetTypePath = getAssetTypeProfileImagePath(assetType.getTenant().getId(), assetType.getId(), assetType.getImageName());
        return assetTypePath;
    }

    public String getAssetTypeProfileImagePath(Long tenantId, Long assetTypeId, String assetTypeImageName){
        String resourcePath = createResourcePath(tenantId, ASSETTYPE_PROFILE_IMAGE_PATH, assetTypeId, assetTypeImageName);
        return resourcePath;
    }

    public URL getAssetTypeProfileImageUrl(AssetType assetType){
        URL eventSignatureUrl = getAssetTypeProfileImageUrl(assetType.getTenant().getId(), assetType.getId(), assetType.getImageName());
        return eventSignatureUrl;
    }

    public URL getAssetTypeProfileImageUrl(Long tenantId, Long assetTypeId, String assetTypeImageName){
        Date expires = new DateTime().plusDays(getExpiryInDays()).toDate();
        String resourcePath = getAssetTypeProfileImagePath(tenantId, assetTypeId, assetTypeImageName);
        URL url = generatePresignedUrl(resourcePath, expires, HttpMethod.GET);
        return url;
    }

    public File downloadAssetTypeProfileImage(AssetType assetType){
        return downloadAssetTypeProfileImage(assetType.getTenant().getId(), assetType.getId(), assetType.getImageName());
    }

    public File downloadAssetTypeProfileImage(Long tenantId, Long assetTypeId, String assetTypeImageName){
        File assetTypeProfileImageFile = null;
        try {
            byte[] assetTypeProfileImageBytes = downloadAssetTypeProfileImageBytes(tenantId, assetTypeId, assetTypeImageName);
            assetTypeProfileImageFile = PathHandler.getUserFile(getCurrentUser(), assetTypeImageName);
            FileOutputStream assetTypeProfileImageFos = new FileOutputStream(assetTypeProfileImageFile);
            assetTypeProfileImageFos.write(assetTypeProfileImageBytes);
        }
        catch(FileNotFoundException e) {
            logger.warn("Unable to write to temp assettype profile image at: " + assetTypeProfileImageFile, e);
        }
        catch(IOException e) {
            logger.warn("Unable to download assettype profile from S3", e);
        }
        return assetTypeProfileImageFile;
    }

    public void uploadAssetTypeProfileImage(File assetTypeProfileImageFile, AssetType assetType){
        uploadAssetTypeProfileImage(assetTypeProfileImageFile, assetType.getTenant().getId(), assetType.getId(), assetType.getImageName());
    }

    public void uploadAssetTypeProfileImage(File assetTypeProfileImageFile, Long tenantId, Long assetTypeId, String assetTypeImageName){
        uploadResource(assetTypeProfileImageFile, tenantId, ASSETTYPE_PROFILE_IMAGE_PATH, assetTypeId, assetTypeImageName);
    }

    public void uploadAssetTypeProfileImageData(byte[] assetTypeProfileImageData, AssetType assetType){
        String contentType = ContentTypeUtil.getContentType(assetType.getImageName());
        uploadAssetTypeProfileImageData(assetTypeProfileImageData, contentType, assetType.getTenant().getId(), assetType.getId(), assetType.getImageName());
    }

    public void uploadAssetTypeProfileImageData(byte[] assetTypeProfileImageData, String contentType, Long tenantId, Long assetTypeId, String assetTypeImageName){
        uploadResource(assetTypeProfileImageData, contentType, tenantId, ASSETTYPE_PROFILE_IMAGE_PATH, assetTypeId, assetTypeImageName);
    }

    public byte[] downloadAssetTypeProfileImageBytes(AssetType assetType) throws IOException {
        //the attachment Filename field is overloaded to house full URL instead of just the filename
        byte[] assetTypeProfileImage = downloadAssetTypeProfileImageBytes(assetType.getTenant().getId(), assetType.getId(), assetType.getImageName());
        return assetTypeProfileImage;
    }

    public byte[] downloadAssetTypeProfileImageBytes(Long tenantId, Long assetTypeId, String assetTypeImageName) throws IOException {
        return downloadResource(tenantId, ASSETTYPE_PROFILE_IMAGE_PATH, assetTypeId, assetTypeImageName);
    }

    public boolean assetTypeProfileImageExists(AssetType assetType){
        return assetTypeProfileImageExists(assetType.getTenant().getId(), assetType.getId(), assetType.getImageName());
    }

    public boolean assetTypeProfileImageExists(Long tenantId, Long assetTypeId, String assetTypeImageName){
        boolean exists = resourceExists(tenantId, ASSETTYPE_PROFILE_IMAGE_PATH, assetTypeId, assetTypeImageName);
        return exists;
    }

    public String getUserSignaturePath(User user){
        String userSignaturePath = getUserSignaturePath(user.getId());
        return userSignaturePath;
    }

    public String getUserSignaturePath(Long userId){
        String resourcePath = createResourcePath(null, USER_SIGNATURE_PATH, userId);
        return resourcePath;
    }

    public URL getUserSignatureUrl(User user){
        URL userSignatureUrl = getUserSignatureUrl(user.getId());
        return userSignatureUrl;
    }

    public URL getUserSignatureUrl(Long userId){
        Date expires = new DateTime().plusDays(getExpiryInDays()).toDate();
        String resourcePath = getUserSignaturePath(userId);
        URL url = generatePresignedUrl(resourcePath, expires, HttpMethod.GET);
        return url;
    }

    public File downloadUserSignature(User user){
        return downloadUserSignature(user.getId());
    }

    public File downloadUserSignature(Long userId){
        File userSignatureFile = null;
        try {
            byte[] userSignatureBytes = downloadUserSignatureBytes(userId);
            userSignatureFile = PathHandler.getUserFile(getCurrentUser(), userId + USER_SIGNATURE_IMAGE_FILE_NAME);
            FileOutputStream userSignatureFos = new FileOutputStream(userSignatureFile);
            userSignatureFos.write(userSignatureBytes);
        }
        catch(FileNotFoundException e) {
            logger.warn("Unable to write to temp signature file at: " + userSignatureFile, e);
        }
        catch(IOException e) {
            logger.warn("Unable to download user signature from S3", e);
        }
        return userSignatureFile;
    }

    public void uploadUserSignature(File userSignatureFile, User user){
        uploadUserSignature(userSignatureFile, user.getId());
    }

    public void uploadUserSignature(File userSignatureFile, Long userId){
        uploadResource(userSignatureFile, null, USER_SIGNATURE_PATH, userId);
    }

    public void uploadUserSignatureData(byte[] userSignatureData, User user){
        String userSignatureFileName = USER_SIGNATURE_PATH.substring(USER_SIGNATURE_PATH.lastIndexOf('/') + 1);
        String contentType = ContentTypeUtil.getContentType(userSignatureFileName);
        uploadUserSignatureData(userSignatureData, contentType, user.getId());
    }

    public void uploadUserSignatureData(byte[] userSignatureData, String contentType, Long userId){
        uploadResource(userSignatureData, contentType, null, USER_SIGNATURE_PATH, userId);
    }

    public byte[] downloadUserSignatureBytes(User user) throws IOException {
        //the attachment Filename field is overloaded to house full URL instead of just the filename
        byte[] userSignatureData = downloadUserSignatureBytes(user.getId());
        return userSignatureData;
    }

    public byte[] downloadUserSignatureBytes(Long userId) throws IOException {
        return downloadResource(null, USER_SIGNATURE_PATH, userId);
    }

    public boolean userSignatureExists(User user){
        return userSignatureExists(user.getId());
    }

    public boolean userSignatureExists(Long userId){
        boolean exists = resourceExists(null, USER_SIGNATURE_PATH, userId);
        return exists;
    }

    public void removeUserSignature(User user){
        removeUserSignature(user.getId());
    }

    public void removeUserSignature(Long userId) {
        removeResource(null, USER_SIGNATURE_PATH, userId);
    }

    public String getEventSignaturePath(SignatureCriteriaResult signatureResult){
        String eventSignaturePath = getEventSignaturePath(signatureResult.getTenant().getId(), signatureResult.getEvent().getId(), signatureResult.getCriteria().getId());
        return eventSignaturePath;
    }

    public String getEventSignaturePath(Long tenantId, Long eventId, Long criteriaId){
        String resourcePath = createResourcePath(tenantId, EVENT_SIGNATURE_PATH, eventId, criteriaId);
        return resourcePath;
    }

    public URL getEventSignatureUrl(SignatureCriteriaResult signatureResult){
        URL eventSignatureUrl = getEventSignatureUrl(signatureResult.getTenant().getId(), signatureResult.getEvent().getId(), signatureResult.getCriteria().getId());
        return eventSignatureUrl;
    }

    public URL getEventSignatureUrl(Long tenantId, Long eventId, Long criteriaId){
        Date expires = new DateTime().plusDays(getExpiryInDays()).toDate();
        String resourcePath = getEventSignaturePath(tenantId, eventId, criteriaId);
        URL url = generatePresignedUrl(resourcePath, expires, HttpMethod.GET);
        return url;
    }

    public File downloadEventSignature(SignatureCriteriaResult signatureResult){
        return downloadEventSignature(signatureResult.getTenant().getId(), signatureResult.getEvent().getId(), signatureResult.getCriteria().getId());
    }

    public File downloadEventSignature(Long tenantId, Long eventId, Long criteriaId){
        File eventSignatureFile = null;
        try {
            byte[] eventSignatureBytes = downloadEventSignatureBytes(tenantId, eventId, criteriaId);
            eventSignatureFile = SignatureService.getTemporarySignatureFile(tenantId, UUID.randomUUID().toString());
            FileOutputStream eventSignatureFos = new FileOutputStream(eventSignatureFile);
            eventSignatureFos.write(eventSignatureBytes);
        }
        catch(FileNotFoundException e) {
            logger.warn("Unable to write to temp signature file at: " + eventSignatureFile, e);
        }
        catch(IOException e) {
            logger.warn("Unable to download event signature from S3", e);
        }
        return eventSignatureFile;
    }

    public void uploadEventSignature(File eventSignatureFile, SignatureCriteriaResult signatureResult){
        uploadEventSignature(eventSignatureFile, signatureResult.getTenant().getId(), signatureResult.getEvent().getId(), signatureResult.getCriteria().getId());
    }

    public void uploadEventSignature(File eventSignatureFile, Long tenantId, Long eventId, Long criteriaId){
        uploadResource(eventSignatureFile, tenantId, EVENT_SIGNATURE_PATH, eventId, criteriaId);
    }

    public void uploadEventSignatureData(byte[] eventSignatureData, SignatureCriteriaResult signatureResult){
        String eventSignatureFileName = EVENT_SIGNATURE_PATH.substring(EVENT_SIGNATURE_PATH.lastIndexOf('/') + 1);
        String contentType = ContentTypeUtil.getContentType(eventSignatureFileName);
        uploadEventSignatureData(eventSignatureData, contentType, signatureResult.getTenant().getId(), signatureResult.getEvent().getId(), signatureResult.getCriteria().getId());
    }

    public void uploadEventSignatureData(byte[] eventSignatureData, String contentType, Long tenantId, Long eventId, Long criteriaId){
        uploadResource(eventSignatureData, contentType, tenantId, EVENT_SIGNATURE_PATH, eventId, criteriaId);
    }

    public byte[] downloadEventSignatureBytes(SignatureCriteriaResult signatureResult) throws IOException {
        //the attachment Filename field is overloaded to house full URL instead of just the filename
        byte[] eventSignatureData = downloadEventSignatureBytes(signatureResult.getTenant().getId(), signatureResult.getEvent().getId(), signatureResult.getCriteria().getId());
        return eventSignatureData;
    }

    public byte[] downloadEventSignatureBytes(Long tenantId, Long eventId, Long criteriaId) throws IOException {
        return downloadResource(tenantId, EVENT_SIGNATURE_PATH, eventId, criteriaId);
    }

    public boolean eventSignatureExists(SignatureCriteriaResult signatureResult){
        return eventSignatureExists(signatureResult.getTenant().getId(), signatureResult.getEvent().getId(), signatureResult.getCriteria().getId());
    }

    public boolean eventSignatureExists(Long tenantId, Long eventId, Long criteriaId){
        boolean exists = resourceExists(tenantId, EVENT_SIGNATURE_PATH, eventId, criteriaId);
        return exists;
    }

    public String getAssetAttachmentPath(AssetAttachment assetAttachment){
        String fileName = assetAttachment.getFileName().substring(assetAttachment.getFileName().lastIndexOf('/') + 1);
        String assetAttachmentPath = getAssetAttachmentPath(assetAttachment.getAsset().getMobileGUID(), assetAttachment.getMobileId(), fileName);
        return assetAttachmentPath;
    }

    public String getAssetAttachmentPath(String assetUuid, String assetAttachmentUuid, String assetAttachmentFilename){
        Assert.hasLength(assetUuid);
        Assert.hasLength(assetAttachmentUuid);
        Assert.hasLength(assetAttachmentFilename);
        assetAttachmentFilename = assetAttachmentFilename.substring(assetAttachmentFilename.lastIndexOf('/') + 1);
        String fullResourcePath = createResourcePath(null, ASSET_ATTACHMENT_PATH, assetUuid, assetAttachmentUuid, assetAttachmentFilename);
        return fullResourcePath;
    }

    public URL getAssetAttachmentUrl(AssetAttachment assetAttachment){
        URL assetAttachmentUrl = getAssetAttachmentUrl(assetAttachment.getAsset().getMobileGUID(), assetAttachment.getMobileId(), assetAttachment.getFileName());
        return assetAttachmentUrl;
    }

    public URL getAssetAttachmentUrl(String assetUuid, String assetAttachmentUuid, String assetAttachmentFilename) {
        Assert.hasLength(assetUuid);
        Assert.hasLength(assetAttachmentUuid);
        Assert.hasLength(assetAttachmentFilename);
        Date expires = new DateTime().plusDays(getExpiryInDays()).toDate();
        assetAttachmentFilename = assetAttachmentFilename.substring(assetAttachmentFilename.lastIndexOf('/') + 1);
        String fullResourcePath = getAssetAttachmentPath(assetUuid, assetAttachmentUuid, assetAttachmentFilename);
        URL url = generatePresignedUrl(fullResourcePath, expires, HttpMethod.GET);
        return url;
    }

    public File downloadAssetAttachment(AssetAttachment attachment){
        File assetAttachmentFile = null;
        try {
            byte[] assetAttachmentBytes = downloadAssetAttachmentBytes(attachment);
            String assetAttachmentFileName = attachment.getFileName().substring(attachment.getFileName().lastIndexOf('/') + 1);
            assetAttachmentFile = PathHandler.getUserFile(getCurrentUser(), assetAttachmentFileName);
            FileOutputStream assetAttachmentFos = new FileOutputStream(assetAttachmentFile);
            assetAttachmentFos.write(assetAttachmentBytes);
        }
        catch(FileNotFoundException e) {
            logger.warn("Unable to write to temp attachment file at: " + assetAttachmentFile, e);
        }
        catch(IOException e) {
            logger.warn("Unable to download asset attachment from S3: " + attachment, e);
        }
        return assetAttachmentFile;
    }

    public void uploadAssetAttachment(File assetAttachmentFile, AssetAttachment attachment){
        String assetAttachmentFileName = assetAttachmentFile.getName().substring(assetAttachmentFile.getName().lastIndexOf('/') + 1);
        uploadAssetAttachment(assetAttachmentFile, attachment.getAsset().getMobileGUID(), attachment.getMobileId(), assetAttachmentFileName);
    }

    public void uploadAssetAttachment(File assetAttachmentFile, String assetUuid, String assetAttachmentUuid, String assetAttachmentFilename){
        uploadResource(assetAttachmentFile, null, ASSET_ATTACHMENT_PATH, assetUuid, assetAttachmentUuid, assetAttachmentFilename);
    }

    public void uploadAssetAttachmentData(byte[] attachmentData, AssetAttachment attachment){
        String assetAttachmentFileName = attachment.getFileName().substring(attachment.getFileName().lastIndexOf('/') + 1);
        String contentType = ContentTypeUtil.getContentType(attachment.getFileName());
        uploadAssetAttachmentData(attachmentData, contentType, attachment.getAsset().getMobileGUID(), attachment.getMobileId(), assetAttachmentFileName);
    }

    public void uploadAssetAttachmentData(byte[] attachmentData, String contentType, String assetUuid, String assetAttachmentUuid, String assetAttachmentFilename){
        uploadResource(attachmentData, contentType, null, ASSET_ATTACHMENT_PATH, assetUuid, assetAttachmentUuid, assetAttachmentFilename);
    }

    public byte[] downloadAssetAttachmentBytes(AssetAttachment attachment) throws IOException {
        //the attachment Filename field is overloaded to house full URL instead of just the filename
        String assetAttachmentFileName = attachment.getFileName().substring(attachment.getFileName().lastIndexOf('/') + 1);
        byte[] assetAttachmentData = downloadAssetAttachmentBytes(attachment.getAsset().getMobileGUID(), attachment.getMobileId(), assetAttachmentFileName);
        return assetAttachmentData;
    }

    public byte[] downloadAssetAttachmentBytes(String assetUuid, String assetAttachmentUuid, String assetAttachmentFilename) throws IOException {
        Assert.hasLength(assetUuid);
        Assert.hasLength(assetAttachmentUuid);
        Assert.hasLength(assetAttachmentFilename);
        return downloadResource(null, ASSET_ATTACHMENT_PATH, assetUuid, assetAttachmentUuid, assetAttachmentFilename);
    }

    public String getAssetProofTestFilePath(ThingEventProofTest proofTest){
        String assetProofTestPath = getAssetProofTestFilePath(proofTest.getAsset().getMobileGUID(), proofTest.getThingEvent().getMobileGUID());
        return assetProofTestPath;
    }

    public String getAssetProofTestChartPath(ThingEventProofTest proofTest){
        String assetProofTestPath = getAssetProofTestChartPath(proofTest.getAsset().getMobileGUID(), proofTest.getThingEvent().getMobileGUID());
        return assetProofTestPath;
    }

    public String getAssetProofTestFilePath(String assetUuid, String eventUuid){
        Assert.hasLength(assetUuid);
        Assert.hasLength(eventUuid);
        return createResourcePath(null, ASSET_PROOFTESTS_FILE_PATH, assetUuid, eventUuid);
    }

    public String getAssetProofTestChartPath(String assetUuid, String eventUuid){
        Assert.hasLength(assetUuid);
        Assert.hasLength(eventUuid);
        return createResourcePath(null, ASSET_PROOFTESTS_CHART_PATH, assetUuid, eventUuid);
    }

    public URL getAssetProofTestFileUrl(ThingEventProofTest proofTest){
        URL assetProofTestUrl = getAssetProofTestFileUrl(proofTest.getAsset().getMobileGUID(), proofTest.getThingEvent().getMobileGUID());
        return assetProofTestUrl;
    }

    public URL getAssetProofTestChartUrl(ThingEventProofTest proofTest){
        URL assetProofTestUrl = getAssetProofTestChartUrl(proofTest.getAsset().getMobileGUID(), proofTest.getThingEvent().getMobileGUID());
        return assetProofTestUrl;
    }

    public URL getAssetProofTestFileUrl(String assetUuid, String eventUuid){
        Assert.hasLength(assetUuid);
        Assert.hasLength(eventUuid);
        Date expires = new DateTime().plusDays(getExpiryInDays()).toDate();
        String fullResourcePath = getAssetProofTestFilePath(assetUuid, eventUuid);
        URL url = generatePresignedUrl(fullResourcePath, expires, HttpMethod.GET);
        return url;
    }

    public URL getAssetProofTestChartUrl(String assetUuid, String eventUuid){
        Assert.hasLength(assetUuid);
        Assert.hasLength(eventUuid);
        Date expires = new DateTime().plusDays(getExpiryInDays()).toDate();
        String fullResourcePath = getAssetProofTestChartPath(assetUuid, eventUuid);
        URL url = generatePresignedUrl(fullResourcePath, expires, HttpMethod.GET);
        return url;
    }

    public File downloadAssetProofTestFile(ThingEventProofTest event){
        File assetProofTestFile = null;
        try {
            byte[] assetProofTestBytes = downloadAssetProofTestFileBytes(event);

            String fileName = event.getProofTestFileName();
            if(fileName == null){
                fileName = CHART_FILE_NAME;
            }
            fileName = fileName.substring(fileName.lastIndexOf('/') + 1);
            fileName = event.getAsset().getMobileGUID() + "-" + fileName;

            assetProofTestFile = PathHandler.getUserFile(getCurrentUser(), fileName);
            FileOutputStream assetProofTestFos = new FileOutputStream(assetProofTestFile);
            assetProofTestFos.write(assetProofTestBytes);
        }
        catch(FileNotFoundException e) {
            logger.warn("Unable to write to temp prooftest file at: " + assetProofTestFile, e);
        }
        catch(IOException e) {
            logger.warn("Unable to download asset prooftest from S3: " + event, e);
        }
        return assetProofTestFile;
    }

    public File downloadAssetProofTestChart(ThingEventProofTest event){
        File assetProofTestFile = null;
        try {
            byte[] assetProofTestBytes = downloadAssetProofTestChartBytes(event);

            String fileName = CHART_FILE_NAME;
            fileName = event.getAsset().getMobileGUID() + "-" + fileName;

            assetProofTestFile = PathHandler.getUserFile(getCurrentUser(), fileName);
            FileOutputStream assetProofTestFos = new FileOutputStream(assetProofTestFile);
            assetProofTestFos.write(assetProofTestBytes);
        }
        catch(FileNotFoundException e) {
            logger.warn("Unable to write to temp prooftest file at: " + assetProofTestFile, e);
        }
        catch(IOException e) {
            logger.warn("Unable to download asset prooftest from S3: " + event, e);
        }
        return assetProofTestFile;
    }

    public void uploadAssetProofTestFile(File assetProofTestFile, ThingEventProofTest proofTest){
        uploadAssetProofTestFile(assetProofTestFile, proofTest.getAsset().getMobileGUID(), proofTest.getThingEvent().getMobileGUID());
    }

    public void uploadAssetProofTestChart(File assetProofTestChart, ThingEventProofTest proofTest){
        uploadAssetProofTestChart(assetProofTestChart, proofTest.getAsset().getMobileGUID(), proofTest.getThingEvent().getMobileGUID());
    }

    public void uploadAssetProofTestFile(File assetProofTestFile, String assetUuid, String eventUuid){
        uploadResource(assetProofTestFile, null, ASSET_PROOFTESTS_FILE_PATH, assetUuid, eventUuid);
    }

    public void uploadAssetProofTestChart(File assetProofTestChart, String assetUuid, String eventUuid){
        uploadResource(assetProofTestChart, null, ASSET_PROOFTESTS_CHART_PATH, assetUuid, eventUuid);
    }

    public void uploadAssetProofTestFile(byte[] proofTestFile, ThingEventProofTest proofTest){
        String contentType = "text/plain";
        uploadAssetProofTestFile(proofTestFile, contentType, proofTest.getAsset().getMobileGUID(), proofTest.getThingEvent().getMobileGUID());
    }

    public void uploadAssetProofTestChart(byte[] proofTestChart, ThingEventProofTest proofTest){
        String contentType = "image/png";
        uploadAssetProofTestChart(proofTestChart, contentType, proofTest.getAsset().getMobileGUID(), proofTest.getThingEvent().getMobileGUID());
    }

    public void uploadAssetProofTestFile(byte[] proofTestChart, String contentType, String assetUuid, String eventUuid){
        uploadResource(proofTestChart, contentType, null, ASSET_PROOFTESTS_FILE_PATH, assetUuid, eventUuid);
    }

    public void uploadAssetProofTestChart(byte[] proofTestChart, String contentType, String assetUuid, String eventUuid){
        uploadResource(proofTestChart, contentType, null, ASSET_PROOFTESTS_CHART_PATH, assetUuid, eventUuid);
    }

    public byte[] downloadAssetProofTestFileBytes(ThingEventProofTest proofTest) throws IOException {
        byte[] assetProofTestChart = downloadAssetProofTestFileBytes(proofTest.getAsset().getMobileGUID(), proofTest.getThingEvent().getMobileGUID());
        return assetProofTestChart;
    }

    public byte[] downloadAssetProofTestChartBytes(ThingEventProofTest proofTest) throws IOException {
        byte[] assetProofTestChart = downloadAssetProofTestChartBytes(proofTest.getAsset().getMobileGUID(), proofTest.getThingEvent().getMobileGUID());
        return assetProofTestChart;
    }

    public byte[] downloadAssetProofTestFileBytes(String assetUuid, String eventUuid) throws IOException {
        Assert.hasLength(assetUuid);
        Assert.hasLength(eventUuid);
        return downloadResource(null, ASSET_PROOFTESTS_FILE_PATH, assetUuid, eventUuid);
    }

    public byte[] downloadAssetProofTestChartBytes(String assetUuid, String eventUuid) throws IOException {
        Assert.hasLength(assetUuid);
        Assert.hasLength(eventUuid);
        return downloadResource(null, ASSET_PROOFTESTS_CHART_PATH, assetUuid, eventUuid);
    }

    public String getFileAttachmentPath(FileAttachment fileAttachment){
        String fileName = fileAttachment.getFileName().substring(fileAttachment.getFileName().lastIndexOf('/') + 1);
        String fileAttachmentPath = getFileAttachmentPath(fileAttachment.getMobileId(), fileName);
        return fileAttachmentPath;
    }

    public String getFileAttachmentPath(String fileAttachmentUuid, String filename){
        Assert.hasLength(fileAttachmentUuid);
        Assert.hasLength(filename);
        Assert.doesNotContain(filename, "/");
        String fileAttachmentsUploadPath = createResourcePath(null, FILE_ATTACHMENT_PATH, fileAttachmentUuid, filename);
        return fileAttachmentsUploadPath;
    }

    public URL getFileAttachmentUrl(FileAttachment fileAttachment){
        String fileName = fileAttachment.getFileName().substring(fileAttachment.getFileName().lastIndexOf('/') + 1);
        URL fileAttachmentUrl = getFileAttachmentUrl(fileAttachment.getMobileId(), fileName);
        return fileAttachmentUrl;
    }

    public URL getFileAttachmentUrl(String fileAttachmentUuid, String filename) {
        Assert.hasLength(fileAttachmentUuid);
        Assert.hasLength(filename);
        Assert.doesNotContain(filename, "/");
        Date expires = new DateTime().plusDays(getExpiryInDays()).toDate();
        filename = filename.substring(filename.lastIndexOf('/') + 1);
        String fullResourcePath = getFileAttachmentPath(fileAttachmentUuid, filename);
        return generatePresignedUrl(fullResourcePath, expires, HttpMethod.GET);
    }

    public File downloadFileAttachment(FileAttachment attachment){
        File file = null;
        try {
            byte[] fileAttachmentBytes = downloadFileAttachmentBytes(attachment);
            String fileName = attachment.getFileName().substring(attachment.getFileName().lastIndexOf('/') + 1);
            file = PathHandler.getUserFile(getCurrentUser(), fileName);
            FileOutputStream fileAttachmentFos = new FileOutputStream(file);
            fileAttachmentFos.write(fileAttachmentBytes);
        }
        catch(FileNotFoundException e) {
            logger.warn("Unable to write to temp attachment file at: " + file, e);
        }
        catch(IOException e) {
            logger.warn("Unable to download file attachment from S3: " + attachment, e);
        }
        return file;
    }

    public void uploadFileAttachment(File file, FileAttachment attachment){
        String fileName = file.getName().substring(file.getName().lastIndexOf('/') + 1);
        uploadFileAttachment(file, attachment.getMobileId(), fileName);
    }

    public void uploadFileAttachment(File file, String fileAttachmentUuid, String filename){
        Assert.hasLength(fileAttachmentUuid);
        Assert.hasLength(filename);
        Assert.doesNotContain(filename, "/");
        uploadResource(file, null, FILE_ATTACHMENT_PATH, fileAttachmentUuid, filename);
    }

    public void uploadFileAttachmentData(byte[] attachmentData, FileAttachment attachment){
        String fileName = attachment.getFileName().substring(attachment.getFileName().lastIndexOf('/') + 1);
        String contentType = ContentTypeUtil.getContentType(attachment.getFileName());
        uploadFileAttachmentData(attachmentData, contentType, attachment.getMobileId(), fileName);
    }

    public void uploadFileAttachmentData(byte[] attachmentData, String contentType, String fileAttachmentUuid, String filename){
        Assert.hasLength(fileAttachmentUuid);
        Assert.hasLength(filename);
        Assert.doesNotContain(filename, "/");
        Assert.hasLength(contentType);
        uploadResource(attachmentData, contentType, null, FILE_ATTACHMENT_PATH, fileAttachmentUuid, filename);
    }

    public byte[] downloadFileAttachmentBytes(FileAttachment attachment) throws IOException {
        //the attachment Filename field is overloaded to house full URL instead of just the filename
        String fileName = attachment.getFileName().substring(attachment.getFileName().lastIndexOf('/') + 1);
        byte[] fileAttachmentData = downloadFileAttachmentBytes(attachment.getMobileId(), fileName);
        return fileAttachmentData;
    }

    public byte[] downloadFileAttachmentBytes(String fileAttachmentUuid, String filename) throws IOException {
        Assert.hasLength(fileAttachmentUuid);
        Assert.hasLength(filename);
        Assert.doesNotContain(filename, "/");
        return downloadResource(null, FILE_ATTACHMENT_PATH, fileAttachmentUuid, filename);
    }

    public String getBucketPolicySigned(String bucketPolicyBase64){
        try {
            Mac hmac = Mac.getInstance("HmacSHA1");
            hmac.init(new SecretKeySpec(getSecretKey().getBytes("UTF-8"), "HmacSHA1"));

            String bucketPolicySignature = (new BASE64Encoder()).encode(hmac.doFinal(bucketPolicyBase64.getBytes("UTF-8"))).replaceAll("\n", "");
            return bucketPolicySignature;
        }
        catch(Exception e){
            logger.warn(e);
            return "";
        }
    }

    public String getBucketPolicyBase64(){
        try {
            return (new BASE64Encoder()).encode(
                    this.getBucketPolicy().getBytes("UTF-8")).replaceAll("\n","").replaceAll("\r","");
        }
        catch(Exception e){
            logger.warn(e);
            return "";
        }
    }

    public String getBucketPolicy(){
        String policyDocument = " {" +
                "'expiration': '" + new DateTime().plusDays(getExpiryInDays()).toString() + "'," +
                "'conditions': [" +
                "   {'bucket': '" + this.getBucket() + "'}," +
                "   {'acl': 'private'}," +
                "   ['starts-with', '$key', '']," +
                "   ['starts-with', '$Content-Type', '']," +
                "   ['starts-with', '$User-Agent', '']," +
                "   ['starts-with', '$Referer', '']," +
                "   ['starts-with', '$Server', '']," +
                "   ['starts-with', '$x-amz-meta-fieldid-user-userid', '']," +
                "   ['starts-with', '$x-amz-meta-fieldid-user-type', '']," +
                "   ['starts-with', '$x-amz-meta-fieldid-user-permissions', '']," +
                "   ['starts-with', '$x-amz-meta-fieldid-user-guid', '']," +
                "   ['starts-with', '$x-amz-meta-fieldid-user-owner-guid', '']," +
                "   ['content-length-range', 1, " + this.getUploadMaxFileSizeBytes() + "] ] }";
        return policyDocument;
    }

    public String getAssetAttachmentsUploadJavascript(String assetUuid, String assetAttachmentUuid, String uploadFormMarkupId, String callbackContainerMarkupId){

        String uploadJavascript = getUploadJavascript(ASSET_ATTACHMENT_FOLDER, assetUuid, assetAttachmentUuid, uploadFormMarkupId, callbackContainerMarkupId);
        return uploadJavascript;
    }

    protected String getUploadJavascript(String pathPattern, String parentUuid, String childUuid, String uploadFormMarkupId, String callbackContainerMarkupId){
        String path = createResourcePath(null, pathPattern, parentUuid, childUuid);
        User user = getCurrentUser();
        String bucketPolicyBase64 = this.getBucketPolicyBase64();

        String uploadJavascript =
            "var control = document.getElementById('" + uploadFormMarkupId + "');" +
            "if(!window.uploadsInProgress){" +
            "   window.uploadsInProgress = 0;" +
            "   window.setInterval(function(){" +
            "       var buttons = document.querySelectorAll('[name=\"actionsContainer:saveButton\"],[name=\"actionsContainer:saveAndStartEventButton\"],[name=\"actionsContainer:saveAndPrintButton\"],[name=\"actionsContainer:mergeLink\"]');" +
            "       for(var i = 0; i < buttons.length; i++){" +
            "           buttons[i].disabled = (window.uploadsInProgress > 0);" +
            "       }" +
            "       var indicators = document.querySelectorAll('.wicket-ajax-indicator');" +
            "       for(var i = 0; i < indicators.length; i++){" +
            "           indicators[i].style.display = (window.uploadsInProgress > 0 ? 'inline' : 'none');" +
            "       }" +
            "   }, 1000);" +
            "}" +
            "for(var i = 0; control.files && i < control.files.length; i++){" +
            "   var file = control.files[i];" +
            //if file size is bigger than allowed, just skip it, the panel will show error message to user
            "   if(file.size >= " + this.getUploadMaxFileSizeBytes() + "){" +
            "       continue;" +
            "   }" +
            "   var uploadStarted = function(){" +
            "       window.uploadsInProgress += 1;" +
            "       wicketAjaxGet(getAjaxCallbackUrl('" + callbackContainerMarkupId + "') + '&filename=' + encodeURI(file.name) + '&status=-1&uuid=" + childUuid + "', function() { }, function() { });" +
            "   };" +
            "   var uploadFinished = function(xhr){" +
            "       window.uploadsInProgress -= 1;" +
            "       wicketAjaxGet(getAjaxCallbackUrl('" + callbackContainerMarkupId + "') + '&filename=' + encodeURI(file.name) + '&status=' + xhr.status + '&uuid=" + childUuid + "', function() { }, function() { });" +
            "   };" +
            "   var fd = new FormData();" +
            "   var key = '" + path + "' + file.name;" +
            // Populate the Post paramters.
            "   fd.append('key', key);" +
            "   fd.append('Content-Type', mimeLookup.getContentType(mimeLookup.getExt(file.name)));" +
            "   fd.append('User-Agent', navigator.userAgent);" +
            "   fd.append('Referer', document.URL);" +
            "   fd.append('Server', '" + FieldIdVersion.getWebVersionDescription() + "');" +
            "   fd.append('AWSAccessKeyId', '" + this.getAccessKey() + "');" +
            "   fd.append('x-amz-meta-fieldid-user-userid', '" + user.getUserID() + "');" +
            "   fd.append('x-amz-meta-fieldid-user-type', '" + user.getUserType().getLabel() + "');" +
            "   fd.append('x-amz-meta-fieldid-user-permissions', '" + user.getPermissions() + "');" +
            "   fd.append('x-amz-meta-fieldid-user-guid', '" + user.getGlobalId() + "');" +
            "   fd.append('x-amz-meta-fieldid-user-owner-guid', '" + user.getOwner().getGlobalId() + "');" +
            "   fd.append('acl', 'private');" +
            "   fd.append('policy', '" + bucketPolicyBase64 + "');" +
            "   fd.append('signature','" + this.getBucketPolicySigned(bucketPolicyBase64) + "');" +
            //This file object is retrieved from a file input
            "   fd.append('file', file );" +
            "   var url = 'https://" + this.getBucketHostname() + "';" +
            "   var timeout = " + getUploadTimeoutMilliseconds() + ";" +
            "   $.ajax({" +
            "       url: url," +
            "       data: fd," +
            "       processData: false," +
            "       type: 'POST'," +
            "       dataType: 'json'," +
            "       crossDomain: true," +
            "       contentType: false," +
            //"       contentType: 'multipart/form-data'," +
            //"       mimeType: 'multipart/form-data'," +
            "       timeout: timeout," +
            "       beforeSend: function(jqXHR, settings){ uploadStarted(); }," +
            "       success: function(data, textStatus, jqXHR){ uploadFinished(jqXHR); }, " +
            "       error: function(jqXHR, textStatus, errorThrown){ uploadFinished(jqXHR); } " +
            "   });" +
            //"   xhr.open('POST', 'https://" + this.getBucketHostname() + "', true);" +
            //"   xhr.send(fd);" +
            "}";
        return uploadJavascript;
    }

    public String copyAssetAttachment(String assetAttachmentAbsPath, String targetAssetUuid, String targetAssetAttachmentUuid){

        String sourceAssetAttachmentFileName = assetAttachmentAbsPath.substring(assetAttachmentAbsPath.lastIndexOf('/') + 1);
        String targetAssetAttachmentPath = getAssetAttachmentPath(targetAssetUuid, targetAssetAttachmentUuid, sourceAssetAttachmentFileName);

        try {
            CopyObjectRequest copyObjectRequest = new CopyObjectRequest(getBucket(), assetAttachmentAbsPath, getBucket(), targetAssetAttachmentPath);
            getClient().copyObject(copyObjectRequest);
            return targetAssetAttachmentPath;
        } catch(AmazonServiceException ase){
            logger.warn("Unable to copy asset attachment file at: " + assetAttachmentAbsPath, ase);
            return assetAttachmentAbsPath;
        }
    }

//    public void uploadTempAttachment(S3Attachment attachment) {
//        for (S3Attachment attachmentFlavour:getS3AttachmentHandler(attachment).createFlavours(attachment)) {
//            putObject(attachmentFlavour.getTempPath(), attachmentFlavour.getBytes(), attachmentFlavour.getContentType());
//        }
//    }
//
//    public void uploadAttachment(S3Attachment attachment) {
//        for (S3Attachment attachmentFlavour:getS3AttachmentHandler(attachment).createFlavours(attachment)) {
//            putObject(attachmentFlavour.getPath(), attachmentFlavour.getBytes(), attachmentFlavour.getContentType());
//        }
//    }

//    private S3AttachmentHandler getS3AttachmentHandler(S3Attachment attachment) {
//        // TODO : check meta-data for content-type and return appropriate handler.
//        // for now we only have one type of attachments running through this code.
//        return s3ImageAttachmentHandler;
//    }
//
//    public void finalize(S3Attachment attachment) {
//        List<S3Attachment> flavours = getS3AttachmentHandler(attachment).createFlavours(attachment);
//        for (S3Attachment flavour:flavours) {
//            finalizeImpl(flavour);
//        }
//    }

//    private void finalizeImpl(S3Attachment attachment) {
//        CopyObjectRequest copyObjectRequest = new CopyObjectRequest(
//                getBucket(),
//                    attachment.getTempPath(),
//                getBucket(),
//                    attachment.getPath());
//
//        getClient().copyObject(copyObjectRequest);
//        // what about removing from temp? when should this be done.
//    }
//
//    public URL getAttachmentUrl(S3Attachment attachment, String suffix) {
//        if (attachment==null || StringUtils.isBlank(attachment.getPath())) {
//            return null;
//        }
//        Date expires = new DateTime().plusDays(getExpiryInDays()).toDate();
//
//        String path = attachment.getPath();
//        if (StringUtils.isNotBlank(suffix)) {
//            path = suffix.startsWith(".") ? path+suffix : path + "." + suffix;
//        }
//        URL url = generatePresignedUrl(path, expires, HttpMethod.GET);
//        return url;
//    }
//    public URL getAttachmentUrl(S3Attachment attachment) {
//        return getAttachmentUrl(attachment,null);
//    }
//
//    public void removeAttachment(S3Attachment attachment) {
//        List<S3Attachment> flavours = getS3AttachmentHandler(attachment).getFlavours(attachment);
//        for (S3Attachment flavour:flavours) {
//            deleteObject(flavour.getPath());
//        }
//    }

    public class S3ImagePath {
        private String origPath;
        private String mediumPath;
        private String thumbnailPath;

        public S3ImagePath(String path, Long tenantId) {
            Preconditions.checkArgument(tenantId != null, "you must give a tenant when uploading images.");
            origPath = createResourcePath(tenantId, path);
            mediumPath = createResourcePath(tenantId, path + MEDIUM_EXTENSION);
            thumbnailPath = createResourcePath(tenantId, path + THUMBNAIL_EXTENSION);
        }

        public String getMediumPath() {
            return mediumPath;
        }

        public String getOrigPath() {
            return origPath;
        }

        public String getThumbnailPath() {
            return thumbnailPath;
        }

    }

}
