package com.n4systems.services.signature;


import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.model.SignatureCriteriaResult;
import com.n4systems.model.Tenant;
import com.n4systems.reporting.PathHandler;
import com.n4systems.services.date.DateService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;


public class SignatureService {

    @Autowired
    private S3Service s3Service;
    @Autowired
    private DateService dateService;

    public String storeTempSignature(Long tenantId, byte[] pngData) throws Exception {
        String fileId = UUID.randomUUID().toString();
        String fileName = buildTempFileName(tenantId, fileId);
        File file = PathHandler.getTempFileInRoot(fileName);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(pngData);
            fileOutputStream.flush();
        } finally {
            IOUtils.closeQuietly(fileOutputStream);
        }
        return fileId;
    }

    static private String buildTempFileName(Long tenantId, String fileId) {
		String fileName = tenantId + "-" + fileId + ".png";
		return fileName;
	}

    static public File getTemporarySignatureFile(Long tenantId, String fileId) {
        String fileName = buildTempFileName(tenantId, fileId);
        return PathHandler.getTempFileInRoot(fileName);
    }

    public void storeSignatureFileFor(SignatureCriteriaResult signatureResult) throws IOException {
            Assert.isTrue(signatureResult.getImage() != null || signatureResult.getTemporaryFileId() != null);
            if (signatureResult.getImage() != null) {
                s3Service.uploadEventSignatureData(signatureResult.getImage(), signatureResult);
            } else if (signatureResult.getTemporaryFileId() != null) {
                File temporarySignatureFile = getTemporarySignatureFile(signatureResult.getTenant().getId(), signatureResult.getTemporaryFileId());
                s3Service.uploadEventSignature(temporarySignatureFile, signatureResult);
            }
    }
    
    public File getSignatureFileFor(Tenant tenant, Long eventId, Long criteriaId) {
        File signatureDirectory = PathHandler.getEventSignatureDirectory(tenant, eventId);
        File sigFile = new File(signatureDirectory, criteriaId + ".png");
        if(sigFile.exists()){
            return sigFile;
        }
        else {
            return s3Service.downloadEventSignature(tenant.getId(), eventId, criteriaId);
        }
    }
    
    public byte[] loadSignatureImage(Tenant tenant, Long eventId, Long criteriaId) throws IOException {
    	File sigFile = getSignatureFileFor(tenant, eventId, criteriaId);
        if(sigFile.exists()){
    	    byte[] imageBytes = loadSignatureImage(sigFile);
            return imageBytes;
        }
        else {
            return s3Service.downloadEventSignatureBytes(tenant.getId(), eventId, criteriaId);
        }
    }
    
    public byte[] loadSignatureImage(Long tenantId, String fileId) throws IOException { 
    	File sigFile = getTemporarySignatureFile(tenantId, fileId);
    	byte[] imageBytes = loadSignatureImage(sigFile);
    	return imageBytes;
    }
    
    private byte[] loadSignatureImage(File signatureFile) throws IOException {
    	byte[] imageBytes;
    	FileInputStream in = null;
    	try {
    		in = new FileInputStream(signatureFile);
    		imageBytes = IOUtils.toByteArray(in);
    	} finally {
    		IOUtils.closeQuietly(in);
    	}
        return imageBytes;
    }
    
}
