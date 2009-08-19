package com.n4systems.fieldid.actions.safetyNetwork;

import java.util.List;

import org.jboss.util.NotImplementedException;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.SafetyNetworkManager;
import com.n4systems.exceptions.NoAccessToTenantException;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.model.Tenant;
import com.n4systems.services.safetyNetwork.CatalogService;
import com.n4systems.services.safetyNetwork.SafetyNetworkAccessService;

public class SafetyNetworkCrud extends AbstractAction {
	private static final long serialVersionUID = 1L;
//	private static Logger logger = Logger.getLogger(SafetyNetworkCrud.class);

//	private SafetyNetworkManager safetyNetworkManager;
	private String fidAC;
//	private Tenant myTenant;
	private SafetyNetworkAccessService safetyNetwork;
	// private ManufacturerOrganization manufacturerTenant;
	
	public SafetyNetworkCrud(PersistenceManager persistenceManager, SafetyNetworkManager safetyNetworkManager) {
		super(persistenceManager);
//		this.safetyNetworkManager = safetyNetworkManager;
	}

	public String doList() {
		safetyNetwork = new SafetyNetworkAccessService(persistenceManager, getTenant());
		return SUCCESS;
	}

	// TODO: tenant_refactor
	public String doAdd() {
		throw new NotImplementedException();
		// if( getTenant().isManufacturer() ) {
		//			
		// InspectorOrganization linkingTenant =
		// safetyNetworkManager.findByFidAC( fidAC );
		// try {
		// if( linkingTenant != null ) {
		// getLinkedTenants();
		// manufacturerTenant = (ManufacturerOrganization)myTenant;
		// boolean found = false;
		// for( InspectorOrganization tenant :
		// manufacturerTenant.getLinkedInspectors() ) {
		// if( tenant.getId().equals( linkingTenant.getId() ) ) {
		// found = true;
		// }
		// }
		// // TODO: this linking should be done in the safety Network managers.
		// if( !found ) {
		// manufacturerTenant.getLinkedInspectors().add( linkingTenant );
		// persistenceManager.update( manufacturerTenant );
		// addActionMessage( "Link created with " +
		// linkingTenant.getDisplayName() );
		// } else {
		// addActionError( "Your company is already linked with " +
		// linkingTenant.getDisplayName() );
		// }
		// } else {
		// addActionError(
		// "There was no company with the given Field ID Access Code." );
		// }
		// } catch( Exception e ) {
		// addActionError( "Could not create the link between companies." );
		// logger.error( "error creating a link with " + fidAC, e );
		// manufacturerTenant = null ; //force a reload of the tenant with its
		// current network.
		// myTenant = null;
		// }
		//
		// } else {
		// addActionError( getText( "error.cannotaddcompaniestosafetynetwork" )
		// );
		// }
		// return doList();
	}

	public String getFidAC() {
		return fidAC;
	}

	public void setFidAC(String fidAC) {
		this.fidAC = fidAC;
	}

	// TODO: tenant_refactor
	public List<Tenant> getLinkedTenants() {
		throw new NotImplementedException();
		// if( myTenant == null ) {
		// String[] fetchFields = {"linkedTenants"};
		// if( getTenant().isManufacturer() ) {
		// myTenant = persistenceManager.find( ManufacturerOrganization.class,
		// getTenantId(), fetchFields );
		// } else {
		// myTenant = persistenceManager.find( InspectorOrganization.class,
		// getTenantId(), fetchFields );
		// }
		// }
		//		
		// return new ArrayList<Tenant>( myTenant.getLinkedTenants() );
	}

	public boolean tenantHasCatalog(Tenant linkedTenant) {
		try {
			CatalogService linkedCatalogAccess = safetyNetwork.getCatalogAccess(linkedTenant);
			return linkedCatalogAccess.hasCatalog();
		} catch (NoAccessToTenantException e) {
			return false;
		}
	}
}
