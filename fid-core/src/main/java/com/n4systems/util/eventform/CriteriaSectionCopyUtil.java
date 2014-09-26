package com.n4systems.util.eventform;

import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaSection;

import java.util.List;

public class CriteriaSectionCopyUtil {

    public CriteriaSection copySection(CriteriaSection section) {
        return copySection(section, null);
    }

    public CriteriaSection copySection(CriteriaSection section, List<CriteriaSection> existingSections) {
        CriteriaSection copiedSection = new CriteriaSection();
        copiedSection.setOldId(section.getId());
        if (existingSections != null) {
            copiedSection.setTitle(findUnusedNameBasedOn(section.getName(), existingSections));
        } else {
            copiedSection.setTitle(section.getName());
        }
        copiedSection.setOptional(section.isOptional());
        copiedSection.setModifiedBy(section.getModifiedBy());
        copiedSection.setModified(section.getModified());
        copiedSection.setCreated(section.getCreated());
        copiedSection.setTenant(section.getTenant());
        copyCriteriaInto(section, copiedSection);
        return copiedSection;
    }

    private void copyCriteriaInto(CriteriaSection section, CriteriaSection copiedSection) {
        CriteriaCopyUtil criteriaCopyUtil = new CriteriaCopyUtil();
        for (Criteria oldCriteria : section.getCriteria()) {
            Criteria newCriteria = criteriaCopyUtil.copyCriteria(oldCriteria);

            copiedSection.getCriteria().add(newCriteria);
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
