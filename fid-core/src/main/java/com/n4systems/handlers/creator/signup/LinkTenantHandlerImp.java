package com.n4systems.handlers.creator.signup;


import com.n4systems.handlers.creator.signup.model.AccountPlaceHolder;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.safetynetwork.CatalogOnlyConnectionSaver;
import com.n4systems.model.safetynetwork.OrgConnection;
import com.n4systems.model.safetynetwork.OrgConnectionSaver;
import com.n4systems.model.safetynetwork.TypedOrgConnection;
import com.n4systems.model.safetynetwork.TypedOrgConnection.ConnectionType;
import com.n4systems.model.user.User;
import com.n4systems.persistence.Transaction;

public class LinkTenantHandlerImp implements LinkTenantHandler {

	private final OrgConnectionSaver orgConnectionSaver;
	private final CatalogOnlyConnectionSaver catalogOnlyConnectionSaver;
	
	private AccountPlaceHolder accountPlaceHolder;
	private PrimaryOrg referrerOrg;

	public LinkTenantHandlerImp(OrgConnectionSaver orgConnectionSaver, CatalogOnlyConnectionSaver catalogOnlyConnectionSaver) {
		super();
		this.orgConnectionSaver = orgConnectionSaver;
		this.catalogOnlyConnectionSaver = catalogOnlyConnectionSaver;
	}


	public void link(Transaction transaction) {
		orgConnectionSaver.save(transaction, createOrgConnection());
		catalogOnlyConnectionSaver.save(transaction, createCatalogOnlyConnection());
	}
	
	
	protected OrgConnection createOrgConnection() {
		OrgConnection conn = new OrgConnection(referrerOrg, getPrimaryOrg());
		conn.setModifiedBy(getAdminUser());
		return conn;
	}


	private TypedOrgConnection createCatalogOnlyConnection() {
		TypedOrgConnection connection = new TypedOrgConnection();
		connection.setOwner(getPrimaryOrg());
		connection.setTenant(getPrimaryOrg().getTenant());
		connection.setConnectionType(ConnectionType.CATALOG_ONLY);
		return connection;
	}
	
	protected User getAdminUser() {
		return accountPlaceHolder.getAdminUser();
	}


	protected PrimaryOrg getPrimaryOrg() {
		return accountPlaceHolder.getPrimaryOrg();
	}



	public LinkTenantHandler setAccountPlaceHolder(AccountPlaceHolder accountPlaceHolder) {
		this.accountPlaceHolder = accountPlaceHolder;
		return this;
	}



	public LinkTenantHandler setReferrerOrg(PrimaryOrg referrerOrg) {
		this.referrerOrg = referrerOrg;
		return this;
	}
	
}
