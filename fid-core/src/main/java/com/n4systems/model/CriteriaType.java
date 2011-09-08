package com.n4systems.model;

import com.n4systems.model.api.Listable;

import java.util.ArrayList;
import java.util.List;

public enum CriteriaType implements Listable {

    ONE_CLICK("One-Click Button", "oneclick", OneClickCriteria.class, OneClickCriteriaResult.class),
    TEXT_FIELD("Text Field", "textfield", TextFieldCriteria.class, TextFieldCriteriaResult.class),
    COMBO_BOX("Combo Box", "select", ComboBoxCriteria.class, ComboBoxCriteriaResult.class),
    SELECT("Select Box", "select", SelectCriteria.class, SelectCriteriaResult.class),
    UNIT_OF_MEASURE("Unit of Measure", "unitofmeasure", UnitOfMeasureCriteria.class, UnitOfMeasureCriteriaResult.class),
    SIGNATURE("Signature", "signature", SignatureCriteria.class, SignatureCriteriaResult.class),
    DATE_FIELD("Date Field", "textfield", DateFieldCriteria.class, DateFieldCriteriaResult.class),
    SCORE("Score", "score", ScoreCriteria.class, ScoreCriteriaResult.class);

    private String description;
    private String reportIdentifier;
	private Class<? extends Criteria> criteriaClass;
	private Class<? extends CriteriaResult> resultClass;

    CriteriaType(String description, String reportIdentifier, Class<? extends Criteria> criteriaClass, Class<? extends CriteriaResult> resultClass) {
        this.description = description;
        this.reportIdentifier = reportIdentifier;
        this.criteriaClass = criteriaClass;
        this.resultClass = resultClass;
    }

    public String getDescription() {
        return description;
    }

    public String getReportIdentifier() {
        return reportIdentifier;
    }

	public Class<? extends Criteria> getCriteriaClass() {
		return criteriaClass;
	}
    
	public Class<? extends CriteriaResult> getResultClass() {
		return resultClass;
	}

	public static CriteriaType valueForCriteriaClass(Class<? extends Criteria> criteriaClass) {
		for (CriteriaType type:values()) {
			if (type.getCriteriaClass().equals(criteriaClass)) { 
				return type;
			}
		}
		return null;
	}

	public static CriteriaType valueForResultClass(Class<? extends CriteriaResult> resultClass) {
		for (CriteriaType type:values()) {
			if (type.getResultClass().equals(resultClass)) { 
				return type;
			}
		}
		return null;
	}

    public static List<String> choiceLabels() {
        List<String> labels = new ArrayList<String>(values().length);
        for (CriteriaType criteriaType : values()) {
            labels.add(criteriaType.getDescription());
        }
        return labels;
    }

    @Override
    public Object getId() {
        return reportIdentifier;
    }

    @Override
    public String getDisplayName() {
        return description;
    }
}
