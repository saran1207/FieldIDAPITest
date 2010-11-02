package com.n4systems.servicedto.converts;

import com.n4systems.api.conversion.ConversionException;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
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
	
	public Asset convert(ProductServiceDTO productServiceDTO, Asset asset) {

		User user = null;
		if (productServiceDTO.assignedUserIdExists()) {
			UserFilteredLoader loader = loaderFactory.createUserFilteredLoader().setId(productServiceDTO.getAssignedUserId());
			user = loader.load();
		}
		asset.setAssignedUser(user);
		
		return asset;
	}

	@Override
	public Event convert(InspectionServiceDTO inspectionServiceDTO, Event event) throws ConversionException {
		if (inspectionServiceDTO.isAssignmentIncluded()) {
			applyAssignment(inspectionServiceDTO, event);
		}
		return event;
	}

	private void applyAssignment(InspectionServiceDTO inspectionServiceDTO, Event event) throws ConversionException {
		User user = null;
		if (inspectionServiceDTO.assignedUserIdExists()) {
			user = loadUser(inspectionServiceDTO);
		}
		
		AssignedToUpdate assignedToUpdate= AssignedToUpdate.assignAssetToUser(user);
		event.setAssignedTo(assignedToUpdate);
	}

	private User loadUser(InspectionServiceDTO inspectionServiceDTO) throws ConversionException {
		User user;
		UserFilteredLoader loader = loaderFactory.createUserFilteredLoader().setId(inspectionServiceDTO.getAssignedUserId());
		user = loader.load();
		if (user == null) { 
			throw new ConversionException("Assigned use lookup failed for " + inspectionServiceDTO.getAssignedUserId());
		}
		
		return user;
	}
	
}
