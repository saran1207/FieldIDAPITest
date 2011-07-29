package com.n4systems.fieldid.wicket.model.admin.tenants;

import java.io.Serializable;

import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.signuppackage.SignUpPackageDetails;
import com.n4systems.model.user.User;

public class AddTenantModel implements Serializable {
	private SignUpPackageDetails signUpPackage = SignUpPackageDetails.Free;
	private Tenant tenant = new Tenant();
	private User adminUser = new User();
	private PrimaryOrg primaryOrg = new PrimaryOrg();

	public SignUpPackageDetails getSignUpPackage() {
		return signUpPackage;
	}

	public void setSignUpPackage(SignUpPackageDetails signUpPackage) {
		this.signUpPackage = signUpPackage;
	}

	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	public User getAdminUser() {
		return adminUser;
	}

	public void setAdminUser(User adminUser) {
		this.adminUser = adminUser;
	}

	public PrimaryOrg getPrimaryOrg() {
		return primaryOrg;
	}

	public void setPrimaryOrg(PrimaryOrg primaryOrg) {
		this.primaryOrg = primaryOrg;
	}

}
