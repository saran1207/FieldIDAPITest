package com.n4systems.fieldid.wicket.pages.assetsearch;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.markup.html.panel.Panel;

@SuppressWarnings("serial")
public class AssetFilterPanel extends Panel {

	public AssetFilterPanel(String id, final Mediator mediator) {
		super(id);
		setOutputMarkupId(true);
		add(new IndicatingAjaxLink("search") {
			@Override public void onClick(AjaxRequestTarget target) {
				mediator.handleEvent(target,this);
			}
		});
	}


}
