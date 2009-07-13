package com.n4systems.model;

import com.n4systems.model.api.Listable;
import com.n4systems.model.api.NamedEntity;
import com.n4systems.model.parents.AbstractEntity;

import rfid.ejb.entity.UserBean;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@MappedSuperclass
abstract public class OrganizationalUnit extends AbstractEntity implements NamedEntity, Listable<Long> {
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="r_tenant")
	private TenantOrganization tenant;	
	private String name;
	private String displayName;
	private String adminEmail;
	
	@Enumerated(EnumType.STRING)
	private OrganizationalUnitType type;
	
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name="r_addressinfo")
	private AddressInfo addressInfo;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="organization", targetEntity=UserBean.class)
	private List<UserBean> users;
	
	public OrganizationalUnit() {}
	
	public OrganizationalUnit(OrganizationalUnitType type) {
		this();
		setType(type);
	}
	
	public OrganizationalUnit(OrganizationalUnitType type, String name) {
		this(type);
		setName(name);
	}

	
	@Override
	protected void onCreate() {
		super.onCreate();
		trimLowerName();
	}
	
	@Override
	protected void onUpdate() {
		super.onUpdate();
		trimLowerName();
	}
	

	private void trimLowerName() {
		name = (name != null) ? name.trim().toLowerCase() : null;
		displayName = (displayName != null) ? displayName.trim() : null;
	}
	
	public TenantOrganization getTenant() {
		return tenant;
	}
	
	public void setTenant( TenantOrganization tenant) {
		this.tenant = tenant;
	}
	
	public OrganizationalUnitType getType() {
		return type;
	}

	public void setType(OrganizationalUnitType type) {
		this.type = type;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	public String getAdminEmail() {
		return adminEmail;
	}

	public void setAdminEmail(String adminEmail) {
		this.adminEmail = adminEmail;
	}

	public AddressInfo getAddressInfo() {
		return addressInfo;
	}

	public void setAddressInfo(AddressInfo addressInfo) {
		this.addressInfo = addressInfo;
	}

	public List<UserBean> getUsers() {
		return users;
	}

	public void setUsers(List<UserBean> users) {
		this.users = users;
	}


	
	
	
}
