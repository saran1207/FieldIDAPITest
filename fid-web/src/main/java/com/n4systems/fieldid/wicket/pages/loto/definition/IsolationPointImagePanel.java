package com.n4systems.fieldid.wicket.pages.loto.definition;

import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.model.common.ImageAnnotation;
import com.n4systems.model.procedure.ProcedureDefinitionImage;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class IsolationPointImagePanel extends Panel {

    private @SpringBean S3Service s3Service;

    private final WebComponent image;
    private final WebMarkupContainer blankSlate;


    public IsolationPointImagePanel(String id) {
        super(id);
        add(blankSlate = new WebMarkupContainer("blankSlate"));
        add(image = new WebComponent("image") {
            @Override protected void onInitialize() {
                super.onInitialize();
                add(new AttributeModifier("src", getImageUrl()));
            }
            @Override protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                checkComponentTag(tag, "img");
            }
        });
    }

    protected Model<String> getImageUrl() {
        return new Model<String>() {
            @Override public String getObject() {
                ImageAnnotation annotation = getImageAnnotation();
                return (annotation==null) ? "" : s3Service.getProcedureDefinitionImageThumbnailURL((ProcedureDefinitionImage) annotation.getImage()).toString();
            }
        };
    }

    private ImageAnnotation getImageAnnotation() {
        return (ImageAnnotation) getDefaultModel().getObject();
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        boolean showBlankSlate = getDefaultModel().getObject()==null;
        blankSlate.setVisible(showBlankSlate);
        image.setVisible(!showBlankSlate);
    }
}
