package com.n4systems.fieldid.actions.inspection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.jboss.logging.Logger;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.utils.ListHelper;
import com.n4systems.fileprocessing.ProofTestType;
import com.n4systems.model.InspectionType;
import com.n4systems.model.InspectionTypeGroup;
import com.n4systems.util.ListingPair;
import com.n4systems.util.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;

public class InspectionTypeCrud extends AbstractCrud {
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(InspectionTypeCrud.class);
	
	private List<ListingPair> inspectionTypeGroups;
	private List<InspectionType> inspectionTypes;
	private InspectionType inspectionType;
	private List<String> infoFields;
	private String saveAndAdd;
	private Map<String,Boolean> types;
	
	public InspectionTypeCrud( PersistenceManager persistenceManager ) {
		super(persistenceManager);
	}
	
	@Override
	protected void initMemberFields() {
		inspectionType = new InspectionType();
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		inspectionType = persistenceManager.find( InspectionType.class, uniqueId, getTenantId(), "sections", "supportedProofTests", "infoFieldNames" );
	}
	
	@SkipValidation
	public String doList() {
		return SUCCESS;
	}
	
	@SkipValidation
	public String doShow() {
		if( inspectionType == null || inspectionType.getId() == null  ) {
			addActionError( getText( "error.noinspectiontype" ) );
			return ERROR;
		}
		return SUCCESS;
	}
	@SkipValidation
	public String doAdd() {
		if( inspectionType == null ) {
			addActionError( getText( "error.noinspectiontype" ) );
			return ERROR;
		}
		infoFields = inspectionType.getInfoFieldNames();
		return SUCCESS;
	}
	
	@SkipValidation
	public String doEdit() {
		if( inspectionType == null || inspectionType.getId() == null ) {
			addActionError( getText( "error.noinspectiontype" ) );
			return ERROR;
		}
		infoFields = inspectionType.getInfoFieldNames();
		return SUCCESS;
	}
	
	public String doSave() {
		if( inspectionType == null ) {
			addActionError( getText( "error.noinspectiontype" ) );
			return ERROR;
		}
		inspectionType.setInfoFieldNames( infoFields );
		
		processSupportedTypes();
		
		ListHelper.clearNulls( inspectionType.getInfoFieldNames() );
		
		inspectionType.setTenant( getTenant() );
		try {
			if( inspectionType.getId() == null ) {
				persistenceManager.save( inspectionType );
				uniqueID = inspectionType.getId();
			} else {
				inspectionType = persistenceManager.update( inspectionType );
			}
			
			addFlashMessage( getText( "message.savedinspectiontype" ) );
			if( saveAndAdd != null ) {
				return "createInspectionForm";
			}
			return SUCCESS;
		} catch (Exception e) {
			logger.error("Failed saving InspectionType", e);
			addActionError( getText("error.failedtosave") );
			return ERROR;
		}
	}

	public List<InspectionType> getInspectionTypes() {
		if( inspectionTypes == null ) {
			try{
				QueryBuilder<InspectionType> queryBuilder= new QueryBuilder<InspectionType>( InspectionType.class );
				SecurityFilter filter = getSecurityFilter();
				filter.setTargets( "tenant.id", null, null );
				queryBuilder.setSecurityFilter( filter );
				queryBuilder.addOrder( "name" );
				queryBuilder.setSimpleSelect();
				inspectionTypes = persistenceManager.findAll( queryBuilder );
				
			} catch (Exception e) {
				logger.error("Failed finding InspectionTypes", e);
			}
		} 
			
		return inspectionTypes; 
	}

	public InspectionType getInspectionType() {
		return inspectionType;
	}

	public List<ListingPair> getInspectionTypeGroups() {
		if( inspectionTypeGroups == null ) {
			inspectionTypeGroups = persistenceManager.findAllLP( InspectionTypeGroup.class, getTenantId(), "name" );
		}
		return inspectionTypeGroups;
	}

	public void setInspectionType(InspectionType inspectionType) {
		this.inspectionType = inspectionType;
	}
	
	public List<ProofTestType> getProofTestTypes() {
		return Arrays.asList( ProofTestType.values() );
	}
	
	@RequiredStringValidator( message = "", key = "error.namerequired" )
	public String getName() {
		return inspectionType.getName();
	}
	
	public void setName( String name ) {
		inspectionType.setName( name );
	}
	
	public Long getGroup() {
		return (inspectionType.getGroup() != null ) ? inspectionType.getGroup().getId() : null;
	}
	
	@RequiredFieldValidator( message = "", key = "error.grouprequired" )
	public void setGroup( Long group ) {
		if( group == null ) {
			inspectionType.setGroup( null );
		} else if( !group.equals( inspectionType.getGroup() ) ) {
			inspectionType.setGroup( persistenceManager.find( InspectionTypeGroup.class, group, getTenantId() ) );
		}
	}
	
	@SuppressWarnings("unchecked")
	public Map getSupportedProofTestTypes() {
		if( types == null ){
			types = new HashMap<String, Boolean>();
		
			// set up the map of types.
			for( ProofTestType type : getProofTestTypes() ) {
				types.put( type.name(), false );
			}
			
			for( ProofTestType type : inspectionType.getSupportedProofTests() ) {
				types.put( type.name(), true );
			}
		}
		
		return types;
	}
	
	private void processSupportedTypes( ) {
		Set<ProofTestType> supportedTypes =  inspectionType.getSupportedProofTests();
		supportedTypes.clear();
		// convert the map of types back to a list of prooftestTypes
		if( !types.isEmpty() ) {
			for( String typeKey : types.keySet() ) {
				if( types.get( typeKey ) != null &&  types.get( typeKey ) == true ) {
					supportedTypes.add( ProofTestType.valueOf( typeKey ) );
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void setSupportedProofTestTypes( Map supportedTypes ) {
		types = supportedTypes;
	}
	
	public boolean isPrintable() {
		return inspectionType.isPrintable();
	}
	
	public void setPrintable( boolean printable ) {
		inspectionType.setPrintable( printable );
	}

	public boolean isMaster() {
		return inspectionType.isMaster();
	}
	
	public void setMaster( boolean master ) {
		inspectionType.setMaster( master );
	}

	public void setSaveAndAdd( String saveAndAdd ) {
		this.saveAndAdd = saveAndAdd;
	}

	public List<String> getInfoFields() {
		if( infoFields == null ) {
			infoFields = new ArrayList<String>();
		}
		return infoFields;
	}

	@CustomValidator( type="requiredStringSet", message="", key="error.inspectionattributeblank" )
	public void setInfoFields( List<String> infoFieldNames ) {
		infoFields = infoFieldNames;
	}
	
}
