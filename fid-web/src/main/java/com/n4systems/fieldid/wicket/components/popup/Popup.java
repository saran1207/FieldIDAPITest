package com.n4systems.fieldid.wicket.components.popup;

import com.google.common.base.Preconditions;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

public class Popup extends Panel {

    public static final String CONTENT_ID = "content";

    public Popup(String id) {
        super(id);
        setOutputMarkupPlaceholderTag(true);
        add(new ContextImage("close", "images/x-black.gif").add(createClosePopupBehavior()));
        add(createContent(CONTENT_ID));
        add(new AttributeAppender("class", Model.of("popup")," "));
    }

    public Behavior createClosePopupBehavior() {
        return new AjaxEventBehavior("onclick") {
            @Override protected void onEvent(AjaxRequestTarget target) {
                Popup.this.setVisible(false);
                target.add(Popup.this);
            }
        };
    }

    protected WebMarkupContainer createContent(String id) {
        return new WebMarkupContainer(id);
    }

    public void setContent(Component component) {
        Preconditions.checkArgument(component.getId().equals(CONTENT_ID));
        replace(component);
        component.setOutputMarkupId(true);
    }

}
