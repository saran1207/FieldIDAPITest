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
            processNote(parseNoteId(params),
                    params.getParameterValue("x").toDouble(),
                    params.getParameterValue("y").toDouble(),
                    params.getParameterValue("text").toString(),
                    parseAnnotationType(params));
        } catch (StringValueConversionException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("invalid parameters for annotating behavior " + params.getParameterNames());
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("invalid parameters for annotating behavior " + params.getParameterNames());
        }
    }

    private ImageAnnotationType parseAnnotationType(IRequestParameters params) {
        String allClasses = params.getParameterValue("type").toString().toUpperCase();
        for (ImageAnnotationType type:ImageAnnotationType.values()) {
            if (allClasses.contains(type.getCssClass().toUpperCase())) {
                return type;
            }
        }
        return null;
    }

    private Long parseNoteId(IRequestParameters params) {
        return parsePrefixedId(params, "noteId", "_note");
    }

    private Long parsePrefixedId(IRequestParameters params, String param, String prefix) {
        String id=getNullSafeStringParameter(params,param);
        try {
            if ( id==null || !id.startsWith(prefix) || id.length()==prefix.length()) {
                return null;
            }
            return Long.parseLong(id.substring(prefix.length()));
        } catch (NumberFormatException nfe) {
            throw new IllegalArgumentException("can't find " + prefix + "  with id " + id.substring(prefix.length()));
        }
    }

    private String getNullSafeStringParameter(IRequestParameters params, String p) {
        return params.getParameterValue(p).isEmpty() ? null : params.getParameterValue(p).toString();
    }

    private final void processNote(Long noteId, Double x, Double y, String text, ImageAnnotationType type) {
        ImageAnnotation annotation = getImageAnnotation(noteId,x,y,text,type);
        getEditableImage().addImageAnnotation(annotation);
        doLabel(getEditableImage(), annotation);
    }

    protected void doLabel(T editableImage, ImageAnnotation annotation) {}

    @Override
    protected void onBind() {
        super.onBind();
    }

    protected abstract T getEditableImage();


    private ImageAnnotation getImageAnnotation(Long id, Double x, Double y, String text, ImageAnnotationType type) {
        ImageAnnotation annotation = findImageAnnotation(id);
        if (annotation==null && id==null) {   // create new one.
            annotation = new ImageAnnotation(x,y,text,type);
            annotation.setTempId(System.currentTimeMillis());  // use this for non-persisted annotations.
        }
        Preconditions.checkState(annotation!=null, "couldn't find annotation with id " + id);

        annotation.setX(x);
        annotation.setY(y);
        annotation.setText(text);
        annotation.setType(type);
        return annotation;
    }

    protected ImageAnnotation findImageAnnotation(Long id) {
        for (ImageAnnotation value:getEditableImage().getAnnotations()) {
            if (value.getId().equals(id)) {
                return value;
            }
        }
        return null;
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

    protected abstract ImageAnnotation getAnnotation();

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

    protected ImageAnnotationType getDefaultType() {
        return getAnnotation() ==null ? ImageAnnotationType.getDefault() : getAnnotation().getType();
    }

    protected String getAnnotationText() {
        return getAnnotationId()!=null ? getAnnotation().getText() : getDefaultText();
    }

    protected Long getAnnotationId() {
        if (getAnnotation()!=null) {
            return getAnnotation().getId()!=null ? getAnnotation().getId() : getAnnotation().getTempId();
        }
        return null;
    }

    protected String getDefaultText() {
        return "a label";
    }


    // ----------------------------------------------------------------------------------------------


    public class AnnotatedImageOptions {
        String type = getDefaultType().getCssClass();
        Long editedId = getAnnotationId();
        String callback = ImageAnnotatingBehavior.this.getCallbackUrl().toString();
        String direction = "arrow-left";
        String text = getAnnotationText();
        Boolean editable = isEditable();
        List<ImageAnnotation> annotations = Lists.newArrayList();

        AnnotatedImageOptions(List<ImageAnnotation> annotations) {
            this.annotations = annotations;
        }
    }

}
