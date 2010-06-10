package com.n4systems.servicedto.converts;

import com.n4systems.model.Product;
import com.n4systems.webservice.dto.ProductServiceDTO;

public interface PopulateAssignedUser {
	
	public Product populateAssignedTo(ProductServiceDTO productServiceDTO, Product product, long tenantId);
	

}
