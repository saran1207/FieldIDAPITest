package com.n4systems.fieldid.wicket.pages.widgets;

import org.apache.wicket.markup.html.JavascriptPackageResource;
import org.apache.wicket.markup.html.internal.HtmlHeaderContainer;
import org.apache.wicket.model.Model;

import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;

@SuppressWarnings("serial")
public class NewsWidget extends Widget {
	
    public NewsWidget(String id, WidgetDefinition widgetDefinition) {
        super(id, new Model<WidgetDefinition>(widgetDefinition));
		add(JavascriptPackageResource.getHeaderContribution("javascript/jquery.zrssfeed.min.js"));        
        setOutputMarkupId(true);
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



