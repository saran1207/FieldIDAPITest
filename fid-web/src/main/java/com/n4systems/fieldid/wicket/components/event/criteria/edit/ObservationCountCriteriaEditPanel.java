package com.n4systems.fieldid.wicket.components.event.criteria.edit;

import com.n4systems.fieldid.wicket.components.observationCount.ObservationCountCounterPanel;
import com.n4systems.model.*;
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

    public ObservationCountCriteriaEditPanel(String id, final IModel<ObservationCountCriteriaResult> result) {
        super(id);
        this.result = result;

        observationCountResultList = new ArrayList<>();
        observationCountList = ((ObservationCountCriteria)result.getObject().getCriteria()).getObservationCountGroup().getObservationCounts();

        for(ObservationCount count:observationCountList){
            ObservationCountResult temp = new ObservationCountResult();
            temp.setObservationCount(count);
            observationCountResultList.add(temp);
        }

        for(ObservationCountResult observationCountResult:observationCountResultList) {
            int i=0;
            add(new ObservationCountCounterPanel(id+i, Model.of(observationCountResult), true));
            i++;
        }
    }

}
