package com.n4systems.fieldid.wicket.model.eventform;

import com.n4systems.fieldid.service.event.ScoreService;
import com.n4systems.fieldid.wicket.model.FieldIDSpringModel;
import com.n4systems.model.ScoreGroup;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class ScoreGroupsForTenantModel extends FieldIDSpringModel<List<ScoreGroup>> {

    @SpringBean
    private ScoreService scoreService;
    private IModel<ScoreGroup> groupToIncludeIfMissing;

    public ScoreGroupsForTenantModel() { }

    public ScoreGroupsForTenantModel(IModel<ScoreGroup> groupToIncludeIfMissing) {
        this.groupToIncludeIfMissing = groupToIncludeIfMissing;
    }

    @Override
    protected List<ScoreGroup> load() {
        List<ScoreGroup> scoreGroups = scoreService.getScoreGroups();
        if (groupToIncludeIfMissing != null && groupToIncludeIfMissing.getObject() != null && !scoreGroups.contains(groupToIncludeIfMissing.getObject())) {
            scoreGroups.add(0, groupToIncludeIfMissing.getObject());
        }
        return scoreGroups;
    }

    @Override
    public void detach() {
        super.detach();
        if (groupToIncludeIfMissing != null) {
            groupToIncludeIfMissing.detach();
        }
    }

}
