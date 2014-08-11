package com.n4systems.fieldid.wicket.components.event.criteria.signature.resource;

import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.model.Tenant;
import com.n4systems.services.signature.SignatureService;
import com.n4systems.util.ServiceLocator;
import org.apache.log4j.Logger;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.eclipse.jdt.internal.core.Assert;
import org.springframework.beans.factory.annotation.Autowired;

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
            return ServiceLocator.getSignatureService().loadSignatureImage(tenant, eventId, criteriaId);
        } catch (IOException e) {
            logger.info("error loading signature for resource", e);
            return null;
        }
    }

    @Override
    protected ResourceResponse newResourceResponse(Attributes attributes) {
        ResourceResponse resourceResponse = super.newResourceResponse(attributes);
        resourceResponse.disableCaching();
        return resourceResponse;
    }
}
