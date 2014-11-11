package com.n4systems.fieldid.service;

import com.n4systems.model.PublicIdEncoder;
import com.n4systems.model.parents.AbstractEntity;
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
	public T update(T model) {
		return persistenceService.update(model);
	}
}
