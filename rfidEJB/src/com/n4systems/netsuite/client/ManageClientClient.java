package com.n4systems.netsuite.client;

public class ManageClientClient extends AbstractNetsuiteClient {

	private Long netSuiteRecordId;
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNumber;
	private Long fieldId;
	private Long tenantId;
	private Boolean isPrimary;

	public ManageClientClient() {
		super(Object.class, "manageclient");
	}
	
	@Override
	protected void addRequestParameters() {
		if (firstName != null) {
			addRequestParameter("firstname", firstName);
		}

		if (lastName != null) {
			addRequestParameter("lastname", lastName);
		}

		if (email != null) {
			addRequestParameter("email", email);
		}

		if (phoneNumber != null) {
			addRequestParameter("phone", phoneNumber);
		}

		if (netSuiteRecordId != null) {
			addRequestParameter("nsrescordid", String.valueOf(netSuiteRecordId));
		}

		if (fieldId != null) {
			addRequestParameter("fieldid", String.valueOf(fieldId));
		}

		if (tenantId != null) {
			addRequestParameter("tenantid", String.valueOf(tenantId));
		}

		if (isPrimary != null) {
			addRequestParameter("isprimary", isPrimary ? "T" : "F");
		}
	}

	public ManageClientClient setNetSuiteRecordId(long netSuiteRecordId) {
		this.netSuiteRecordId = netSuiteRecordId;
		return this;
	}

	public ManageClientClient setFirstName(String firstName) {
		this.firstName = firstName;
		return this;
	}

	public ManageClientClient setLastName(String lastName) {
		this.lastName = lastName;
		return this;
	}

	public ManageClientClient setEmail(String email) {
		this.email = email;
		return this;
	}

	public ManageClientClient setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
		return this;
	}

	public ManageClientClient setFieldId(long fieldId) {
		this.fieldId = fieldId;
		return this;
	}

	public ManageClientClient setTenantId(long tenantId) {
		this.tenantId = tenantId;
		return this;
	}

	public ManageClientClient setPrimary(boolean isPrimary) {
		this.isPrimary = isPrimary;
		return this;
	}

}
