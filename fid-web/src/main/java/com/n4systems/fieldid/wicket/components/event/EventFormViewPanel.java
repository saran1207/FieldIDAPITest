package com.n4systems.fieldid.wicket.components.event;

import com.n4systems.model.AbstractEvent;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.CriteriaSection;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import java.text.NumberFormat;
import java.util.List;

public class EventFormViewPanel extends EventFormPanel {

    public EventFormViewPanel(String id, final IModel<? extends AbstractEvent> event, IModel<List<AbstractEvent.SectionResults>> results) {
        super(id, event, results);
        setVisible(!results.getObject().isEmpty());
    }

    @Override
    protected Panel getCriteriaSectionPanel(Class<? extends AbstractEvent> eventClass, PropertyModel<List<CriteriaResult>> results) {
        return new CriteriaSectionViewPanel("criteriaPanel", results);
    }

    @Override
    protected Component getSectionScore(String id, IModel<CriteriaSection> criteriaSectionModel) {
        return new Label(id, Model.of(getScoresForSections().get(criteriaSectionModel.getObject())))
                .setVisible(event.getObject().getType().isDisplaySectionTotals());
    }

    @Override
    protected Component getSectionScorePercentage(String id, IModel<CriteriaSection> criteriaSectionModel) {
        Double percentage = getScorePercentageForSections().get(criteriaSectionModel.getObject());
        return new Label(id, NumberFormat.getPercentInstance().format(percentage))
                .setVisible(event.getObject().getType().isDisplayScorePercentage());
    }
}
