package com.n4systems.fieldid.service.amazon;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.google.common.base.Preconditions;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.images.ImageService;
import com.n4systems.fieldid.service.uuid.UUIDService;
import com.n4systems.model.Attachment;
import com.n4systems.model.attachment.S3Attachment;
import com.n4systems.model.criteriaresult.CriteriaResultImage;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.procedure.ProcedureDefinitionImage;
import com.n4systems.services.ConfigService;
import com.n4systems.util.ConfigEntry;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.activation.MimetypesFileTypeMap;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.List;

@Transactional
public class S3Service extends FieldIdPersistenceService {
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

    public static final String CRITERIA_RESULT_IMAGE_TEMP = "/temp_criteria_result_images/%s";
    public static final String CRITERIA_RESULT_THUMB_IMAGE_TEMP = "/temp_criteria_result_images/%s.thumbnail";
    public static final String CRITERIA_RESULT_MEDIUM_IMAGE_TEMP = "/temp_criteria_result_images/%s.medium";

    public static final String ASSET_PROFILE_IMAGE_PATH = "/assets/%d/profile/";
    public static final String ASSET_PROFILE_IMAGE_PATH_ORIG = "/assets/%d/profile/%s";
    public static final String ASSET_PROFILE_IMAGE_PATH_THUMB = "/assets/%d/profile/%s.thumbnail";
    public static final String ASSET_PROFILE_IMAGE_PATH_MEDIUM = "/assets/%d/profile/%s.medium";

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

    public void uploadSecondaryOrgCertificateLogo(File file, Long secondaryOrgId) {
        uploadResource(file, null, SECONDARY_CERTIFICATE_LOGO_PATH, secondaryOrgId);
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
            getObjectMetadata(createResourcePath(tenantId, path, pathArgs));
            return true;
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
            tenantId = securityContext.getTenantSecurityFilter().getTenantId();
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
        return getClient().generatePresignedUrl(getBucket(), path, expires, method);
    }

    private AmazonS3Client getClient() {
        return s3client;
    }

    private String getBucket() {
        String bucket = configService.getString(ConfigEntry.AMAZON_S3_BUCKET);
        return bucket;
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

    public void uploadTempAttachment(S3Attachment attachment) {
        for (S3Attachment attachmentFlavour:getS3AttachmentHandler(attachment).createFlavours(attachment)) {
            putObject(attachmentFlavour.getTempPath(), attachmentFlavour.getBytes(), attachmentFlavour.getContentType());
        }
    }

    public void uploadAttachment(S3Attachment attachment) {
        for (S3Attachment attachmentFlavour:getS3AttachmentHandler(attachment).createFlavours(attachment)) {
            putObject(attachmentFlavour.getPath(), attachmentFlavour.getBytes(), attachmentFlavour.getContentType());
        }
    }

    private S3AttachmentHandler getS3AttachmentHandler(S3Attachment attachment) {
        // TODO : check meta-data for content-type and return appropriate handler.
        // for now we only have one type of attachments running through this code.
        return s3ImageAttachmentHandler;
    }

    public void finalize(S3Attachment attachment) {
        List<S3Attachment> flavours = getS3AttachmentHandler(attachment).createFlavours(attachment);
        for (S3Attachment flavour:flavours) {
            finalizeImpl(flavour);
        }
    }

    private void finalizeImpl(S3Attachment attachment) {
        CopyObjectRequest copyObjectRequest = new CopyObjectRequest(
                getBucket(),
                    attachment.getTempPath(),
                getBucket(),
                    attachment.getPath());

        getClient().copyObject(copyObjectRequest);
        // what about removing from temp? when should this be done.
    }

    public URL getAttachmentUrl(S3Attachment attachment, String suffix) {
        if (attachment==null || StringUtils.isBlank(attachment.getPath())) {
            return null;
        }
        Date expires = new DateTime().plusDays(getExpiryInDays()).toDate();

        String path = attachment.getPath();
        if (StringUtils.isNotBlank(suffix)) {
            path = suffix.startsWith(".") ? path+suffix : path + "." + suffix;
        }
        URL url = generatePresignedUrl(path, expires, HttpMethod.GET);
        return url;
    }

    public URL getAttachmentUrl(S3Attachment attachment) {
        return getAttachmentUrl(attachment,null);
    }

    public void removeAttachment(S3Attachment attachment) {
        List<S3Attachment> flavours = getS3AttachmentHandler(attachment).getFlavours(attachment);
        for (S3Attachment flavour:flavours) {
            deleteObject(flavour.getPath());
        }
    }

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