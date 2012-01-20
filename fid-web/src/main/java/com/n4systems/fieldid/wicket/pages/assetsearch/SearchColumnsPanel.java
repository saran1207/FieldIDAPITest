package com.n4systems.fieldid.wicket.pages.assetsearch;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.model.Model;


@SuppressWarnings("serial")
public class SearchColumnsPanel extends SearchConfigPanel {

	public SearchColumnsPanel(String id, final Mediator mediator) {
		super(id, mediator);
	}

	@Override
	protected void updateMenu(Component... components ) {
		for (Component c:components) { 
			if (c.getId().equals("columns")) {				
				c.add(new AttributeAppender("class", new Model<String>("disabled"), " "));				
			} else if (c.getId().equals("filters")) { 
				c.add(createShowConfigBehavior("filter"));
			}			
		}
	}
	
}
