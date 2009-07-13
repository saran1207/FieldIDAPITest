package com.n4systems.fieldid.actions.inspection;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.n4systems.ejb.CustomerManager;
import com.n4systems.ejb.InspectionManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProofTestHandler;
import com.n4systems.exceptions.FileProcessingException;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.fileprocessing.ProofTestType;
import com.n4systems.model.Inspection;
import com.n4systems.reporting.PathHandler;
import com.n4systems.util.ListingPair;

public class MultiProofTestUpload extends AbstractAction {
	private static final long serialVersionUID = 1L;
	
	private ProofTestHandler proofTestHandler;
	private InspectionManager inspectionManager;
	private CustomerManager customerManager;
	
	private Inspection inspection = new Inspection();
	private ProofTestType proofTestType; 
	private List<ListingPair> customers;
	private List<ListingPair> inspectionBooks;
	
	private File filedata;
	private String filedataFileName;
	
	private Map<String, Exception> fileProcessingFailureMap = new HashMap<String, Exception>();
	private Map<String, Map<String, Inspection>> inspectionProcessingFailureMap = new HashMap<String, Map<String, Inspection>>();
	
	public MultiProofTestUpload(InspectionManager inspectionManager, ProofTestHandler proofTestHandler, CustomerManager customerManager, PersistenceManager persistenceManager) {
		super(persistenceManager);
		this.inspectionManager = inspectionManager;
		this.proofTestHandler = proofTestHandler;
		this.customerManager = customerManager;
	}

	public String doAdd() {		 
		return SUCCESS;
	}
	
	public String doSave() {
		
		/*
		 *  Note that inspection will have the Customer and InspectionBook attached.
		 *  These objects are no fully constructed.  They are actually just holders for the Id's
		 *  DO NOT! persist this inspection on the attached Customer/InspectionBook!!
		 */
		inspection = (Inspection)getSession().get( "proofTestFiles_inspection" );
		proofTestType = (ProofTestType)getSession().get( "proofTestFiles_type" );
		
		List<File> proofTestFiles = getProofTestFiles();
		
		// we should clear these out of session asap
		getSession().remove("proofTestFiles_inspection");
		getSession().remove("proofTestFiles_type");
		getSession().remove("proofTestFiles");
		
		// clear status maps before we start processing
		fileProcessingFailureMap.clear();
		inspectionProcessingFailureMap.clear();
		
		Map<String, Inspection> inspectionMap;
		// process each uploaded proof test file
		for(File proofTest: proofTestFiles) {
			
			try {
				// processes this prooftest file
				inspectionMap = proofTestHandler.multiProofTestUpload(
											proofTest, 
											proofTestType, 
											getSessionUser().getTenant().getId(), 
											getSessionUser().getUniqueID(), 
											inspection.getCustomer().getId(), 
											inspection.getBook().getId()
										);
				
				inspectionProcessingFailureMap.put(proofTest.getName(), inspectionMap);
				
			} catch (Exception e) {
				fileProcessingFailureMap.put(proofTest.getName(), e);
				inspectionProcessingFailureMap.put(proofTest.getName(), new HashMap<String, Inspection>());
			}
			
			
			// a null exception means that processing was successful
			fileProcessingFailureMap.put(proofTest.getName(), null);
		}
		
		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
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
		
		getSession().put("proofTestFiles_inspection", inspection);
		getSession().put("proofTestFiles_type", proofTestType);
		
		return SUCCESS;
	}
	
	public List<ProofTestType> getProofTestTypes() {
		return Arrays.asList( ProofTestType.values() );
	}

	public List<ListingPair> getCustomers() {
		if( customers == null ) {
			customers = customerManager.findCustomersLP(getSecurityFilter().getTenantId(), getSecurityFilter());
		}
		return customers;
	}

	public List<ListingPair> getInspectionBooks() {
		if( inspectionBooks == null ) {
			inspectionBooks = inspectionManager.findAvailableInspectionBooksLP( getSecurityFilter(), false, ( inspection.getCustomer() != null ) ? inspection.getCustomer().getId() : null  );
		}
		return inspectionBooks;
	}

	public Inspection getInspection() {
		return inspection;
	}

	public void setInspection( Inspection inspection ) {
		this.inspection = inspection;
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

	public Map<String, Map<String, Inspection>> getInspectionProcessingFailureMap() {
		return inspectionProcessingFailureMap;
	}
}
