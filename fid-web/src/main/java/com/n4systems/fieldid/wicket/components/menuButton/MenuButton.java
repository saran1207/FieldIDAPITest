package com.n4systems.fieldid.wicket.components.menuButton;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import java.util.List;


public class MenuButton<T> extends Panel {

    public MenuButton(String id, IModel<String> label, List<T> items) {
        super(id);
        setOutputMarkupId(true);
        add(new AjaxLink("button") {
            @Override public void onClick(AjaxRequestTarget target) {
                 buttonClicked(target);
            }
        }.add(new Label("label", label)));
        add(new AjaxLink("dropDown") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                dropDownClicked(target);
            }
        });
        add(new ListView<T>("items", items) {
            @Override
            protected void populateItem(ListItem<T> item) {
                Component link = populateIcon("icon", item);
                if (link==null) {
                    item.add(new WebMarkupContainer("icon").setVisible(false));
                } else {
                    item.add(link);
                }

                Component label = populateLink("link", "label", item);
                if (label==null) {
                    item.add(new WebMarkupContainer("link").setVisible(false));
                } else {
                    item.add(label);
                }
            }
        });
    }

    protected Component populateLink(String linkId, String labelId, ListItem<T> item) { return null; }

    protected Component populateIcon(String id, ListItem<T> item)  { return null; }

    protected void dropDownClicked(AjaxRequestTarget target) { }

    protected void buttonClicked(AjaxRequestTarget target) { }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderJavaScriptReference("javascript/fieldIdWidgets.js");
        response.renderCSSReference("style/newCss/component/matt_buttons.css");
        response.renderOnDomReadyJavaScript("fieldIdWidgets.createMenuButton('"+getMarkupId()+"');");
    }
}
