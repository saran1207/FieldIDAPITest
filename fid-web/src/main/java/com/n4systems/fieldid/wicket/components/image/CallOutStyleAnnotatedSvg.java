package com.n4systems.fieldid.wicket.components.image;

import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.model.common.ImageAnnotation;
import com.n4systems.model.procedure.ProcedureDefinitionImage;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
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

public class CallOutStyleAnnotatedSvg extends Panel {

    @SpringBean
    private S3Service s3Service;

    private ProcedureDefinitionImage image;

    private Double scale = 1.0;

    public CallOutStyleAnnotatedSvg(String id, IModel<ProcedureDefinitionImage> imageModel) {
        super(id, imageModel);

        this.image = imageModel.getObject();
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        URL imageUrl = s3Service.getProcedureDefinitionImageMediumURL(image);

        Dimension imageDimensions = acquireImageDimensions(imageUrl);

        add(new AttributeModifier("xmlns", "http://www.w3.org/2000/svg"));
        add(new AttributeModifier("xmlns:xlink", "http://www.w3.org/1999/xlink"));
        add(new AttributeModifier("version", "1.1"));
        add(new AttributeModifier("viewBox", "0 0 " + imageDimensions.getWidth() + " " + imageDimensions.getHeight()));
        add(new AttributeModifier("width", imageDimensions.getWidth()));
        add(new AttributeModifier("height", imageDimensions.getHeight()));

        add(new ListView<ImageAnnotation>("group", image.getAnnotations()) {
            @Override
            protected void populateItem(ListItem<ImageAnnotation> item) {
                ImageAnnotation annotation = item.getModelObject();

                Boolean isReversed =  Math.round(imageDimensions.getWidth() * annotation.getX()) < imageDimensions.getWidth()/2;

                item.setMarkupId(annotation.getType().name() + "_" + annotation.getId());

                if (scale != 1.0) {
                    item.add(new AttributeModifier("transform", "scale("+ scale + ")"));
                }

                WebMarkupContainer path = new WebMarkupContainer("path");
                if (isReversed) {
                    path.add(new AttributeModifier("transform", "scale(-1, 1)"));
                }

                item.add(path);

                WebMarkupContainer rect = new WebMarkupContainer("rect");
                rect.add(new AttributeModifier("stroke", annotation.getType().getBorderColor()));
                rect.add(new AttributeModifier("fill", annotation.getType().getBackgroundColor()));

                if (isReversed) {
                    rect.add(new AttributeModifier("transform", "scale(-1, 1)"));
                }
                item.add(rect);

                WebMarkupContainer text = new WebMarkupContainer("text");
                text.add(new FlatLabel("content", annotation.getText()));
                text.add(new AttributeModifier("fill", annotation.getType().getFontColor()));

                if (isReversed) {
                    text.add(new AttributeModifier("x", "100"));
                    text.add(new AttributeModifier("y", "5"));
                } else {
                    text.add(new AttributeModifier("x", "-150"));
                    text.add(new AttributeModifier("y", "5"));
                }

                item.add(text);
            }


        });

        add(new WebMarkupContainer("imageElement").add(new AttributeModifier("xlink:href", imageUrl)));

        add(new ListView<ImageAnnotation>("annotation", image.getAnnotations()) {
            @Override
            protected void populateItem(ListItem<ImageAnnotation> item) {
                ImageAnnotation annotation = item.getModelObject();
                Long x = Math.round(imageDimensions.getWidth() * annotation.getX());
                Long y = Math.round(imageDimensions.getHeight() * annotation.getY());
                setMarkupId(annotation.getType().name() + "_" + annotation.getId());
                item.add(new AttributeModifier("xlink:href", "#" + annotation.getType().name() + "_" + annotation.getId()));
                item.add(new AttributeModifier("x", x.toString()));
                item.add(new AttributeModifier("y", y.toString()));
            }
        });
    }

    public CallOutStyleAnnotatedSvg withScale(Double scale) {
        this.scale = scale;
        return this;
    }

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
