package com.n4systems.fieldid.wicket.components.event.criteria.edit;

import com.n4systems.fieldid.wicket.components.observationCount.ObservationCountCounterPanel;
import com.n4systems.model.ObservationCount;
import com.n4systems.model.ObservationCountCriteria;
import com.n4systems.model.ObservationCountCriteriaResult;
import com.n4systems.model.ObservationCountResult;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rrana on 2015-01-27.
 */
public class ObservationCountCriteriaEditPanel extends Panel {

    private IModel<ObservationCountCriteriaResult> result;
    private List<ObservationCount> observationCountList;
    private List<ObservationCountResult> observationCountResultList;

    private List<ObservationCountCounterPanel> panels;

    public ObservationCountCriteriaEditPanel(String id, final IModel<ObservationCountCriteriaResult> result) {
        super(id);

        this.result = result;

        panels = new ArrayList<>();
        observationCountResultList = new ArrayList<>();
        observationCountList = ((ObservationCountCriteria)result.getObject().getCriteria()).getObservationCountGroup().getObservationCounts();

        if(result.getObject().getObservationCountResults().size() > 0) {
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

        for(ObservationCountResult observationCountResult:observationCountResultList) {
            panels.add(new ObservationCountCounterPanel("counterPanels", Model.of(observationCountResult), true));
        }

        add(new ListView("counterPanel", panels) {
            protected void populateItem(ListItem item) {
                item.add((ObservationCountCounterPanel) item.getModelObject());
            }
        });
    }
}
