package com.n4systems.fieldid.wicket.utils;

import org.apache.wicket.markup.html.form.IChoiceRenderer;

import com.n4systems.model.api.Listable;

@SuppressWarnings("serial")
public class EnumDropDownChoiceRenderer<E extends Enum<?>> implements IChoiceRenderer<E> {

	public EnumDropDownChoiceRenderer() { 
	}
	
	@Override
	@SuppressWarnings({ "unchecked" })
	public Object getDisplayValue(E object) {
		if (object instanceof Listable) { 
			Listable<String> listable = (Listable<String>) object;
			return listable.getDisplayName();
		}
		return object.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getIdValue(E object, int index) {
		if (object instanceof Listable) { 
			Listable<String> listable = (Listable<String>) object;
			return listable.getId();
		}
		return object.name();
	}			

	
	
	
}
