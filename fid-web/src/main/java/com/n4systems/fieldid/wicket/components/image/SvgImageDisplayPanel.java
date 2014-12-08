package com.n4systems.fieldid.wicket.components.image;

import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.model.common.ImageAnnotation;
import com.n4systems.model.common.ImageAnnotationType;
import com.n4systems.model.procedure.ProcedureDefinitionImage;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

/**
 * This component can be used to display SVG Images in the browser.
 *
 * It works by altering SVG image tags within the Markup Container, using AttributeModifiers.  This allows us to use the
 * same basic SVG frame for all Arrow Style SVGs.  This same method may not work as easily for Callout Style images,
 * specifically where more than one Annotation is expected for each image.
 *
 * Created by Jordan Heath on 14-12-03.
 */
public class SvgImageDisplayPanel extends Panel {

    @SpringBean
    private S3Service s3Service;

    private ProcedureDefinitionImage theImage;
    private ImageAnnotation theAnnotation;
    private Boolean showAnnotations = true;

    /**
     * This is the main constructor for the SvgImageDisplayPanel.  Since the S3 Service requires a full
     * ProcedureDefinitionImage to be able to acquire a URL for the image, the easiest solution is to just pass in
     * the whole image.  In addition to the image, we also need to be provided the ImageAnnotation object representing
     * the Annotation to be generated on top of the image.
     *
     * @param id - The <b>String</b> representation of the Wicket ID for the SvgImageDisplayPanel instance.
     * @param theAnnotation - The <b>ImageAnnotation</b> representing the Annotation to be drawn over the image.
     */
    public SvgImageDisplayPanel(String id, ImageAnnotation theAnnotation) {
        super(id);
        this.theAnnotation = theAnnotation;
        this.theImage = (ProcedureDefinitionImage) theAnnotation.getImage();
    }

    /**
     * In some situations, you may want to only display the image and hide the Annotation arrow, such as when you are
     * showing an image that has yet to have an annotation added to it.
     *
     * @param id The <b>String</b> represenation of the markup ID or some such thing for the SvgImageDisplayPanel.
     */
    public SvgImageDisplayPanel(String id) {
        super(id);
        this.showAnnotations = false;
    }

    public SvgImageDisplayPanel(String id, ProcedureDefinitionImage theImage) {
        super(id);
        this.theImage = theImage;
    }

    /**
     * This override of the onInitialize method simply ensures that the necessary components have their Attributes
     * modified to contain references to data from the ProcedureDefinitionImage and ImageAnnotation objects.
     */
    @Override
    protected void onInitialize() {
        super.onInitialize();

        URL imageUrl;
        if(theImage == null)
            try {
                imageUrl = new URL("file:///var/fieldid/images/loto/upload-lightbox-blank-slate.png");
            } catch (MalformedURLException e) {
                e.printStackTrace();
                imageUrl = null;
            }
        else
            imageUrl = s3Service.getProcedureDefinitionImageMediumURL(theImage);

        Dimension imageDimensions = acquireImageDimensions(imageUrl);

        add(new AttributeModifier("xmlns", "http://www.w3.org/2000/svg"));
        add(new AttributeModifier("xmlns:xlink", "http://www.w3.org/1999/xlink"));
        add(new AttributeModifier("version", "1.1"));
        add(new AttributeModifier("viewBox", "0 0 " + imageDimensions.getWidth() + " " + imageDimensions.getHeight()));
        add(new AttributeModifier("width", imageDimensions.getWidth()));
        add(new AttributeModifier("height", imageDimensions.getHeight()));

        WebMarkupContainer imageElement = new WebMarkupContainer("imageElement");
        //Holy metal, Batman! It's really this simple!!
        imageElement.add(new AttributeModifier("xlink:href", imageUrl));
        add(imageElement);


        WebMarkupContainer annotationsElement = new WebMarkupContainer("annotationsElement");
        annotationsElement.add(new AttributeModifier("visibility", showAnnotations ? "visible" : "hidden"));

        WebMarkupContainer lineElement = new WebMarkupContainer("lineElement");
        if(theAnnotation != null) {
            lineElement.add(new AttributeModifier("x1", String.valueOf(Math.round(imageDimensions.getWidth() * theAnnotation.getX()))));
            lineElement.add(new AttributeModifier("y1", String.valueOf(Math.round(imageDimensions.getHeight() * theAnnotation.getY()))));
            lineElement.add(new AttributeModifier("x2", String.valueOf(Math.round(imageDimensions.getWidth() * theAnnotation.getX_tail()))));
            lineElement.add(new AttributeModifier("y2", String.valueOf(Math.round(imageDimensions.getHeight() * theAnnotation.getY_tail()))));
        }

        annotationsElement.add(lineElement);
        add(annotationsElement);

    }

    /**
     * Call this method when initializing an SvgImageDisplayPanel instance to hide the annotation arrow.
     *
     * @return this
     */
    public SvgImageDisplayPanel withNoAnnotations() {
        this.showAnnotations = false;
        return this;
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
