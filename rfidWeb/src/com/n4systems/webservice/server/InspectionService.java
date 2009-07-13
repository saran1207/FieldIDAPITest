package com.n4systems.webservice.server;

import java.util.List;

import com.n4systems.webservice.server.bundles.AuthBundle;
import com.n4systems.webservice.server.bundles.ProofTestBundle;
import com.n4systems.webservice.server.bundles.ProofTestStatusBundle;


public interface InspectionService extends AbstractWebService {

	@Deprecated
	public boolean processInspectionFile(String tenantName, String userName, String userPassword, byte[] fileData, String fileName, String fileType) throws WebserviceAuthenticationException, WebserviceException;
	
	public List<ProofTestStatusBundle> uploadProofTest(AuthBundle authUser, List<ProofTestBundle> bundles) throws WebserviceAuthenticationException, WebserviceException;
}
