package com.n4systems.fieldid.wicket.components.image;

import com.google.common.collect.Lists;
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

public abstract class ImageAnnotatingBehavior extends AbstractDefaultAjaxBehavior {

    private @SpringBean JsonRenderer jsonRenderer;

    enum ImageEditorAction { LABEL };


    @Override
    protected void respond(AjaxRequestTarget target) {
        IRequestParameters params = RequestCycle.get().getRequest().getRequestParameters();
        String action = params.getParameterValue("action").toString();
        performAction(ImageEditorAction.valueOf(action.toUpperCase()), params);
    }

    private void performAction(ImageEditorAction action, IRequestParameters params) {
        try {
            switch (action) {
                case LABEL:
                    doLabel(getNullSafeLongParameter(params, "id"),
                            getNullSafeLongParameter(params, "imageId"),
                            params.getParameterValue("x").toDouble(),
                            params.getParameterValue("y").toDouble(),
                            params.getParameterValue("text").toString(),
                            params.getParameterValue("dir").toString(),
                            params.getParameterValue("style").toString().toUpperCase());
                    break;
                default:
                    // other actions to be implemented later.
                    break;
            }
        } catch (StringValueConversionException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("invalid parameters for action " + action);
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("invalid parameters for action " + action);
        }
    }

    private Long getNullSafeLongParameter(IRequestParameters params, String p) {
        return params.getParameterValue(p).isEmpty() ? null : params.getParameterValue(p).toLong();
    }

    private void doLabel(Long id, Long imageId, Number x, Number y,  String text, String direction, String type) {
        // TODO : allow for remove, restyle, etc...  check if id already exists.
        //if (StringUtils.isNotEmpty(id)) {annotation = getAnnotationWithIdFromEditableImage(id);}  else...
        ImageAnnotation annotation = getAnnotationTypeWithId(id, x, y, text, direction, type);

        //if (imageId is different, then what to do???)
        getEditableImage().getAnnotations().add(annotation);
    }

    @Override
    protected void onBind() {
        super.onBind();
    }

    protected abstract EditableImage getEditableImage();

    private ImageAnnotation getAnnotationTypeWithId(Long id, Number x, Number y, String text, String direction, String type) {
        ImageAnnotation annotation = new ImageAnnotation();
        if (id!=null) {
            for (ImageAnnotation value:getEditableImage().getAnnotations()) {
                if (value.getId().equals(id)) {
                    annotation = value;
                }
            }
        }
        // Preconditions.checkState(if id not null, you must find one!);
        annotation.setX(x.doubleValue());
        annotation.setY(y.doubleValue());
        annotation.withDirection(direction);
        annotation.setText(text);
        annotation.setType(getAnnotationType(type));
        return annotation;
    }

    protected ImageAnnotationType getAnnotationType(String typeId) {
        ///TODO FIX THIS.   make it look up image types used in this.annotationTypes.  for now i'll just create/save a new one every time.
        ImageAnnotationType type = new ImageAnnotationType();
        //persistenceService.save(type);
        return type;
    }

    @Override
    public void renderHead(Component component, IHeaderResponse response) {
        super.renderHead(component, response);
        response.renderJavaScriptReference(WicketAjaxReference.INSTANCE);

        // CAVEAT : the reason a special (datepicker only) version of jquery ui was brought in
        //  is because if you referenced the entire ui library it would conflict the use of some
        //  wiquery ui things.  (AutoComplete in this case).
        // the best situation would be just to have all components use the predefined wiquery js references
        //  i.e. renderJavaScriptReference(CoreUIJavaScriptResourceReference.get());
        response.renderJavaScriptReference("javascript/jquery-ui-1.8.20.no-autocomplete.min.js");

        response.renderCSSReference("style/component/annotated-image.css");
        response.renderJavaScriptReference("javascript/jquery.annotate.js");
        response.renderJavaScriptReference("javascript/imageEditor.js");
    }

    protected AnnotatedImageOptions getImageAnnotationOptions() {
        return new AnnotatedImageOptions(getEditableImage().getAnnotations());
    }

    // ----------------------------------------------------------------------------------------------

    public class AnnotatedImageOptions {
        String type = "note";
        String direction = "west";
        String text = "a label";
        String callback = ImageAnnotatingBehavior.this.getCallbackUrl().toString();
        List<ImageAnnotation> annotations = Lists.newArrayList();

        AnnotatedImageOptions(List<ImageAnnotation> annotations) {
            this.annotations = annotations;
        }
    }

}
