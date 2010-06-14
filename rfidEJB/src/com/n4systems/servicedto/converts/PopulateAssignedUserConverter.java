package com.n4systems.servicedto.converts;

import com.n4systems.model.Product;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserFilteredLoader;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.webservice.dto.ProductServiceDTO;

public class PopulateAssignedUserConverter implements AssignedUserConverter {

	private LoaderFactory loaderFactory;
	
	public PopulateAssignedUserConverter(LoaderFactory loaderFactory) {
		this.loaderFactory = loaderFactory;
	}
	
	public Product convert(ProductServiceDTO productServiceDTO, Product product) {

		User user = null;
		if (productServiceDTO.assignedUserIdExists()) {
			UserFilteredLoader loader = loaderFactory.createUserFilteredLoader().setId(productServiceDTO.getAssignedUserId());
			user = loader.load();
		}
		product.setAssignedUser(user);
		
		return product;
	}
	
}
