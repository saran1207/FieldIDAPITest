package com.n4systems.fieldid.wicket.pages.assetsearch.version2;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.search.ColumnMappingView;

@SuppressWarnings("serial")
public class ColumnGroupPanel extends Panel {

	public ColumnGroupPanel(String id, IModel<?> model) {
		super(id, model);
		
		PropertyModel<List<ColumnMappingView>> columnsModel = new PropertyModel<List<ColumnMappingView>>(model, "mappings");
	    
	    add(new ListView<ColumnMappingView>("columns", columnsModel)  {
	        @Override
	        protected void populateItem(ListItem<ColumnMappingView> item) {
                item.add(new Label("checkboxLabel", new FIDLabelModel(new PropertyModel<String>(item.getModel(), "label"))));
                item.add(new CheckBox("checkbox", new PropertyModel<Boolean>(item.getModel(), "enabled")));
	        }
	    });
	    
	}

}
