package com.n4systems.ejb;

import java.io.File;
import java.util.Map;


import com.n4systems.exceptions.FileProcessingException;
import com.n4systems.fileprocessing.ProofTestType;
import com.n4systems.model.Event;
import com.n4systems.model.user.User;
import com.n4systems.tools.FileDataContainer;

public interface ProofTestHandler {
	public Map<String, Event> multiProofTestUpload(File proofTestFile, ProofTestType type, Long tenantId, Long userId, Long ownerId, Long eventBookId) throws FileProcessingException;
	public Map<String, Event> eventServiceUpload(FileDataContainer fileData, User performedBy) throws FileProcessingException;
}
