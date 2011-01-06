package com.n4systems.fieldid.wicket.components.eventform.details;

import java.util.List;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import com.n4systems.fieldid.wicket.components.eventform.StringListEditor;
import com.n4systems.model.SelectCriteria;

public class SelectDetailsPanel extends Panel {
	
    private StringListEditor selectOptionsEditor;

	public SelectDetailsPanel(String id, IModel<SelectCriteria> selectCriteria) {
		super(id, selectCriteria);
		
		add(selectOptionsEditor = new StringListEditor("selectOptionsEditor", new PropertyModel<List<String>>(selectCriteria, "options")));
	}


}
