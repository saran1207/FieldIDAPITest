package com.n4systems.fieldid.wicket.components.event.criteria.view;

import com.n4systems.fieldid.wicket.components.observationCount.ObservationCountCounterPanel;
import com.n4systems.model.ObservationCount;
import com.n4systems.model.ObservationCountCriteria;
import com.n4systems.model.ObservationCountCriteriaResult;
import com.n4systems.model.ObservationCountResult;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rrana on 2015-02-03.
 */
public class ObservationCountCriteriaResultPanel extends Panel {

    private IModel<ObservationCountCriteriaResult> result;
    private List<ObservationCount> observationCountList;
    private List<ObservationCountResult> observationCountResultList;

    private List<ObservationCountCounterPanel> panels;

    public ObservationCountCriteriaResultPanel(String id, IModel<ObservationCountCriteriaResult> result) {
        super(id);

        this.result = result;

        panels = new ArrayList<>();
        observationCountResultList = new ArrayList<>();
        observationCountList = ((ObservationCountCriteria)result.getObject().getCriteria()).getObservationCountGroup().getObservationCounts();

        int numObservations = result.getObject().getObservationCountResults().size();
        if(numObservations > 0) {
            observationCountResultList = result.getObject().getObservationCountResults();
        } else {
            for (ObservationCount count : observationCountList) {
                ObservationCountResult temp = new ObservationCountResult();
                temp.setObservationCount(count);
                temp.setTenant(result.getObject().getTenant());
                observationCountResultList.add(temp);
            }
            result.getObject().setObservationCountResults(observationCountResultList);
        }

        add(new AttributeAppender("class", "observation-counter-items-" + numObservations).setSeparator(" "));

        for(ObservationCountResult observationCountResult:observationCountResultList) {
            panels.add(new ObservationCountCounterPanel("counterPanels", Model.of(observationCountResult), false));
        }

        add(new ListView("counterPanel", panels) {
            protected void populateItem(ListItem item) {
                item.add((ObservationCountCounterPanel) item.getModelObject());
            }
        });


    }

}
