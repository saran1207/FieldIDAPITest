package com.n4systems.fieldid.migrator.migrations;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.n4systems.fieldid.migrator.DbUtils;
import com.n4systems.fieldid.migrator.Migration;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.images.ImageService;
import com.n4systems.util.ConfigEntry;
import org.apache.commons.io.FileUtils;

import javax.activation.MimetypesFileTypeMap;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class M201212211056_MigrateAssetProfileImagesToS3 extends Migration {
    @Override
    public void up(Connection conn) throws Exception {

        PreparedStatement pstmt = null;
        try {
            String bucket = getBucket(conn);
            AmazonS3Client s3Client = getAmazonS3Client(getSystemProtocol(conn));
            ImageService imageService = new ImageService();

            logger.info("Bucket: " + bucket);
            
            pstmt = conn.prepareStatement("SELECT *, DATE_FORMAT(a.created, '%y/%m') as createdDir from assets a join tenants t on t.id = a.tenant_id where a.imageName is not null");
            ResultSet resultSet = pstmt.executeQuery();

            int uploadedImages = 0;
            while(resultSet.next()) {
                String filePath = "/var/fieldid/private/products/images/" + resultSet.getString("t.name")
                        + "/" + resultSet.getString("createdDir")
                        + "/" + resultSet.getLong("a.id")
                        + "/" + resultSet.getString("a.imageName");
                
                String remotePath = "tenants/" + resultSet.getLong("t.id")
                        + "/assets/" + resultSet.getLong("a.id")
                        + "/profile/" + resultSet.getString("a.imageName");

                File image = new File(filePath);
                
                if(image.exists()) {
                    logger.info("Uploading: " + remotePath);
                    uploadedImages++;
                    
                    byte[] imageData = FileUtils.readFileToByteArray(image);
                    String contentType = new MimetypesFileTypeMap().getContentType(image);
                    byte[] thumbnailImage = imageService.generateThumbnail(imageData);
                    byte[] mediumImage = imageService.generateMedium(imageData);

                    s3Client.putObject(bucket, remotePath, image);
                    s3Client.putObject(new PutObjectRequest(bucket, remotePath + S3Service.THUMBNAIL_EXTENSION, new ByteArrayInputStream(thumbnailImage), getObjectMetadata(thumbnailImage, contentType)));
                    s3Client.putObject(new PutObjectRequest(bucket, remotePath + S3Service.MEDIUM_EXTENSION, new ByteArrayInputStream(mediumImage), getObjectMetadata(mediumImage, contentType)));
                }
            }
            resultSet.last();
            logger.info("Uploaded " + uploadedImages + " out of " + resultSet.getRow());
        } finally {
            DbUtils.close(pstmt);
        }
    }

    private ObjectMetadata getObjectMetadata(byte[] data, String contentType) {
        ObjectMetadata objectMeta = new ObjectMetadata();
		objectMeta.setContentLength(data.length);
		objectMeta.setContentType(contentType);
        return objectMeta;
    }

    private String getBucket(Connection conn) throws Exception {
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement("SELECT value from configurations WHERE identifier='AMAZON_S3_BUCKET'");
            ResultSet resultSet = pstmt.executeQuery();
            while(resultSet.next())
                return resultSet.getString("value");
        } finally {
            DbUtils.close(pstmt);
        }

        return ConfigEntry.AMAZON_S3_BUCKET.getDefaultValue();
    }

    private String getSystemProtocol(Connection conn) throws Exception {
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement("SELECT value from configurations WHERE identifier='SYSTEM_PROTOCOL'");
            ResultSet resultSet = pstmt.executeQuery();
            while(resultSet.next())
                return resultSet.getString("value");
        } finally {
            DbUtils.close(pstmt);
        }

        return ConfigEntry.SYSTEM_PROTOCOL.getDefaultValue();
    }

    private AmazonS3Client getAmazonS3Client(String systemProtocol) {
        String accessKeyId = ConfigEntry.AMAZON_ACCESS_KEY_ID.getDefaultValue();
        String secretAccessKey = ConfigEntry.AMAZON_SECRET_ACCESS_KEY.getDefaultValue();
        AWSCredentials credentials = new BasicAWSCredentials(accessKeyId, secretAccessKey);
        ClientConfiguration config = new ClientConfiguration();
        if ("https".equals(systemProtocol)) {
            config.setProtocol(Protocol.HTTPS);
        }
        return new AmazonS3Client(credentials, config);
    }
}
