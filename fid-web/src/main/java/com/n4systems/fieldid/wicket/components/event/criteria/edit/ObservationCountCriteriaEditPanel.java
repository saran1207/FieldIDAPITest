package com.n4systems.fieldid.wicket.components.event.criteria.edit;

import com.n4systems.model.ObservationCount;
import com.n4systems.model.ObservationCountCriteriaResult;
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

/**
 * Created by rrana on 2015-01-27.
 */
public class ObservationCountCriteriaEditPanel extends Panel {

    public ObservationCountCriteriaEditPanel(String id, final IModel<ObservationCountCriteriaResult> result) {
        super(id);

        RadioGroup<ObservationCount> scoreRadioGroup = new RadioGroup<ObservationCount>("scoresRadioGroup", new PropertyModel<ObservationCount>(result, "value"));
        scoreRadioGroup.add(new AjaxFormChoiceComponentUpdatingBehavior() {
            @Override protected void onUpdate(AjaxRequestTarget target) { }
        });
        add(scoreRadioGroup);

        scoreRadioGroup.add(new ListView<ObservationCount>("scores", new PropertyModel<List<? extends ObservationCount>>(result, "criteria.scoreGroup.scores")) {
            @Override
            protected void populateItem(ListItem<ObservationCount> item) {
                Radio<ObservationCount> radio = new Radio<ObservationCount>("score", item.getModel());
                item.add(radio);
                item.add(new Label("scoreLabel", new PropertyModel<String>(item.getModel(), "name")));
            }
        });
    }

}
