package com.n4systems.model.orgs;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.easymock.EasyMock;
import org.junit.Test;

import com.n4systems.model.builders.OrgBuilder;
import com.n4systems.testutils.DummyEntityManager;


public class OrgSaverTest {

	@Test
	public void save_touches_and_resaves() {
		OrgSaver saver = new OrgSaver() {
			boolean updateCalled = false;
			
			@Override
			public void save(EntityManager em, BaseOrg entity) {
				entity.setModified(new Date());
				super.save(em, entity);
				assertTrue("BaseOrg was not resaved", updateCalled);
			}
			
			@Override
			protected BaseOrg update(EntityManager em, BaseOrg entity) {
				assertNull("BaseOrg was not touched before update", entity.getModified());
				updateCalled = true;
				return super.update(em, entity);
			}
		};
		
		saver.save(new DummyEntityManager(), new PrimaryOrg());
	}
	
	@Test
	public void update_internal_org_updates_linked_orgs() {
		PrimaryOrg mainOrg = OrgBuilder.aPrimaryOrg().buildPrimary();
		
		final List<ExternalOrg> linkedOrgs = Arrays.asList(OrgBuilder.aCustomerOrg().buildCustomerAsExternal(), OrgBuilder.aCustomerOrg().buildCustomerAsExternal());
		
		OrgSaver saver = new OrgSaver(new LinkedOrgListLoader() {
			protected List<ExternalOrg> load(EntityManager em) {
				return linkedOrgs;
			}
		});
		
		EntityManager em = EasyMock.createMock(EntityManager.class);
		
		EasyMock.expect(em.merge(mainOrg)).andReturn(mainOrg);
		em.persist(EasyMock.anyObject());
		
		for (ExternalOrg linkedOrg: linkedOrgs) {
			EasyMock.expect(em.merge(linkedOrg)).andReturn(linkedOrg);
			em.persist(EasyMock.anyObject());
		}
		
		EasyMock.replay(em);
		
		saver.update(em, mainOrg);
	}
	
	@Test
	public void update_creates_and_persists_null_address_info() {
		PrimaryOrg mainOrg = OrgBuilder.aPrimaryOrg().buildPrimary();
		
		OrgSaver saver = new OrgSaver(new LinkedOrgListLoader() {
			@SuppressWarnings("unchecked")
			protected List<ExternalOrg> load(EntityManager em) {
				return Collections.EMPTY_LIST;
			}
		});
		
		mainOrg.setAddressInfo(null);
		
		EntityManager em = EasyMock.createMock(EntityManager.class);
		
		EasyMock.expect(em.merge(mainOrg)).andReturn(mainOrg);
		em.persist(EasyMock.anyObject());
		EasyMock.replay(em);
		
		BaseOrg newMain = saver.update(em, mainOrg);
		assertNotNull(newMain.getAddressInfo());
	}
	
	@Test
	public void save_creates_null_address_info() {
		PrimaryOrg mainOrg = OrgBuilder.aPrimaryOrg().buildPrimary();
		
		OrgSaver saver = new OrgSaver();
		
		mainOrg.setAddressInfo(null);
		
		EntityManager em = EasyMock.createMock(EntityManager.class);
		em.persist(mainOrg);
		EasyMock.expect(em.merge(mainOrg)).andReturn(mainOrg);
		
		saver.save(em, mainOrg);
		assertNotNull(mainOrg.getAddressInfo());
	}
}
