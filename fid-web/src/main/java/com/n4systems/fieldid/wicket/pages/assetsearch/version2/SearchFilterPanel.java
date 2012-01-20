package com.n4systems.fieldid.wicket.pages.assetsearch.version2;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;

import com.n4systems.model.search.AssetSearchCriteriaModel;



@SuppressWarnings("serial")
public class SearchFilterPanel extends SearchConfigPanel {

	public SearchFilterPanel(String id, Model<AssetSearchCriteriaModel> model, final Mediator mediator) {
		super(id,model, mediator);
	}
	
	@Override
	protected void updateMenu(Component... components ) {
		for (Component c:components) { 
			if (c.getId().equals("filters")) {
				c.add(new AttributeAppender("class", new Model<String>("disabled"), " "));
			} else if (c.getId().equals("columns")) { 
				c.add(createShowConfigBehavior("columns"));
			}						
		}
	}

	@Override
	protected Form<AssetSearchCriteriaModel> createForm(String id,
			Model<AssetSearchCriteriaModel> model) {
		return new Form(id) {			
		};
	}

}
