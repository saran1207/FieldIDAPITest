package com.n4systems.fieldid.wicket.model.eventform;

import com.n4systems.fieldid.service.event.ScoreService;
import com.n4systems.fieldid.wicket.model.FieldIDSpringModel;
import com.n4systems.model.ScoreGroup;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class ScoreGroupsForTenantModel extends FieldIDSpringModel<List<ScoreGroup>> {

    @SpringBean
    private ScoreService scoreService;

    @Override
    protected List<ScoreGroup> load() {
        return scoreService.getScoreGroups();
    }

}
