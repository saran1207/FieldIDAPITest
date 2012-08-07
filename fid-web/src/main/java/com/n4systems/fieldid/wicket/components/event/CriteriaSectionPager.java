package com.n4systems.fieldid.wicket.components.event;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class CriteriaSectionPager extends Panel {

    public CriteriaSectionPager(String id, IModel<Integer> currentPageModel, IModel<Integer> totalPageModel) {
        super(id);
        setOutputMarkupId(true);

        add(new Label("currentSection", currentPageModel));
        add(new Label("totalSections", totalPageModel));

        ContextImage prevImage = new ContextImage("prevImage", "images/prev-button.png");
        ContextImage nextImage = new ContextImage("nextImage", "images/next-button.png");

        add(prevImage);
        add(nextImage);

        prevImage.add(new AjaxEventBehavior("onclick") {
            @Override
            protected void onEvent(AjaxRequestTarget target) {
                onBack(target);
            }
        });

        nextImage.add(new AjaxEventBehavior("onclick") {
            @Override
            protected void onEvent(AjaxRequestTarget target) {
                onForward(target);
            }
        });
    }

    protected void onBack(AjaxRequestTarget target) { }
    protected void onForward(AjaxRequestTarget target) { }


}
