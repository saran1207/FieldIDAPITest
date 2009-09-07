package com.n4systems.model.user;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.n4systems.model.orgs.BaseOrg;


public class AbstractUser {
	
	private String modifiedBy;
	private String userID;
	private String archivedUserID;
	private String firstName;
	private String lastName;
	private String emailAddress;	
	private String timeZoneID;
	private String hashPassword;
	private String position;
	private String initials;
	
	private String resetPasswordKey;
	private String hashSecurityCardNumber;
	
	// XXX - this will be removed when Customer becomes a subclass of OrganizationUnit
	private Long r_EndUser;
	private Long r_Division;
	
	private boolean active = false;
	private boolean deleted = false;
	private boolean system = false;
	private boolean admin = false;
	
	@Column(name="permissions", nullable=false)
	private int permissions = 0; // permissions should always start out empty
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "organization_id")
	private BaseOrg organization;
	
}
