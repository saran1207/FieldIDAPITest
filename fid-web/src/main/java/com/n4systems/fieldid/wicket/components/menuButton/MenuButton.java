package com.n4systems.fieldid.wicket.components.menuButton;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.IHeaderResponse;
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
            @Override public void onClick(AjaxRequestTarget target) {
                dropDownClicked(target);
            }
        });
        add(new ListView<T>("items", items) {
            @Override protected void populateItem(ListItem<T> item) {
                populateMenuItem(item);
            }
        });
    }

    protected void populateMenuItem(ListItem<T> item) { }

    protected void dropDownClicked(AjaxRequestTarget target) { }

    protected void buttonClicked(AjaxRequestTarget target) { }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderJavaScriptReference("javascript/fieldIdWidgets.js");
        response.renderCSSReference("style/newCss/component/matt_buttons.css");
        response.renderOnDomReadyJavaScript("fieldIdWidgets.createMenuButton('"+getMarkupId()+"');");
    }
}
