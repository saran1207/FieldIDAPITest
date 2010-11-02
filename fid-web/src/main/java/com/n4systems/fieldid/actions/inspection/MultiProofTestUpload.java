package com.n4systems.fieldid.actions.inspection;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.n4systems.model.Event;
import com.n4systems.model.inspectionbook.EventBookListLoader;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProofTestHandler;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.fieldid.actions.utils.OwnerPicker;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.fileprocessing.ProofTestType;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.reporting.PathHandler;
import com.n4systems.security.Permissions;
import com.n4systems.util.ListingPair;
import com.opensymphony.xwork2.Preparable;

@UserPermissionFilter(userRequiresOneOf={Permissions.CreateInspection})
public class MultiProofTestUpload extends AbstractAction implements Preparable {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(MultiProofTestUpload.class);
	
	private ProofTestHandler proofTestHandler;
	private Event event = new Event();
	private ProofTestType proofTestType;
	private List<ListingPair> inspectionBooks;
	
	private File filedata;
	private String filedataFileName;
	
	private Map<String, Exception> fileProcessingFailureMap = new HashMap<String, Exception>();
	private Map<String, Map<String, Event>> inspectionProcessingFailureMap = new HashMap<String, Map<String, Event>>();

	private OwnerPicker ownerPicker;
	
	public MultiProofTestUpload(ProofTestHandler proofTestHandler, PersistenceManager persistenceManager) {
		super(persistenceManager);
		this.proofTestHandler = proofTestHandler;
	}

	public void prepare() throws Exception {
		ownerPicker = new OwnerPicker(getLoaderFactory().createFilteredIdLoader(BaseOrg.class), event);
	}
	
	
	public String doAdd() {		 
		return SUCCESS;
	}
	
	public String doSave() {
		
		/*
		 *  Note that inspection will have the Customer and InspectionBook attached.
		 *  These objects are not fully constructed.  They are actually just holders for the Id's
		 *  DO NOT! persist this inspection on the attached Customer/InspectionBook!!
		 */
		event = (Event)getSession().get( "proofTestFiles_inspection" );
		proofTestType = (ProofTestType)getSession().get( "proofTestFiles_type" );
		
		List<File> proofTestFiles = getProofTestFiles();
		
		// we should clear these out of session asap
		getSession().remove("proofTestFiles_inspection");
		getSession().remove("proofTestFiles_type");
		getSession().remove("proofTestFiles");
		
		// clear status maps before we start processing
		fileProcessingFailureMap.clear();
		inspectionProcessingFailureMap.clear();
		
		Map<String, Event> inspectionMap;
		// process each uploaded proof test file
		for(File proofTest: proofTestFiles) {
			
			try {
				// processes this prooftest file
				inspectionMap = proofTestHandler.multiProofTestUpload(
											proofTest, 
											proofTestType, 
											getSessionUser().getTenant().getId(), 
											getSessionUser().getUniqueID(), 
											event.getOwner().getId(),
											event.getBook().getId()
										);
				
				inspectionProcessingFailureMap.put(proofTest.getName(), inspectionMap);
				
			} catch (Exception e) {
				fileProcessingFailureMap.put(proofTest.getName(), e);
				inspectionProcessingFailureMap.put(proofTest.getName(), new HashMap<String, Event>());
				logger.error("failed while processing multiproof upload for file " + proofTest.getName(), e);
			}
			
			
			// a null exception means that processing was successful
			fileProcessingFailureMap.put(proofTest.getName(), null);
		}
		
		return SUCCESS;
	}
	
	
	public String doUpload() {
		/*
		 * We need to move the file before the end of this request otherwise struts will clear it out
		 * we'll use the PathHandler to find a new temp spot.  Notice we also preserve the original file name.
		 */
		File newPath = PathHandler.getTempFile(filedataFileName);
		
		try {
			FileUtils.copyFile(filedata, newPath);
		} catch (Exception e) {
			addActionError( getText("error.fileuploadfailed" ) );
			return ERROR;
		}
		
		// now we can add it to our list of files.  Note that this call actually operates on a session variable
		getProofTestFiles().add(newPath);
		
		getSession().put("proofTestFiles_inspection", event);
		getSession().put("proofTestFiles_type", proofTestType);
		
		return SUCCESS;
	}
	
	public List<ProofTestType> getProofTestTypes() {
		return Arrays.asList( ProofTestType.values() );
	}

	public List<ListingPair> getInspectionBooks() {
		if( inspectionBooks == null ) {
			EventBookListLoader loader = new EventBookListLoader(getSecurityFilter());
			loader.setOpenBooksOnly(true);
			loader.setOwner(event.getOwner());
			inspectionBooks = loader.loadListingPair();
		}
		return inspectionBooks;
	}

	public Event getInspection() {
		return event;
	}

	public void setInspection( Event event) {
		this.event = event;
	}

	public String getProofTestType() {
		return ( proofTestType != null ) ? proofTestType.name() : null;
	}

	public void setProofTestType( String proofTestType ) {
		this.proofTestType = ProofTestType.valueOf( proofTestType );
	}
	
	public File getFiledata() {
		return filedata;
	}

	public void setFiledata( File filedata ) {
		this.filedata = filedata;
	}

	public String getFiledataFileName() {
		return filedataFileName;
	}

	public void setFiledataFileName( String filedataFileName ) {
		this.filedataFileName = filedataFileName;
	}

	public Map<String, Exception> getFileProcessingFailureMap() {
		return fileProcessingFailureMap;
	}
	
	@SuppressWarnings("unchecked")
	private List<File> getProofTestFiles() {
		List<File> files = (List<File>)getSession().get("proofTestFiles");
		
		if(files == null) {
			files = new ArrayList<File>();
			getSession().put("proofTestFiles", files);
		}
		
		return files;
	}

	public Map<String, Map<String, Event>> getInspectionProcessingFailureMap() {
		return inspectionProcessingFailureMap;
	}

	
	
	public BaseOrg getOwner() {
		return ownerPicker.getOwner();
	}

	public Long getOwnerId() {
		return ownerPicker.getOwnerId();
	}

	public void setOwnerId(Long id) {
		ownerPicker.setOwnerId(id);
	}
}
