package com.n4systems.fieldid.wicket.components.event.criteria.signature.resource;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.model.Tenant;
import com.n4systems.services.signature.SignatureService;
import org.apache.log4j.Logger;
import org.apache.wicket.request.resource.DynamicImageResource;

import java.io.IOException;

public class SignatureResource extends DynamicImageResource {

    private static final Logger logger = Logger.getLogger(SignatureResource.class);

    @Override
    protected byte[] getImageData(Attributes attributes) {
        Long eventId = attributes.getParameters().get("eventId").toLong();
        Long criteriaId = attributes.getParameters().get("criteriaId").toLong();

        Tenant tenant = FieldIDSession.get().getSessionUser().getTenant();

        if (tenant == null) {
            return null;
        }

        try {
            return new SignatureService().loadSignatureImage(tenant, eventId, criteriaId);
        } catch (IOException e) {
            logger.info("error loading signature for resource", e);
            return null;
        }
    }

}
