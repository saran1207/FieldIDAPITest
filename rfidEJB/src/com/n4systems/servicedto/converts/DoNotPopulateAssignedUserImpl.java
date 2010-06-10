package com.n4systems.servicedto.converts;

import com.n4systems.model.Product;
import com.n4systems.webservice.dto.ProductServiceDTO;

public class DoNotPopulateAssignedUserImpl implements PopulateAssignedUser {

	public Product populateAssignedTo(ProductServiceDTO productServiceDTO, Product product, long tenantId) {
		return product;
	}
}
