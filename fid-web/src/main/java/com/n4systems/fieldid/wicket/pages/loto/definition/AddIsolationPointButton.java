package com.n4systems.fieldid.wicket.pages.loto.definition;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.wicket.components.menuButton.MenuButton;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.IsolationPointSourceType;
import com.n4systems.model.common.ImageAnnotationType;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.model.Model;


public class AddIsolationPointButton extends MenuButton<IsolationPointSourceType> {

    private static final String CSS_FORMAT = "width:%dpx;";

    private Integer width = 20;

    public AddIsolationPointButton(String id) {
        super(id, new FIDLabelModel("label.add_isolation_point"), Lists.newArrayList(IsolationPointSourceType.values()));
        setOutputMarkupId(true);
    }

    @Override
    protected void buttonClicked(AjaxRequestTarget target) {
        doAdd(target, getDefaultSourceType());
    }

    protected IsolationPointSourceType getDefaultSourceType() {
        return IsolationPointSourceType.values()[0];
    }

    protected void doAdd(AjaxRequestTarget target, IsolationPointSourceType sourceType) {

    }

    @Override
    protected Component populateLink(String linkId, String labelId, ListItem<IsolationPointSourceType> item) {
        final IsolationPointSourceType type = item.getModelObject();
        ImageAnnotationType label = ImageAnnotationType.valueOf(type.name());

        return new AjaxLink(linkId) {
            @Override public void onClick(AjaxRequestTarget target) {
                doAdd(target,type);
            }
        }.add(new Label(labelId, Model.of(type.getIdentifier())).add(new AttributeAppender("style", Model.of(label.getCss()), ";")));
    }

    @Override
    protected Component populateIcon(String id, ListItem<IsolationPointSourceType> item) {
        final IsolationPointSourceType type = item.getModelObject();
        ImageAnnotationType label = ImageAnnotationType.valueOf(type.name());

        Component icon = new ContextImage(id, label.getIcon());
        if (width!=null) {
            icon.add(new AttributeAppender("style", String.format(CSS_FORMAT,width))); // TODO : refactor width stuff up to parent class.
        }
        return icon;
    }

}
