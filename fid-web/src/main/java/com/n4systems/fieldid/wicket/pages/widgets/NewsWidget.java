package com.n4systems.fieldid.wicket.pages.widgets;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.JavascriptPackageResource;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.internal.HtmlHeaderContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.n4systems.fieldid.wicket.pages.widgets.config.WidgetConfigPanel;
import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.widget.WidgetConfiguration;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;

@SuppressWarnings("serial")
public class NewsWidget extends Widget<WidgetConfiguration> {

    private WebMarkupContainer fieldIdNews;
	
    public NewsWidget(String id, WidgetDefinition<WidgetConfiguration> widgetDefinition) {
        super(id, new Model<WidgetDefinition<WidgetConfiguration>>(widgetDefinition));

        add(fieldIdNews = new WebMarkupContainer("fieldIdNews"));
        fieldIdNews.setOutputMarkupId(true);
		add(CSSPackageResource.getHeaderContribution("style/dashboard/widgets/news.css"));
		add(JavascriptPackageResource.getHeaderContribution("javascript/jquery-ui-1.8.13.custom.min.js"));
		add(JavascriptPackageResource.getHeaderContribution("javascript/jquery.zrssfeed.js"));
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
        jsBuffer.append("limit: 3, content: false, dateformat: 'D, M d', snippet: false, ");
        jsBuffer.append("header: false, titletag: 'div', newimage: '../images/new.png', ");
        jsBuffer.append("linktarget: '_blank'");
   		jsBuffer.append("});");
        
        return jsBuffer.toString();
	}

	@Override
	protected Component createConfigPanel(String id) {
		IModel<WidgetConfiguration> configModel = new Model<WidgetConfiguration>(getWidgetDefinition().getObject().getConfig());		
		return new WidgetConfigPanel<WidgetConfiguration>(id, configModel);
	}

}



