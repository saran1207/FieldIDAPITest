package com.n4systems.fieldid.wicket.components.image;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.procedure.ProcedureDefinitionService;
import com.n4systems.model.common.EditableImage;
import com.n4systems.model.common.ImageAnnotation;
import com.n4systems.model.common.ImageAnnotationType;
import com.n4systems.util.json.JsonRenderer;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValueConversionException;

public abstract class ArrowStyleAnnotatingBehavior <T extends EditableImage> extends ImageAnnotatingBehavior {

    @SpringBean
    private JsonRenderer jsonRenderer;

    @SpringBean
    private PersistenceService persistenceService;

    @SpringBean
    private ProcedureDefinitionService procedureDefinitionService;

    private boolean editable = false;

    protected ArrowStyleAnnotatingBehavior() {
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
                    params.getParameterValue("x2").toDouble(),
                    params.getParameterValue("y2").toDouble(),
                    params.getParameterValue("text").toString(),
                    parseAnnotationType(params));
        } catch (StringValueConversionException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("invalid parameters for arrow annotating behavior " + params.getParameterNames());
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("invalid parameters for arrow annotating behavior " + params.getParameterNames());
        }
    }

    private final void processNote(Long noteId, Double x, Double y, Double x2, Double y2, String text, ImageAnnotationType type) {
        ImageAnnotation annotation = getImageAnnotation(noteId, x, y, x2, y2, text, type);
        annotation = procedureDefinitionService.addImageAnnotationToImage(getEditableImage(),annotation);
        processNote(getEditableImage(), annotation);
    }

    public ArrowStyleAnnotatingBehavior withEditing() {
        editable = true;
        return this;
    }

    protected abstract ImageAnnotation getImageAnnotation(Long id, Double x, Double y, Double x2, Double y2, String text, ImageAnnotationType type);

    @Override
    protected ImageAnnotation getImageAnnotation(Long id, Double x, Double y, String text, ImageAnnotationType type) {
        return null;
    }

    //TODO include different annotation JS?
    @Override
    public void renderHead(Component component, IHeaderResponse response) {
        super.renderHead(component, response);
    }
}
