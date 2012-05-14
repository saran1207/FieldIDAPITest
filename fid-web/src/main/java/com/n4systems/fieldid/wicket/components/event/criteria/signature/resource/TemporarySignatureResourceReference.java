package com.n4systems.fieldid.wicket.components.event.criteria.signature.resource;

import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;

public class TemporarySignatureResourceReference extends ResourceReference {

    public TemporarySignatureResourceReference() {
        super(TemporarySignatureResourceReference.class, "temporarySignature");
    }

    @Override
    public IResource getResource() {
        return new TemporarySignatureResource();
    }

}
