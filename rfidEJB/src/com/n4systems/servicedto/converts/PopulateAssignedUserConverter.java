package com.n4systems.servicedto.converts;

import com.n4systems.model.Inspection;
import com.n4systems.model.Product;
import com.n4systems.model.inspection.AssignedToUpdate;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserFilteredLoader;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.webservice.dto.InspectionServiceDTO;
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

	@Override
	public Inspection convert(InspectionServiceDTO inspectionServiceDTO, Inspection inspection) {
		User user = null;
		if (inspectionServiceDTO.assignedUserIdExists()) {
			UserFilteredLoader loader = loaderFactory.createUserFilteredLoader().setId(inspectionServiceDTO.getAssignedUserId());
			user = loader.load();
		}
		AssignedToUpdate assignedToUpdate= AssignedToUpdate.assignAssetToUser(user);
		inspection.setAssignedTo(assignedToUpdate);
		
		return inspection;
	}
	
}
