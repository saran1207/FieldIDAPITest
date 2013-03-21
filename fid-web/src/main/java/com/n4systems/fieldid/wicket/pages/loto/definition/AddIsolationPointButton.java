package com.n4systems.fieldid.wicket.pages.loto.definition;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.IsolationPointSourceType;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;


// TODO : refactor this into general ButtonMenu component.
public class AddIsolationPointButton extends Panel {

    public AddIsolationPointButton(String id) {
        super(id);
        setOutputMarkupId(true);
        add(new AjaxLink("button") {
            @Override public void onClick(AjaxRequestTarget target) {
                // do default add behaviour...i.e. add(getFirstSourceType());
            }
        }.add(new Label("label", new FIDLabelModel("label.add_isolation_point"))));
        add(new AjaxLink("dropDown") {
            @Override public void onClick(AjaxRequestTarget target) {
                // show drop down...
            }
        });
        add(new ListView<IsolationPointSourceType>("sourceTypes", Lists.newArrayList(IsolationPointSourceType.values())) {
            @Override protected void populateItem(ListItem<IsolationPointSourceType> item) {
                IsolationPointSourceType type = item.getModelObject();
                item.add(new AjaxLink("sourceType") {
                    @Override public void onClick(AjaxRequestTarget target) {
                    }
                }.add(new Label("name", Model.of(type.getIdentifier()))));
                item.add(new ContextImage("icon","images/x.gif"));
            }
        });
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderJavaScriptReference("javascript/fieldIdWidgets.js");
        response.renderCSSReference("style/newCss/component/matt_buttons.css");
        response.renderOnDomReadyJavaScript("fieldIdWidgets.createMenuButton('"+getMarkupId()+"');");
    }
}
