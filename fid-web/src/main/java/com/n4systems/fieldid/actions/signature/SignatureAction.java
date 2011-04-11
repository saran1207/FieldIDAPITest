package com.n4systems.fieldid.actions.signature;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.downloaders.DownloadAction;
import com.n4systems.services.signature.SignatureService;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class SignatureAction extends DownloadAction {

    private static Logger logger = Logger.getLogger(SignatureAction.class);

    private String pngData;
    private String signatureFileId;
    private Long criteriaId;
    private Long eventId;
    private Integer criteriaCount;

    public SignatureAction(PersistenceManager persistenceManager) {
        super(persistenceManager);
    }

    public String doDownloadTemporary() throws Exception {
        File tempSignatureFile = new SignatureService().getTemporarySignatureFile(getTenantId(), signatureFileId);
        return sendFile(tempSignatureFile);
    }

    public String doDownload() throws Exception {
        File signatureFile = new SignatureService().getSignatureFileFor(getTenant(), eventId, criteriaId);
        return sendFile(signatureFile);
    }

    private String sendFile(File tempSignatureFile) throws IOException {
        setFileName(tempSignatureFile.getName());
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(tempSignatureFile);
            sendFile(inputStream);
        } finally {
            if (inputStream != null)
                IOUtils.closeQuietly(inputStream);
        }
        return null;
    }

    public String doSign() {
        return SUCCESS;
    }

    public String doStoreSignature() throws Exception {
        String pngBase64Data = pngData.substring(pngData.lastIndexOf(",") + 1);
        byte[] decodedBytes = new Base64().decode(pngBase64Data.getBytes());
        signatureFileId = new SignatureService().storeSignature(getTenantId(), decodedBytes);
        return SUCCESS;
    }

    public String doClearSignature() throws Exception {
        return SUCCESS;
    }

    public String getPngData() {
        return pngData;
    }

    public void setPngData(String pngData) {
        this.pngData = pngData;
    }

    public String getSignatureFileId() {
        return signatureFileId;
    }

    public void setSignatureFileId(String signatureFileId) {
        this.signatureFileId = signatureFileId;
    }

    public Long getCriteriaId() {
        return criteriaId;
    }

    public void setCriteriaId(Long criteriaId) {
        this.criteriaId = criteriaId;
    }

    public Integer getCriteriaCount() {
        return criteriaCount;
    }

    public void setCriteriaCount(Integer criteriaCount) {
        this.criteriaCount = criteriaCount;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }
}
