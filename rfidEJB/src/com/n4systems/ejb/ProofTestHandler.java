package com.n4systems.ejb;

import java.io.File;
import java.util.Map;


import com.n4systems.exceptions.FileProcessingException;
import com.n4systems.fileprocessing.ProofTestType;
import com.n4systems.model.Inspection;
import com.n4systems.model.user.User;
import com.n4systems.tools.FileDataContainer;

public interface ProofTestHandler {
	public Map<String, Inspection> multiProofTestUpload(File proofTestFile, ProofTestType type, Long tenantId, Long userId, Long ownerId, Long inspectionBookId) throws FileProcessingException;
	public Map<String, Inspection> inspectionServiceUpload(FileDataContainer fileData, User performedBy) throws FileProcessingException;
}
