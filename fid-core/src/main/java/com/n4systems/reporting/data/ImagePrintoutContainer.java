package com.n4systems.reporting.data;

import java.io.InputStream;

/**
 * This container is built just to hold the path to the image.  We should keep this POJO, however, because it will
 * make it easy to modify and add new fields to the image portion of the Short Printout.
 *
 * Created by Jordan Heath on 14-11-05.
 */
public class ImagePrintoutContainer {
    private InputStream image;

    public InputStream getImage() {
        return image;
    }

    public void setImage(InputStream path) {
        this.image = path;
    }
}
