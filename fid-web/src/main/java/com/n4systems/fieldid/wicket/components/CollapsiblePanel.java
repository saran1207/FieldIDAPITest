package com.n4systems.fieldid.wicket.components;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class CollapsiblePanel extends Panel {

    private static final String CONTAINED_PANEL_MARKUP_ID = "containedPanel";

    private AjaxLink collapseExpandLink;
    private WebMarkupContainer containedPanel;

    private WebMarkupContainer collapseImage;
    private WebMarkupContainer expandImage;
    private boolean expanded = false;

    public CollapsiblePanel(String id, final IModel<String> titleModel) {
        super(id);
        setOutputMarkupId(true);
        add(collapseExpandLink = new AjaxLink("collapseExpandLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                setExpanded(!expanded);
                target.addComponent(CollapsiblePanel.this);
                target.addComponent(collapseImage);
                onExpandedOrCollapsed(titleModel.getObject(), expanded);
            }
        });
        collapseExpandLink.add(new Label("collapseExpandLinkLabel", titleModel));
        collapseExpandLink.add(expandImage = new WebMarkupContainer("expandImage"));
        collapseExpandLink.add(collapseImage = new WebMarkupContainer("collapseImage"));
        collapseImage.setVisible(false);
        expandImage.setOutputMarkupPlaceholderTag(true);
        collapseImage.setOutputMarkupPlaceholderTag(true);
    }

    public void addContainedPanel(WebMarkupContainer containedPanel) {
        this.containedPanel = containedPanel;
        containedPanel.setOutputMarkupId(true);
        containedPanel.setVisible(false);
        add(containedPanel);
    }

    public String getContainedPanelMarkupId() {
        return CONTAINED_PANEL_MARKUP_ID;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
        containedPanel.setVisible(expanded);
        collapseImage.setVisible(expanded);
        expandImage.setVisible(!expanded);
    }

    protected void onExpandedOrCollapsed(String title, Boolean expanded) { }

}
