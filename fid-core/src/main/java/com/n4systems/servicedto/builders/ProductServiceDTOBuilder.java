package com.n4systems.servicedto.builders;

import com.n4systems.model.builders.BaseBuilder;
import com.n4systems.webservice.dto.ProductServiceDTO;

public class ProductServiceDTOBuilder extends BaseBuilder<ProductServiceDTO> {

	private final long assignedUserId;
	
	@Override
	public ProductServiceDTO createObject() {
		ProductServiceDTO productServiceDTO = new ProductServiceDTO();
		productServiceDTO.setId(getId());
		productServiceDTO.setAssignedUserId(assignedUserId);
		
		return productServiceDTO;
	}
	
	public static ProductServiceDTOBuilder aProductServiceDTO() {
		return new ProductServiceDTOBuilder(0L);
	}
	
	private ProductServiceDTOBuilder(long assignedUserId) {
		super();
		this.assignedUserId = assignedUserId;
	}
	
	public ProductServiceDTOBuilder withAssignedUserId(long assisgnedUserId) {
		return new ProductServiceDTOBuilder(assisgnedUserId);
	}

}
