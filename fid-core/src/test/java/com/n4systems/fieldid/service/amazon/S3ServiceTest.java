package com.n4systems.fieldid.service.amazon;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.google.common.base.Preconditions;
import com.n4systems.fieldid.junit.FieldIdServiceTest;
import com.n4systems.fieldid.service.images.ImageService;
import com.n4systems.fieldid.service.uuid.UUIDService;
import com.n4systems.model.AbstractEvent;
import com.n4systems.model.Tenant;
import com.n4systems.model.TextFieldCriteriaResult;
import com.n4systems.model.ThingEvent;
import com.n4systems.model.builders.AssetBuilder;
import com.n4systems.model.builders.ProcedureDefinitionBuilder;
import com.n4systems.model.criteriaresult.CriteriaResultImage;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.procedure.ProcedureDefinitionImage;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.services.ConfigService;
import com.n4systems.services.SecurityContext;
import com.n4systems.test.TestMock;
import com.n4systems.test.TestTarget;
import com.n4systems.util.ConfigEntry;
import org.apache.http.client.methods.HttpRequestBase;
import org.easymock.EasyMock;
import org.easymock.IArgumentMatcher;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;

public class S3ServiceTest extends FieldIdServiceTest {

    @TestTarget private S3Service s3Service;

    @TestMock private ConfigService configService;
    @TestMock private ImageService imageService;
    @TestMock private AmazonS3Client s3client;
    @TestMock private SecurityContext securityContext;
    @TestMock private UUIDService uuidService;

    private SecurityFilter securityFilter;

    private int expiryDays = 1;
    private String bucket = "bucket";
    private Long tenantId = 111L;

    @Override
    @Before
    public void setUp() {
        super.setUp();
        securityFilter = createMock(SecurityFilter.class);
    }

    @Override
    protected Object createSut(Field sutField) throws Exception {
        return new S3Service() {
            @Override protected int getExpiryInDays() {
                return expiryDays;
            }

            @Override
            protected Tenant getCurrentTenant() {
                return new Tenant(tenantId,"test");
            }

        };
    }

    @Test
    public void test_getBrandingLogo() throws MalformedURLException {
        Long myTenantId = 123L;

        URL expectedUrl = new URL("http://somebogusurl.com");

        Date expires =  new DateTime().plusDays(expiryDays).toDate();

        expect(configService.getString(ConfigEntry.AMAZON_S3_BUCKET)).andReturn(bucket);
        replay(configService);

        expect(s3client.generatePresignedUrl(bucket, S3Service.TENANTS_PREFIX + myTenantId + S3Service.BRANDING_LOGO_PATH, expires, HttpMethod.GET)).andReturn(expectedUrl);
        replay(s3client);

        URL result = s3Service.getBrandingLogoURL(myTenantId);

        assertEquals(expectedUrl, result);

        verifyTestMocks();
    }

    @Test
    public void test_getBrandingLogo_no_tenant() throws MalformedURLException {
        URL expectedUrl = new URL("http://expected.com");

        Date expires =  new DateTime().plusDays(expiryDays).toDate();

        expect(securityContext.getTenantSecurityFilter()).andReturn(securityFilter);
        replay(securityContext);

        expect(securityFilter.getTenantId()).andReturn(tenantId);
        replay(securityFilter);

        expect(configService.getString(ConfigEntry.AMAZON_S3_BUCKET)).andReturn(bucket);
        replay(configService);

        expect(s3client.generatePresignedUrl(bucket, S3Service.TENANTS_PREFIX + tenantId + S3Service.BRANDING_LOGO_PATH, expires, HttpMethod.GET) ).andReturn(expectedUrl);
        replay(s3client);

        URL result = s3Service.getBrandingLogoURL();

        assertEquals(expectedUrl, result);

        verifyTestMocks();

    }

    @Test
    public void test_uploadBrandingLogo() {
        File file = new File("/pics/image.png");

        expect(securityContext.getTenantSecurityFilter()).andReturn(securityFilter);
        replay(securityContext);

        expect(securityFilter.getTenantId()).andReturn(tenantId);
        replay(securityFilter);

        expect(configService.getString(ConfigEntry.AMAZON_S3_BUCKET)).andReturn(bucket);
        replay(configService);

        expect(s3client.putObject(bucket, S3Service.TENANTS_PREFIX + tenantId + S3Service.BRANDING_LOGO_PATH, file)).andReturn(new PutObjectResult()/*ignored*/);
        replay(s3client);

        s3Service.uploadBrandingLogo(file);

        verifyTestMocks();

    }

    @Test
    public void test_uploadDefaultBrandingLogo() {
        Long tenantId = 84L;

        String sourcePath = S3Service.DEFAULT_BRANDING_LOGO_PATH;
        String destPath = S3Service.TENANTS_PREFIX + tenantId + S3Service.BRANDING_LOGO_PATH;

        expect(configService.getString(ConfigEntry.AMAZON_S3_BUCKET)).andReturn(bucket);
        replay(configService);

        expect(s3client.copyObject(bucket, sourcePath, bucket, destPath)).andReturn(new CopyObjectResult()/*ignored*/);
        replay(s3client);

        s3Service.uploadDefaultBrandingLogo(tenantId);

        verifyTestMocks();

    }

    @Test
    public void test_uploadCriteriaResultImage() {
        final byte[] imageData = "data".getBytes();
        final Long resultId = 22L;
        final Long eventId = 38L;
        final Long tenantId = 73L;
        String fileName = "image.png";
        String contentType = "png";
        String tempFileName = "0fea31509bc";

        TextFieldCriteriaResult result = new TextFieldCriteriaResult() {
            @Override public Tenant getTenant() {
                Tenant tenant = new Tenant();
                tenant.setID(tenantId);
                return tenant;
            }

            @Override public Long getId() {
                return resultId;
            }

            @Override public AbstractEvent getEvent() {
                return new ThingEvent() {
                    @Override public Long getId() {
                        return eventId;
                    }
                };
            }
        };

        final CriteriaResultImage criteriaResultImage = new CriteriaResultImage();
        criteriaResultImage.setTempFileName(tempFileName);
        criteriaResultImage.setFileName(fileName);
        criteriaResultImage.setContentType(contentType);
        criteriaResultImage.setCriteriaResult(result);

        expect(configService.getString(ConfigEntry.AMAZON_S3_BUCKET)).andReturn(bucket).anyTimes();
        replay(configService);

        String key4 = String.format(S3Service.TENANTS_PREFIX + tenantId + S3Service.CRITERIA_RESULT_IMAGE_TEMP, tempFileName);
        String key5 = String.format(S3Service.TENANTS_PREFIX + tenantId + S3Service.CRITERIA_RESULT_MEDIUM_IMAGE_TEMP, tempFileName);
        String key6 = String.format(S3Service.TENANTS_PREFIX + tenantId + S3Service.CRITERIA_RESULT_THUMB_IMAGE_TEMP, tempFileName);

        S3Object s3Object = createMock(S3Object.class);
        S3ObjectInputStream s3ObjectInputStream = new S3ObjectInputStream(new ByteArrayInputStream(imageData), null);
        expect(s3Object.getObjectContent()).andReturn(s3ObjectInputStream);

        expect(s3client.copyObject(anyObject(CopyObjectRequest.class))).andReturn(new CopyObjectResult()).times(3);

        /*expect*/s3client.deleteObject(bucket, key4);
        /*expect*/s3client.deleteObject(bucket, key5);
        /*expect*/s3client.deleteObject(bucket, key6);
        replay(s3client, s3Object);

        s3Service.finalizeCriteriaResultImageUpload(criteriaResultImage);

        verifyTestMocks();

    }

    @Test
    public void test_downloadCustomerLogo() throws IOException {
        final S3ObjectInputStream content = createMock(S3ObjectInputStream.class);
        Long orgId = 12L;

        S3Object s3Object = createMock(S3Object.class);
        byte[] data = "data".getBytes();
        S3ObjectInputStream s3ObjectInputStream = new S3ObjectInputStream(new ByteArrayInputStream(data), (HttpRequestBase)null);

        String path = S3Service.TENANTS_PREFIX + tenantId + String.format(S3Service.CUSTOMER_LOGO_PATH,orgId);

        expect(securityContext.getTenantSecurityFilter()).andReturn(securityFilter);
        replay(securityContext);

        expect(securityFilter.getTenantId()).andReturn(tenantId);
        replay(securityFilter);

        expect(configService.getString(ConfigEntry.AMAZON_S3_BUCKET)).andReturn(bucket);
        replay(configService);

        expect(s3client.getObject(bucket, path)).andReturn(s3Object);
        replay(s3client);

        expect(s3Object.getObjectContent()).andReturn(s3ObjectInputStream);
        replay(s3Object);

        s3Service.downloadCustomerLogo(orgId);

        verifyTestMocks();

    }

    @Test
    public void test_uploadImage() {
        byte[] data = "data".getBytes();
        byte[] thumbnail = "thumbnail".getBytes();
        byte[] medium = "medium".getBytes();
        String contentType = "png";
        String path = "/my/custom/path/image.png";
        Long tenantId = 574L;

        String path1 = S3Service.TENANTS_PREFIX + tenantId + path;
        String path2 = path1+S3Service.THUMBNAIL_EXTENSION;
        String path3 = path1+S3Service.MEDIUM_EXTENSION;
        expect(s3client.putObject(PutObjectRequestMatcher.eq(bucket,path1,data.length,contentType))).andReturn(new PutObjectResult()/*ignored*/);
        expect(s3client.putObject(PutObjectRequestMatcher.eq(bucket,path2,thumbnail.length,contentType))).andReturn(new PutObjectResult()/*ignored*/);
        expect(s3client.putObject(PutObjectRequestMatcher.eq(bucket,path3,medium.length,contentType))).andReturn(new PutObjectResult()/*ignored*/);
        replay(s3client);

        expect(configService.getString(ConfigEntry.AMAZON_S3_BUCKET)).andReturn(bucket).times(3);
        replay(configService);

        expect(imageService.generateMedium(data)).andReturn(medium);
        expect(imageService.generateThumbnail(data)).andReturn(thumbnail);
        replay(imageService);

        S3Service.S3ImagePath result = s3Service.uploadImage(data, contentType, path, tenantId);

        verifyTestMocks();
    }


    @Test
    public void test_uploadTempProcedureDefImage() {

        byte[] data = "data".getBytes();
        byte[] thumbnail = "thumbnail".getBytes();
        byte[] medium = "medium".getBytes();

        String contentType="contentType";

        expect(imageService.generateMedium(data)).andReturn(medium);
        expect(imageService.generateThumbnail(data)).andReturn(thumbnail);
        replay(imageService);

        String tempFileName = "uuid";
        expect(uuidService.createUuid()).andReturn(tempFileName);
        replay(uuidService);

        expect(s3client.putObject(PutObjectRequestMatcher.eq(bucket,tenantId, data.length,contentType,S3Service.PROCEDURE_DEFINITION_IMAGE_TEMP,tempFileName))).andReturn(new PutObjectResult()/*ignored*/);
        expect(s3client.putObject(PutObjectRequestMatcher.eq(bucket,tenantId, medium.length,contentType,S3Service.PROCEDURE_DEFINITION_IMAGE_TEMP_MEDIUM,tempFileName))).andReturn(new PutObjectResult()/*ignored*/);
        expect(s3client.putObject(PutObjectRequestMatcher.eq(bucket,tenantId, thumbnail.length,contentType,S3Service.PROCEDURE_DEFINITION_IMAGE_TEMP_THUMB,tempFileName))).andReturn(new PutObjectResult()/*ignored*/);
        replay(s3client);

        expect(configService.getString(ConfigEntry.AMAZON_S3_BUCKET)).andReturn(bucket).times(3);
        replay(configService);

        ProcedureDefinitionImage image = new ProcedureDefinitionImage();
        s3Service.uploadTempProcedureDefImage(image, contentType, data);

        assertEquals(tempFileName,image.getTempFileName());
        assertEquals(contentType, image.getContentType());
    }

    @Test
    public void test_finalizeProcedureDefinitionImageUpload() {
        long assetId = 123L;
        long procedureDefinitionId = 456L;
        Tenant tenant = new Tenant(tenantId, "testTenant");
        String tempFileName = "tempFileName";
        String origFileName = "origFileName";
        ProcedureDefinition procedureDefinition = ProcedureDefinitionBuilder.aProcedureDefinition().withAsset(AssetBuilder.anAsset().withId(assetId).build()).withId(procedureDefinitionId).build();
        ProcedureDefinitionImage image = new ProcedureDefinitionImage();
        image.setProcedureDefinition(procedureDefinition);
        image.setTenant(tenant);
        image.setTempFileName(tempFileName);
        image.setFileName(origFileName);

        expect(configService.getString(ConfigEntry.AMAZON_S3_BUCKET)).andReturn(bucket).times(9);
        replay(configService);

        expect(s3client.copyObject(CopyObjectRequestMatcher.eq(bucket, tenantId, String.format(S3Service.PROCEDURE_DEFINITION_IMAGE_TEMP,tempFileName),String.format(S3Service.PROCEDURE_DEFINITION_IMAGE_PATH,assetId,procedureDefinitionId,origFileName)))).andReturn(new CopyObjectResult()/*ignored*/);
        expect(s3client.copyObject(CopyObjectRequestMatcher.eq(bucket, tenantId, String.format(S3Service.PROCEDURE_DEFINITION_IMAGE_TEMP_MEDIUM,tempFileName),String.format(S3Service.PROCEDURE_DEFINITION_IMAGE_PATH_MEDIUM,assetId,procedureDefinitionId,origFileName)))).andReturn(new CopyObjectResult()/*ignored*/);
        expect(s3client.copyObject(CopyObjectRequestMatcher.eq(bucket, tenantId, String.format(S3Service.PROCEDURE_DEFINITION_IMAGE_TEMP_THUMB,tempFileName),String.format(S3Service.PROCEDURE_DEFINITION_IMAGE_PATH_THUMB,assetId,procedureDefinitionId,origFileName)))).andReturn(new CopyObjectResult()/*ignored*/);

        s3client.deleteObject(bucket,String.format(S3Service.TENANTS_PREFIX+tenantId+S3Service.PROCEDURE_DEFINITION_IMAGE_TEMP_THUMB, tempFileName));
        s3client.deleteObject(bucket,String.format(S3Service.TENANTS_PREFIX+tenantId+S3Service.PROCEDURE_DEFINITION_IMAGE_TEMP_MEDIUM, tempFileName));
        s3client.deleteObject(bucket,String.format(S3Service.TENANTS_PREFIX+tenantId+S3Service.PROCEDURE_DEFINITION_IMAGE_TEMP, tempFileName));
        replay(s3client);

        s3Service.finalizeProcedureDefinitionImageUpload(image);
    }

    @Test
    public void test_copyProcedureDefImageToTemp() {
        long assetId = 123L;
        long procedureDefinitionId = 456L;
        Tenant tenant = new Tenant(tenantId, "testTenant");
        String tempFileName = "tempFileName";
        String origFileName = "origFileName";
        String contentType = "contentType";

        ProcedureDefinition procedureDefinition = ProcedureDefinitionBuilder.aProcedureDefinition().withAsset(AssetBuilder.anAsset().withId(assetId).build()).withId(procedureDefinitionId).build();
        ProcedureDefinitionImage from = new ProcedureDefinitionImage();
        from.setProcedureDefinition(procedureDefinition);
        from.setTenant(tenant);
        from.setTempFileName(tempFileName);
        from.setFileName(origFileName);

        ProcedureDefinitionImage to = new ProcedureDefinitionImage();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(contentType);

        String path = String.format(S3Service.TENANTS_PREFIX+tenantId+S3Service.PROCEDURE_DEFINITION_IMAGE_PATH,
                assetId,
                procedureDefinitionId,
                origFileName);
        expect(configService.getString(ConfigEntry.AMAZON_S3_BUCKET)).andReturn(bucket).times(9);
        replay(configService);

        expect(uuidService.createUuid()).andReturn(tempFileName);
        replay(uuidService);

        expect(s3client.copyObject(CopyObjectRequestMatcher.eq(bucket, tenantId, String.format(S3Service.PROCEDURE_DEFINITION_IMAGE_PATH, assetId, procedureDefinitionId, origFileName), String.format(S3Service.PROCEDURE_DEFINITION_IMAGE_TEMP,tempFileName)))).andReturn(new CopyObjectResult()/*ignored*/);
        expect(s3client.copyObject(CopyObjectRequestMatcher.eq(bucket, tenantId, String.format(S3Service.PROCEDURE_DEFINITION_IMAGE_PATH_MEDIUM, assetId, procedureDefinitionId, origFileName), String.format(S3Service.PROCEDURE_DEFINITION_IMAGE_TEMP_MEDIUM,tempFileName)))).andReturn(new CopyObjectResult()/*ignored*/);
        expect(s3client.copyObject(CopyObjectRequestMatcher.eq(bucket, tenantId, String.format(S3Service.PROCEDURE_DEFINITION_IMAGE_PATH_THUMB, assetId, procedureDefinitionId, origFileName), String.format(S3Service.PROCEDURE_DEFINITION_IMAGE_TEMP_THUMB,tempFileName)))).andReturn(new CopyObjectResult()/*ignored*/);

        expect(s3client.getObjectMetadata(bucket,path)).andReturn(metadata);

        replay(s3client);

        s3Service.copyProcedureDefImageToTemp(from, to);

        assertEquals(tempFileName,to.getTempFileName());
        assertEquals(contentType,to.getContentType());
    }

    //---------------------------------------------------------MATCHERS------------------------------------------------------------------------------

    static protected class PutObjectRequestMatcher implements IArgumentMatcher {

        private String bucket;
        private String key;
        private long length;
        private String contentType;

        public PutObjectRequestMatcher(String bucket, String key, long length, String contentType) {
            Preconditions.checkArgument(bucket!=null);
            Preconditions.checkArgument(key!=null);
            this.bucket = bucket;
            this.key = key;
            this.length = length;
            this.contentType = contentType;
        }

        public static PutObjectRequest eq(String bucket, Long tenantId, int length, String contentType, String path, String...args ) {
            String fullPath = S3Service.TENANTS_PREFIX+tenantId+String.format(path,args);
            EasyMock.reportMatcher(new PutObjectRequestMatcher(bucket,fullPath,length,contentType));
            return null;
        }

        public static final PutObjectRequest eq(String bucket, String key, long length, String contentType) {
            EasyMock.reportMatcher(new PutObjectRequestMatcher(bucket,key,length,contentType));
            return null;
        }

        @Override
        public void appendTo(StringBuffer buffer) {
            buffer.append("'").append(bucket).append("'").append(key);
        }

        @Override
        public boolean matches(Object argument) {
            if (!(argument instanceof PutObjectRequest) ) {
                return false;
            }
            PutObjectRequest actual = (PutObjectRequest) argument;
            boolean result = bucket.equals(actual.getBucketName()) &&
                    key.equals(actual.getKey()) &&
                    length == actual.getMetadata().getContentLength() &&
                    contentType.equals(actual.getMetadata().getContentType());
            return result;
        }

    }


    static protected class CopyObjectRequestMatcher implements IArgumentMatcher {

        private String bucket;
        private final String source;
        private final String dest;

        public CopyObjectRequestMatcher(String bucket, String source, String dest) {
            Preconditions.checkArgument(bucket!=null && source!=null && dest!=null);
            this.bucket = bucket;
            this.source = source;
            this.dest = dest;
        }

        public static CopyObjectRequest eq(String bucket, Long tenantId, String source, String dest) {
            String fullSourcePath = S3Service.TENANTS_PREFIX+tenantId+source;
            String fullDestPath = S3Service.TENANTS_PREFIX+tenantId+dest;
            EasyMock.reportMatcher(new CopyObjectRequestMatcher(bucket,fullSourcePath,fullDestPath));
            return null;
        }

        @Override
        public void appendTo(StringBuffer buffer) {
            buffer.append("'").append(bucket).append("'").append(source).append("-->").append(dest);
        }

        @Override
        public boolean matches(Object argument) {
            if (!(argument instanceof CopyObjectRequest) ) {
                return false;
            }
            CopyObjectRequest actual = (CopyObjectRequest) argument;
            boolean result = bucket.equals(actual.getSourceBucketName()) &&
                    bucket.equals(actual.getDestinationBucketName()) &&
                    source.equals(actual.getSourceKey()) &&
                    dest.equals(actual.getDestinationKey());
            return result;
        }

    }

}
