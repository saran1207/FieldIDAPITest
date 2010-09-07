package com.n4systems.model.safetynetwork;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.parents.EntityWithOwner;

@Entity
@Table(name="typedorgconnections")
public class TypedOrgConnection extends EntityWithOwner {
	public enum ConnectionType {
		CUSTOMER("label.customer"), VENDOR("label.vendor"), CATALOG_ONLY("label.catalog_only");
		
		private String label;

		private ConnectionType(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}
		
		
	}
	
	@ManyToOne(fetch=FetchType.EAGER, optional=false)
	@JoinColumn(name="connectedorg_id", nullable=false)
	private BaseOrg connectedOrg;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable=false)
	private ConnectionType connectionType;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="orgconnection_id")
	private OrgConnection orgConnection;
	
	public TypedOrgConnection() {
	}

	public TypedOrgConnection(Tenant tenant, BaseOrg owner) {
		super(tenant, owner);
	}


	public BaseOrg getConnectedOrg() {
		return connectedOrg;
	}

	// The saver will handle setting this value to the house account.
	protected void setConnectedOrg(BaseOrg connectedOrg) {
		this.connectedOrg = connectedOrg;
	}

	public OrgConnection getOrgConnection() {
		return orgConnection;
	}

	public void setOrgConnection(OrgConnection orgConnection) {
		this.orgConnection = orgConnection;
	}

	public ConnectionType getConnectionType() {
		return connectionType;
	}
	
	public boolean isCustomerConnection(){
		return connectionType.equals(ConnectionType.CUSTOMER);
	}
	
	public boolean isVendorConnection(){
		return connectionType.equals(ConnectionType.VENDOR);
	}
	
	public boolean isCatalogOnlyConnection(){
		return connectionType.equals(ConnectionType.CATALOG_ONLY);
	}
	
	public void setConnectionType(ConnectionType connectionType) {
		this.connectionType = connectionType;
	}
	


}
