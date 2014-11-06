package com.n4systems.reporting.data;

import java.io.File;

/**
 * This container is built just to hold the path to the image.  We should keep this POJO, however, because it will
 * make it easy to modify and add new fields to the image portion of the Short Printout.
 *
 * Created by Jordan Heath on 14-11-05.
 */
public class ImageShortPrintoutContainer {
    private File path;

    public File getPath() {
        return path;
    }

    public void setPath(File path) {
        this.path = path;
    }
}
