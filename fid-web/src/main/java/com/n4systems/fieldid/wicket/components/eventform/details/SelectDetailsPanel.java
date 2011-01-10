package com.n4systems.fieldid.wicket.components.eventform.details;

import java.util.List;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import com.n4systems.fieldid.wicket.components.eventform.SortableStringListEditor;
import com.n4systems.model.SelectCriteria;

public class SelectDetailsPanel extends Panel {
	
    private SortableStringListEditor selectOptionsEditor;

	public SelectDetailsPanel(String id, IModel<SelectCriteria> selectCriteria) {
		super(id, selectCriteria);
		
		add(selectOptionsEditor = new SortableStringListEditor("selectOptionsEditor", new PropertyModel<List<String>>(selectCriteria, "options")));
	}
}
