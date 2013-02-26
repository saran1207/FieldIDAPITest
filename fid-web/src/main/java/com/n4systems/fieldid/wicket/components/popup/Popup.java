package com.n4systems.fieldid.wicket.components.popup;

import com.google.common.base.Preconditions;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.odlabs.wiquery.core.events.Event;
import org.odlabs.wiquery.core.events.MouseEvent;
import org.odlabs.wiquery.core.events.WiQueryEventBehavior;
import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.ui.core.JsScopeUiEvent;

public class Popup extends Panel {

    public Popup(String id) {
        super(id);
        setOutputMarkupPlaceholderTag(true);
        add(new ContextImage("close", "images/x-black.gif").add(createClosePopupBehavior()));
        add(createContent("content"));
        add(new AttributeAppender("class", Model.of("popup")," "));
    }

    public Behavior createClosePopupBehavior() {
        return new WiQueryEventBehavior(new Event(MouseEvent.CLICK) {
            @Override public JsScope callback() {
                return JsScopeUiEvent.quickScope("$('#" + getMarkupId() + "').hide();");
            }
        });
    }

    protected WebMarkupContainer createContent(String id) {
        return new WebMarkupContainer(id);
    }

    public void setContent(Component component) {
        Preconditions.checkArgument(component.getId().equals("content"));
        replace(component);
    }

}
