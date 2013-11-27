package com.n4systems.fieldid.service.attachment;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.model.attachment.AbstractS3Attachment;
import com.n4systems.model.attachment.S3Attachment;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.util.persistence.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public class AttachmentService extends FieldIdPersistenceService {

    @Autowired private S3Service s3Service;

    public List<S3Attachment> getAttachmentsFor(BaseOrg org) {
        // TODO DD : placeholder code for now until data is hooked up correctly.
        QueryBuilder<S3Attachment> builder = createUserSecurityBuilder(S3Attachment.class);
        builder.setLimit(10);
        System.out.println("FIX THIS TO RETURN CORRECT ATTACHMENTS");
        return persistenceService.findAll(builder);
    }

    public void save(AbstractS3Attachment attachment) {
        persistenceService.save(attachment);
        s3Service.finalize(attachment);
    }

}
