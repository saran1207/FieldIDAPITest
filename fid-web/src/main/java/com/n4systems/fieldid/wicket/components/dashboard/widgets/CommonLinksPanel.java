package com.n4systems.fieldid.wicket.components.dashboard.widgets;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.Panel;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.reporting.ReportingPage;

public class CommonLinksPanel extends Panel {

	public CommonLinksPanel(String id) {
		super(id);
			
		add(new NonWicketLink("upcomingEventsLink", "schedule!createSearch.action?criteria.status=INCOMPLETE"));
		add(new BookmarkablePageLink<Void>("eventHistoryLink", ReportingPage.class));
		add(new NonWicketLink("findAssetLink", "search.action"));
	
		String companySiteUrl = FieldIDSession.get().getPrimaryOrg().getWebSite();
		ExternalLink companySiteLink;
		add(companySiteLink = new ExternalLink("companySiteLink", companySiteUrl));
		
		companySiteLink.setVisible(companySiteUrl != null && !companySiteUrl.isEmpty());
		companySiteLink.add(new Label("companySiteLabel",  new FIDLabelModel("label.web_site", FieldIDSession.get().getPrimaryOrg().getDisplayName())));
	}

}
