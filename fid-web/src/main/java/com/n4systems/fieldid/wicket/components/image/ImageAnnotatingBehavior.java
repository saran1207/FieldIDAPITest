    package com.n4systems.fieldid.wicket.components.image;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.model.common.EditableImage;
import com.n4systems.model.common.ImageAnnotation;
import com.n4systems.model.common.ImageAnnotationType;
import com.n4systems.util.json.JsonRenderer;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.WicketAjaxReference;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValueConversionException;

import java.util.List;

public abstract class ImageAnnotatingBehavior<T extends EditableImage> extends AbstractDefaultAjaxBehavior {

    private @SpringBean JsonRenderer jsonRenderer;
    private @SpringBean PersistenceService persistenceService;

    private boolean editable = false;


    protected ImageAnnotatingBehavior() {
    }

    @Override
    protected void respond(AjaxRequestTarget target) {
        IRequestParameters params = RequestCycle.get().getRequest().getRequestParameters();
        performAction(params);
    }

    private void performAction(IRequestParameters params) {
        try {
            doLabel(getNoteId(params),
                    getImageId(params),
                    params.getParameterValue("x").toDouble(),
                    params.getParameterValue("y").toDouble(),
                    params.getParameterValue("text").toString(),
                    getAnnotationType(params));
        } catch (StringValueConversionException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("invalid parameters for annotating behavior " + params.getParameterNames());
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("invalid parameters for annotating behavior " + params.getParameterNames());
        }
    }

    private ImageAnnotationType getAnnotationType(IRequestParameters params) {
        String allClasses = params.getParameterValue("type").toString().toUpperCase();
        for (ImageAnnotationType type:ImageAnnotationType.values()) {
            if (allClasses.contains(type.name())) {
                return type;
            }
        }
        return null;
    }

    private Long getNoteId(IRequestParameters params) {
        String id=getNullSafeStringParameter(params,"noteId");
        String prefix = "_note";
        return id==null ? null :
                id.indexOf(prefix)>=0 ? Long.parseLong(id.substring(prefix.length())) : null;
    }

    private Long getImageId(IRequestParameters params) {
        String id=getNullSafeStringParameter(params,"imageId");
        String prefix = "_image";
        return id==null ? null :
                id.indexOf(prefix)>=0 ? Long.parseLong(id.substring(prefix.length())) : null;
    }

    private String getNullSafeStringParameter(IRequestParameters params, String p) {
        return params.getParameterValue(p).isEmpty() ? null : params.getParameterValue(p).toString();
    }

    private void doLabel(Long noteId, Long imageId, Double x, Double y,  String text, ImageAnnotationType type) {
        Preconditions.checkArgument(imageId!=null, "you must specify image when updating an annotation (needs to know which image it is applied to)");
        ImageAnnotation annotation = getImageAnnotation(noteId,x,y,text,type);
        getEditableImage().addImageAnnotation(annotation);
//        persistenceService.saveOrUpdate(annotation);
//        persistenceService.saveOrUpdate(getEditableImage());
    }

    @Override
    protected void onBind() {
        super.onBind();
    }

    protected abstract T getEditableImage();


    private ImageAnnotation getImageAnnotation(Long id, Double x, Double y, String text, ImageAnnotationType type) {
        ImageAnnotation annotation = null;
        if (id==null) {   // create new one.
            annotation = new ImageAnnotation(x,y,text,type);
        } else {
            for (ImageAnnotation value:getEditableImage().getAnnotations()) {
                if (value.getId().equals(id)) {
                    annotation = value;
                }
            }
        }
        Preconditions.checkState(annotation!=null, "couldn't find annotation with id " + id);

        annotation.setX(x);
        annotation.setY(y);
        annotation.setText(text);
        annotation.setType(type);
        return annotation;
    }

    @Override
    public void renderHead(Component component, IHeaderResponse response) {
        super.renderHead(component, response);
        response.renderJavaScriptReference(WicketAjaxReference.INSTANCE);
        response.renderJavaScriptReference("javascript/jquery-ui-1.8.20.no-autocomplete.min.js");
        response.renderCSSReference("style/component/annotated-image.css");
        response.renderJavaScriptReference("javascript/jquery.annotate.js");
        response.renderJavaScriptReference("javascript/imageEditor.js");
    }

    protected final Object getJsonImageAnnotationOptions() {
        return jsonRenderer.render(getImageAnnotationOptions());
    }

    protected AnnotatedImageOptions getImageAnnotationOptions() {
        return new AnnotatedImageOptions(getEditableImage().getAnnotations());
    }

    protected Boolean isEditable() {
        return editable;
    }

    public ImageAnnotatingBehavior withNoEditing() {
        editable = false;
        return this;
    }

    public ImageAnnotatingBehavior withEditing() {
        editable = true;
        return this;
    }

    protected abstract String getDefaultType();

    // ----------------------------------------------------------------------------------------------


    public class AnnotatedImageOptions {
        String type = getDefaultType();
        String direction = "arrow-left";
        String text = "a label";
        String callback = ImageAnnotatingBehavior.this.getCallbackUrl().toString();
        List<ImageAnnotation> annotations = Lists.newArrayList();
        Boolean editable = isEditable();

        AnnotatedImageOptions(List<ImageAnnotation> annotations) {
            this.annotations = annotations;
        }
    }


}
