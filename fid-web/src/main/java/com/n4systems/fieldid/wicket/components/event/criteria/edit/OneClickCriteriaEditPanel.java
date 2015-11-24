package com.n4systems.fieldid.wicket.components.event.criteria.edit;

import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.Button;
import com.n4systems.model.OneClickCriteria;
import com.n4systems.model.OneClickCriteriaResult;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.List;

import static ch.lambdaj.Lambda.on;

public class OneClickCriteriaEditPanel extends Panel {
    private List<Button> buttons;
    private int currentStateIndex;
    
    private ContextImage buttonImage;
    private Label buttonLabel;
    private IModel<OneClickCriteriaResult> result;


    public OneClickCriteriaEditPanel(String id, final IModel<OneClickCriteriaResult> result) {
        super(id);
        this.result = result;

        add(new AttributeAppender("class", "one-click").setSeparator(" "));
        
        buttons = ((OneClickCriteria)result.getObject().getCriteria()).getButtonGroup().getAvailableButtons();

        if (result.getObject().getButton() == null) {
            currentStateIndex = 0;
            result.getObject().setButton(buttons.get(0));
        } else {
            currentStateIndex = buttons.indexOf(result.getObject().getButton());
        }
        
        buttonImage = new ContextImage("buttonImage", new PropertyModel<String>(this, "buttonImageUrl"));
        buttonImage.setOutputMarkupId(true);
        buttonImage.add(new AjaxEventBehavior("onclick") {
            @Override
            protected void onEvent(AjaxRequestTarget target) {
                currentStateIndex = (currentStateIndex + 1) % buttons.size();
                result.getObject().setButton(buttons.get(currentStateIndex));
                target.add(buttonImage, buttonLabel);
                doUpdateAction(target, result);
            }
        });
        
        buttonLabel = new Label("buttonLabel", ProxyModel.of(result, on(OneClickCriteriaResult.class).getButton().getDisplayText() ));
        buttonLabel.setOutputMarkupId(true);

        add(buttonImage);
        add(buttonLabel);
    }
    
    public String getButtonImageUrl() {
        return "images/eventButtons/"+result.getObject().getButton().getButtonName()+".png";
    }

    protected void doUpdateAction(AjaxRequestTarget target, IModel<OneClickCriteriaResult> result) {}
}
