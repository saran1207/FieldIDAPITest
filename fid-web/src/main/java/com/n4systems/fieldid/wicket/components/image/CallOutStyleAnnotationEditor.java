package com.n4systems.fieldid.wicket.components.image;

import com.n4systems.fieldid.service.uuid.AtomicLongService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.model.common.ImageAnnotation;
import com.n4systems.model.common.ImageAnnotationType;
import com.n4systems.model.procedure.IsolationPoint;
import com.n4systems.model.procedure.ProcedureDefinitionImage;
import com.n4systems.util.json.CallOutStyleAnnotationJsonRenderer;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

/**
 * Created by rrana on 2015-03-09.
 */
public abstract class CallOutStyleAnnotationEditor extends CallOutStyleAnnotatedSvg {
    protected String EDITOR_JS_CALL = "$('#%s').callOutAnnotationEditor(%s);";

    @SpringBean
    protected CallOutStyleAnnotationJsonRenderer jsonRenderer;

    @SpringBean
    private AtomicLongService atomicLongService;

    private CallOutStyleAnnotatingBehaviour ajaxBehaviour;

    public CallOutStyleAnnotationEditor(String id, ImageAnnotation theAnnotation) {
        super(id,theAnnotation);
    }

    public CallOutStyleAnnotationEditor(String id) {
        super(id);
    }

    public CallOutStyleAnnotationEditor(String id, ProcedureDefinitionImage theImage) {
        super(id, Model.of(theImage));
    }

    @Override
    public void onInitialize() {
        super.onInitialize();
        this.setOutputMarkupId(true);
        this.add(ajaxBehaviour = createAjaxBehaviour());
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderJavaScriptReference("javascript/callOutAnnotationEditor.js");
        response.renderOnLoadJavaScript(createEditorInitJS());
    }

    protected CallOutStyleAnnotatingBehaviour createAjaxBehaviour() {
        return new CallOutStyleAnnotatingBehaviour() {
            @Override
            protected void processAnnotation(ImageAnnotation annotation) {
                annotation.setText(retrieveIsolationPoint().getIdentifier());
                retrieveIsolationPoint().setAnnotation(annotation);
            }

            @Override
            protected ProcedureDefinitionImage getEditedImage() {
                return retrieveCurrentImage();
            }

            @Override
            protected ImageAnnotation getImageAnnotation(Double x, Double y) {
                ImageAnnotation currentAnnotation = retrieveIsolationPoint().getAnnotation();
                if(currentAnnotation == null) {
                    //Must be a new annotation... so make a new one.
                    currentAnnotation = new ImageAnnotation(x, y, retrieveIsolationPoint().getIdentifier(), ImageAnnotationType.fromIsolationPointSourceType(retrieveIsolationPoint().getSourceType()), ImageAnnotationType.fromIsolationPointSourceType(retrieveIsolationPoint().getSourceType()).getBackgroundColor(), ImageAnnotationType.fromIsolationPointSourceType(retrieveIsolationPoint().getSourceType()).getFontColor());
                    currentAnnotation.setTempId(atomicLongService.getNext());
                    currentAnnotation.setTenant(FieldIDSession.get().getTenant());
                }
                currentAnnotation.setY(y);
                currentAnnotation.setX(x);
                currentAnnotation.setText(retrieveIsolationPoint().getIdentifier());
                currentAnnotation.setType(ImageAnnotationType.fromIsolationPointSourceType(retrieveIsolationPoint().getSourceType()));
                currentAnnotation.setFill(ImageAnnotationType.fromIsolationPointSourceType(retrieveIsolationPoint().getSourceType()).getBackgroundColor());
                currentAnnotation.setStroke(ImageAnnotationType.fromIsolationPointSourceType(retrieveIsolationPoint().getSourceType()).getFontColor());

                return currentAnnotation;
            }

            @Override
            protected List<ImageAnnotation> getAnnotations() {
                return annotationList;
            }

            @Override
            protected ImageAnnotation getActiveAnnotation() {
                if(retrieveIsolationPoint().getAnnotation() == null) {
                    retrieveIsolationPoint().setAnnotation(new ImageAnnotation());

                    //Hack to move the annotation out of the view box so that it looks "new".
                    retrieveIsolationPoint().getAnnotation().setX(-1.0);
                    retrieveIsolationPoint().getAnnotation().setY(-1.0);
                }

                //Hack to move the annotation out of the view box so that it looks "new".
                if(retrieveIsolationPoint().getAnnotation().getX() == 0.0 && retrieveIsolationPoint().getAnnotation().getY() == 0.0) {
                    retrieveIsolationPoint().getAnnotation().setX(-1.0);
                    retrieveIsolationPoint().getAnnotation().setY(-1.0);
                }
                retrieveIsolationPoint().getAnnotation().setType(ImageAnnotationType.fromIsolationPointSourceType(retrieveIsolationPoint().getSourceType()));
                retrieveIsolationPoint().getAnnotation().setText(retrieveIsolationPoint().getIdentifier());
                retrieveIsolationPoint().getAnnotation().setFill(ImageAnnotationType.fromIsolationPointSourceType(retrieveIsolationPoint().getSourceType()).getBackgroundColor());
                retrieveIsolationPoint().getAnnotation().setStroke(ImageAnnotationType.fromIsolationPointSourceType(retrieveIsolationPoint().getSourceType()).getFontColor());
                return retrieveIsolationPoint().getAnnotation();
            }
        };
    }

    protected String createEditorInitJS() {
        return String.format(EDITOR_JS_CALL,
                this.getMarkupId(),
                getEditorJSON());
    }

    private String getEditorJSON() {
        return jsonRenderer.render(annotationList == null ?
                ajaxBehaviour.getEmptyEditorParams() :
                ajaxBehaviour.getEditorParams());
    }

    protected abstract ProcedureDefinitionImage retrieveCurrentImage();

    protected abstract IsolationPoint retrieveIsolationPoint();
}
