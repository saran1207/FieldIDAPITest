package com.n4systems.model;


public enum ExtendedFeature {
	Integration("feature.integration"),
	Projects("feature.jobs"),
	Branding("feature.branding"),
	EmailAlerts("feature.email_alerts"),
	ManufacturerCertificate("feature.manufacturer_certificate"),
	JobSites("feature.job_sites"),
	AssignedTo("feature.assigned_to"),
	AdvancedLocation("feature.advanced_location"),
	ProofTestIntegration("feature.proof_test_integration"), 
	OrderDetails("feature.order_details"),
    LotoProcedures("feature.loto_procedures"),
    GlobalSearch("feature.global_search"),
    AdvancedEventSearch("feature.advanced_event_search"),
    CriteriaTrends("feature.criteria_trends"),
    UserGroupFiltering("feature.user_group_filtering");
	
	private String label;
	
	private ExtendedFeature(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
	
	public String getName() {
		return name();
	}

	public String featureEnabledMethodName() {
		String featureName = name();
		return "is" + featureName + "Enabled";
	}
}

