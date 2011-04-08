package com.n4systems.services.signature;

import com.n4systems.model.SignatureCriteriaResult;
import com.n4systems.model.Tenant;
import com.n4systems.reporting.PathHandler;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class SignatureService {

    public String storeSignature(Long tenantId, byte[] pngData) throws Exception {
        String fileId = UUID.randomUUID().toString();
        String fileName = tenantId + "-" + fileId + ".png";
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

    public File getTemporarySignatureFile(Long tenantId, String fileId) {
        String fileName = tenantId + "-" + fileId + ".png";
        return PathHandler.getTempFileInRoot(fileName);
    }

    public File getSignatureFileFor(Tenant tenant, Long eventId, Long criteriaId) {
        File signatureDirectory = PathHandler.getEventSignatureDirectory(tenant, eventId);
        return new File(signatureDirectory, criteriaId + ".png");
    }

    public void copySignatureFilesForCriteriaResult(Tenant tenant, Long eventId, SignatureCriteriaResult result) {
        if (result.getTemporaryFileId() != null) {
            File temporarySignatureFile = getTemporarySignatureFile(tenant.getId(), result.getTemporaryFileId());
            File destinationFile = getSignatureFileFor(tenant, eventId, result.getCriteria().getId());
            try {
                FileUtils.copyFile(temporarySignatureFile, destinationFile);
            } catch (IOException e) {
                throw new RuntimeException("Unable to copy temporary signature file to permanent storage", e);
            }
            result.setSigned(true);
        }
    }
}
