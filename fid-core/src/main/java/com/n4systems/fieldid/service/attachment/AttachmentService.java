package com.n4systems.fieldid.service.attachment;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.Attachment;
import com.n4systems.model.asset.AssetAttachment;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.util.persistence.QueryBuilder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public class AttachmentService extends FieldIdPersistenceService {

    public List<? extends Attachment> getAttachmentsFor(BaseOrg org) {
        // TODO DD : placeholder code for now until data is hooked up correctly.
        QueryBuilder<AssetAttachment> builder = createUserSecurityBuilder(AssetAttachment.class);
        builder.setLimit(10);
        System.out.println("FIX THIS TO RETURN CORRECT ATTACHMENTS");
        return persistenceService.findAll(builder);
    }

}
