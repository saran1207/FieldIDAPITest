package com.n4systems.fieldid.wicket.components.event.observations;

import com.n4systems.model.Observation;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.List;

public class ObservationsButton extends Panel {

    protected IModel<List<? extends Observation>> observations;

    private String emptyListImage;
    private String nonEmptyListImage;

    public ObservationsButton(String id, IModel<List<? extends Observation>> observations, String emptyListImage, String nonEmptyListImage) {
        super(id, observations);
        this.observations = observations;
        this.emptyListImage = emptyListImage;
        this.nonEmptyListImage = nonEmptyListImage;

        ContextImage observationImage;
        add(observationImage = new ContextImage("observationImage", new PropertyModel<String>(this, "pathForImage")));
        observationImage.add(new AjaxEventBehavior("onclick") {
            @Override
            protected void onEvent(AjaxRequestTarget target) {
                onClick(target);
            }
        });
    }

    protected void onClick(AjaxRequestTarget target) { }
    
    public String getPathForImage() {
        if (observations.getObject().isEmpty()) {
            return emptyListImage;
        } else {
            return nonEmptyListImage;
        }
    }

}
