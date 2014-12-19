package com.n4systems.fieldid.wicket.components.image;

import com.n4systems.fieldid.service.uuid.AtomicLongService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.model.common.ImageAnnotation;
import com.n4systems.model.common.ImageAnnotationType;
import com.n4systems.model.procedure.IsolationPoint;
import com.n4systems.model.procedure.ProcedureDefinitionImage;
import com.n4systems.util.json.ArrowStyleAnnotationJsonRenderer;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * An Extension of the SVG Display Panel with some JS bolted to the front of it...  You can maybe use this to edit
 * annotations and stuff.
 *
 * This is really just a wrapper for this panel that invokes the JS.
 *
 * Created by Jordan Heath on 14-12-09.
 */
public abstract class ArrowStyleAnnotationEditor extends ArrowStyleAnnotatedSvg {
    protected String EDITOR_JS_CALL = "$('#%s').arrowAnnotationEditor(%s);";

    @SpringBean
    protected ArrowStyleAnnotationJsonRenderer jsonRenderer;

    @SpringBean
    private AtomicLongService atomicLongService;

    private ArrowStyleAnnotatingBehaviour ajaxBehaviour;

    public ArrowStyleAnnotationEditor(String id, ImageAnnotation theAnnotation) {
        super(id, theAnnotation);
        withNoAnnotations();
    }

    public ArrowStyleAnnotationEditor(String id) {
        super(id);
        withNoAnnotations();
    }

    public ArrowStyleAnnotationEditor(String id, ProcedureDefinitionImage theImage) {
        super(id, theImage);
        withNoAnnotations();
    }

    @Override
    public void onInitialize() {
        super.onInitialize();
        this.setOutputMarkupId(true);
        this.add(ajaxBehaviour = createAjaxBehaviour());
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderJavaScriptReference("javascript/arrowAnnotationEditor.js");
        response.renderOnLoadJavaScript(createEditorInitJS());
    }

    protected ArrowStyleAnnotatingBehaviour createAjaxBehaviour() {
        return new ArrowStyleAnnotatingBehaviour() {
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
            protected ImageAnnotation getImageAnnotation(Double x, Double y, Double x2, Double y2) {
                ImageAnnotation currentAnnotation = retrieveIsolationPoint().getAnnotation();
                if(currentAnnotation == null) {
                    //Must be a new annotation... so make a new one.
                    currentAnnotation = new ImageAnnotation(x, y, x2, y2, "", ImageAnnotationType.fromIsolationPointSourceType(retrieveIsolationPoint().getSourceType()));
                    currentAnnotation.setTempId(atomicLongService.getNext());
                    currentAnnotation.setTenant(FieldIDSession.get().getTenant());
                }
                currentAnnotation.setY(y);
                currentAnnotation.setX(x);
                currentAnnotation.setX_tail(x2);
                currentAnnotation.setY_tail(y2);
                currentAnnotation.setType(ImageAnnotationType.fromIsolationPointSourceType(retrieveIsolationPoint().getSourceType()));
                currentAnnotation.setText("");

                return currentAnnotation;
            }

            @Override
            protected ImageAnnotation getAnnotation() {
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
        return jsonRenderer.render(theAnnotation == null ?
                ajaxBehaviour.getEmptyEditorParams() :
                ajaxBehaviour.getEditorParams());
    }

    protected abstract ProcedureDefinitionImage retrieveCurrentImage();

    protected abstract IsolationPoint retrieveIsolationPoint();
}
