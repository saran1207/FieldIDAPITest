package com.n4systems.reporting.data;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * This is just a temporary container to make sure the report is working... since it was built with an ass-backward
 * architecture, we need this to temporarily hold stuff... key here is TEMPORARY.
 *
 * Created by Jordan Heath on 14-11-05.
 */
public class ImageShortPrintoutBaseContainer {

    private List<ImageShortPrintoutContainer> images;

    public ImageShortPrintoutBaseContainer() {
        images = Lists.newArrayList();
    }

    public List<ImageShortPrintoutContainer> getImages() {
        return images;
    }

    public void setImages(List<ImageShortPrintoutContainer> images) {
        this.images = images;
    }
}
