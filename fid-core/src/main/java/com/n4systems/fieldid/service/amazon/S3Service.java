package com.n4systems.fieldid.service.amazon;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.HttpMethod;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.images.ImageService;
import com.n4systems.model.criteriaresult.CriteriaResultImage;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.services.ConfigService;
import com.n4systems.util.ConfigEntry;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Transactional
public class S3Service extends FieldIdPersistenceService {
    private static final long TTL = TimeUnit.DAYS.toMillis(1);
    public static final String DEFAULT_BRANDING_LOGO_PATH = "common/default_branding_logo.gif";
    public static final String BRANDING_LOGO_PATH = "/logos/branding_logo.gif";
    public static final String CUSTOMER_LOGO_PATH = "/logos/customer_logo_%d.gif";
    public static final String PRIMARY_CERTIFICATE_LOGO_PATH = "/logos/primary_certificate_logo.gif";
    public static final String SECONDARY_CERTIFICATE_LOGO_PATH = "/logos/secondary_certificate_logo_%d.gif";
	public static final String CRITERIA_RESULT_IMAGE_PATH_ORIG = "/events/%d/criteria_results/%d/criteria_images/%s";
	public static final String CRITERIA_RESULT_IMAGE_PATH_THUMB = "/events/%d/criteria_results/%d/criteria_images/%s.thumbnail";
	public static final String CRITERIA_RESULT_IMAGE_PATH_MEDIUM = "/events/%d/criteria_results/%d/criteria_images/%s.medium";

    @Autowired private ConfigService configService;
	@Autowired private ImageService imageService;

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

	public void uploadCriteriaResultImage(CriteriaResultImage criteriaResultImage) {
		byte[] thumbnailImage = imageService.generateThumbnail(criteriaResultImage.getImageData());
		byte[] mediumImage = imageService.generateMedium(criteriaResultImage.getImageData());

		uploadResource(
				thumbnailImage,
				criteriaResultImage.getContentType(),
				criteriaResultImage.getCriteriaResult().getTenant().getId(),
				CRITERIA_RESULT_IMAGE_PATH_THUMB,
				criteriaResultImage.getCriteriaResult().getEvent().getId(),
				criteriaResultImage.getCriteriaResult().getId(),
				criteriaResultImage.getFileName());

		uploadResource(
				mediumImage,
				criteriaResultImage.getContentType(),
				criteriaResultImage.getCriteriaResult().getTenant().getId(),
				CRITERIA_RESULT_IMAGE_PATH_MEDIUM,
				criteriaResultImage.getCriteriaResult().getEvent().getId(),
				criteriaResultImage.getCriteriaResult().getId(),
				criteriaResultImage.getFileName());

		uploadResource(
				criteriaResultImage.getImageData(),
				criteriaResultImage.getContentType(),
				criteriaResultImage.getCriteriaResult().getTenant().getId(),
				CRITERIA_RESULT_IMAGE_PATH_ORIG,
				criteriaResultImage.getCriteriaResult().getEvent().getId(),
				criteriaResultImage.getCriteriaResult().getId(),
				criteriaResultImage.getFileName());
	}

	public URL getCriteriaResultImageOriginalURL(CriteriaResultImage criteriaResultImage) {
		return generateResourceUrl(null, CRITERIA_RESULT_IMAGE_PATH_ORIG,
				criteriaResultImage.getCriteriaResult().getEvent().getId(),
				criteriaResultImage.getCriteriaResult().getId(),
				criteriaResultImage.getFileName());
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
	
	public byte[] downloadCriteriaResultImageMedium(CriteriaResultImage criteriaResultImage) throws IOException {
		return downloadResource(null, CRITERIA_RESULT_IMAGE_PATH_MEDIUM,
				criteriaResultImage.getCriteriaResult().getEvent().getId(),
				criteriaResultImage.getCriteriaResult().getId(),
				criteriaResultImage.getFileName());
	}

	private void uploadResource(byte[] data, String contentType, Long tenantId, String path, Object...pathArgs) {
		putObject(createResourcePath(tenantId, path, pathArgs), data, contentType);
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

    private URL generateResourceUrl(Long tenantId, String path, Object...pathArgs) {
        String fullResourcePath = createResourcePath(tenantId, path, pathArgs);
        Date expires = new Date(System.currentTimeMillis() + TTL);

        URL url = generatePresignedUrl(fullResourcePath, expires, HttpMethod.GET);
        return url;
    }

    private String createResourcePath(Long tenantId, String resourcePath, Object...pathArgs) {
        if (tenantId == null) {
            tenantId = securityContext.getTenantSecurityFilter().getTenantId();
        }
        String path = "tenants/" + tenantId + String.format(resourcePath, pathArgs);
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
        ClientConfiguration config = new ClientConfiguration();
        if ("https".equals(configService.getString(ConfigEntry.SYSTEM_PROTOCOL))) {
            config.setProtocol(Protocol.HTTPS);
        }
        AmazonS3Client client = new AmazonS3Client(getCredentials(), config);
        return client;
    }

    private AWSCredentials getCredentials() {
        String accessKeyId = configService.getString(ConfigEntry.AMAZON_ACCESS_KEY_ID);
        String secretAccessKey = configService.getString(ConfigEntry.AMAZON_SECRET_ACCESS_KEY);
        AWSCredentials credentials = new BasicAWSCredentials(accessKeyId, secretAccessKey);
        return credentials;
    }

    private String getBucket() {
        String bucket = configService.getString(ConfigEntry.AMAZON_S3_BUCKET);
        return bucket;
    }

}
