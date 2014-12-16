package com.n4systems.fieldid.wicket.components.image;

import com.n4systems.fieldid.service.procedure.ProcedureDefinitionService;
import com.n4systems.model.common.ImageAnnotation;
import com.n4systems.model.procedure.ProcedureDefinitionImage;
import com.n4systems.util.json.ArrowStyleAnnotationJsonRenderer;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.WicketAjaxReference;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * This is the new AJAX Behaviour for the new Image Editor.
 *
 * Created by Jordan Heath on 14-12-05.
 */
public abstract class ArrowStyleAnnotatingBehaviour extends AbstractDefaultAjaxBehavior {

    @SpringBean
    private ArrowStyleAnnotationJsonRenderer jsonRenderer;

    @SpringBean
    private ProcedureDefinitionService procedureDefinitionService;

    public ArrowStyleAnnotatingBehaviour() {
        //Do nothing, look pretty...
    }

    /*
        Private methods...
     */

    private void performAction(IRequestParameters params) {
        persistAnnotation(params.getParameterValue("x").toDouble(),
                          params.getParameterValue("y").toDouble(),
                          params.getParameterValue("x2").toDouble(),
                          params.getParameterValue("y2").toDouble());
    }

    private void persistAnnotation(Double x, Double y, Double x2, Double y2) {
        ImageAnnotation editedAnnotation = getImageAnnotation(x, y, x2, y2);
        editedAnnotation = procedureDefinitionService.addImageAnnotationToImage(getEditedImage(), editedAnnotation);
        processAnnotation(editedAnnotation);
    }

    /*
        Overrides...
     */

    @Override
    protected void respond(AjaxRequestTarget target) {
        IRequestParameters params = RequestCycle.get().getRequest().getRequestParameters();
        performAction(params);
    }

    @Override
    public void renderHead(Component component, IHeaderResponse response) {
        super.renderHead(component, response);
        response.renderJavaScriptReference(WicketAjaxReference.INSTANCE);
    }


    /*
        Protected...
     */

    protected EditorParams getEditorParams() {
        if(getAnnotation() != null) {
            return new EditorParams(getAnnotation().getX(),
                                    getAnnotation().getY(),
                                    getAnnotation().getX_tail(),
                                    getAnnotation().getY_tail());
        } else {
            return getEmptyEditorParams();
        }
    }

    protected EditorParams getEmptyEditorParams() {
        return new EditorParams(null, null, null, null);
    }

    /*
        Abstracts...
     */

    protected abstract void processAnnotation(ImageAnnotation annotation);

    protected abstract ProcedureDefinitionImage getEditedImage();

    protected abstract ImageAnnotation getImageAnnotation(Double x, Double y, Double x2, Double y2);

    protected abstract ImageAnnotation getAnnotation();


    /*
        Internal Classes...
     */

    private class EditorParams {
        String apiUrl = ArrowStyleAnnotatingBehaviour.this.getCallbackUrl().toString();
        Arrow arrow;

        public EditorParams(Double x, Double y, Double x2, Double y2) {
            arrow = new Arrow(x, y, x2, y2);
        }
    }

    private class Arrow {
        CoordBundle head;
        CoordBundle tail;

        public Arrow(Double x, Double y, Double x2, Double y2) {
            this.head = new CoordBundle(x, y);
            this.tail = new CoordBundle(x2, y2);
        }
    }

    private class CoordBundle {
        Double x;
        Double y;

        public CoordBundle(Double x, Double y) {
            this.x = x;
            this.y = y;
        }
    }
}
