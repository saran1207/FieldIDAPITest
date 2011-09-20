package com.n4systems.fieldid.wicket.components.eventform.details;

import com.n4systems.fieldid.wicket.behavior.UpdateComponentOnChange;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.renderer.ListableChoiceRenderer;
import com.n4systems.fieldid.wicket.model.eventform.ScoreGroupsForTenantModel;
import com.n4systems.model.Score;
import com.n4systems.model.ScoreCriteria;
import com.n4systems.model.ScoreGroup;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.List;

public class ScoreDetailsPanel extends Panel {

    private IModel<ScoreCriteria> scoreCriteriaModel;
    private WebMarkupContainer scoreGroupsDisplayContainer;

    public ScoreDetailsPanel(String id, IModel<ScoreCriteria> scoreCriteriaModel) {
        super(id);
        this.scoreCriteriaModel = scoreCriteriaModel;

        ScoreGroupsForTenantModel scoreGroupsForTenantModel = new ScoreGroupsForTenantModel(new PropertyModel<ScoreGroup>(scoreCriteriaModel, "scoreGroup"));
        DropDownChoice<ScoreGroup> scoreGroupSelect;

        add(scoreGroupSelect = new DropDownChoice<ScoreGroup>("scoreGroupSelect", new PropertyModel<ScoreGroup>(scoreCriteriaModel, "scoreGroup"), scoreGroupsForTenantModel, new ListableChoiceRenderer<ScoreGroup>()));
        scoreGroupSelect.add(new UpdateComponentOnChange() {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                target.addComponent(scoreGroupsDisplayContainer);
            }
        });

        scoreGroupsDisplayContainer = new WebMarkupContainer("scoreGroupsDisplayContainer");
        scoreGroupsDisplayContainer.add(new ListView<Score>("scores", new PropertyModel<List<Score>>(scoreCriteriaModel, "scoreGroup.scores")) {
            @Override
            protected void populateItem(ListItem<Score> item) {
                item.add(new FlatLabel("name", new PropertyModel<String>(item.getModel(), "name")));
                item.add(new FlatLabel("value", new PropertyModel<Double>(item.getModel(), "value")).setVisible(!item.getModelObject().isNa()));
                item.add(new WebMarkupContainer("isNa").setVisible(item.getModelObject().isNa()));
            }
        });

        scoreGroupsDisplayContainer.setOutputMarkupId(true);
        add(scoreGroupsDisplayContainer);
    }

}
