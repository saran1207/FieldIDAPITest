package com.n4systems.webservice.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;


import com.n4systems.fileprocessing.FileProcessorFactory;
import com.n4systems.model.Inspection;
import com.n4systems.model.user.User;
import com.n4systems.tools.FileDataContainer;
import com.n4systems.util.ServiceLocator;
import com.n4systems.webservice.server.bundles.AuthBundle;
import com.n4systems.webservice.server.bundles.ProofTestBundle;
import com.n4systems.webservice.server.bundles.ProofTestStatusBundle;
import com.n4systems.webservice.server.bundles.WebServiceStatus;

public class InspectionServiceImpl extends AbstractWebServiceImpl implements InspectionService {
	private Logger logger = Logger.getLogger(InspectionServiceImpl.class);
	
	public boolean processInspectionFile(String tenantName, String userName, String userPassword, byte[] fileData, String fileName, String fileType) throws WebserviceAuthenticationException, WebserviceException {
		
		ProofTestBundle bundle = new ProofTestBundle();
		bundle.setCreateProduct(false);
		bundle.setFileData(fileData);
		bundle.setFileName(fileName);
		
		if(fileType != null && fileType.length() > 0) {
			bundle.setFileType(fileType);
		} else {
			bundle.setFileType("roberts");
		}
		
		List<ProofTestStatusBundle> statuses = uploadProofTest(new AuthBundle(tenantName, userName, userPassword), Arrays.asList(bundle));
		
		// we only sent one bundle in so we'll only get one status out
		return (statuses.get(0).getStatus() == WebServiceStatus.SUCCESSFUL) ? true : false;
	}
	
	public List<ProofTestStatusBundle> uploadProofTest(AuthBundle authUser, List<ProofTestBundle> bundles) throws WebserviceAuthenticationException, WebserviceException {
		//authenticate the user
		User user = authenticateUser(authUser);
		
		List<ProofTestStatusBundle> statusList = new ArrayList<ProofTestStatusBundle>();
		
		for(ProofTestBundle bundle: bundles) {
			processBundle(authUser, user, statusList, bundle);
		}
		
		return statusList;
	}

	private void processBundle(AuthBundle authUser, User user, List<ProofTestStatusBundle> statusList, ProofTestBundle bundle) {
		try {
			
			logger.info("Processing Inspection File [" + bundle.getFileName() + "] for tenant [" + authUser.getTenantName() + "] user [" + authUser.getUserName() + "]");
			FileDataContainer dataContainer = FileProcessorFactory.getFileProcessor(bundle.getFileType()).processFile(bundle.getFileData(), bundle.getFileName());

			configureDataContainerForWebServiceProcessing(dataContainer, bundle);
			
			// proocess the file data container
			Map<String, Inspection> inspectionMap = ServiceLocator.getProofTestHandler().inspectionServiceUpload(dataContainer, user);
			
			
			
			applyProcessingResults(statusList, inspectionMap, bundle);
		} catch (Exception e) {
			if (e instanceof NoSerialNumbersException) {
				logger.warn("No Serail number in file: " + bundle.getFileName());
			} else {
				logger.error("Failure during processing of file: " + bundle.getFileName(), e);
			}
			statusList.add(new ProofTestStatusBundle(bundle.getFileName(), WebServiceStatus.FAILED, "Unable to process file"));
		}
	}

	
	private void configureDataContainerForWebServiceProcessing(FileDataContainer dataContainer, ProofTestBundle bundle) {
		// calls coming in from the web service should always be set to resolve a customer
		dataContainer.setResolveCustomer(true);
		dataContainer.setCreateCustomer(bundle.isCreateCustomer());
		dataContainer.setCreateProduct(bundle.isCreateProduct());
	}

	/*
	 * the inspection map allows for multiple serials to have been processed out of one dataContainer (aka one proof test).
	 * This functionality really only exists for CG's use and is not a standard of any proof test type.
	 * For simplicity, we're going to assume there was only one asset serial in the file and thus only have a single entry in our inspectionMap
	 */
	protected void applyProcessingResults(List<ProofTestStatusBundle> statusListCollector, Map<String, Inspection> inspectionMap, ProofTestBundle bundle) throws NoSerialNumbersException {
		String serial;
		String message;
		WebServiceStatus status;
		
		if (inspectionMap.isEmpty()) {
			throw new NoSerialNumbersException("File had no serail numbers defined.");
		}
		
		serial = getSerialNumber(inspectionMap);
		
		if(inspectionMap.get(serial) != null) {
			status = WebServiceStatus.SUCCESSFUL;
			message = "Created or updated an inspection for serial number: " + serial;
		} else {
			status = WebServiceStatus.FAILED;
			message = "Unable to create or update an inspection for serial number: " + serial;
		}
		// construct the status message
		statusListCollector.add(new ProofTestStatusBundle(bundle.getFileName(), status, message));
	}

	private String getSerialNumber(Map<String, Inspection> inspectionMap) {
		return inspectionMap.keySet().iterator().next();
	}

}
