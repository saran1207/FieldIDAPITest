package com.n4systems.fieldid.wicket.components.image;

import com.n4systems.fieldid.service.procedure.ProcedureDefinitionService;
import com.n4systems.model.common.ImageAnnotation;
import com.n4systems.model.procedure.ProcedureDefinitionImage;
import com.n4systems.util.json.CallOutStyleAnnotationJsonRenderer;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.WicketAjaxReference;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rrana on 2015-03-09.
 */
public abstract class CallOutStyleAnnotatingBehaviour extends AbstractDefaultAjaxBehavior {

    @SpringBean
    private CallOutStyleAnnotationJsonRenderer jsonRenderer;

    @SpringBean
    private ProcedureDefinitionService procedureDefinitionService;

    public CallOutStyleAnnotatingBehaviour() {
        //Do nothing, look pretty...
    }

    /*
        Private methods...
     */

    private void performAction(IRequestParameters params) {
        persistAnnotation(params.getParameterValue("x").toDouble(),
                          params.getParameterValue("y").toDouble());
    }

    private void persistAnnotation(Double x, Double y) {
        ImageAnnotation editedAnnotation = getImageAnnotation(x, y);
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

        ImageAnnotation active = getActiveAnnotation();
        List<ImageAnnotation> innactive = getAnnotations();

        //create new list with the editable one first
        //add the remaining ones last

        if(getAnnotations() != null) {
            return new EditorParams(active, innactive);
        } else {
            return getEmptyEditorParams();
        }
    }

    protected EditorParams getEmptyEditorParams() {
        return new EditorParams(getActiveAnnotation(), null);
    }

    /*
        Abstracts...
     */

    protected abstract void processAnnotation(ImageAnnotation annotation);

    protected abstract ProcedureDefinitionImage getEditedImage();

    protected abstract ImageAnnotation getImageAnnotation(Double x, Double y);

    protected abstract List<ImageAnnotation> getAnnotations();

    protected abstract ImageAnnotation getActiveAnnotation();


    /*
        Internal Classes...
     */

    private class EditorParams {
        String apiUrl = CallOutStyleAnnotatingBehaviour.this.getCallbackUrl().toString();

        CoordBundle activeAnnotation;
        List<CoordBundle> annotations;

        public EditorParams(ImageAnnotation active, List<ImageAnnotation> innactive) {
            activeAnnotation = new CoordBundle(active.getX(), active.getY(), active.getText(), active.getType().getBackgroundColor(), active.getType().getFontColor());

            annotations = new ArrayList<>();
            /*
             * We are sending an empty list now because the SVG has the annotations flattened on it.
             *

            if(innactive != null && innactive.size() > 0) {
                for (ImageAnnotation image : innactive) {
                    annotations.add(new CoordBundle(image.getX(), image.getY(), image.getText(), image.getType().getBackgroundColor(), image.getType().getFontColor()));
                }
            }
            */
        }
    }

    private class CoordBundle {
        Double x;
        Double y;
        String label;
        String fill;
        String stroke;

        public CoordBundle(Double x, Double y, String label, String fill, String stroke) {
            this.x = x;
            this.y = y;
            this.label = label;
            this.fill = fill;
            this.stroke = stroke;
        }
    }
}
