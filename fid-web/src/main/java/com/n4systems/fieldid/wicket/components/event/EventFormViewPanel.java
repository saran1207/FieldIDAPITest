package com.n4systems.fieldid.wicket.components.event;

import com.n4systems.fieldid.wicket.components.event.criteria.view.ObservationCriteriaResultTotalPanel;
import com.n4systems.model.AbstractEvent;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.CriteriaSection;
import com.n4systems.model.ObservationCount;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import java.text.NumberFormat;
import java.util.List;
import java.util.Map;

public class EventFormViewPanel extends EventFormPanel {

    public EventFormViewPanel(String id, final IModel<? extends AbstractEvent> event, IModel<List<AbstractEvent.SectionResults>> results) {
        super(id, event, results, false);
        setVisible(!results.getObject().isEmpty());
    }

    @Override
    protected Panel getCriteriaSectionPanel(Class<? extends AbstractEvent> eventClass, PropertyModel<List<CriteriaResult>> results) {
        return new CriteriaSectionViewPanel("criteriaPanel", results);
    }

    @Override
    protected Component getSectionScore(String id, IModel<CriteriaSection> criteriaSectionModel) {
        return new Label(id, Model.of(getScoresForSections().get(criteriaSectionModel.getObject())))
                .setVisible(event.getObject().getType().isDisplayScoreSectionTotals());
    }

    @Override
    protected Component getSectionScorePercentage(String id, IModel<CriteriaSection> criteriaSectionModel) {
        Label percentageLabel;
        Double percentage = getScorePercentageForSections().get(criteriaSectionModel.getObject());

        if(percentage != null)
            percentageLabel = new Label(id, NumberFormat.getPercentInstance().format(percentage));
        else
            percentageLabel = new Label(id);
        percentageLabel.setVisible(event.getObject().getType().isDisplayScorePercentage());

        return percentageLabel;
    }

    @Override
    protected Component getSectionObservations(String id, IModel<CriteriaSection> criteriaSectionModel) {
        return new ObservationCriteriaResultTotalPanel(id, event, getObservationsForSections().get(criteriaSectionModel.getObject()));
    }
}
