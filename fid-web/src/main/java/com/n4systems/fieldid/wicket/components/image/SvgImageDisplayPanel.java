package com.n4systems.fieldid.wicket.components.image;

import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.model.common.ImageAnnotation;
import com.n4systems.model.procedure.ProcedureDefinitionImage;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

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

    /**
     * This is the main constructor for the SvgImageDisplayPanel.  Since the S3 Service requires a full
     * ProcedureDefinitionImage to be able to acquire a URL for the image, the easiest solution is to just pass in
     * the whole image.  In addition to the image, we also need to be provided the ImageAnnotation object representing
     * the Annotation to be generated on top of the image.
     *
     * @param id - The <b>String</b> representation of the Wicket ID for the SvgImageDisplayPanel instance.
     * @param theImage - The <b>ProcedureDefinitionImage</b> representing the Image to have an Annotation drawn over it.
     * @param theAnnotation - The <b>ImageAnnotation</b> representing the Annotation to be drawn over the image.
     */
    public SvgImageDisplayPanel(String id, ProcedureDefinitionImage theImage, ImageAnnotation theAnnotation) {
        super(id);
        this.theImage = theImage;
        this.theAnnotation = theAnnotation;
    }

    /**
     * This override of the onInitialize method simply ensures that the necessary components have their Attributes
     * modified to contain references to data from the ProcedureDefinitionImage and ImageAnnotation objects.
     */
    @Override
    protected void onInitialize() {
        super.onInitialize();
        WebMarkupContainer imageElement = new WebMarkupContainer("imageElement");
        //Holy metal, Batman! It's really this simple!!
        imageElement.add(new AttributeModifier("xlink:href", s3Service.getProcedureDefinitionImageMediumURL(theImage)));
        add(imageElement);


        WebMarkupContainer lineElement = new WebMarkupContainer("lineElement");
        lineElement.add(new AttributeModifier("x", theAnnotation.getX()));
        lineElement.add(new AttributeModifier("y", theAnnotation.getY()));
        lineElement.add(new AttributeModifier("x2", theAnnotation.getX_tail()));
        lineElement.add(new AttributeModifier("y2", theAnnotation.getY_tail()));
        add(lineElement);
    }
}
