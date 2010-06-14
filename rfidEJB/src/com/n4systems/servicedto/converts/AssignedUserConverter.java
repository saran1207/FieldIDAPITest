package com.n4systems.servicedto.converts;

import com.n4systems.model.Product;
import com.n4systems.webservice.dto.ProductServiceDTO;

public interface AssignedUserConverter {
	
	public Product convert(ProductServiceDTO productServiceDTO, Product product);
	

}
