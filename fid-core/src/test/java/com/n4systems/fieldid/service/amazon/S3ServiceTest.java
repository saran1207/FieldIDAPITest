package com.n4systems.fieldid.service.amazon;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.google.common.base.Preconditions;
import com.n4systems.fieldid.junit.FieldIdServiceTest;
import com.n4systems.fieldid.service.images.ImageService;
import com.n4systems.model.AbstractEvent;
import com.n4systems.model.Tenant;
import com.n4systems.model.TextFieldCriteriaResult;
import com.n4systems.model.criteriaresult.CriteriaResultImage;
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

    private SecurityFilter securityFilter;

    private int expiryDays = 1;
    private String bucket = "bucket";
    private Long tenantId = 678L;

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
                return new AbstractEvent() {
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




}
