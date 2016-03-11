package com.n4systems.fieldid.service.attachment;


import com.n4systems.model.attachment.Attachment;

import java.net.URL;

public interface AttachmentHandler<T extends Attachment> {
    
    void upload(T attachment);
    void uploadTemp(T attachment);
    void finalize(T attachment);
    URL getUrl(T attachment);
    URL getUrl(T attachment, String  flavourRequest);
    int remove(T attachment);
    String getTempPath(T attachment);
}
