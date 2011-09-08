package com.n4systems.fieldid.wicket.components.eventform.details;

import com.n4systems.fieldid.wicket.behavior.UpdateComponentOnChange;
import com.n4systems.fieldid.wicket.components.renderer.ListableChoiceRenderer;
import com.n4systems.fieldid.wicket.model.eventform.ScoreGroupsForTenantModel;
import com.n4systems.model.ScoreCriteria;
import com.n4systems.model.ScoreGroup;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class ScoreDetailsPanel extends Panel {

    private IModel<ScoreCriteria> scoreCriteriaModel;

    public ScoreDetailsPanel(String id, IModel<ScoreCriteria> scoreCriteriaModel) {
        super(id);
        this.scoreCriteriaModel = scoreCriteriaModel;

        DropDownChoice<ScoreGroup> scoreGroupSelect;
        add(scoreGroupSelect = new DropDownChoice<ScoreGroup>("scoreGroupSelect", new PropertyModel<ScoreGroup>(scoreCriteriaModel, "scoreGroup"), new ScoreGroupsForTenantModel(), new ListableChoiceRenderer<ScoreGroup>()));
        scoreGroupSelect.add(new UpdateComponentOnChange());
    }

}
