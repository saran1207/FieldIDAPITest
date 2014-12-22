package com.n4systems.fieldid.wicket.pages.loto.definition;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.wicket.behavior.TipsyBehavior;
import com.n4systems.fieldid.wicket.components.image.ArrowStyleAnnotatedSvg;
import com.n4systems.fieldid.wicket.components.image.CallOutStyleAnnotatedSvg;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.common.ImageAnnotation;
import com.n4systems.model.procedure.AnnotationType;
import com.n4systems.model.procedure.IsolationPoint;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.procedure.ProcedureDefinitionImage;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.io.Serializable;
import java.util.List;

public class IsolationPointImagePanel extends Panel {

    private static final String INIT_JS = "imageList.createImageList('%s');";

    private Component image;
    private final WebMarkupContainer blankSlate;
    private final WebMarkupContainer outer;
    private final WebMarkupContainer container;

    private IModel<AnnotationType> annotationTypeModel;

    public IsolationPointImagePanel(String id, IModel<IsolationPoint> isolationPointModel, IModel<AnnotationType> annotationTypeModel) {
        super(id, isolationPointModel);
        setOutputMarkupId(true);
        this.annotationTypeModel = annotationTypeModel;
        add(blankSlate = new WebMarkupContainer("blankSlate"));
        WebMarkupContainer annotatedImage;
        add(annotatedImage = new WebMarkupContainer("annotatedImage"));
        annotatedImage.add(outer = new WebMarkupContainer("outerContainer"));

        outer.add(container = new WebMarkupContainer("container"));

        container.add(image = getImage());

        annotatedImage.add(new WebMarkupContainer("labelButton").add(new TipsyBehavior(new FIDLabelModel("message.isolation_point.label_image"), TipsyBehavior.Gravity.N)));

        annotatedImage.add(new AjaxLink("remove") {

            @Override
            public void onClick(AjaxRequestTarget target) {
                ImageAnnotation annotation = isolationPointModel.getObject().getAnnotation();

                if (annotation != null && annotation.getImage() != null && annotation.hasCoordinates(annotationTypeModel.getObject())) {
                    ProcedureDefinitionImage procedureDefinitionImage = (ProcedureDefinitionImage) annotation.getImage();

                    ProcedureDefinition procedureDefinition = procedureDefinitionImage.getProcedureDefinition();

                    procedureDefinition.softDeleteImage(annotation);
                }

                isolationPointModel.getObject().setAnnotation(null);

                Component newImagePanel = getImage();

                image.replaceWith(newImagePanel);
                image = newImagePanel;

                target.add(IsolationPointImagePanel.this);

            }
        });

    }

    private Component getImage() {
        Component image;
        if (annotationTypeModel.getObject().equals(AnnotationType.ARROW_STYLE)) {
            image = new ArrowStyleAnnotatedSvg("image",  new PropertyModel<>(getDefaultModel(), "annotation")).withScale(2.0);
        } else {
            image = new CallOutStyleAnnotatedSvg("image", new PropertyModel<>(getDefaultModel(), "annotation.image"),
                                                          new PropertyModel<>(getDefaultModel(), "annotation")).withScale(2.0);
        }
        image.setOutputMarkupId(true);
        return image;
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
        response.renderJavaScriptReference("javascript/imageList.js");
        response.renderOnLoadJavaScript(String.format(INIT_JS, getMarkupId()));
    }

    public void onReloadImage(AjaxRequestTarget target) {
        Component newImage = getImage();
        image.replaceWith(newImage);
        image.setParent(container);
        target.add(image);
    }

}
