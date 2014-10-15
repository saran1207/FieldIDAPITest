package com.n4systems.fieldid.service;

import com.n4systems.model.BaseEntity;
import com.n4systems.model.PublicIdEncoder;
import com.n4systems.model.parents.*;
import com.n4systems.util.persistence.QueryBuilder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public abstract class CrudService<T extends AbstractEntity> extends FieldIdPersistenceService {
	private final Class<T> entity;

	protected CrudService(Class<T> entity) {
		this.entity = entity;
	}

	@Transactional(readOnly = true)
	public T findById(Long id) {
		return persistenceService.findById(entity, id);
	}

	@Transactional(readOnly = true)
	public T findByPublicId(String id) {
		return findById(PublicIdEncoder.decode(id));
	}

	@Transactional(readOnly = true)
	public T findOne(QueryBuilder<T> builder) {
		return persistenceService.find(builder);
	}

	@Transactional(readOnly = true)
	public List<T> findAll(int page, int pageSize) {
		return findAll(createUserSecurityBuilder(entity), page, pageSize);
	}

	@Transactional(readOnly = true)
	public List<T> findAll(QueryBuilder<T> builder, int page, int pageSize) {
		return persistenceService.findAll(builder, page, pageSize);
	}

	@Transactional(readOnly = true)
	public Long count() {
		return count(createUserSecurityBuilder(entity));
	}

	@Transactional(readOnly = true)
	public Long count(QueryBuilder<T> builder) {
		return persistenceService.count(builder);
	}

	@Transactional
	public T save(T model) {
		persistenceService.save(model);
		return model;
	}

	@Transactional
	public T update(Long id, T model) {
		T old = findById(id);
		if (old == null)
			throw new IllegalArgumentException("Existing entity with id [" + id + "] not found");

		mergeEntity(model, old);
		return persistenceService.update(old);
	}

	protected abstract void mergeEntity(T from, T to);

	protected void merge(BaseEntity from, BaseEntity to) {
		to.setId(from.getId());
	}

	protected void merge(AbstractEntity from, AbstractEntity to) {
		merge((BaseEntity) from, to);
		to.setCreated(from.getCreated());
		to.setCreatedBy(from.getCreatedBy());
		to.setModified(from.getModified());
		to.setModifiedBy(from.getModifiedBy());
	}

	protected void merge(EntityWithTenant from, EntityWithTenant to) {
		merge((AbstractEntity) from, to);
		to.setTenant(from.getTenant());
	}

	protected void merge(ArchivableEntityWithTenant from, ArchivableEntityWithTenant to) {
		merge((EntityWithTenant) from, to);
		to.setState(from.getEntityState());
	}

	protected void merge(EntityWithOwner from, EntityWithOwner to) {
		merge((EntityWithTenant) from, to);
		to.setOwner(from.getOwner());
	}

	protected void merge(ArchivableEntityWithOwner from, ArchivableEntityWithOwner to) {
		merge((EntityWithOwner) from, to);
		to.setEntityState(from.getEntityState());
	}
}
