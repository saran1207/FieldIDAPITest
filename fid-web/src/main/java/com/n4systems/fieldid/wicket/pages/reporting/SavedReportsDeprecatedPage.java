package com.n4systems.fieldid.wicket.pages.reporting;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

import org.apache.wicket.markup.html.link.ExternalLink;

import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;

public class SavedReportsDeprecatedPage extends FieldIDFrontEndPage {
	
	public SavedReportsDeprecatedPage() {
		String url = "http://customers.fieldid.com/2012/01/new-feature-my-saved-items/";
		add(new ExternalLink("blogPostLink", url, url));
	}
	
	@Override
    protected void addNavBar(String navBarId) {
        add(new NavigationBar(navBarId,
                aNavItem().label("nav.new_report").page(ReportingPage.class).build(),
                aNavItem().label("nav.saved_reports").page(SavedReportsDeprecatedPage.class).build()));
    }

}
