package com.n4systems.persistence.archivers;

import static org.easymock.EasyMock.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Test;


import com.n4systems.model.builders.UserBuilder;
import com.n4systems.model.parents.ArchivableEntityWithTenant;
import com.n4systems.model.user.User;

public class EntityListArchiverTest {

	@SuppressWarnings("serial")
	class SimpleArchivableEntity extends ArchivableEntityWithTenant {}
	
	@Test
	public void test_archive() {
		User modifyUser = UserBuilder.anEmployee().build();

		List<SimpleArchivableEntity> entities = Arrays.asList(createMock(SimpleArchivableEntity.class), createMock(SimpleArchivableEntity.class), createMock(SimpleArchivableEntity.class));
		List<Long> ids = Arrays.asList(1L, 2L, 3L);
		
		EntityManager em = createMock(EntityManager.class);
		
		SimpleArchivableEntity entity;
		for (int i = 0; i < ids.size(); i++) {
			entity = entities.get(i);
			
			expect(em.find(SimpleArchivableEntity.class, ids.get(i))).andReturn(entity);
			
			entity.archiveEntity();
			entity.setModifiedBy(modifyUser);
			replay(entity);
			
			expect(em.merge(entities.get(i))).andReturn(entities.get(i));
		}
		
		replay(em);
		
		EntityListArchiver<SimpleArchivableEntity> archiver = new EntityListArchiver<SimpleArchivableEntity>(SimpleArchivableEntity.class, new HashSet<Long>(ids), modifyUser);
		
		archiver.archive(em);		
	}
}
