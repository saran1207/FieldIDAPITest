package com.n4systems.webservice;

import com.n4systems.model.BaseEntity;
import com.n4systems.webservice.dto.AbstractBaseServiceDTO;

public interface ModelToServiceConverter<M extends BaseEntity, S extends AbstractBaseServiceDTO> {
	public S toServiceDTO(M model);
}
