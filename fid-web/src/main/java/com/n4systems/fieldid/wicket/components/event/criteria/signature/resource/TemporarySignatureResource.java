package com.n4systems.fieldid.wicket.components.event.criteria.signature.resource;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.model.Tenant;
import com.n4systems.services.signature.SignatureService;
import org.apache.log4j.Logger;
import org.apache.wicket.request.resource.DynamicImageResource;

import java.io.IOException;

public class TemporarySignatureResource extends DynamicImageResource {

    private static final Logger logger = Logger.getLogger(TemporarySignatureResource.class);

    @Override
    protected byte[] getImageData(Attributes attributes) {
        String fileId = attributes.getParameters().get("fileId").toString();

        Tenant tenant = FieldIDSession.get().getSessionUser().getTenant();

        if (tenant == null) {
            return null;
        }

        try {
            return new SignatureService().loadSignatureImage(tenant.getId(), fileId);
        } catch (IOException e) {
            logger.info("error loading signature for resource", e);
            return null;
        }
    }

}
