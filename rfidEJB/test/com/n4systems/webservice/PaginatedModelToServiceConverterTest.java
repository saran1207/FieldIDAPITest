package com.n4systems.webservice;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.n4systems.model.BaseEntity;
import com.n4systems.tools.SillyPager;
import com.n4systems.webservice.dto.AbstractBaseServiceDTO;

public class PaginatedModelToServiceConverterTest {

	@SuppressWarnings("serial")
	private class TestEntity extends BaseEntity {
		public TestEntity(Long id) {
			super(id);
		}
	};
	
	private class TestDoublePaginatedModelToServiceConverter extends PaginatedModelToServiceConverter<TestEntity, AbstractBaseServiceDTO> {
		@Override
		public AbstractBaseServiceDTO toServiceDTO(TestEntity model) {
			AbstractBaseServiceDTO dto = new AbstractBaseServiceDTO() {};
			dto.setId(model.getId());
			return dto;
		}
	}
	
	private TestDoublePaginatedModelToServiceConverter converter = new TestDoublePaginatedModelToServiceConverter();
	
	@Test
	public void to_service_dto_list_handles_zero() {
		List<AbstractBaseServiceDTO> dtos = converter.toServiceDTOList(new SillyPager<TestEntity>(new ArrayList<TestEntity>()));
		
		assertNotNull(dtos);
		assertEquals(0, dtos.size());
	}
	
	@Test
	public void to_service_dto_list_converts_entire_list() {
		List<AbstractBaseServiceDTO> dtos = converter.toServiceDTOList(new SillyPager<TestEntity>(Arrays.asList(new TestEntity(1L), new TestEntity(2L), new TestEntity(3L))));
		
		assertEquals(3, dtos.size());
		assertEquals((Long)1L, dtos.get(0).getId());
		assertEquals((Long)2L, dtos.get(1).getId());
		assertEquals((Long)3L, dtos.get(2).getId());
	}
	
}
