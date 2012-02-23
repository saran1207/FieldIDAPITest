package com.n4systems.fieldid.wicket.components.renderer;

import com.n4systems.fieldid.viewhelpers.handlers.PublishedState;
import org.apache.wicket.markup.html.form.IChoiceRenderer;

import com.n4systems.fieldid.wicket.model.FIDLabelModel;

public class PublishedStateChoiceRenderer implements IChoiceRenderer<PublishedState> {

	@Override
	public Object getDisplayValue(PublishedState object) {
		return new FIDLabelModel(object.getLabel()).getObject();
	}

	@Override
	public String getIdValue(PublishedState object, int index) {
		return object.name();
	}

}
