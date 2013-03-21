package com.n4systems.fieldid.wicket.pages.loto.definition;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.wicket.components.menuButton.MenuButton;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.IsolationPointSourceType;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.model.Model;


public class AddIsolationPointButton extends MenuButton<IsolationPointSourceType> {

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
    protected void populateMenuItem(ListItem<IsolationPointSourceType> item) {
        final IsolationPointSourceType type = item.getModelObject();
        item.add(new ContextImage("icon","images/x.gif"));
        item.add(new AjaxLink("sourceType") {
            @Override public void onClick(AjaxRequestTarget target) {
                doAdd(target,type);
            }
        }.add(new Label("name", Model.of(type.getIdentifier()))));
    }
}
