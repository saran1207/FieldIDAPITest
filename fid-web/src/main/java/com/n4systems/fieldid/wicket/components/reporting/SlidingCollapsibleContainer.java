package com.n4systems.fieldid.wicket.components.reporting;

import com.n4systems.fieldid.utils.Predicate;
import com.n4systems.fieldid.wicket.behavior.DisplayNoneIfCondition;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.model.IModel;

public class SlidingCollapsibleContainer extends Border {

    private Component component;

    private boolean expanded;

    private WebMarkupContainer expandLink;
    private WebMarkupContainer collapseLink;

    public SlidingCollapsibleContainer(String id, IModel<String> titleModel) {
        this(id, titleModel, false);
    }

    public SlidingCollapsibleContainer(String id, IModel<String> titleModel, boolean initiallyExpanded) {
        super(id);
        setOutputMarkupId(true);

        expanded = initiallyExpanded;

        expandLink = new AjaxLink("expandLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                setExpanded(target, true);
            }
        };
        expandLink.setOutputMarkupId(true);
        ContextImage expandImage = new ContextImage("expandImage", "images/expandLarge.gif");
        expandLink.add(expandImage);
        if (initiallyExpanded)
            expandLink.add(new AttributeModifier("style","display:none;"));

        collapseLink = new AjaxLink("collapseLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                setExpanded(target, false);
            }
        };
        if (!initiallyExpanded)
            collapseLink.add(new AttributeModifier("style","display:none;"));
        collapseLink.setOutputMarkupId(true);
        ContextImage collapseImage = new ContextImage("collapseImage", "images/collapseLarge.gif");
        collapseLink.add(collapseImage);

        addToBorder(expandLink);
        addToBorder(collapseLink);

        addToBorder(new FlatLabel("titleLabel", titleModel));
    }

    private void setExpanded(AjaxRequestTarget target, boolean expanded) {
        this.expanded = expanded;
        if (expanded) {
            target.appendJavaScript("openSection('"+component.getMarkupId()+"', '"+expandLink.getMarkupId()+"', '"+collapseLink.getMarkupId()+"');");
        } else {
            target.appendJavaScript("closeSection('" + component.getMarkupId() + "', '" + collapseLink.getMarkupId() + "', '" + expandLink.getMarkupId() + "');");
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
