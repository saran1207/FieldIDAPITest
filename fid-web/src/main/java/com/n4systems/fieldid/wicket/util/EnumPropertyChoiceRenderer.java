package com.n4systems.fieldid.wicket.util;

import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import org.apache.wicket.markup.html.form.IChoiceRenderer;

@SuppressWarnings("serial")
public class EnumPropertyChoiceRenderer<E extends Enum<?>> implements IChoiceRenderer<E> {

	public EnumPropertyChoiceRenderer() {
	}
	
	@Override
	public Object getDisplayValue(E object) {
        String key = object.getClass().getSimpleName() + "." + object.name();
        return new FIDLabelModel(key).getObject();
	}

	@Override
	public String getIdValue(E object, int index) {
		return object.name();
	}			

}
