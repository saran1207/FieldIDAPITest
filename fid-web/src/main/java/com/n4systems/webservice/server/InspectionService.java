package com.n4systems.webservice.server;

import com.n4systems.webservice.server.bundles.AuthBundle;
import com.n4systems.webservice.server.bundles.ProofTestBundle;
import com.n4systems.webservice.server.bundles.ProofTestStatusBundle;

import java.util.List;


public interface InspectionService extends AbstractWebService {

	public List<ProofTestStatusBundle> uploadProofTest(AuthBundle authUser, List<ProofTestBundle> bundles) throws WebserviceAuthenticationException, WebserviceException;
}
