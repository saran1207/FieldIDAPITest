package com.n4systems.fieldid.wicket.components.event.criteria;

import com.n4systems.model.Observation;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.List;

public abstract class CriteriaActionButton extends Panel {
    private String emptyListImage;
    private String nonEmptyListImage;

    public CriteriaActionButton(String id, String emptyListImage, String nonEmptyListImage) {
		super(id);
        setRenderBodyOnly(true);
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

    protected abstract void onClick(AjaxRequestTarget target);
	protected abstract boolean isEmpty();

    public String getPathForImage() {
        if (isEmpty()) {
            return emptyListImage;
        } else {
            return nonEmptyListImage;
        }
    }

}
