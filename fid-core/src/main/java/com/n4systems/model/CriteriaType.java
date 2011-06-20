package com.n4systems.model;

public enum CriteriaType {

    ONE_CLICK("One-Click Button", "oneclick", OneClickCriteriaResult.class),
    TEXT_FIELD("Text Field", "textfield", TextFieldCriteriaResult.class),
    COMBO_BOX("Combo Box", "select", ComboBoxCriteriaResult.class),
    SELECT("Select Box", "select", SelectCriteriaResult.class),
    UNIT_OF_MEASURE("Unit of Measure", "unitofmeasure", UnitOfMeasureCriteriaResult.class),
    SIGNATURE("Signature", "signature", SignatureCriteriaResult.class),
    DATE_FIELD("Date Field", "textfield", DateFieldCriteriaResult.class);

    private String description;
    private String reportIdentifier;
	private Class<? extends CriteriaResult> resultClass;

    CriteriaType(String description, String reportIdentifier, Class<? extends CriteriaResult> clazz) {
        this.description = description;
        this.reportIdentifier = reportIdentifier;
        this.resultClass = clazz;
    }

    public String getDescription() {
        return description;
    }

    public String getReportIdentifier() {
        return reportIdentifier;
    }

	public Class<? extends CriteriaResult> getResultClass() {
		return resultClass;
	}

    
}
