package com.n4systems.fieldid.service.attachment;

import com.n4systems.fieldid.service.images.ImageService;
import com.n4systems.model.attachment.Attachment;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageFlavour extends Flavour<ImageOptions> {

    private @Autowired ImageService imageService;

    private static Pattern format = Pattern.compile("\\d+x\\d+");

    public ImageFlavour() {
        super();
    }

    @Override
    public ImageOptions isSupportedRequest(String... request) {
        // only accepts single strings in the format of "1000x500"
        // currently, no bounds checks are done.
        // TODO : allow for requests like ["image", "320x500" ]   or ["image", "jpg", "1000x800"]     (i.e. multiple strings in request).
        if (request.length!=1) return null;
        Matcher matcher = format.matcher(request[0]);
        if (matcher.matches()) {
            int width = new Integer(matcher.group(0));
            int height = new Integer(matcher.group(1));
            return new ImageOptions(width,height);
        }
        return null;
    }

    @Override
    public byte[] createBytes(byte[] bytes, ImageOptions options) {
         return imageService.generateImage(bytes,options);
    }

}
