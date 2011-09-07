package com.n4systems.fieldid.wicket.components.reporting;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;

import com.n4systems.fieldid.wicket.pages.setup.ImportPage;

public class BlankSlatePanel extends Panel {

	public BlankSlatePanel(String id) {
		super(id);
		add(CSSPackageResource.getHeaderContribution("style/pageStyles/reporting.css"));
		add(new BookmarkablePageLink<Void> ("importPageLink", ImportPage.class));
		add(new Button("performFirstEventButton") {
			@Override
			protected void onComponentTag(ComponentTag tag) {
				tag.put("onclick", "location.href=\'/fieldid/startEvent.action\';");
			}
		});
	}

}
