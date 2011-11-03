package com.n4systems.fieldid.wicket.pages.widgets;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.model.Model;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.reporting.ReportingPage;
import com.n4systems.model.dashboard.WidgetDefinition;

@SuppressWarnings("serial")
public class CommonLinksWidget extends Widget {

	public CommonLinksWidget(String id, WidgetDefinition widgetDefinition) {
		super(id, new Model<WidgetDefinition>(widgetDefinition));			
		add(new NonWicketLink("upcomingEventsLink", "schedule!createSearch.action?criteria.status=INCOMPLETE"));
		add(new BookmarkablePageLink<Void>("eventHistoryLink", ReportingPage.class));
		add(new NonWicketLink("findAssetLink", "search.action"));
		
		String companySiteUrl = FieldIDSession.get().getPrimaryOrg().getWebSite();
		ExternalLink companySiteLink;
		add(companySiteLink = new ExternalLink("companySiteLink", companySiteUrl));
		
		companySiteLink.setVisible(companySiteUrl != null && !companySiteUrl.isEmpty());
		companySiteLink.add(new Label("companySiteLabel",  new FIDLabelModel("label.web_site", FieldIDSession.get().getPrimaryOrg().getDisplayName())));
	}

	@Override
	protected Component createConfigPanel(String id) {
		return new Label(id, "hello");
	}

}
