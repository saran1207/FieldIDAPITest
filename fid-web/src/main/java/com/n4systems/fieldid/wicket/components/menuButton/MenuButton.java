package com.n4systems.fieldid.wicket.components.menuButton;

import com.n4systems.util.json.JsonRenderer;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.io.Serializable;
import java.util.List;

@Deprecated
//For new FieldIDTemplatePage use MenuButton2 with the new template markup
public class MenuButton<T> extends Panel {

    private @SpringBean JsonRenderer jsonRenderer;

    private final String BUTTON_ID = "button";

    private boolean ajaxButton = true;
    private final IModel<String> labelModel;

    public MenuButton(String id, IModel<String> label, List<T> items) {
        super(id);
        this.labelModel = label;
        setOutputMarkupId(true);
        add(new AjaxLink("dropDown") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                dropDownClicked(target);
            }
        });
        add(new ListView<T>("items", items) {
            @Override protected void populateItem(ListItem<T> item) {
                WebMarkupContainer link = populateLink("link", "label", item);
                if (link==null) {
                    link = new WebMarkupContainer("link");
                    link.setVisible(false);
                }

                Component icon = populateIcon("icon", item);
                if (icon==null) {
                    icon = new WebMarkupContainer("icon").setVisible(false);
                }
                link.add(icon);

                item.add(link);
            }
        });
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        if (ajaxButton) {
            add(new AjaxLink(BUTTON_ID) {
                @Override public void onClick(AjaxRequestTarget target) {
                    buttonClicked(target);
                }
            });
        } else {
            add(populateButton(BUTTON_ID));
        }
        ((MarkupContainer)get(BUTTON_ID)).add(new Label("label", labelModel));
    }

    protected WebMarkupContainer populateButton(String linkId) {
        return new WebMarkupContainer(linkId);
    }

    protected WebMarkupContainer populateLink(String linkId, String labelId, ListItem<T> item) { return null; }

    protected Component populateIcon(String id, ListItem<T> item)  { return null; }

    protected void dropDownClicked(AjaxRequestTarget target) { }

    protected void buttonClicked(AjaxRequestTarget target) { }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderJavaScriptReference("javascript/fieldIdWidgets.js");
        response.renderCSSReference("style/legacy/newCss/component/matt_buttons.css");
        response.renderOnDomReadyJavaScript("fieldIdWidgets.createMenuButton('"+getMarkupId()+"',"+jsonRenderer.render(new MenuButtonOptions())+");");
    }

    public MenuButton withNoAjax() {
        ajaxButton = false;
        return this;
    }

    public MenuButton withAjax() {
        ajaxButton = true;
        return this;
    }

    class MenuButtonOptions implements Serializable {
        private boolean ajaxButton=MenuButton.this.ajaxButton;
    }

}
