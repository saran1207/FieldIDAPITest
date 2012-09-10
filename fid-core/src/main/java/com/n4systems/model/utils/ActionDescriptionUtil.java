package com.n4systems.model.utils;

import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.CriteriaSection;
import com.n4systems.model.Event;

public class ActionDescriptionUtil {

    public static String getDescription(Event triggerEvent, CriteriaResult sourceCriteriaResult) {
        if (triggerEvent == null || sourceCriteriaResult == null) {
            return "Unknown";
        }

        CriteriaAndSection sourceCriteriaInfo = getSourceCriteriaInfo(triggerEvent, sourceCriteriaResult);

        if (sourceCriteriaInfo == null) {
            return "Unknown";
        }

        return sourceCriteriaInfo.section.getName() + " > " + sourceCriteriaInfo.criteria.getDisplayName() + " > " + sourceCriteriaResult.getResultString();
    }

    private static CriteriaAndSection getSourceCriteriaInfo(Event triggerEvent, CriteriaResult sourceCriteriaResult) {
        for (CriteriaSection section : triggerEvent.getEventForm().getSections()) {
            for (Criteria criteria : section.getCriteria()) {
                if (sourceCriteriaResult.getCriteria().getId().equals(criteria.getId())) {
                    return new CriteriaAndSection(criteria, section);
                }
            }
        }
        return null;
    }

    static class CriteriaAndSection {
        public Criteria criteria;
        public CriteriaSection section;

        public CriteriaAndSection(Criteria criteria, CriteriaSection section) {
            this.section = section;
            this.criteria = criteria;
        }
    }
}
