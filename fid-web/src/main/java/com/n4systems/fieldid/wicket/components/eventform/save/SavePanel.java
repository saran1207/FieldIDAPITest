package com.n4systems.fieldid.wicket.components.eventform.save;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.JavascriptPackageResource;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

public class SavePanel extends Panel implements IHeaderContributor {

    private Label minutesAgoLabel;
    private WebMarkupContainer warningContainer;

    public SavePanel(String id) {
        super(id);
        setOutputMarkupId(true);

        add(JavascriptPackageResource.getHeaderContribution("javascript/updateLastSaveWarning.js"));
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
        warningContainer.add(minutesAgoLabel = new Label("minutesAgoLabel", ""));
        warningContainer.setOutputMarkupId(true);
        minutesAgoLabel.setOutputMarkupId(true);
    }

    private void resetLastSaveCounter(AjaxRequestTarget target) {
        target.appendJavascript("minutesSinceLastSave = 0;");
        target.appendJavascript("startWarningTimer();");
    }

    protected void onSaveAndContinueClicked(AjaxRequestTarget target) { }
    protected void onSaveAndFinishClicked(AjaxRequestTarget target) { }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderOnDomReadyJavascript("minuteCountIds.push('"+minutesAgoLabel.getMarkupId()+"');");
        response.renderOnDomReadyJavascript("warningPanelIds.push('"+warningContainer.getMarkupId()+"');");
    }

}
