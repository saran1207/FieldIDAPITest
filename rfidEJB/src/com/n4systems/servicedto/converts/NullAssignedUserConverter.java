package com.n4systems.servicedto.converts;

import com.n4systems.model.Product;
import com.n4systems.webservice.dto.ProductServiceDTO;

public class NullAssignedUserConverter implements AssignedUserConverter {

	public Product convert(ProductServiceDTO productServiceDTO, Product product) {
		return product;
	}
}
