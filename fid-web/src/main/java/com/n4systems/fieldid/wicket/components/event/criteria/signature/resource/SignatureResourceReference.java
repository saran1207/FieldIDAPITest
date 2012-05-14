package com.n4systems.fieldid.wicket.components.event.criteria.signature.resource;

import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;

public class SignatureResourceReference extends ResourceReference {

    public SignatureResourceReference() {
        super(SignatureResourceReference.class, "signature");
    }

    @Override
    public IResource getResource() {
        return new SignatureResource();
    }

}
