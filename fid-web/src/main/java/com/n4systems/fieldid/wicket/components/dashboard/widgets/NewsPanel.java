package com.n4systems.fieldid.wicket.components.dashboard.widgets;

import org.apache.wicket.markup.html.JavascriptPackageResource;
import org.apache.wicket.markup.html.internal.HtmlHeaderContainer;
import org.apache.wicket.markup.html.panel.Panel;

import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;

public class NewsPanel extends Panel {

	public NewsPanel(String id) {
		super(id);
		add(JavascriptPackageResource.getHeaderContribution("javascript/jquery.zrssfeed.min.js"));
	}
	
	@Override
	public void renderHead(HtmlHeaderContainer container) {
		super.renderHead(container);
		container.getHeaderResponse().renderOnDomReadyJavascript(getRssSetUpScript());
	}

	private String getRssSetUpScript() {
        StringBuffer jsBuffer = new StringBuffer();
        
        String feed = ConfigContext.getCurrentContext().getString(ConfigEntry.RSS_FEED);

        jsBuffer.append("$('#fieldIdNews').rssfeed('" + feed + "', {");
        jsBuffer.append("limit: 3");
   		jsBuffer.append("});");
        
        return jsBuffer.toString();
	}

}
