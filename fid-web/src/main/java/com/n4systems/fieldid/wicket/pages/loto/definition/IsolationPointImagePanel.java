package com.n4systems.fieldid.wicket.pages.loto.definition;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.wicket.behavior.TipsyBehavior;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.common.ImageAnnotation;
import com.n4systems.model.procedure.IsolationPoint;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.procedure.ProcedureDefinitionImage;
import com.n4systems.util.json.JsonRenderer;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.io.Serializable;
import java.util.List;

public class IsolationPointImagePanel extends Panel {

    private static final String INIT_JS = "fieldIdWidgets.createImageList('%s',%s);";

    private @SpringBean S3Service s3Service;
    private @SpringBean JsonRenderer jsonRenderer;

    private final WebComponent image;
    private final WebMarkupContainer blankSlate;
    private final WebMarkupContainer outer;

    public final IsolationPoint isolationPoint;


    public IsolationPointImagePanel(String id, final IsolationPoint isolationPoint) {
        super(id);
        this.isolationPoint = isolationPoint;
        add(blankSlate = new WebMarkupContainer("blankSlate"));
        WebMarkupContainer annotatedImage, container;
        add(annotatedImage = new WebMarkupContainer("annotatedImage"));
        annotatedImage.add(outer = new WebMarkupContainer("outerContainer"));

        outer.add(container = new WebMarkupContainer("container"));

        container.add(image = new WebComponent("image") {
            @Override protected void onInitialize() {
                super.onInitialize();
                add(new AttributeModifier("src", getImageUrl()));
            }

            @Override protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                checkComponentTag(tag, "img");
            }
        });

        annotatedImage.add(new WebMarkupContainer("labelButton").add(new TipsyBehavior(new FIDLabelModel("message.isolation_point.label_image"), TipsyBehavior.Gravity.N)));

        annotatedImage.add(new AjaxLink("remove") {

            @Override
            public void onClick(AjaxRequestTarget target) {
                ImageAnnotation annotation = getImageAnnotation();

                if (null != annotation && null != annotation.getImage()) {
                    ProcedureDefinitionImage procedureDefinitionImage = (ProcedureDefinitionImage) annotation.getImage();

                    ProcedureDefinition procedureDefinition = procedureDefinitionImage.getProcedureDefinition();
                    isolationPoint.setAnnotation(null);

                    procedureDefinition.softDeleteImage(annotation);
                }


                target.add(IsolationPointImagePanel.this);



            }
        });

    }

    protected Model<String> getImageUrl() {
        return new Model<String>() {
            @Override public String getObject() {
                ImageAnnotation annotation = getImageAnnotation();
                return (annotation==null) ? "" : s3Service.getProcedureDefinitionImageMediumURL((ProcedureDefinitionImage) annotation.getImage()).toString();
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
        outer.setVisible(!showBlankSlate);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderCSSReference("style/legacy/component/imageList.css");
        response.renderJavaScriptReference("javascript/fieldIdWidgets.js");
        response.renderOnLoadJavaScript(String.format(INIT_JS, getMarkupId(),jsonRenderer.render(new EditableImageOptions())));
    }

    class EditableImageOptions {
        List<List<AnnotationOptions>> annotationOptions = Lists.newArrayList();

        EditableImageOptions() {
            super();
            if(getImageAnnotation()==null) {
                return;
            }
            List<AnnotationOptions> options = Lists.newArrayList(new AnnotationOptions(getImageAnnotation()));
            annotationOptions.add(options);
        }
    }

    class AnnotationOptions implements Serializable {
        double x;
        double y;
        String text;
        String cssStyle;

        public AnnotationOptions(ImageAnnotation annotation) {
            this.x = annotation.getX();
            this.y = annotation.getY();
            this.text = annotation.getText();
            this.cssStyle = annotation.getType().getCssClass();
        }
    }

}
