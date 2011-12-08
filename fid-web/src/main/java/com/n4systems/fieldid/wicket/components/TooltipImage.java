package com.n4systems.fieldid.wicket.components;

import com.n4systems.fieldid.wicket.behavior.TooltipBehavior;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.model.IModel;

public class TooltipImage extends ContextImage {

    public TooltipImage(String id, IModel<String> tooltipText) {
        super(id, "images/tooltip-icon.png");
        add(new TooltipBehavior(tooltipText));
    }

}
