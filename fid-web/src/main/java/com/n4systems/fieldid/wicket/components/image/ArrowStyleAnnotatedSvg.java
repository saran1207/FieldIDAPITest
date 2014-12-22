package com.n4systems.fieldid.wicket.components.image;

import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.model.common.ImageAnnotation;
import com.n4systems.model.procedure.AnnotationType;
import com.n4systems.model.procedure.ProcedureDefinitionImage;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
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
public class ArrowStyleAnnotatedSvg extends Panel {

    @SpringBean
    protected S3Service s3Service;

    protected ProcedureDefinitionImage theImage;
    protected ImageAnnotation theAnnotation;
    protected Boolean showAnnotations = true;

    private Dimension imageDimensions;
    private static String BLANK_SLATE_PATH = "/fieldid/images/loto/label-blank-slate.png";
    private static Dimension BLANK_SLATE_DIMENSIONS = new Dimension(177, 133);

    private Double scale = 1.0;

    /**
     * This is the main constructor for the SvgImageDisplayPanel.  Since the S3 Service requires a full
     * ProcedureDefinitionImage to be able to acquire a URL for the image, the easiest solution is to just pass in
     * the whole image.  In addition to the image, we also need to be provided the ImageAnnotation object representing
     * the Annotation to be generated on top of the image.
     *
     * @param id - The <b>String</b> representation of the Wicket ID for the SvgImageDisplayPanel instance.
     * @param theAnnotation - The <b>ImageAnnotation</b> representing the Annotation to be drawn over the image.
     */
    public ArrowStyleAnnotatedSvg(String id, ImageAnnotation theAnnotation) {
        super(id);
        this.theAnnotation = theAnnotation;
        if(theAnnotation != null && theAnnotation.hasCoordinates(AnnotationType.ARROW_STYLE)) {
            this.theImage = (ProcedureDefinitionImage) theAnnotation.getImage();
        }
    }

    /**
     * This is the main constructor for the SvgImageDisplayPanel.  Since the S3 Service requires a full
     * ProcedureDefinitionImage to be able to acquire a URL for the image, the easiest solution is to just pass in
     * the whole image.  In addition to the image, we also need to be provided the ImageAnnotation object representing
     * the Annotation to be generated on top of the image.
     *
     * @param id - The <b>String</b> representation of the Wicket ID for the SvgImageDisplayPanel instance.
     * @param theAnnotation - The <b>ImageAnnotation</b> representing the Annotation to be drawn over the image.
     */
    public ArrowStyleAnnotatedSvg(String id, IModel<ImageAnnotation> theAnnotation) {
        super(id);
        this.theAnnotation = theAnnotation.getObject();
        if (theAnnotation.getObject() != null && theAnnotation.getObject().hasCoordinates(AnnotationType.ARROW_STYLE)) {
            this.theImage = (ProcedureDefinitionImage) theAnnotation.getObject().getImage();
        }
    }

    /**
     * In some situations, you may want to only display the image and hide the Annotation arrow, such as when you are
     * showing an image that has yet to have an annotation added to it.
     *
     * @param id The <b>String</b> represenation of the markup ID or some such thing for the SvgImageDisplayPanel.
     */
    public ArrowStyleAnnotatedSvg(String id) {
        super(id);
        this.showAnnotations = false;
    }

    public ArrowStyleAnnotatedSvg(String id, ProcedureDefinitionImage theImage) {
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
        if (theImage != null) {
            imageUrl = s3Service.getProcedureDefinitionImageMediumURL(theImage);
            imageDimensions = acquireImageDimensions(imageUrl);
        } else {
            imageUrl = null;
            imageDimensions = BLANK_SLATE_DIMENSIONS;
        }

        add(new AttributeModifier("xmlns", "http://www.w3.org/2000/svg"));
        add(new AttributeModifier("xmlns:xlink", "http://www.w3.org/1999/xlink"));
        add(new AttributeModifier("version", "1.1"));
        add(new AttributeModifier("viewBox", "0 0 " + imageDimensions.getWidth() + " " + imageDimensions.getHeight()));
        add(new AttributeModifier("width", imageDimensions.getWidth()));
        add(new AttributeModifier("height", imageDimensions.getHeight()));

        WebMarkupContainer imageElement = new WebMarkupContainer("imageElement");
        if (imageUrl != null) {
            imageElement.add(new AttributeModifier("xlink:href", imageUrl));
        } else {
            imageElement.add(new AttributeModifier("xlink:href", BLANK_SLATE_PATH));
        }

        imageElement.add(new AttributeAppender("height", imageDimensions.getHeight()));
        imageElement.add(new AttributeAppender("width", imageDimensions.getWidth()));

        add(imageElement);

        WebMarkupContainer lineElement = new WebMarkupContainer("lineElement");
        WebMarkupContainer arrowElement = new WebMarkupContainer("arrowElement");
        if(theAnnotation != null && theAnnotation.hasCoordinates(AnnotationType.ARROW_STYLE)) {
            lineElement.add(new AttributeModifier("x1", String.valueOf(Math.round(imageDimensions.getWidth() * theAnnotation.getX()))));
            lineElement.add(new AttributeModifier("y1", String.valueOf(Math.round(imageDimensions.getHeight() * theAnnotation.getY()))));
            lineElement.add(new AttributeModifier("x2", String.valueOf(Math.round(imageDimensions.getWidth() * theAnnotation.getX_tail()))));
            lineElement.add(new AttributeModifier("y2", String.valueOf(Math.round(imageDimensions.getHeight() * theAnnotation.getY_tail()))));
            lineElement.add(new AttributeModifier("stroke-width", 5 * scale));

            arrowElement.add(new AttributeModifier("points", getArrowHeadPoints(imageDimensions, theAnnotation)));


        } else {
            showAnnotations = false;
        }

        lineElement.setVisible(showAnnotations);
        arrowElement.setVisible(showAnnotations);

        add(lineElement);
        add(arrowElement);
    }

    //Arrow styling constants
    private static double HEAD_OFFSET = 5.0;
    private static double HEAD_LENGTH = 22.0;
    private static double HEAD_INDENT = 18.0;
    private static double HEAD_WIDTH = 12.0;

    private String getArrowHeadPoints(Dimension imageDimensions, ImageAnnotation annotation) {

        Double headOffset = HEAD_OFFSET * scale;
        Double headLength = HEAD_LENGTH * scale;
        Double headIndent = HEAD_INDENT * scale;
        Double headWidth = HEAD_WIDTH * scale;

        // Get delta values (aka reference from 0,0)
        Double deltaX = annotation.getX_tail() - annotation.getX();
        Double deltaY = annotation.getY_tail() - annotation.getY();

        // Convert delta values from normalized to image coords (internal svg)
        deltaX = deltaX * imageDimensions.getWidth();
        deltaY = deltaY * imageDimensions.getHeight();

        // Get the length of the line
        Double magnitude = Math.sqrt( (deltaX * deltaX) + (deltaY * deltaY) );

        // Calculate the unit vector
        Double unitX = deltaX / magnitude;
        Double unitY = deltaY / magnitude;

        //-------------------------------[ Perpendicular Vector ]------
        // Generate offset cp of the arrow (point down the line)
        Double cpOffsetX = (annotation.getX() * imageDimensions.getWidth())  - (unitX * headOffset);
        Double cpOffsetY = (annotation.getY() *  imageDimensions.getHeight()) - (unitY * headOffset);

        // Generate back cp of the arrow
        Double cpBackX = cpOffsetX + (unitX * headLength);
        Double cpBackY = cpOffsetY + (unitY * headLength);

        // Generate slight back indent for the arrow
        Double cpIndentX = cpOffsetX + (unitX * headIndent);
        Double cpIndentY = cpOffsetY + (unitY * headIndent);

        // Generate side cp of the arrow (left/right of the line)
        // cpBack is used to position the arrow head
        //     - at the head (arrow.x1)
        //     - move that position by head.length
        //     - add that position to the left/right positions
        //
        // Basic form of perpendicular vector
        // left.x =  unit.y
        // left.y = -unit.x
        // right.x = -unit.y
        // right.x =  unit.x

        Double cpLeftX = cpBackX + ( unitY * headWidth);
        Double cpLeftY = cpBackY + (-unitX * headWidth);

        Double cpRightX = cpBackX + (-unitY * headWidth);
        Double cpRightY = cpBackY + ( unitX * headWidth);

        // Compile point data into something that svg polygon can use
        return "" +
                cpOffsetX +","+ cpOffsetY +" "+
                cpLeftX   +","+ cpLeftY   +" "+
                cpIndentX +","+ cpIndentY +" "+
                cpRightX  +","+ cpRightY;
    }

    /**
     * Call this method when initializing an SvgImageDisplayPanel instance to hide the annotation arrow.
     *
     * @return this
     */
    public ArrowStyleAnnotatedSvg withNoAnnotations() {
        this.showAnnotations = false;
        return this;
    }

    public ArrowStyleAnnotatedSvg withScale(Double scale) {
        this.scale = scale;
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
