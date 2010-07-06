package com.n4systems.webservice;

import com.n4systems.webservice.dto.RequestInformation;
import com.n4systems.webservice.dto.RequestResponse;
import com.n4systems.webservice.exceptions.ServiceException;

public interface RequestHandler<REQ extends RequestInformation, RESP extends RequestResponse> {
	
	public RESP getResponse(REQ request) throws ServiceException;
	
}
