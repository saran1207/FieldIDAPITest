package com.n4systems.fieldid.wicket.components;

import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.model.common.EditableImage;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.net.URL;

public class ExternalS3Image extends ExternalImage {

    private @SpringBean S3Service s3Service;

    private String s3Path;

    public ExternalS3Image(String id, String s3Path) {
        super(id, s3Path);
        this.s3Path = s3Path;
    }

    public ExternalS3Image(String id, EditableImage image) {
        this(id, image.getFileName());
    }

    @Override
    protected String getImageUrl() {
        URL url = s3Service.generateResourceUrl(s3Path);
        if (url==null) {
            throw new IllegalArgumentException("can't find image for path '" + s3Path + "'");
        }
        return url.toString();
    }

}
