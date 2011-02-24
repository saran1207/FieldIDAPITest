package com.n4systems.fieldid.wicket.components.eventform.details;

import java.util.List;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import com.n4systems.fieldid.wicket.components.eventform.SortableStringListEditor;
import com.n4systems.model.ComboBoxCriteria;

public class ComboBoxDetailsPanel extends Panel {
	
	private SortableStringListEditor comboBoxOptionsEditor;

	public ComboBoxDetailsPanel(String id, IModel<ComboBoxCriteria> comboBoxCriteria) {
		super(id, comboBoxCriteria);
		
		add(comboBoxOptionsEditor = new SortableStringListEditor("comboBoxOptionsEditor", new PropertyModel<List<String>>(comboBoxCriteria, "options"),
                new Model<String>("Combo Box Options")));

	}

}
