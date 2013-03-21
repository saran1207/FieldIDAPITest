package com.n4systems.model.tenant;

import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.model.security.AccountPolicy;
import com.n4systems.model.security.PasswordPolicy;
import com.n4systems.model.user.Assignable;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserGroup;

import javax.persistence.*;

@SuppressWarnings("serial")
@Entity
@Table(name = "tenant_settings")
public class TenantSettings extends EntityWithTenant {
	private boolean secondaryOrgsEnabled;
	
	@Embedded
	private UserLimits userLimits = new UserLimits();
	
	@Embedded
	private AccountPolicy accountPolicy = new AccountPolicy();
	
	@Embedded
	private PasswordPolicy passwordPolicy = new PasswordPolicy();
	
	private boolean gpsCapture;

	private String supportUrl;
    
    private String logoutUrl;

    @ManyToOne
    @JoinColumn(name = "approval_user_id")
    private User approvalUser;

    @ManyToOne
    @JoinColumn(name = "approval_user_group_id")
    private UserGroup approvalUserGroup;

    public boolean isSecondaryOrgsEnabled() {
		return secondaryOrgsEnabled;
	}

	public void setSecondaryOrgsEnabled(boolean secondaryOrgsEnabled) {
		this.secondaryOrgsEnabled = secondaryOrgsEnabled;
	}

	public UserLimits getUserLimits() {
		return userLimits;
	}

	public void setUserLimits(UserLimits userLimits) {
		this.userLimits = userLimits;
	}

	public void setAccountPolicy(AccountPolicy accountPolicy) {
		this.accountPolicy = accountPolicy;
	}

	public AccountPolicy getAccountPolicy() {
		return accountPolicy;
	}

	public void setPasswordPolicy(PasswordPolicy passwordPolicy) {
		this.passwordPolicy = passwordPolicy;
	}

	public PasswordPolicy getPasswordPolicy() {
		return passwordPolicy;
	}

	public boolean isGpsCapture() {
		return gpsCapture;
	}

	public void setGpsCapture(boolean gpsCapture) {
		this.gpsCapture = gpsCapture;
	}

	public void setSupportUrl(String supportUrl) {
		this.supportUrl = supportUrl;
	}

	public String getSupportUrl() {
		return supportUrl;
	}

    public String getLogoutUrl() {
        return logoutUrl;
    }

    public void setLogoutUrl(String logoutUrl) {
        this.logoutUrl = logoutUrl;
    }

    public User getApprovalUser() {
        return approvalUser;
    }

    public void setApprovalUser(User approvalUser) {
        this.approvalUserGroup = null;
        this.approvalUser = approvalUser;
    }

    public UserGroup getApprovalUserGroup() {
        return approvalUserGroup;
    }

    public void setApprovalUserGroup(UserGroup approvalUserGroup) {
        this.approvalUser = null;
        this.approvalUserGroup = approvalUserGroup;
    }

    @Transient
    public Assignable getApprovalUserOrGroup() {
        return approvalUser != null ?  approvalUser : approvalUserGroup;
    }

    public void setApprovalUserOrGroup(Assignable assignee) {
        if (assignee instanceof User) {
            setApprovalUser((User) assignee);
        } else if (assignee instanceof UserGroup) {
            setApprovalUserGroup((UserGroup) assignee);
        } else if (assignee == null) {
            this.approvalUser = null;
            this.approvalUserGroup = null;
        }
    }
}
