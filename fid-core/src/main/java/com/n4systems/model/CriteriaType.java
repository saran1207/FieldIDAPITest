package com.n4systems.model;

public enum CriteriaType {

    ONE_CLICK("One-Click Button", "oneclick", OneClickCriteria.class, OneClickCriteriaResult.class),
    TEXT_FIELD("Text Field", "textfield", TextFieldCriteria.class,TextFieldCriteriaResult.class),
    COMBO_BOX("Combo Box", "select", ComboBoxCriteria.class,ComboBoxCriteriaResult.class),
    SELECT("Select Box", "select", SelectCriteria.class,SelectCriteriaResult.class),
    UNIT_OF_MEASURE("Unit of Measure", "unitofmeasure", UnitOfMeasureCriteria.class,UnitOfMeasureCriteriaResult.class),
    SIGNATURE("Signature", "signature", SignatureCriteria.class,SignatureCriteriaResult.class),
    DATE_FIELD("Date Field", "textfield", DateFieldCriteria.class,DateFieldCriteriaResult.class);

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
    
}
