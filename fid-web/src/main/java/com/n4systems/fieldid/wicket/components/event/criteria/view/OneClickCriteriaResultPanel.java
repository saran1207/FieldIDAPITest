package com.n4systems.fieldid.wicket.components.event.criteria.view;

import com.n4systems.model.OneClickCriteriaResult;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class OneClickCriteriaResultPanel extends Panel{
    public OneClickCriteriaResultPanel(String id, IModel<OneClickCriteriaResult> resultModel) {
        super(id);

        add(new ContextImage("buttonImage", getButtonImageUrl(resultModel.getObject())));
        add(new Label("buttonLabel", new PropertyModel<String>(resultModel, "button.displayText")));
        add(new AttributeAppender("class", "one-click").setSeparator(" "));

    }

    private String getButtonImageUrl(OneClickCriteriaResult result) {
        return "images/eventButtons/" + result.getButton().getButtonName() + ".png";
    }
}
