package rfid.ejb.session;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.Set;

import javax.persistence.EntityManager;

import org.junit.Test;

import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import com.n4systems.exceptions.MissingEntityException;
import com.n4systems.model.builders.InfoFieldBeanBuilder;
import com.n4systems.model.builders.InfoOptionBeanBuilder;
import com.n4systems.webservice.dto.InfoOptionServiceDTO;
import com.n4systems.webservice.dto.ProductServiceDTO;


public class ServiceDTOConverterInfoOptionTest {
	
	private static final Long A_INFO_FIELD_ID_THAT_NO_LONGER_EXISTS = 12345L;
	private static final Long A_MOBILE_ID = -2L;
	private static final Long AN_INFO_FIELD_ID_THAT_DOES_EXIST = 1L;
	private static final Long INFO_OPTION_ID_FROM_THE_WEB = 1L;
	
	

	@Test(expected=MissingEntityException.class)
	public void should_raise_missing_entity_if_the_info_field_could_not_be_looked_up() throws Exception {
		ServiceDTOBeanConverterTestExtension sut = new ServiceDTOBeanConverterTestExtension();
		
		EntityManager em = createMock(EntityManager.class);
		expect(em.find(InfoFieldBean.class, A_INFO_FIELD_ID_THAT_NO_LONGER_EXISTS)).andReturn(null);
		replay(em);
		
		sut.setEntityManager(em);
		
		InfoOptionServiceDTO infoOptionDTO = new InfoOptionServiceDTO();
		
		infoOptionDTO.setName("name");
		infoOptionDTO.setInfoFieldId(A_INFO_FIELD_ID_THAT_NO_LONGER_EXISTS);
		infoOptionDTO.setId(A_MOBILE_ID);
		
		sut.convert(infoOptionDTO);
	}
	
	
	
	@Test
	public void should_create_the_info_option_when_info_field_is_found() throws Exception {
		ServiceDTOBeanConverterTestExtension sut = new ServiceDTOBeanConverterTestExtension();
		
		EntityManager em = createMock(EntityManager.class);
		expect(em.find(InfoFieldBean.class, AN_INFO_FIELD_ID_THAT_DOES_EXIST)).andReturn(InfoFieldBeanBuilder.aTextField().build());
		replay(em);
		
		sut.setEntityManager(em);
		
		InfoOptionServiceDTO infoOptionDTO = new InfoOptionServiceDTO();
		
		infoOptionDTO.setName("name");
		
		infoOptionDTO.setInfoFieldId(AN_INFO_FIELD_ID_THAT_DOES_EXIST);
		infoOptionDTO.setId(A_MOBILE_ID);
		
		InfoOptionBean infoOptionBean = sut.convert(infoOptionDTO);
		
		assertEquals("name", infoOptionBean.getName());
	}
	
	
	
	@Test
	public void should_attempt_to_load_info_option_when_it_was_originally_created_on_the_web() throws Exception {
		ServiceDTOBeanConverterTestExtension sut = new ServiceDTOBeanConverterTestExtension();
		
		EntityManager em = createMock(EntityManager.class);
		expect(em.find(InfoOptionBean.class, 1L)).andReturn(InfoOptionBeanBuilder.aStaticInfoOption().build());
		replay(em);
		
		sut.setEntityManager(em);
		
		InfoOptionServiceDTO infoOptionDTO = new InfoOptionServiceDTO();
		infoOptionDTO.setId(1L);
		
		sut.convert(infoOptionDTO);
		
		verify(em);
	}
	
	@Test
	public void should_return_the_loaded_info_option_when_it_was_originally_created_on_the_web() throws Exception {
		ServiceDTOBeanConverterTestExtension sut = new ServiceDTOBeanConverterTestExtension();
		InfoOptionBean infoOptionToLoad = InfoOptionBeanBuilder.aStaticInfoOption().build();
		
		EntityManager em = createMock(EntityManager.class);
		expect(em.find(InfoOptionBean.class, INFO_OPTION_ID_FROM_THE_WEB)).andReturn(infoOptionToLoad);
		replay(em);
		
		sut.setEntityManager(em);
		
		InfoOptionServiceDTO infoOptionDTO = new InfoOptionServiceDTO();
		infoOptionDTO.setId(1L);
		
		InfoOptionBean actualInfoOption = sut.convert(infoOptionDTO);
		
		assertSame("infoOption returned is not the loaded one", infoOptionToLoad, actualInfoOption);
	}
	
	
	@Test
	public void should_create_the_info_option_when_the_it_was_created_on_the_web_but_is_no_longer_there() throws Exception {
		ServiceDTOBeanConverterTestExtension sut = new ServiceDTOBeanConverterTestExtension();
		InfoFieldBean infoFieldToLoad = InfoFieldBeanBuilder.aTextField().build();
		
		
		EntityManager em = createMock(EntityManager.class);
		expect(em.find(InfoOptionBean.class, INFO_OPTION_ID_FROM_THE_WEB)).andReturn(null);
		expect(em.find(InfoFieldBean.class, AN_INFO_FIELD_ID_THAT_DOES_EXIST)).andReturn(infoFieldToLoad);
		replay(em);
		
		sut.setEntityManager(em);
		
		InfoOptionServiceDTO infoOptionDTO = new InfoOptionServiceDTO();
		infoOptionDTO.setInfoFieldId(AN_INFO_FIELD_ID_THAT_DOES_EXIST);
		infoOptionDTO.setId(INFO_OPTION_ID_FROM_THE_WEB);
		infoOptionDTO.setName("name");
		
		
		InfoOptionBean actualInfoOption = sut.convert(infoOptionDTO);
		
		
		assertEquals("name", actualInfoOption.getName());
		assertEquals(infoFieldToLoad, actualInfoOption.getInfoField());
		assertNull(actualInfoOption.getUniqueID());
	}
	
	
	
	@Test
	public void should_throw_away_all_info_options_when_all_of_them_fail_to_load_there_info_field() throws Exception {
		ServiceDTOBeanConverterTestExtension sut = new ServiceDTOBeanConverterTestExtension();
		EntityManager em = createEntityManagerThatWillAlwaysFailToLoadTheInfoField();
		sut.setEntityManager(em);
		
		ProductServiceDTO productServiceDTO = new ProductServiceDTO();
		
		productServiceDTO.getInfoOptions().add(createInfoOptionServiceDTO(A_MOBILE_ID, "name", A_INFO_FIELD_ID_THAT_NO_LONGER_EXISTS));
		productServiceDTO.getInfoOptions().add(createInfoOptionServiceDTO(A_MOBILE_ID, "name", A_INFO_FIELD_ID_THAT_NO_LONGER_EXISTS));
		productServiceDTO.getInfoOptions().add(createInfoOptionServiceDTO(A_MOBILE_ID, "name", A_INFO_FIELD_ID_THAT_NO_LONGER_EXISTS));
		productServiceDTO.getInfoOptions().add(createInfoOptionServiceDTO(A_MOBILE_ID, "name", A_INFO_FIELD_ID_THAT_NO_LONGER_EXISTS));
		
		
		Set<InfoOptionBean> infoOptionBeans = sut.convertInfoOptions(productServiceDTO);
		
		
		assertTrue("infoOptionSet should be empty", infoOptionBeans.isEmpty());
	}
	
	@Test
	public void should_only_throw_away_the_info_options_fail_to_load_there_info_field() throws Exception {
		ServiceDTOBeanConverterTestExtension sut = new ServiceDTOBeanConverterTestExtension();
		
		EntityManager em = createMock(EntityManager.class);
		expect(em.find(InfoFieldBean.class, A_INFO_FIELD_ID_THAT_NO_LONGER_EXISTS)).andReturn(null).anyTimes();
		expect(em.find(InfoFieldBean.class, AN_INFO_FIELD_ID_THAT_DOES_EXIST)).andReturn(InfoFieldBeanBuilder.aTextField().build()).anyTimes();
		replay(em);
		
		sut.setEntityManager(em);
		
		ProductServiceDTO productServiceDTO = new ProductServiceDTO();
		productServiceDTO.getInfoOptions().add(createInfoOptionServiceDTO(A_MOBILE_ID, "include", AN_INFO_FIELD_ID_THAT_DOES_EXIST));
		productServiceDTO.getInfoOptions().add(createInfoOptionServiceDTO(A_MOBILE_ID, "throw away", A_INFO_FIELD_ID_THAT_NO_LONGER_EXISTS));
		productServiceDTO.getInfoOptions().add(createInfoOptionServiceDTO(A_MOBILE_ID, "include2", AN_INFO_FIELD_ID_THAT_DOES_EXIST));
		productServiceDTO.getInfoOptions().add(createInfoOptionServiceDTO(A_MOBILE_ID, "include3", AN_INFO_FIELD_ID_THAT_DOES_EXIST));
		
		
		Set<InfoOptionBean> infoOptionBeans = sut.convertInfoOptions(productServiceDTO);
		
		
		assertEquals(3, infoOptionBeans.size());
	}
	
	
	@Test
	public void should_not_throw_away_any_info_options_fail_to_load_there_info_field() throws Exception {
		ServiceDTOBeanConverterTestExtension sut = new ServiceDTOBeanConverterTestExtension();
		
		EntityManager em = createMock(EntityManager.class);
		expect(em.find(InfoFieldBean.class, AN_INFO_FIELD_ID_THAT_DOES_EXIST)).andReturn(InfoFieldBeanBuilder.aTextField().build()).anyTimes();
		replay(em);
		
		sut.setEntityManager(em);
		
		ProductServiceDTO productServiceDTO = new ProductServiceDTO();
		
		productServiceDTO.getInfoOptions().add(createInfoOptionServiceDTO(A_MOBILE_ID, "include", AN_INFO_FIELD_ID_THAT_DOES_EXIST));
		productServiceDTO.getInfoOptions().add(createInfoOptionServiceDTO(A_MOBILE_ID, "include2", AN_INFO_FIELD_ID_THAT_DOES_EXIST));
		productServiceDTO.getInfoOptions().add(createInfoOptionServiceDTO(A_MOBILE_ID, "include3", AN_INFO_FIELD_ID_THAT_DOES_EXIST));
		productServiceDTO.getInfoOptions().add(createInfoOptionServiceDTO(A_MOBILE_ID, "include4", AN_INFO_FIELD_ID_THAT_DOES_EXIST));
		
		Set<InfoOptionBean> infoOptionBeans = sut.convertInfoOptions(productServiceDTO);
		
		
		assertEquals(4, infoOptionBeans.size());
	}



	private InfoOptionServiceDTO createInfoOptionServiceDTO(Long id, String name, Long infoFieldId) {
		InfoOptionServiceDTO infoOptionDTO = new InfoOptionServiceDTO();
		infoOptionDTO.setName(name);
		infoOptionDTO.setInfoFieldId(infoFieldId);
		infoOptionDTO.setId(id);
		return infoOptionDTO;
	}



	private EntityManager createEntityManagerThatWillAlwaysFailToLoadTheInfoField() {
		EntityManager em = createMock(EntityManager.class);
		expect(em.find(InfoFieldBean.class, A_INFO_FIELD_ID_THAT_NO_LONGER_EXISTS)).andReturn(null).anyTimes();
		replay(em);
		return em;
	}
	
	
	
	
	
	
	

	
	
}
