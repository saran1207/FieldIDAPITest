package com.n4systems.fieldid.wicket.pages.widgets;

import com.n4systems.fieldid.wicket.pages.widgets.config.EventKPIConfigPanel;
import com.n4systems.fieldid.wicket.pages.widgets.config.WidgetConfigPanel;
import com.n4systems.fieldid.wicket.util.AjaxCallback;
import com.n4systems.model.dashboard.widget.EventKPIWidgetConfiguration;
import com.n4systems.model.dashboard.widget.WidgetConfiguration;
import org.apache.wicket.markup.html.JavascriptPackageResource;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.internal.HtmlHeaderContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;

@SuppressWarnings("serial")
public class NewsWidget extends Widget {

    private WebMarkupContainer fieldIdNews;
	
    public NewsWidget(String id, WidgetDefinition widgetDefinition) {
        super(id, new Model<WidgetDefinition>(widgetDefinition));

        add(fieldIdNews = new WebMarkupContainer("fieldIdNews"));
        fieldIdNews.setOutputMarkupId(true);
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

        jsBuffer.append("$('#"+fieldIdNews.getMarkupId()+"').rssfeed('" + feed + "', {");
        jsBuffer.append("limit: 3");
   		jsBuffer.append("});");
        
        return jsBuffer.toString();
	}

    @Override
    public <T extends WidgetConfiguration> WidgetConfigPanel createConfigurationPanel(String id, IModel<T> config, AjaxCallback<Boolean> saveCallback) {
        return new EventKPIConfigPanel(id, (IModel<EventKPIWidgetConfiguration>)config, saveCallback);
    }

}



