package com.n4systems.fieldid.wicket.components.eventform.save;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

public class SavePanel extends Panel implements IHeaderContributor {

    private Label minutesAgoLabel;
    private WebMarkupContainer warningContainer;

    public SavePanel(String id) {
        super(id);
        setOutputMarkupId(true);

        add(new AjaxLink("saveNowAndFinishLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                onSaveAndFinishClicked(target);
            }
        });

        add(warningContainer = new WebMarkupContainer("warningContainer"));
        warningContainer.add(new AjaxLink("saveAndContinueLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                resetLastSaveCounter(target);
                onSaveAndContinueClicked(target);
            }
        });
        warningContainer.add(minutesAgoLabel = new Label("minutesAgoLabel", "0"));
        warningContainer.setOutputMarkupId(true);
        minutesAgoLabel.setOutputMarkupId(true);
    }

    private void resetLastSaveCounter(AjaxRequestTarget target) {
        target.appendJavaScript("minutesSinceLastSave = 0;");
        target.appendJavaScript("startWarningTimer();");
    }

    protected void onSaveAndContinueClicked(AjaxRequestTarget target) { }
    protected void onSaveAndFinishClicked(AjaxRequestTarget target) { }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderJavaScriptReference("javascript/updateLastSaveWarning.js");
        
        response.renderOnDomReadyJavaScript("minuteCountIds.push('" + minutesAgoLabel.getMarkupId() + "');");
        response.renderOnDomReadyJavaScript("warningPanelIds.push('" + warningContainer.getMarkupId() + "');");
    }

}
