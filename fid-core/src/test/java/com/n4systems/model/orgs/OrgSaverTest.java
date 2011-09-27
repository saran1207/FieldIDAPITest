package com.n4systems.model.orgs;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

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
			public BaseOrg update(EntityManager em, BaseOrg entity) {
				assertNull("BaseOrg was not touched before update", entity.getModified());
				updateCalled = true;
				return super.update(em, entity);
			}
		};
		
		saver.save(new DummyEntityManager(), new PrimaryOrg());
	}
	
	@Test
	public void update_creates_and_persists_null_address_info() {
		PrimaryOrg mainOrg = OrgBuilder.aPrimaryOrg().buildPrimary();
		
		OrgSaver saver = new OrgSaver(new LinkedOrgListLoader() {
			@SuppressWarnings("unchecked")
			public List<ExternalOrg> load(EntityManager em) {
				return Collections.EMPTY_LIST;
			}
		});
		
		mainOrg.setAddressInfo(null);
		
		EntityManager em = createMock(EntityManager.class);
		
		expect(em.merge(mainOrg)).andReturn(mainOrg);
		em.persist(anyObject());
		replay(em);
		
		BaseOrg newMain = saver.update(em, mainOrg);
		assertNotNull(newMain.getAddressInfo());
	}
	
	@Test
	public void save_creates_null_address_info() {
		PrimaryOrg mainOrg = OrgBuilder.aPrimaryOrg().buildPrimary();
		
		OrgSaver saver = new OrgSaver();
		
		mainOrg.setAddressInfo(null);
		
		EntityManager em = createMock(EntityManager.class);
		em.persist(mainOrg);
		expect(em.merge(mainOrg)).andReturn(mainOrg);
		
		saver.save(em, mainOrg);
		assertNotNull(mainOrg.getAddressInfo());
	}
}
