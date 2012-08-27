package com.n4systems.fieldid.wicket.pages.setup.actions;

import com.n4systems.fieldid.wicket.components.actions.PriorityCodeGroupPanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;

public class ActionsSetupPage extends FieldIDFrontEndPage {

    PriorityCodeGroupPanel priorityCodeGroupPanel;

    public ActionsSetupPage() {
        add(priorityCodeGroupPanel = new PriorityCodeGroupPanel("priorityCodeGroups"));
        priorityCodeGroupPanel.setOutputMarkupId(true);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/newCss/component/matt_buttons.css");

        response.renderCSSReference("style/newCss/setup/actiontypes.css");
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("label.actions_setup"));
    }
}
