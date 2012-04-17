package com.n4systems.fieldid.wicket.pages.widgets;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.assetsearch.version2.AbstractSearchPage;
import com.n4systems.fieldid.wicket.pages.assetsearch.version2.ReportPage;
import com.n4systems.fieldid.wicket.pages.assetsearch.version2.SearchPage;
import com.n4systems.fieldid.wicket.pages.reporting.RunReportPage;
import com.n4systems.fieldid.wicket.pages.widgets.config.WidgetConfigPanel;
import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.widget.WidgetConfiguration;

@SuppressWarnings("serial")
public class CommonLinksWidget extends Widget<WidgetConfiguration> {
	
	public CommonLinksWidget(String id, WidgetDefinition<WidgetConfiguration> widgetDefinition) {
		super(id, new Model<WidgetDefinition<WidgetConfiguration>>(widgetDefinition));			
		
        PageParameters params = new PageParameters().add(AbstractSearchPage.SOURCE_PARAMETER, AbstractSearchPage.WIDGET_SOURCE)
                                                    .add(AbstractSearchPage.WIDGET_DEFINITION_PARAMETER, widgetDefinition.getId());
		add(new BookmarkablePageLink<Void>("upcomingEventsLink", RunReportPage.class, params));
		add(new BookmarkablePageLink<Void>("eventHistoryLink", ReportPage.class));
		add(new BookmarkablePageLink<Void>("findAssetLink", SearchPage.class));
		
		String companySiteUrl = FieldIDSession.get().getPrimaryOrg().getWebSite();
		ExternalLink companySiteLink;
		add(companySiteLink = new ExternalLink("companySiteLink", companySiteUrl));
		
		companySiteLink.setVisible(companySiteUrl != null && !companySiteUrl.isEmpty());
		companySiteLink.add(new Label("companySiteLabel",  new FIDLabelModel("label.web_site", FieldIDSession.get().getPrimaryOrg().getDisplayName())));
	}

	@Override
    public Component createConfigPanel(String id) {
		IModel<WidgetConfiguration> configModel = new Model<WidgetConfiguration>(getWidgetDefinition().getObject().getConfig());		
		return new WidgetConfigPanel<WidgetConfiguration>(id, configModel);
	}

}
