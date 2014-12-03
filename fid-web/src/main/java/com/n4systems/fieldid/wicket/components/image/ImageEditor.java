package com.n4systems.fieldid.wicket.components.image;

import com.google.gson.JsonObject;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.wicket.components.ExternalImage;
import com.n4systems.model.common.EditableImage;
import com.n4systems.model.common.ImageAnnotation;
import com.n4systems.model.common.ImageAnnotationType;
import com.n4systems.util.json.JsonRenderer;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.WicketAjaxReference;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValueConversionException;

@Deprecated
public class ImageEditor extends Panel {

    private static final String IMAGE_EDITOR_FORMAT = "imageEditor.init('%s',%s,'%s');";

    enum ImageEditorAction { LABEL,SAVE };

    private @SpringBean S3Service s3Service;
    private @SpringBean PersistenceService persistenceService;
    private @SpringBean JsonRenderer jsonRenderer;

    private final IModel<EditableImage> imageModel;
    private final WebMarkupContainer container;
    private final AbstractDefaultAjaxBehavior behavior;

    // pass annotation type to constructor.
    public ImageEditor(String id, IModel<EditableImage> model) {
        super(id, model);
        imageModel = model;
        setOutputMarkupId(true);
        setMarkupId(model.getObject().getId()+"");
        add(new AttributeAppender("class","image-editor"));

        // TESTING ONLY....
        ImageAnnotation annotation = new ImageAnnotation();
        annotation.setImage(imageModel.getObject());
        annotation.setText("hello");
        annotation.setX(.5);
        annotation.setY(.5);
        annotation.setType(ImageAnnotationType.W);
        model.getObject().getAnnotations().add(annotation);
        // ------------------------

        setOutputMarkupId(true);
        container = new WebMarkupContainer("container");
        add(container);
        container.add(new AttributeAppender("style",Model.of("position:relative"), ";"));
        container.add(new ExternalImage("image",s3Service.generateResourceUrl(model.getObject().getFileName()).toString()));

        container.add(behavior = createBehavior());

        add(new WebMarkupContainer("label"));

        add(new WebMarkupContainer("save"));
    }

    private AbstractDefaultAjaxBehavior createBehavior() {
        return new AbstractDefaultAjaxBehavior() {
            @Override protected void respond(AjaxRequestTarget target) {
                IRequestParameters params = RequestCycle.get().getRequest().getRequestParameters();
                String action = params.getParameterValue("action").toString();
                performAction(ImageEditorAction.valueOf(action.toUpperCase()), params);
            }
        };
    }

    private void performAction(ImageEditorAction action, IRequestParameters params) {
        try {
            switch (action) {
                case LABEL:
                    doLabel(getNullSafeLongParameter(params, "id"),
                            getNullSafeLongParameter(params, "image"),
                            params.getParameterValue("x").toDouble(),
                            params.getParameterValue("y").toDouble(),
                            params.getParameterValue("text").toString(),
                            params.getParameterValue("dir").toString(),
                            params.getParameterValue("style").toString().toUpperCase());
                    break;
                case SAVE:
                    break;
                default:
                    // other actions to be implemented later.
                    break;
            }
        } catch (StringValueConversionException e) {
            throw new IllegalArgumentException("invalid parameters for action " + action);
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("invalid parameters for action " + action);
        }
    }

    private Long getNullSafeLongParameter(IRequestParameters params, String p) {
        return params.getParameterValue(p).isEmpty() ? null : params.getParameterValue(p).toLong();
    }

    private void save() {
        throw new UnsupportedOperationException("Not Implemented");
//        s3Service.saveEditableImage(imageModel.getObject());
   //     persistenceService.save(imageModel.getObject());
    }

    private void doLabel(Long id, Long imageId, Number x, Number y,  String text, String direction, String type) {
        // TODO : allow for remove, restyle, etc...  check if id already exists.
        //if (StringUtils.isNotEmpty(id)) {annotation = getAnnotationWithIdFromEditableImage(id);}  else...
        ImageAnnotation annotation = getAnnotationTypeWithId(id, x, y, text, direction, type);

        annotation.setImage(imageModel.getObject());
        imageModel.getObject().getAnnotations().add(annotation);
    }

    private ImageAnnotation getAnnotationTypeWithId(Long id, Number x, Number y, String text, String direction, String type) {
        ImageAnnotation annotation = new ImageAnnotation();
        for (ImageAnnotation value:imageModel.getObject().getAnnotations()) {
            if (value.getId().equals(id)) {
                annotation = value;
            }
        }
        // Preconditions.checkState(if id not null, you must find one!);
        annotation.setX(x.doubleValue());
        annotation.setY(y.doubleValue());
        annotation.setText(text);
        annotation.setType(getAnnotationType(type));
        return annotation;
    }

    private ImageAnnotationType getAnnotationType(String typeId) {
        ImageAnnotationType type = ImageAnnotationType.valueOf(typeId);
        return type;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderJavaScriptReference(WicketAjaxReference.INSTANCE);

        // CAVEAT : the reason a special (datepicker only) version of jquery ui was brought in
        //  is because if you referenced the entire ui library it would conflict the use of some
        //  wiquery ui things.  (AutoComplete in this case).
        // the best situation would be just to have all components use the predefined wiquery js references
        //  i.e. renderJavaScriptReference(CoreUIJavaScriptResourceReference.get());
        response.renderJavaScriptReference("javascript/jquery-ui-1.8.20.no-autocomplete.min.js");

        response.renderCSSReference("style/legacy/component/annotated-image.css");
        response.renderJavaScriptReference("javascript/jquery.annotate.js");
        response.renderJavaScriptReference("javascript/imageEditor.js");
        response.renderOnLoadJavaScript(String.format(IMAGE_EDITOR_FORMAT, getMarkupId(),getJsonAnnotations(),behavior.getCallbackUrl()));
    }

    private String getJsonAnnotations() {
        return jsonRenderer.render(imageModel.getObject().getAnnotations());
    }

    protected Object getOptions() {
        JsonObject options = new JsonObject();
        options.addProperty("type","water");
        options.addProperty("direction","arrow-left");
        options.addProperty("text","a label");
        return options;
    }

}
