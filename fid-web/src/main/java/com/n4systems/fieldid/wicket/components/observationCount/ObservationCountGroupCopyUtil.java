package com.n4systems.fieldid.wicket.components.observationCount;

import com.n4systems.model.ObservationCount;
import com.n4systems.model.ObservationCountGroup;

import java.util.List;
import java.util.stream.Collectors;

public class ObservationCountGroupCopyUtil {

    public ObservationCountGroup copy(ObservationCountGroup group) {
        ObservationCountGroup newGroup = new ObservationCountGroup();

        newGroup.setTenant(group.getTenant());
        newGroup.setName(group.getName());

        List<ObservationCount> observationCountList = group.getObservationCounts()
                .stream()
                .map(item -> copyObservationCount(item))
                .collect(Collectors.toList());

        newGroup.setObservationCounts(observationCountList);

        return newGroup;
    }

    private ObservationCount copyObservationCount(ObservationCount observationCount) {
        ObservationCount newObservationCount = new ObservationCount(observationCount.getTenant());
        newObservationCount.setName(observationCount.getName());
        newObservationCount.setCounted(observationCount.isCounted());
        newObservationCount.setState(observationCount.getEntityState());
        return newObservationCount;
    }

}
