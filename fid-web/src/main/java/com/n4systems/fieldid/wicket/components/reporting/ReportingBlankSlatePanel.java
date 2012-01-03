package com.n4systems.fieldid.wicket.components.reporting;

import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.fieldid.wicket.pages.setup.ImportPage;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.flow.RedirectToUrlException;

public class ReportingBlankSlatePanel extends Panel {

	public ReportingBlankSlatePanel(String id) {
		super(id);

        Form form = new Form("form");
        add(form);
		form.add(new AjaxButton("performFirstEventButton") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                throw new RedirectToUrlException("/startEvent.action");
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
            }
        });
        form.add(new NonWicketLink("massEventLink", "assetSelection.action"));
		form.add(new BookmarkablePageLink<Void> ("importPageLink", ImportPage.class));
	}

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderCSSReference("style/pageStyles/reporting.css");
    }
}
