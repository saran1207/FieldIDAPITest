package com.n4systems.services.signature;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import org.apache.commons.io.IOUtils;

import com.n4systems.model.SignatureCriteriaResult;
import com.n4systems.model.Tenant;
import com.n4systems.reporting.PathHandler;

public class SignatureService {

    public String storeSignature(Long tenantId, byte[] pngData) throws Exception {
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

	private String buildTempFileName(Long tenantId, String fileId) {
		String fileName = tenantId + "-" + fileId + ".png";
		return fileName;
	}

    public File getTemporarySignatureFile(Long tenantId, String fileId) {
        String fileName = buildTempFileName(tenantId, fileId);
        return PathHandler.getTempFileInRoot(fileName);
    }

    public File storeSignatureFileFor(SignatureCriteriaResult signatureResult) throws IOException {
    	File sigFile = getSignatureFileFor(signatureResult.getTenant(), signatureResult.getEvent().getId(), signatureResult.getCriteria().getId());
    	FileOutputStream out = null;
    	try {
    		if (!sigFile.getParentFile().exists()) {
    			sigFile.getParentFile().mkdirs();
    		}
    		
    		out = new FileOutputStream(sigFile);
            if (signatureResult.getImage() != null) {
                IOUtils.write(signatureResult.getImage(), out);
            } else if (signatureResult.getTemporaryFileId() != null) {
                IOUtils.copy(new FileInputStream(getTemporarySignatureFile(signatureResult.getTenant().getId(), signatureResult.getTemporaryFileId())), out);
            }

        } finally {
            IOUtils.closeQuietly(out);
        }
        return sigFile;
    }
    
    public File getSignatureFileFor(Tenant tenant, Long eventId, Long criteriaId) {
        File signatureDirectory = PathHandler.getEventSignatureDirectory(tenant, eventId);
        return new File(signatureDirectory, criteriaId + ".png");
    }
    
    public byte[] loadSignatureImage(Tenant tenant, Long eventId, Long criteriaId) throws IOException {
    	File sigFile = getSignatureFileFor(tenant, eventId, criteriaId);
    	byte[] imageBytes = loadSignatureImage(sigFile);
        return imageBytes;
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
