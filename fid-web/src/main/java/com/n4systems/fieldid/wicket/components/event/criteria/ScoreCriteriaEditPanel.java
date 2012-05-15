package com.n4systems.fieldid.wicket.components.event.criteria;

import com.n4systems.model.Score;
import com.n4systems.model.ScoreCriteria;
import com.n4systems.model.ScoreCriteriaResult;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.List;

public class ScoreCriteriaEditPanel extends Panel {

    public ScoreCriteriaEditPanel(String id, final IModel<ScoreCriteriaResult> result) {
        super(id);

        ScoreCriteria scoreCriteria = (ScoreCriteria) result.getObject().getCriteria();

        if (result.getObject().getScore() == null) {
            Score firstScore = scoreCriteria.getScoreGroup().getScores().get(0);
            result.getObject().setScore(firstScore);
        }

        RadioGroup<Score> scoreRadioGroup = new RadioGroup<Score>("scoresRadioGroup", new PropertyModel<Score>(result, "score"));
        scoreRadioGroup.add(new AjaxFormChoiceComponentUpdatingBehavior() {
            @Override protected void onUpdate(AjaxRequestTarget target) { }
        });
        add(scoreRadioGroup);
        
        scoreRadioGroup.add(new ListView<Score>("scores", new PropertyModel<List<? extends Score>>(result, "criteria.scoreGroup.scores")) {
            @Override
            protected void populateItem(ListItem<Score> item) {
                Radio<Score> radio = new Radio<Score>("score", item.getModel());
                item.add(radio);
                item.add(new Label("scoreLabel", new PropertyModel<String>(item.getModel(), "name")));
            }
        });
    }

}
