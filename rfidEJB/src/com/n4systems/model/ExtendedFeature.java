package com.n4systems.model;

/**
 * this enum represents the sets of extended features for the system.
 * 
 * these are feature sets that not all tenants get with the system.
 * 
 * Current supported extended features.
 * 
 * - integration.
 * 
 * @author aaitken
 *
 */

public enum ExtendedFeature {
	Integration("feature.integration", "allows access to orders."),
	Compliance("feature.compliance", "allows access to compliance check and risk analysis."),
	JobSites("feature.job_sites", "allows the use of jobsites to control the assignment of assets and inspections."),
	Projects("feature.jobs", "allows the use of projects to track larger safety systems."),
	Branding("feature.branding", "allows the user to up load their own image for the tenant logo."),
	PartnerCenter("feature.partner_center", "allows the tenant to add customer users"),
	EmailAlerts("feature.email_alerts", "allows the tenant to use Notifications");
	
	private String label;
	private String description;
	
	private ExtendedFeature( String label, String description ) {
		this.label = label;
		this.description = description;
	}

	public String getLabel() {
		return label;
	}
	
	public String getName() {
		return name();
	}

	public String getDescription() {
		return description;
	}
	
}

