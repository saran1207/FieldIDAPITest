package com.n4systems.fieldid.wicket.components.reporting;

import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.fieldid.wicket.pages.setup.ImportPage;
import org.apache.wicket.RedirectToUrlException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;

public class ReportingBlankSlatePanel extends Panel {

	public ReportingBlankSlatePanel(String id) {
		super(id);
		add(CSSPackageResource.getHeaderContribution("style/pageStyles/reporting.css"));

        Form form = new Form("form");
        add(form);
		form.add(new AjaxButton("performFirstEventButton") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                throw new RedirectToUrlException("/startEvent.action");
            }
        });
        form.add(new NonWicketLink("massEventLink", "assetSelection.action"));
		form.add(new BookmarkablePageLink<Void> ("importPageLink", ImportPage.class));
	}

}
