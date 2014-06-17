package com.n4systems.fieldid.wicket.components.event.criteria.view;

import com.n4systems.model.ScoreCriteriaResult;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class ScoreCriteriaResultPanel extends Panel{
    public ScoreCriteriaResultPanel(String id, IModel<ScoreCriteriaResult> resultModel) {
        super(id);

        add(new Label("scoreResult", new PropertyModel<String>(resultModel, "score")));

    }

}
