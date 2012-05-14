package com.n4systems.fieldid.wicket.components.event.criteria;

import com.n4systems.model.OneClickCriteria;
import com.n4systems.model.OneClickCriteriaResult;
import com.n4systems.model.State;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.List;

public class OneClickCriteriaEditPanel extends Panel {

    private List<State> states;
    private int currentStateIndex;
    
    private ContextImage buttonImage;
    private Label buttonLabel;
    private IModel<OneClickCriteriaResult> result;


    public OneClickCriteriaEditPanel(String id, final IModel<OneClickCriteriaResult> result) {
        super(id);
        this.result = result;
        
        states = ((OneClickCriteria)result.getObject().getCriteria()).getStates().getAvailableStates();

        if (result.getObject().getState() == null) {
            currentStateIndex = 0;
            result.getObject().setState(states.get(0));
        } else {
            currentStateIndex = states.indexOf(result.getObject().getState());
        }
        
        buttonImage = new ContextImage("buttonImage", new PropertyModel<String>(this, "buttonImageUrl"));
        buttonImage.setOutputMarkupId(true);
        buttonImage.add(new AjaxEventBehavior("onclick") {
            @Override
            protected void onEvent(AjaxRequestTarget target) {
                currentStateIndex = (currentStateIndex + 1) % states.size();
                result.getObject().setState(states.get(currentStateIndex));
                target.add(buttonImage, buttonLabel);
            }
        });
        
        buttonLabel = new Label("buttonLabel", new PropertyModel<String>(result, "state.displayText"));
        buttonLabel.setOutputMarkupId(true);

        add(buttonImage);
        add(buttonLabel);
    }
    
    public String getButtonImageUrl() {
        return "images/eventButtons/"+result.getObject().getState().getButtonName()+".png";
    }

}
