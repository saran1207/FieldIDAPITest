package com.n4systems.fieldid.wicket.components.eventform.util;

import com.n4systems.model.ComboBoxCriteria;
import com.n4systems.model.OneClickCriteria;
import com.n4systems.model.Criteria;
import com.n4systems.model.SelectCriteria;
import com.n4systems.model.TextFieldCriteria;

import java.util.ArrayList;
import java.util.List;

public class CriteriaCopyUtil {

    public Criteria copyCriteria(Criteria criteria) {
        return copyCriteria(criteria, null);
    }

    public Criteria copyCriteria(Criteria criteria, List<Criteria> existingCriteria) {
        Criteria newCriteria = null;
        if (criteria instanceof OneClickCriteria) {
            newCriteria = copyOneClickCriteria((OneClickCriteria) criteria);
        } else if (criteria instanceof TextFieldCriteria){
            newCriteria = copyTextFieldCriteria((TextFieldCriteria) criteria);
        } else if (criteria instanceof SelectCriteria){
            newCriteria = copySelectCriteria((SelectCriteria) criteria);
        } else if (criteria instanceof ComboBoxCriteria) {
        	newCriteria = copyComboBoxCriteria((ComboBoxCriteria) criteria);
        }
        
        copyCommonFields(criteria, newCriteria, existingCriteria);
        return newCriteria;
    }

	private void copyCommonFields(Criteria criteria, Criteria newCriteria, List<Criteria> existingCriteria) {
        newCriteria.setRetired(criteria.isRetired());
        newCriteria.setTenant(criteria.getTenant());
        newCriteria.setRecommendations(copyList(criteria.getRecommendations()));
        newCriteria.setDeficiencies(copyList(criteria.getDeficiencies()));
        newCriteria.setModified(criteria.getModified());
        newCriteria.setModifiedBy(criteria.getModifiedBy());
        newCriteria.setCreated(criteria.getCreated());
        if (existingCriteria != null) {
            newCriteria.setDisplayText(findUnusedNameBasedOn(criteria.getDisplayText(), existingCriteria));
        } else {
            newCriteria.setDisplayText(criteria.getDisplayText());
        }
    }

    private Criteria copyTextFieldCriteria(TextFieldCriteria textFieldCriteria) {
        return new TextFieldCriteria();
    }
    
    private Criteria copySelectCriteria(SelectCriteria criteria) {
		SelectCriteria selectCriteria = new SelectCriteria();
		selectCriteria.setOptions(new ArrayList<String>(criteria.getOptions()));
		return selectCriteria;
	}
    
	private Criteria copyComboBoxCriteria(ComboBoxCriteria criteria) {
		ComboBoxCriteria comboBoxCriteria = new ComboBoxCriteria();
		comboBoxCriteria.setOptions(new ArrayList<String>(criteria.getOptions()));
		return comboBoxCriteria;
	}


    private Criteria copyOneClickCriteria(OneClickCriteria oneClickCriteria) {
        OneClickCriteria criteria = new OneClickCriteria();
        criteria.setPrincipal(oneClickCriteria.isPrincipal());
        criteria.setStates(oneClickCriteria.getStates());
        return criteria;
    }

    private String findUnusedNameBasedOn(String name, List<Criteria> existingCriteria) {
        for (int candidateNameIndex = 2; ; candidateNameIndex++) {
            String candidateName = name + " ("+candidateNameIndex+")";
            if (!isNameUsed(candidateName, existingCriteria)) {
                return candidateName;
            }
        }
    }

    private boolean isNameUsed(String name, List<Criteria> existingSections) {
        for (Criteria section : existingSections) {
            if (section.getDisplayText().equals(name)) {
                return true;
            }
        }
        return false;
    }

    private List<String> copyList(List<String> list) {
        List<String> copiedList = new ArrayList<String>(list.size());
        copiedList.addAll(list);
        return copiedList;
    }

}
