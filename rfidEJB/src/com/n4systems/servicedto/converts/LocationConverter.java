package com.n4systems.servicedto.converts;

import com.n4systems.model.Product;
import com.n4systems.webservice.dto.LocationServiceDTO;

public interface LocationConverter {
	public void convert(LocationServiceDTO locationDTO, Product product);
}
