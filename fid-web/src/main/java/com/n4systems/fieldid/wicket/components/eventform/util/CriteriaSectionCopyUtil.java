package com.n4systems.fieldid.wicket.components.eventform.util;

import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaSection;

import java.util.List;

public class CriteriaSectionCopyUtil {

    public CriteriaSection copySection(CriteriaSection section, List<CriteriaSection> existingSections) {
        CriteriaSection copiedSection = new CriteriaSection();
        String newName = findUnusedNameBasedOn(section.getName(), existingSections);
        copiedSection.setTitle(newName);
        copyCriteriaInto(section, copiedSection);
        return copiedSection;
    }

    private void copyCriteriaInto(CriteriaSection section, CriteriaSection copiedSection) {
        CriteriaCopyUtil criteriaCopyUtil = new CriteriaCopyUtil();
        for (Criteria criteria : section.getCriteria()) {
            copiedSection.getCriteria().add(criteriaCopyUtil.copyCriteria(criteria));
        }
    }

    private String findUnusedNameBasedOn(String name, List<CriteriaSection> existingSections) {
        for (int candidateNameIndex = 2; ; candidateNameIndex++) {
            String candidateName = name + " ("+candidateNameIndex+")";
            if (!isNameUsed(candidateName, existingSections)) {
                return candidateName;
            }
        }
    }

    private boolean isNameUsed(String name, List<CriteriaSection> existingSections) {
        for (CriteriaSection section : existingSections) {
            if (section.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

}
