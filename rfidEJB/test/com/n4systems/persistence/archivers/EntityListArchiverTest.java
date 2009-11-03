package com.n4systems.persistence.archivers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.persistence.EntityManager;

import org.easymock.classextension.EasyMock;
import org.junit.Test;

import rfid.ejb.entity.UserBean;

import com.n4systems.model.builders.UserBuilder;
import com.n4systems.model.parents.ArchivableEntityWithTenant;

public class EntityListArchiverTest {

	@SuppressWarnings("serial")
	class SimpleArchivableEntity extends ArchivableEntityWithTenant {}
	
	@Test
	public void test_archive() {
		UserBean modifyUser = UserBuilder.anEmployee().build();

		List<SimpleArchivableEntity> entities = Arrays.asList(EasyMock.createMock(SimpleArchivableEntity.class), EasyMock.createMock(SimpleArchivableEntity.class), EasyMock.createMock(SimpleArchivableEntity.class));
		List<Long> ids = Arrays.asList(1L, 2L, 3L);
		
		EntityManager em = EasyMock.createMock(EntityManager.class);
		
		SimpleArchivableEntity entity;
		for (int i = 0; i < ids.size(); i++) {
			entity = entities.get(i);
			
			EasyMock.expect(em.find(SimpleArchivableEntity.class, ids.get(i))).andReturn(entity);
			
			entity.archiveEntity();
			entity.setModifiedBy(modifyUser);
			EasyMock.replay(entity);
			
			EasyMock.expect(em.merge(entities.get(i))).andReturn(entities.get(i));
		}
		
		EasyMock.replay(em);
		
		EntityListArchiver<SimpleArchivableEntity> archiver = new EntityListArchiver<SimpleArchivableEntity>(SimpleArchivableEntity.class, new HashSet<Long>(ids), modifyUser);
		archiver.archive(em);		
	}
}
