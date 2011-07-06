package com.n4systems.fieldid.wicket.components.reporting;

import com.n4systems.fieldid.utils.Predicate;
import com.n4systems.fieldid.wicket.behavior.DisplayNoneIfCondition;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.model.IModel;

public class SlidingReportSectionCollapseContainer extends Border {

    private Component component;
    private WebMarkupContainer linksContainer;

    private boolean expanded;

    private WebMarkupContainer expandLink;
    private WebMarkupContainer collapseLink;

    public SlidingReportSectionCollapseContainer(String id, IModel<String> titleModel) {
        super(id);
        setOutputMarkupId(true);

        linksContainer = new WebMarkupContainer("linksContainer");
        linksContainer.setOutputMarkupId(true);

        expandLink = new AjaxLink("expandLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                setExpanded(target, true);
            }
        };
        expandLink.setOutputMarkupId(true);
        ContextImage expandImage = new ContextImage("expandImage", "images/expandLarge.gif");
        expandLink.add(expandImage);

        collapseLink = new AjaxLink("collapseLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                setExpanded(target, false);
            }
        };
        collapseLink.add(new SimpleAttributeModifier("style", "display:none;"));
        collapseLink.setOutputMarkupId(true);
        ContextImage collapseImage = new ContextImage("collapseImage", "images/collapseLarge.gif");
        collapseLink.add(collapseImage);

        linksContainer.add(expandLink);
        linksContainer.add(collapseLink);

        add(linksContainer);

        add(new Label("titleLabel", titleModel));

        add(getBodyContainer());
    }

    private void setExpanded(AjaxRequestTarget target, boolean expanded) {
        this.expanded = expanded;
        if (expanded) {
            target.appendJavascript("openSection('"+component.getMarkupId()+"', '"+expandLink.getMarkupId()+"', '"+collapseLink.getMarkupId()+"');");
        } else {
            target.appendJavascript("closeSection('" + component.getMarkupId() + "', '" + collapseLink.getMarkupId() + "', '" + expandLink.getMarkupId() + "');");
        }
    }

    public void addContainedPanel(Component component) {
        this.component = component;
        component.add(new DisplayNoneIfCondition(new Predicate() {
            @Override
            public boolean evaluate() {
                return !expanded;
            }
        }));
        component.setOutputMarkupPlaceholderTag(true);
        add(component);
    }

}
