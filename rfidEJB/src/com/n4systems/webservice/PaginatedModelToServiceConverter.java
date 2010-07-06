package com.n4systems.webservice;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.model.BaseEntity;
import com.n4systems.tools.Pager;
import com.n4systems.webservice.dto.AbstractBaseServiceDTO;

public abstract class PaginatedModelToServiceConverter<M extends BaseEntity, S extends AbstractBaseServiceDTO> implements ModelToServiceConverter<M, S> {
	
	public List<S> toServiceDTOList(Pager<M> pager) {
		List<S> serviceDTOs = new ArrayList<S>();
		
		for (M model: pager.getList()) {
			serviceDTOs.add(toServiceDTO(model));
		}
		
		return serviceDTOs;
	}
	
}
