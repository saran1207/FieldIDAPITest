package com.n4systems.fieldid.wicket.components;

import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.JavascriptPackageResource;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.Panel;

public class ModalLoadingPanel extends Panel implements IHeaderContributor  {
	
	public ModalLoadingPanel(String id) {
		super(id);
		setOutputMarkupId(true);

		add(CSSPackageResource.getHeaderContribution("style/loadingAnimation.css"));
		add(JavascriptPackageResource.getHeaderContribution("javascript/loadingAnimation.js"));
		add(new ContextImage("loadingAnimation", "images/loading.gif"));
	}
	
	@Override
	public void renderHead(IHeaderResponse response) {
		response.renderOnLoadJavascript("positionModalContainer()");
	}
}
