package com.n4systems.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.n4systems.util.StringListingPair;

@Entity
@Table(name = "Organization")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "accountDiscriminator", discriminatorType = DiscriminatorType.STRING)
public abstract class Organization extends OrganizationalUnit {
	private static final long serialVersionUID = 1L;
	
	private String certificateName;
	private boolean usingSerialNumber = true;
	private String serialNumberFormat;
	private String fidAC;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Organization parent;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="parent")
	private List<Organization> children = new ArrayList<Organization>();
	
	public Organization() {
		super(OrganizationalUnitType.ORGANIZATION);
	}

	public Organization(String name) {
		this();
		setName(name);
	}
	
	public String getCertificateName() {
		return certificateName;
	}

	public void setCertificateName(String certificateName) {
		this.certificateName = certificateName;
	}

	public boolean isUsingSerialNumber() {
		return usingSerialNumber;
	}

	public void setUsingSerialNumber(boolean usingSerialNumber) {
		this.usingSerialNumber = usingSerialNumber;
	}

	public String getSerialNumberFormat() {
		return serialNumberFormat;
	}

	public void setSerialNumberFormat(String serialNumberFormat) {
		this.serialNumberFormat = serialNumberFormat;
	}
	
	public Organization getParent() {
		return parent;
	}

	public void setParent(Organization parent) {
		this.parent = parent;
	}

	public List<Organization> getChildren() {
		return children;
	}

	public void setChildren(List<Organization> children) {
		this.children = children;
	}
	
	public void attachChild(Organization child) {
		if(!children.contains(child)) {
			child.setParent(this);
			children.add(child);
		}
	}
	
	public void detachChild(Organization child) {
		if(children.contains(child)) {
			children.remove(child);
			child.setParent(null);
		}
	}
	
	public OrganizationalUnit getRoot() {
		return getRoot(this);
	}

	public List<Organization> getRootTreeList() {
		return getRootTreeList(this);
	}
	
	public List<Organization> getChildTreeList() {
		return getChildTreeList(this);
	}
	
	public static List<Organization> getRootTreeList(Organization unit) {
		return getChildTreeList(getRoot(unit));
	}
	
	public static List<Organization> getChildTreeList(Organization unit) {
		List<Organization> treeList = new ArrayList<Organization>();
		populateChildTreeList(unit, treeList);
		return treeList;
	}
	
	public static void populateChildTreeList(Organization unit, List<Organization> orgList) {
		orgList.add(unit);
		for(Organization child: unit.getChildren()) {
			populateChildTreeList(child, orgList);
		}
	}
	
	public static Organization getRoot(Organization unit) {
		Organization currentOrg = unit;
		while(currentOrg.getParent() != null) {
			currentOrg = currentOrg.getParent();
		}
		return currentOrg;
	}

	
	public String getFidAC() {
		return fidAC;
	}

	public void setFidAC(String snac) {
		this.fidAC = snac;
	}
	
	public static List<StringListingPair> tenantTypes() {
		List<StringListingPair> tenantTypes = new ArrayList<StringListingPair>();
		tenantTypes.add( new StringListingPair( "inspector", "Inspector Account" ) );
		tenantTypes.add( new StringListingPair( "manufacturer", "Manufacturer Account" ) );
		tenantTypes.add( new StringListingPair( "siteSafety", "Site Safety Account" ) );
		return tenantTypes;
	}
	
	public boolean isManufacturer() {
		return (this instanceof ManufacturerOrganization);
	}
	
	public boolean isInspector() {
		return (this instanceof InspectorOrganization);
	}
	
}
