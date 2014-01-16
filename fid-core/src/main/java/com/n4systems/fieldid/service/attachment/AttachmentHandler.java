package com.n4systems.fieldid.service.attachment;


import com.n4systems.model.attachment.Attachment;

import java.net.URL;
import java.util.Collection;

public interface AttachmentHandler<T extends Attachment> {
    
    void upload(T attachment);
    void uploadTemp(T attachment);
    void finalize(T attachment);
    URL getUrl(T attachment);
    URL getUrl(T attachment, Class<? extends Flavour> flavour);
    Collection<URL> getUrls(T attachment);
    int remove(T attachment);
}
