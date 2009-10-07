package com.n4systems.fieldid.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.CustomerManager;
import com.n4systems.ejb.MassUpdateManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.UpdateFailureException;
import com.n4systems.fieldid.actions.search.InspectionReportAction;
import com.n4systems.fieldid.actions.utils.OwnerPicker;
import com.n4systems.fieldid.viewhelpers.InspectionSearchContainer;
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionBook;
import com.n4systems.model.inspectionbook.InspectionBookListLoader;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.util.ListingPair;
import com.opensymphony.xwork2.Preparable;

public class InspectionMassUpdate extends MassUpdate implements Preparable {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger( InspectionMassUpdate.class );
	
	private InspectionSearchContainer criteria;
	private Inspection inspection = new Inspection();
	
	private OwnerPicker ownerPicker;

	public InspectionMassUpdate(CustomerManager customerManager, MassUpdateManager massUpdateManager, PersistenceManager persistenceManager) {
		super( customerManager, massUpdateManager, persistenceManager);
	}
	
	
	public void prepare() throws Exception {
		ownerPicker = new OwnerPicker(getLoaderFactory().createFilteredIdLoader(BaseOrg.class), inspection);
	}


	private void applyCriteriaDefaults() {
		
		setOwnerId(criteria.getOwnerId());
		
		setInspectionBook( criteria.getInspectionBook() );
	}
	
	private boolean findCriteria() {
		if(getSession().containsKey( InspectionReportAction.REPORT_CRITERIA ) && getSession().get( InspectionReportAction.REPORT_CRITERIA ) != null) {
			criteria = (InspectionSearchContainer)getSession().get( InspectionReportAction.REPORT_CRITERIA );
		}
		
		if( criteria == null || searchId == null || !searchId.equals( criteria.getSearchId() ) ) {
			return false;
		}
		return true;
	}
	
	@SkipValidation
	public String doEdit(){
		if( !findCriteria() ) {
			addFlashError( getText( "error.reportexpired" ) );
			return ERROR;
		}
		
		applyCriteriaDefaults();
		return SUCCESS;
	}
	
	public String doSave(){
		if( !findCriteria() ) {
			addFlashError( getText( "error.reportexpired" ) );
			return ERROR;
		}
		
		try {
			List<Long> inspectionIds = persistenceManager.idSearch(criteria, criteria.getSecurityFilter());
			Long results = massUpdateManager.updateInspections(inspectionIds, inspection, select, getSessionUser().getUniqueID());
			List<String> messageArgs = new ArrayList<String>();
			messageArgs.add( results.toString() );
			addFlashMessage( getText( "message.inspectionmassupdatesuccessful", messageArgs ) ) ;
			
			return SUCCESS;
		} catch ( UpdateFailureException ufe) {
			logger.error( "failed to run a mass update on inspections", ufe );
		} 
		catch (Exception e){
			logger.error( "failed to run a mass update on inspections", e );
		}
		
		addActionError( getText( "error.failedtomassupdate" ) );
		return INPUT;
	}
	

	
	
	public String getLocation() {
		return inspection.getLocation();
	}
	
	public void setLocation( String location ) {
		inspection.setLocation( location );
	}
	
	public Long getInspectionBook() {
		return ( inspection.getBook() == null ) ? null : inspection.getBook().getId();
	}
	
	public void setInspectionBook( Long inspectionBookId ) {
		if( inspectionBookId == null ) {
			inspection.setBook( null );
		} else if( inspection.getBook() == null || !inspectionBookId.equals( inspection.getBook().getId() ) ) {
			InspectionBook inspectionBook = persistenceManager.find( InspectionBook.class, inspectionBookId );
			inspection.setBook( inspectionBook );
		}
	}
	
	public Collection<ListingPair> getInspectionBooks() {
		InspectionBookListLoader loader = new InspectionBookListLoader(getSecurityFilter());
		loader.setOpenBooksOnly(true);
		return loader.loadListingPair();
	}

	public boolean isPrintable() {
		return inspection.isPrintable();
	}

	public void setPrintable( boolean printable ) {
		inspection.setPrintable( printable );
	}
	
	public Long getOwnerId() {
		return ownerPicker.getOwnerId();
	}

	public BaseOrg getOwner() {
		return ownerPicker.getOwner();
	}

	public void setOwnerId(Long id) {
		ownerPicker.setOwnerId(id);
	}
}
