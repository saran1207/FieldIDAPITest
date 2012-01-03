package com.n4systems.fieldid.wicket.pages.widgets;

import com.n4systems.fieldid.wicket.pages.widgets.config.WidgetConfigPanel;
import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.widget.WidgetConfiguration;
import com.n4systems.services.ConfigService;
import com.n4systems.util.ConfigEntry;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

@SuppressWarnings("serial")
public class NewsWidget extends Widget<WidgetConfiguration> {

    @SpringBean private ConfigService configService;
	
    private WebMarkupContainer fieldIdNews;
    private final String rssCallbackJs = "function() { $('.rssFooter a').attr('target','_blank');}";
    
    public NewsWidget(String id, WidgetDefinition<WidgetConfiguration> widgetDefinition) {
        super(id, new Model<WidgetDefinition<WidgetConfiguration>>(widgetDefinition));

        add(fieldIdNews = new WebMarkupContainer("fieldIdNews"));
        fieldIdNews.setOutputMarkupId(true);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderCSSReference("style/dashboard/widgets/news.css");
        response.renderJavaScriptReference("javascript/jquery-ui-1.8.13.custom.min.js");
        response.renderJavaScriptReference("javascript/jquery.zrssfeed.js");
        response.renderOnDomReadyJavaScript(getRssSetUpScript());
    }

    private String getRssSetUpScript() {
        StringBuffer jsBuffer = new StringBuffer();
        
        String feed = configService.getString(ConfigEntry.RSS_FEED);

        jsBuffer.append("$('#"+fieldIdNews.getMarkupId()+"').rssfeed('" + feed + "', {");
        jsBuffer.append("	limit: 3, content: false, dateformat: 'D, M d', snippet: false, ");
        jsBuffer.append("	header: false, titletag: 'div', newimage: '../images/new.png', ");
        jsBuffer.append("	linktarget: '_blank',");
        jsBuffer.append("	ssl: true");
   		jsBuffer.append("},");
   		jsBuffer.append(rssCallbackJs);
   		jsBuffer.append(");");
   		        
        return jsBuffer.toString();
	}

	@Override
    public Component createConfigPanel(String id) {
		IModel<WidgetConfiguration> configModel = new Model<WidgetConfiguration>(getWidgetDefinition().getObject().getConfig());		
		return new WidgetConfigPanel<WidgetConfiguration>(id, configModel);
	}

}
