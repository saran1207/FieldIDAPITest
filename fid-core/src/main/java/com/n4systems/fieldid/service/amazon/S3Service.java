package com.n4systems.fieldid.service.amazon;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.images.ImageService;
import com.n4systems.fieldid.service.uuid.UUIDService;
import com.n4systems.fieldid.version.FieldIdVersion;
import com.n4systems.model.*;
import com.n4systems.model.asset.AssetAttachment;
import com.n4systems.model.criteriaresult.CriteriaResultImage;
import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.procedure.IsolationPoint;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.procedure.ProcedureDefinitionImage;
import com.n4systems.model.security.TenantOnlySecurityFilter;
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
import org.springframework.util.Assert;

import javax.activation.MimetypesFileTypeMap;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

//import sun.misc.BASE64Encoder;

/**
 * This is your one stop shoppe for everything S3 related.  This will upload and download files, plus anything else
 * we might need to do with S3.
 *
 * All methods for sending FILE objects to S3 have been marked deprecated, since S3 tries to be smart about what
 * is being sent to it.  It attempts to discern your mime type from your file extension, which isn't always ideal.
 * Methods handling Byte Arrays effectively circumvent this issue by explicitly telling S3 what mime type to expect.
 */
@Transactional
public class S3Service extends FieldIdPersistenceService {

    private static final Logger logger = Logger.getLogger(S3Service.class);

    private static final int DEFAULT_EXPIRATION_DAYS = 1;

    private Integer expirationDays = null;
    public static final String TENANTS_PREFIX = "tenants/";
    public static final String DEFAULT_BRANDING_LOGO_PATH = "common/default_branding_logo.gif";
    public static final String CUSTOMER_LOGO_BASE_PATH = "/logos/";
    public static final String BRANDING_LOGO_PATH = CUSTOMER_LOGO_BASE_PATH + "branding_logo.gif";
    public static final String CUSTOMER_FILE_PREFIX = "customer_logo_";
    public static final String CUSTOMER_FILE_EXT = "gif";
    public static final String CUSTOMER_LOGO_PATH = CUSTOMER_LOGO_BASE_PATH + CUSTOMER_FILE_PREFIX + "%d." + CUSTOMER_FILE_EXT;
    public static final String PRIMARY_CERTIFICATE_LOGO_PATH = CUSTOMER_LOGO_BASE_PATH + "primary_certificate_logo.gif";
    public static final String SECONDARY_CERTIFICATE_LOGO_PATH = CUSTOMER_LOGO_BASE_PATH + "secondary_certificate_logo_%d.gif";
    public static final String CRITERIA_RESULT_IMAGE_PATH_ORIG = "/events/%d/criteria_results/%d/criteria_images/%s";
    public static final String CRITERIA_RESULT_IMAGE_PATH_THUMB = "/events/%d/criteria_results/%d/criteria_images/%s.thumbnail";
    public static final String CRITERIA_RESULT_IMAGE_PATH_MEDIUM = "/events/%d/criteria_results/%d/criteria_images/%s.medium";

//    public static final String GENERATED_REPORT_PATH = "/printouts/%d/%s/%s";
    public static final String GENERATED_REPORT_PATH = "/printouts/%d/%s/%d.%s";
    public static final String GENERATED_EXPORT_PATH = "/exports/%d/%s/%d.%s";

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

    public static final String LOTO_BUCKET = "fieldid-loto";

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

    public byte[] downloadCertificateLogo(Long customerOrgId, boolean isPrimary) throws IOException {
        byte[] logoData = null;
        if(isPrimary) {
            logoData = downloadResource(null, PRIMARY_CERTIFICATE_LOGO_PATH, customerOrgId);
        } else {
            logoData = downloadResource(null, SECONDARY_CERTIFICATE_LOGO_PATH, customerOrgId);
        }
        return logoData;
    }

    /**
     * This method is used to check whether or not a Ceritificate Logo exists.  This helps eliminate log entries which
     * result from trying to download resources that don't exist.  It's typically a bad idea to check if something
     * exists by trying to download it.
     *
     * Typically, if a resource isn't found with this method, it means it never existed... you should only worry if
     * this is returning false when you know damn well the file exists.  That would totally be cause for alarm.
     *
     * @param customerOrgId - A Long representing the ID of the Customer Org.
     * @param isPrimary - A boolean value indicating whether (true) or not (false) the Customer Org is the Primary Org.
     * @return A boolean value indicating whether (true) or not (false) the Logo exists in S3.
     */
    public boolean isCertificateLogoExists(Long customerOrgId, boolean isPrimary) {
        if(isPrimary) {
            return resourceExists(null, PRIMARY_CERTIFICATE_LOGO_PATH, customerOrgId);
        } else {
            return resourceExists(null, SECONDARY_CERTIFICATE_LOGO_PATH, customerOrgId);
        }
    }

    public List<S3ObjectSummary> getAllCustomerLogos() {
        // this path will contain both customer logos as well as branding and cert logos.  Need to filter down to just customers.
        return findResources(createResourcePath(null, CUSTOMER_LOGO_BASE_PATH), "^.*/" + CUSTOMER_FILE_PREFIX + "\\d+\\." + CUSTOMER_FILE_EXT + "$");
    }

    public String getCustomerLogoPath(Long customerOrgId) {
        String fullResourcePath = createResourcePath(null, CUSTOMER_LOGO_PATH, customerOrgId);
        return fullResourcePath;
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

    /**
     * This method should shove a generated report file up into the cloud in S3, at a predictable location.  This
     * predictable part is especially important, because we would eventually like to pull that file back OUT of the
     * cloud.
     *
     * @param file - A File representing the generated report you would like to jam up into S3 with a plunger.
     * @param downloadLink - A DownloadLink entity, representing the report you're uploading.
     */
    @Deprecated
    public void uploadGeneratedReport(File file, DownloadLink downloadLink) {
        uploadResource(file,
                       downloadLink.getTenant().getId(),
                       GENERATED_REPORT_PATH,
                       downloadLink.getUser().getId(),
                       downloadLink.getContentType().getExtension(),
                       downloadLink.getId(),
                       downloadLink.getContentType().getExtension());
    }

    /**
     * This method moves a generated report up to S3 at a predictable location.  Instead of using a full file, it only
     * uses a byte array and uses a DownloadLink entity to determine any other important information, such as Content
     * Type and a unique file name.
     *
     * @param fileContents - A byte array representing the contents of the generated report to be uploaded.
     * @param downloadLink - A DownloadLink entity which represents the file to be uploaded.
     */
    public void uploadGeneratedReport(byte[] fileContents, DownloadLink downloadLink) {
        uploadResource(fileContents,
                       downloadLink.getContentType().getMimeType(),
                       downloadLink.getTenant().getId(),
                       GENERATED_REPORT_PATH,
                       downloadLink.getUser().getId(),
                       downloadLink.getContentType().getExtension(),
                       downloadLink.getId(),
                       downloadLink.getContentType().getExtension());
    }

    /**
     * This method moves a generated Excel Export up to S3 at a predictable location.  This is done by passing a byte
     * array representative of the file contents and the related DownloadLink entity, which should provide the rest of
     * the necessary data to construct the path to the file.
     *
     * @param fileContents - A byte array representing the contents of a generated Export.
     * @param downloadLink - A DownloadLink entity representative of the Export.
     */
    public void uploadGeneratedExport(byte[] fileContents, DownloadLink downloadLink) {
        uploadResource(fileContents,
                       downloadLink.getContentType().getMimeType(),
                       downloadLink.getTenant().getId(),
                       GENERATED_EXPORT_PATH,
                       downloadLink.getUser().getId(),
                       downloadLink.getContentType().getExtension(),
                       downloadLink.getId(),
                       downloadLink.getContentType().getExtension());
    }

    /**
     * This method downloads a generated report from S3 as a byte array and passes it to the caller.  This allows us
     * to handle files in S3 without having to use physical files on disk, thus reducing overall disk I/O.
     *
     * @param downloadLink - A DownloadLink entity representing a generated report to be pulled from S3.
     * @return A byte array representing the contents of the desired file.
     */
    public byte[] getGeneratedExportByteArray(DownloadLink downloadLink) {
        try {
            return downloadResource(downloadLink.getTenant().getId(),
                                    GENERATED_EXPORT_PATH,
                                    downloadLink.getUser().getId(),
                                    downloadLink.getContentType().getExtension(),
                                    downloadLink.getId(),
                                    downloadLink.getContentType().getExtension());
        } catch (IOException e) {
            logger.error("Unable to download export file for DownloadLink with ID " + downloadLink.getId(), e);
            return null;
        }
    }

    /**
     * This method pulls a generated report from S3 as a byte array to be fed to some logic down stream to send the user
     * the report.  This logic will allow the report to be named by a value in the DownloadLink entity, instead of
     * forcing us to lock the name to something less readable by the user.
     *
     * @param downloadLink - A DownloadLink entity representing a generated report to be pulled from S3.
     * @return A byte array representing the contents of the desired file.
     */
    public byte[] getGeneratedReportByteArray(DownloadLink downloadLink) {
        try {
            return downloadResource(downloadLink.getTenant().getId(),
                                    GENERATED_REPORT_PATH,
                                    downloadLink.getUser().getId(),
                                    downloadLink.getContentType().getExtension(),
                                    downloadLink.getId(),
                                    downloadLink.getContentType().getExtension());
        } catch (IOException e) {
            logger.error("Unable to download file for DownloadLink with ID " + downloadLink.getId(), e);
            return null;
        }
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

    @Deprecated
    public void uploadPrimaryOrgCertificateLogo(File file) {
        uploadResource(file, null, PRIMARY_CERTIFICATE_LOGO_PATH);
    }

    public void uploadPrimaryOrgCertificateLogo(String contentType, byte[] bytes) {
        uploadResource(bytes, contentType, null, PRIMARY_CERTIFICATE_LOGO_PATH);
    }

    @Deprecated
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

    //---------------------------------------------------------------------------------
    public void deleteLotoPrintout(LotoPrintout printout) {
        deleteLotoObject(PathHandler.getS3BasePath(printout));
    }

    public byte[] downloadZippedLotoPrintout(LotoPrintout printout) throws  IOException {

        byte[] fileData = downloadLoto(PathHandler.getZipS3Path(printout));

        //String fileName = printout.getPrintoutName() + ".zip";
        //File file = PathHandler.getUserFile(getCurrentUser(), fileName);
        //FileOutputStream fileAttachmentFos = new FileOutputStream(file);
        //fileAttachmentFos.write(fileData);

        return fileData;
    }

    public Map<String, InputStream> downloadDefaultLotoJasperMap(LotoPrintout printout) throws IOException {
        return createLotoReportMap(printout.getPrintoutType(), PathHandler.getLotoDefaultPath(printout.getPrintoutType()));
    }

    public Map<String, InputStream> downloadCustomLotoJasperMap(LotoPrintout printout) throws IOException {
        return createLotoReportMap(printout.getPrintoutType(), PathHandler.getLotoPath(printout));
    }

    private Map<String, InputStream> createLotoReportMap(LotoPrintoutType type, String path) throws IOException {
        Map<String, InputStream> returnMe = Maps.newHashMap();

        returnMe.put("main", new ByteArrayInputStream(downloadLoto(path + "/procedure.jasper")));

        //NOTE: While we handle the failure here, we need to report the failure to load subreport sections at a level
        //      where we have more visibility of the Procedure Definition.  We need to attach as much information as
        //      possible to anything we log.  Otherwise, this message becomes meaningless.

        if(type.equals(LotoPrintoutType.LONG)) {
            byte[] ipSubreport = downloadLoto(path + "/procedure-isolation-points-long.jasper");
            if(ipSubreport != null) {
                returnMe.put("isolationPointSubreport", new ByteArrayInputStream(ipSubreport));
            }
        } else {
            byte[] ipSubreport = downloadLoto(path + "/isolation-points-short.jasper");
            if(ipSubreport != null) {
                returnMe.put("isolationPointSubreport", new ByteArrayInputStream(ipSubreport));
            }

            byte[] imageSubreport = downloadLoto(path + "/isolation-points-images-short.jasper");
            if(imageSubreport != null) {
                returnMe.put("imageSubreport", new ByteArrayInputStream(downloadLoto(path + "/isolation-points-images-short.jasper")));
            }
        }

        return returnMe;
    }

    public byte[] downloadLotoLogo() throws IOException {
        return downloadLoto("logos/loto-logo-mlock-horiz.jpg");
    }

    public byte[] downloadDefaultLotoPrintout(LotoPrintout printout) throws IOException {
        File printoutFile = null;

        byte[] fileData = downloadLoto(PathHandler.getDefaultCompiledLotoFilePath(printout));
//        FileUtils.writeByteArrayToFile(printoutFile, fileData);

        return fileData;
    }

    public byte[] downloadCustomLotoPrintout(LotoPrintout printout) throws IOException {
        File printoutFile = null;

        byte[] fileData = downloadLoto(PathHandler.getCompiledLotoFilePath(printout));
//        FileUtils.writeByteArrayToFile(printoutFile, fileData);

        return fileData;
    }

    @Deprecated
    public void saveLotoPrintout(File file, String path) {
        //path = path + "/";
        putObjectInLotoBucket(path, file);
        //uploadResource(file, null, path);
    }

    private byte[] downloadLoto(String path) throws IOException {
        InputStream resourceInput = null;
        try {
            S3Object resource = getLotoObject(path);
            resourceInput = resource.getObjectContent();
            byte[] resourceContent = IOUtils.toByteArray(resourceInput);
            return resourceContent;
        } catch (AmazonS3Exception e) {
            return handleAmazonS3Exception(e, (byte[]) null);
        } finally {
            IOUtils.closeQuietly(resourceInput);
        }
    }

    private S3Object getLotoObject(String path) {
        return getClient().getObject(LOTO_BUCKET, getLotoFolder() + "/" + path);
    }

    private PutObjectResult putObjectInLotoBucket(String path, File file) {
        PutObjectResult result = getClient().putObject(LOTO_BUCKET, getLotoFolder() + "/" + path, file);
        return result;
    }

    //---------------------------------------------------------------------------------



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

    @Deprecated
    public void uploadProcedureDefinitionSvg(ProcedureDefinition procedureDefinition, File svgFile) {


        uploadResource(svgFile, procedureDefinition.getTenant().getId(),
                PROCEDURE_DEFINITION_IMAGE_PATH,
                procedureDefinition.getAsset().getId(),
                procedureDefinition.getId(),
                svgFile.getName());
    }

    public void uploadProcedureDefinitionSvg(ProcedureDefinition procedureDefinition, byte[] fileContents, String fileName) {
        uploadResource(fileContents,
                        //This is the MIME type for SVG images.  We can statically place this here since this is the
                        //only type we're dealing with... you know... because this method is for SVGs.
                        "image/svg+xml",
                        procedureDefinition.getTenant().getId(),
                        PROCEDURE_DEFINITION_IMAGE_PATH,
                        procedureDefinition.getAsset().getId(),
                        procedureDefinition.getId(),
                        fileName);
    }

    public byte[] downloadProcedureDefinitionArrowImageSvg(ProcedureDefinitionImage image, int index) throws IOException {
        return downloadResource(getCurrentTenant().getId(), PROCEDURE_DEFINITION_IMAGE_PATH,
                image.getProcedureDefinition().getAsset().getId(),
                image.getProcedureDefinition().getId(),
                (image.getAnnotations().get(index).getImage().getFileName() + "_" + image.getAnnotations().get(index).getID() + ".svg"));
    }

    public byte[] downloadProcedureDefinitionImageSvg(ProcedureDefinitionImage image) throws IOException {
        return downloadResource(getCurrentTenant().getId(), PROCEDURE_DEFINITION_IMAGE_PATH,
                image.getProcedureDefinition().getAsset().getId(),
                image.getProcedureDefinition().getId(),
                (image.getFileName() + ".svg"));
    }

    public byte[] downloadProcedureDefinitionImageSvg(ProcedureDefinitionImage image, IsolationPoint isolationPoint) throws IOException {
        return downloadResource(getCurrentTenant().getId(), PROCEDURE_DEFINITION_IMAGE_PATH,
                image.getProcedureDefinition().getAsset().getId(),
                image.getProcedureDefinition().getId(),
                (image.getFileName() + "_" + isolationPoint.getAnnotation().getId() + ".svg"));
    }

    public byte[] downloadProcedureDefinitionImageSvg(ProcedureDefinition procedureDefinition, String filename, IsolationPoint isolationPoint) throws IOException {
        return downloadResource(getCurrentTenant().getId(), PROCEDURE_DEFINITION_IMAGE_PATH,
                procedureDefinition.getAsset().getId(),
                procedureDefinition.getId(),
                (filename + "_" + isolationPoint.getAnnotation().getId() + ".svg"));
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

    public void finalizeMultiCriteriaResultImageUpload(CriteriaResultImage criteriaResultImage) {
        copyTemporaryCriteriaImageToFinal(criteriaResultImage, CRITERIA_RESULT_IMAGE_TEMP, CRITERIA_RESULT_IMAGE_PATH_ORIG);
        copyTemporaryCriteriaImageToFinal(criteriaResultImage, CRITERIA_RESULT_MEDIUM_IMAGE_TEMP, CRITERIA_RESULT_IMAGE_PATH_MEDIUM);
        copyTemporaryCriteriaImageToFinal(criteriaResultImage, CRITERIA_RESULT_THUMB_IMAGE_TEMP, CRITERIA_RESULT_IMAGE_PATH_THUMB);
    }

    public void removeTempAfterMassUpload(CriteriaResultImage criteriaResultImage) {
        Long tenantId = criteriaResultImage.getCriteriaResult().getTenant().getId();
        String tempFileName = criteriaResultImage.getTempFileName();

        removeResource(tenantId, CRITERIA_RESULT_IMAGE_TEMP, tempFileName);
        removeResource(tenantId, CRITERIA_RESULT_MEDIUM_IMAGE_TEMP, tempFileName);
        removeResource(tenantId, CRITERIA_RESULT_THUMB_IMAGE_TEMP, tempFileName);
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

    public byte[] downloadProcedureDefinitionImage(ProcedureDefinitionImage procedureDefinitionImage) throws IOException {
        return downloadResource(procedureDefinitionImage.getProcedureDefinition().getTenant().getId(),
                PROCEDURE_DEFINITION_IMAGE_PATH,
                procedureDefinitionImage.getProcedureDefinition().getAsset().getId(),
                procedureDefinitionImage.getProcedureDefinition().getId(),
                procedureDefinitionImage.getFileName()
        );
    }

    public URL getPlaceAttachment(BaseOrg org, Attachment attachment) {
        return generateResourceUrl(org.getTenant().getId(), attachment.getFileName(), org.getId());
    }

    public String getCriteriaResultImageMediumPath(CriteriaResultImage criteriaResultImage) {
        return createResourcePath(null, CRITERIA_RESULT_IMAGE_PATH_MEDIUM,
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

    private List<S3ObjectSummary> findResources(String basePath, String matchingRegex) {
        List<S3ObjectSummary> resources = getSummariesFromObjectListing(getClient().listObjects(getBucket(), basePath));
        List<S3ObjectSummary> filteredResources = (matchingRegex != null) ? resources.stream().filter(s -> s.getKey().matches(matchingRegex)).collect(Collectors.toList()) : resources;
        return filteredResources;
    }

    private List<S3ObjectSummary> getSummariesFromObjectListing(ObjectListing listing) {
        List<S3ObjectSummary> paths = listing.getObjectSummaries();

        if (listing.isTruncated()) {
            paths.addAll(getSummariesFromObjectListing(getClient().listNextBatchOfObjects(listing)));
        }

        return paths;
    }

    private void uploadResource(File file, Long tenantId, String path, Object...pathArgs) {
        PutObjectResult result = putObject(createResourcePath(tenantId, path, pathArgs), file);
        logger.debug("Resource Upload Result: " + result.toString());
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
            return IOUtils.toByteArray(resourceInput);
        } catch (AmazonS3Exception e) {
            //We should be logging errors when they happen.  Handling things quietly is a bad idea.
            logger.error("Error processing file contents!!", e);
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

    private void deleteLotoObject(String path) {

        //Delete the items in the folder first
        List<S3ObjectSummary> objectSummaryList = getClient().listObjects(LOTO_BUCKET, getLotoFolder() + "/" + path).getObjectSummaries();
        for(S3ObjectSummary summary: objectSummaryList) {
            if(summary.getSize() > 0) {
                getClient().deleteObject(LOTO_BUCKET, summary.getKey());
            }
        }

        //delete the folder
        getClient().deleteObject(LOTO_BUCKET, getLotoFolder() + "/" + path);
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

    private String getLotoFolder() {
        String bucket = configService.getString(ConfigEntry.AMAZON_S3_LOTO_REPORTS);
        return bucket;
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
        String bucketHostname = configService.getString(ConfigEntry.AMAZON_S3_ENDPOINT);
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

    public String getAssetAttachmentContentType(AssetAttachment assetAttachment) {
        ObjectMetadata meta = getObjectMetadata(getAssetAttachmentPath(assetAttachment.getAsset().getMobileGUID(), assetAttachment.getMobileId(), assetAttachment.getFileName()));
        return (meta != null) ? meta.getContentType() : null;
    }

    public URL getAssetAttachmentUrl(AssetAttachment assetAttachment){
        URL assetAttachmentUrl = getAssetAttachmentUrl(assetAttachment.getAsset().getMobileGUID(), assetAttachment.getMobileId(), assetAttachment.getFileName());
        return assetAttachmentUrl;
    }

    public URL getAssetAttachmentUrl(String assetUuid, String assetAttachmentUuid, String assetAttachmentFilename) {
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
        return downloadResource(null, ASSET_PROOFTESTS_CHART_PATH, assetUuid, eventUuid);
    }

    public String getFileAttachmentPath(FileAttachment fileAttachment){
        String fileName = fileAttachment.getFileName().substring(fileAttachment.getFileName().lastIndexOf('/') + 1);
        String fileAttachmentPath = getFileAttachmentPath(fileAttachment.getMobileId(), fileName);
        return fileAttachmentPath;
    }

    public String getFileAttachmentPath(String fileAttachmentUuid, String filename){
        String fileAttachmentsUploadPath = createResourcePath(null, FILE_ATTACHMENT_PATH, fileAttachmentUuid, filename);
        return fileAttachmentsUploadPath;
    }

    public String getFileAttachmentContentType(FileAttachment fileAttachment){
        ObjectMetadata meta = getObjectMetadata(getFileAttachmentPath(fileAttachment));
        return (meta != null) ? meta.getContentType() : null;
    }

    public URL getFileAttachmentUrl(FileAttachment fileAttachment){
        String fileName = fileAttachment.getFileName().substring(fileAttachment.getFileName().lastIndexOf('/') + 1);
        URL fileAttachmentUrl = getFileAttachmentUrl(fileAttachment.getMobileId(), fileName);
        return fileAttachmentUrl;
    }

    /**
     * When running email notification tasks, we're effectively operating outside of any Tenant or User context, but
     * occasionally still need to access information specific to a Tenant. Since we can safely assume that we are
     * working within a Tenant, due to the Tenant implied from the FileAttachment object, we will just use that Tenant
     * to configure the Tenant Security Filter.
     *
     * @param fileAttachment - A FileAttachment entity for which you want the S3 URL.
     * @return A URL object pointing to the FileAttachment in S3.
     */
    public URL getFileAttachmentUrlForImpliedTenant(FileAttachment fileAttachment) {
        securityContext.setTenantSecurityFilter(new TenantOnlySecurityFilter(fileAttachment.getTenant()));
        URL returnMe = getFileAttachmentUrl(fileAttachment);
        securityContext.reset();
        return returnMe;
    }

    public URL getFileAttachmentUrl(String fileAttachmentUuid, String filename) {
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

            String bucketPolicySignature = Base64.getEncoder().encodeToString(hmac.doFinal(bucketPolicyBase64.getBytes("UTF-8"))).replaceAll("\n", "");
            return bucketPolicySignature;
        }
        catch(Exception e){
            logger.warn(e);
            return "";
        }
    }

    public String getBucketPolicyBase64(){
        try {
            return Base64.getEncoder().encodeToString(
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
