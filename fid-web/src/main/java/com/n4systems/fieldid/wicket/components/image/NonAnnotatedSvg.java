package com.n4systems.fieldid.wicket.components.image;

import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.model.common.ImageAnnotation;
import com.n4systems.model.procedure.ProcedureDefinitionImage;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;

/**
 * This Panel holds a similar design to the two flavours of AnnotatedSvg panels, but lacks any actual annotations or
 * supporting JS for editing an annotation.  The purpose of this panel is for being used by non-annotated Isolation
 * Point images.  These can be mixed with both Call-Out style and Arrow Style annotations.
 *
 * Created by Jordan Heath on 2015-09-24.
 */
public class NonAnnotatedSvg extends Panel {
    @SpringBean
    protected S3Service s3Service;

    protected ProcedureDefinitionImage theImage;
    protected ImageAnnotation theAnnotation;

    private Dimension imageDimensions;
    private static String BLANK_SLATE_PATH = "/fieldid/images/loto/label-blank-slate.png";
    private static Dimension BLANK_SLATE_DIMENSIONS = new Dimension(177, 133);

    public NonAnnotatedSvg(String id) {
        super(id);
    }

    public NonAnnotatedSvg(String id, ImageAnnotation theAnnotation) {
        super(id);
        this.theAnnotation = theAnnotation;
        if(theAnnotation != null && theAnnotation.getImage() != null) {
            this.theImage = (ProcedureDefinitionImage)theAnnotation.getImage();
        }
    }

    public NonAnnotatedSvg(String id, IModel<ImageAnnotation> theAnnotation) {
        this(id, theAnnotation.getObject());
    }

    public NonAnnotatedSvg(String id, ProcedureDefinitionImage theImage) {
        super(id);
        this.theImage = theImage;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        URL imageURL;
        if(theImage != null) {
            imageURL = s3Service.getProcedureDefinitionImageMediumURL(theImage);
            imageDimensions = acquireImageDimensions(imageURL);

            if(imageDimensions == null) {
                imageURL = null;
                imageDimensions = BLANK_SLATE_DIMENSIONS;
            }
        } else {
            imageURL = null;
            imageDimensions = BLANK_SLATE_DIMENSIONS;
        }

        add(new AttributeModifier("xmlns", "http://www.w3.org/2000/svg"));
        add(new AttributeModifier("xmlns:xlink", "http://www.w3.org/1999/xlink"));
        add(new AttributeModifier("version", "1.1"));
        add(new AttributeModifier("viewBox", "0 0 " + imageDimensions.getWidth() + " " + imageDimensions.getHeight()));
        add(new AttributeModifier("width", imageDimensions.getWidth()));
        add(new AttributeModifier("height", imageDimensions.getHeight()));

        WebMarkupContainer imageElement = new WebMarkupContainer("imageElement");

        imageElement.add(new AttributeModifier("xlink:href", (imageURL == null ? BLANK_SLATE_PATH : imageURL)));

        imageElement.add(new AttributeModifier("height", imageDimensions.getHeight()));
        imageElement.add(new AttributeModifier("width", imageDimensions.getWidth()));

        add(imageElement);
    }

    /**
     * Determine the image size without having to download the whole image.  You can gather this from metadata in the
     * reader... I think, anyways.  Otherwise this is wasteful and there are far easier ways to get this data.
     *
     * @param imageUrl - URL for the image you need to acquire the dimensions of.
     * @return A <b>Dimension</b> object representing the dimensions of the specified image.
     */
    private Dimension acquireImageDimensions(URL imageUrl) {
        try {
            ImageInputStream in = ImageIO.createImageInputStream(imageUrl.openStream());

            final Iterator<ImageReader> readers = ImageIO.getImageReaders(in);
            if(readers.hasNext()) {
                ImageReader reader = readers.next();

                try {
                    reader.setInput(in);
                    return new Dimension(reader.getWidth(0), reader.getHeight(0));
                } finally {
                    in.close();
                }
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
