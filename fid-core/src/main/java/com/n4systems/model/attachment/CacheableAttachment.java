package com.n4systems.model.attachment;

import com.n4systems.fieldid.service.attachment.SupportedFlavour;

import java.util.Collection;

public interface CacheableAttachment {
    public Collection<SupportedFlavour> getFlavoursToInitiallyCache();
}
