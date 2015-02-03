package com.n4systems.fieldid.wicket.components.event.criteria.view;

import com.n4systems.fieldid.wicket.components.observationCount.ObservationCountCounterPanel;
import com.n4systems.model.ObservationCount;
import com.n4systems.model.ObservationCountCriteria;
import com.n4systems.model.ObservationCountCriteriaResult;
import com.n4systems.model.ObservationCountResult;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rrana on 2015-02-03.
 */
public class ObservationCountCriteriaResultPanel extends Panel {

    private List<ObservationCount> observationCountList;
    private List<ObservationCountResult> observationCountResultList;

    public ObservationCountCriteriaResultPanel(String id, IModel<ObservationCountCriteriaResult> resultModel) {
        super(id);

        if(resultModel.getObject().getObservationCountResults().size() == 0) {
            observationCountResultList = new ArrayList<>();
            observationCountList = ((ObservationCountCriteria)resultModel.getObject().getCriteria()).getObservationCountGroup().getObservationCounts();

            for(ObservationCount count:observationCountList){
                ObservationCountResult temp = new ObservationCountResult();
                temp.setObservationCount(count);
                observationCountResultList.add(temp);
            }

            for(ObservationCountResult observationCountResult:observationCountResultList) {
                add(new ObservationCountCounterPanel(id, Model.of(observationCountResult), false));
            }
        } else {
            for(ObservationCountResult observationCountResult:resultModel.getObject().getObservationCountResults()) {
                add(new ObservationCountCounterPanel(id, Model.of(observationCountResult), false));
            }
        }

    }

}
