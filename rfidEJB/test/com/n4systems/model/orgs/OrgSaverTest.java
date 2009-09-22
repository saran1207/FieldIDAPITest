package com.n4systems.model.orgs;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Test;

import com.n4systems.testutils.DummyEntityManager;


public class OrgSaverTest {

	@Test
	public void save_touches_and_resaves() {
		OrgSaver saver = new OrgSaver() {
			boolean updateCalled = false;
			
			@Override
			protected void save(EntityManager em, BaseOrg entity) {
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
	public void update_linked_fields_sets_the_org_and_updates() {
		
		class TestOrgSaver extends OrgSaver {
			private int updateCount = 0;
			private List<ExternalOrg> linkedOrgs = Arrays.asList(new CustomerOrg(), new DivisionOrg());
			
			public TestOrgSaver() {
				super(null);
			}
			
			protected void updateLinkedFields(EntityManager em, InternalOrg org) {
				super.updateLinkedFields(em, org);
				
				assertEquals("Linked orgs were not updated", linkedOrgs.size(), updateCount);
				
				for (ExternalOrg extOrg: linkedOrgs) {
					assertEquals("Org was not set on link", org, extOrg.getLinkedOrg());
				}
			}
			
			public List<ExternalOrg> loadLinkedOrgs(EntityManager em, InternalOrg org) {
				return linkedOrgs;
			}

			@Override
			protected BaseOrg update(EntityManager em, BaseOrg entity) {
				updateCount++;
				return entity;
			}
		};
		
		TestOrgSaver saver = new TestOrgSaver();
		
		PrimaryOrg testOrg = new PrimaryOrg();
		testOrg.setId(1L);
		
		saver.updateLinkedFields(null, testOrg);
	}
}
