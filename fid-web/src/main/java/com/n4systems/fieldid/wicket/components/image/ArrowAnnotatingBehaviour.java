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
public abstract class ArrowAnnotatingBehaviour extends AbstractDefaultAjaxBehavior {

    @SpringBean
    private ArrowStyleAnnotationJsonRenderer jsonRenderer;

    @SpringBean
    private ProcedureDefinitionService procedureDefinitionService;

    public ArrowAnnotatingBehaviour() {
        //Do nothing, look pretty...
    }

    @Override
    protected void respond(AjaxRequestTarget target) {
        IRequestParameters params = RequestCycle.get().getRequest().getRequestParameters();
        performAction(params);
    }

    private void performAction(IRequestParameters params) {
        persistAnnotation((params.getParameterValue("id") == null?null:params.getParameterValue("id").toLong()),
                            params.getParameterValue("x").toDouble(),
                            params.getParameterValue("y").toDouble(),
                            params.getParameterValue("x2").toDouble(),
                            params.getParameterValue("y2").toDouble());
    }

    private void persistAnnotation(Long id, Double x, Double y, Double x2, Double y2) {
        ImageAnnotation editedAnnotation = getImageAnnotation(id, x, y, x2, y2);
        editedAnnotation = procedureDefinitionService.addImageAnnotationToImage(getEditedImage(), editedAnnotation);
        processAnnotation(editedAnnotation);
    }

    protected abstract void processAnnotation(ImageAnnotation annotation);

    protected abstract ProcedureDefinitionImage getEditedImage();

    //TODO Do we need to have Type (ie. Water, Electrical...) here???
    protected abstract ImageAnnotation getImageAnnotation(Long id, Double x, Double y, Double x2, Double y2);

    protected abstract ImageAnnotation getAnnotation();

    //Not sure if this is necessary...
    @Override
    protected void onBind() {
        super.onBind();
    }

    @Override
    public void renderHead(Component component, IHeaderResponse response) {
        super.renderHead(component, response);
        response.renderJavaScriptReference(WicketAjaxReference.INSTANCE);
    }

    protected ArrowAnnotatedImageOptions getArrowAnnotatedImageOptions() {
        return new ArrowAnnotatedImageOptions(getAnnotation().getId(),
                                              getAnnotation().getX(),
                                              getAnnotation().getY(),
                                              getAnnotation().getX_tail(),
                                              getAnnotation().getY_tail());
    }

    public class ArrowAnnotatedImageOptions {
        String callback = ArrowAnnotatingBehaviour.this.getCallbackUrl().toString();
        String annotationType = "ARROW_STYLE";
        long id;
        double x;
        double y;
        double x2;
        double y2;

        public ArrowAnnotatedImageOptions(long id,
                                          double x,
                                          double y,
                                          double x2,
                                          double y2) {
            this.id = id;
            this.x = x;
            this.y = y;
            this.x2 = x2;
            this.y2 = y2;
        }
    }
}
