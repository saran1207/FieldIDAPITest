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
	Projects("feature.jobs", "allows the use of projects to track larger safety systems."),
	Branding("feature.branding", "allows the user to up load their own image for the tenant logo."),
	EmailAlerts("feature.email_alerts", "allows the tenant to use Notifications"),
	ReadOnlyUser("feature.partner_center", "allows the tenant to add customer users"),
	MultiLocation("feature.multi_location", "allows the tenant to create as many secondary orgs."), 
	UnlimitedLinkedAssets("feature.unlimited_linked_assets", "Assets registered against this Tenant will not count towards their asset limit"),
	ManufacturerCertificate("feature.manufacturer_certificate", "allows the use of manufacturer certificates"),
	JobSites("feature.job_sites", "allows the use of jobsites to control the assignment of assets and events."),
	AssignedTo("feature.assigned_to", "allows users to be assigned to assets."),
	AdvancedLocation("feature.advanced_location", "Allows for a location hierarchy."),
	ProofTestIntegration("feature.proof_test_integration", "Allows you to conduct Proof Tests on your equipment with your Test Bed using Field ID. Currently, Field ID integrates with Roberts, Chant, Wirop, and National Automation"),
	CustomCert("feature.cust_cert", "This tenant gets a custom cert creation included"), 
	DedicatedProgramManager("feature.dedicated_program_manager", "Some individual will be assigned to this tenant."), 
	AllowIntegration("feature.allow_integration", "Says that someone could have integration turned on.");
	
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

	public String featureEnabledMethodName() {
		String featureName = name();
		return "is" + featureName + "Enabled";
	}
	
}

