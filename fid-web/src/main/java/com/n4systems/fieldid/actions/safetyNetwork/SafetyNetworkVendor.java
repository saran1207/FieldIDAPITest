package com.n4systems.fieldid.actions.safetyNetwork;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.model.Product;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.security.OrgOnlySecurityFilter;
import com.n4systems.reporting.PathHandler;
import com.n4systems.tools.Pager;
import com.n4systems.util.persistence.QueryBuilder;

@SuppressWarnings("serial")
public class SafetyNetworkVendor extends AbstractCrud{
	
	private static final int PAGE_SIZE = 10;
	private static Logger logger = Logger.getLogger(SafetyNetworkVendor.class);
	private PrimaryOrg vendor;

	public SafetyNetworkVendor(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	@Override
	protected void initMemberFields() {
		
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		vendor = getLoaderFactory().createVendorLinkedOrgLoader().setLinkedOrgId(uniqueID).load();
	}
	
	private Pager<Product> page;
	
	@SkipValidation
	public String doListPreAssigned() {
		QueryBuilder<Product> queryBuilder = new QueryBuilder<Product>(Product.class, new OrgOnlySecurityFilter(vendor));
		//queryBuilder.addSimpleWhere("owner.linkedOrg", getPrimaryOrg());
		queryBuilder.addPostFetchPaths("infoOptions");
		
		//page = queryBuilder.getPaginatedResults(em, getCurrentPage(), PAGE_SIZE);
		
		try {
			page = persistenceManager.findAllPaged(queryBuilder, getCurrentPage(), PAGE_SIZE);
			return SUCCESS;
		} catch (InvalidQueryException iqe) {
			logger.error("couldn't load the list of pre assigned assets", iqe);
			return ERROR;
		}
	}
	
	public String doShow() {
		return SUCCESS;
	}

	
	public Pager<Product> getPage() {	
		return page;
	}
	
	public PrimaryOrg getVendor() {		
		return vendor;
	}

	public String getLogo() {
		return PathHandler.getTenantLogo(vendor.getTenant()).getAbsolutePath();
	}
	
}
