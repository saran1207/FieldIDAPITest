package com.n4systems.fieldid.wicket.components;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.Panel;

public class ModalLoadingPanel extends Panel implements IHeaderContributor  {
	private final Component componentToCover;
	private final Component modalPanel;
	
	public ModalLoadingPanel(String id, Component componentToCover) {
		super(id);
		setOutputMarkupId(true);
		
		componentToCover.setOutputMarkupId(true);
		this.componentToCover = componentToCover;
		
		modalPanel = new WebMarkupContainer("modalPanel");
		modalPanel.setOutputMarkupId(true);
		
		add(modalPanel);
		add(new ContextImage("loadingImage", "images/loading.gif"));
	}
	
	@Override
	public void renderHead(IHeaderResponse response) {
		String js = String.format("positionModalContainer('%s', '%s')", modalPanel.getMarkupId(), componentToCover.getMarkupId());
        response.renderOnLoadJavaScript(js);

        response.renderCSSReference("style/ModalLoadingPanel.css");
        response.renderJavaScriptReference("javascript/ModalLoadingPanel.js");
	}
}
