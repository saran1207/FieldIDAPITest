package com.n4systems.fieldid.actions.event;

import com.n4systems.fieldid.actions.event.viewmodel.CriteriaResultWebModel;

import java.util.List;

public interface ActionWithCriteriaResults {

    public List<CriteriaResultWebModel> getCriteriaResults();

}
