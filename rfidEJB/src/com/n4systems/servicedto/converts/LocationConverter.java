package com.n4systems.servicedto.converts;

import com.n4systems.model.location.LocationContainer;
import com.n4systems.webservice.dto.LocationServiceDTO;

public interface LocationConverter {
	public void convert(LocationServiceDTO locationDTO, LocationContainer container);
}
