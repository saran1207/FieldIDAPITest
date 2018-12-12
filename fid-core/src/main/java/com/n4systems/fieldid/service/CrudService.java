package com.n4systems.fieldid.service;

import com.n4systems.model.PublicIdEncoder;
import com.n4systems.model.parents.AbstractEntity;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
	public List<T> findAll(int page, int pageSize, Map<String, Object> optionalParameters) {
		QueryBuilder<T> builder = createUserSecurityBuilder(entity);
		addFindAllParameters(builder, optionalParameters);
		return findAll(builder, page, pageSize);
	}

	@Transactional(readOnly = true)
	public List<T> findAll(QueryBuilder<T> builder, int page, int pageSize) {
		return persistenceService.findAll(builder, page, pageSize);
	}

	@Transactional(readOnly = true)
	public List<T> findAllActionItem(int page, int pageSize) {
		QueryBuilder<T> builder = createUserSecurityBuilder(entity);
		builder.addWhere(WhereParameter.Comparator.NOTNULL, "triggerEvent", "triggerEvent", "");
		return findAll(builder, page, pageSize);
	}

	@Transactional(readOnly = true)
	public List<T> findAllActionItem(int page, int pageSize, Date delta) {
		QueryBuilder<T> builder = createUserSecurityBuilder(entity);
		builder.addWhere(WhereParameter.Comparator.GE, "modified", "modified", delta);
		builder.addWhere(WhereParameter.Comparator.NOTNULL, "triggerEvent", "triggerEvent", "");
		return findAll(builder, page, pageSize);
	}

	@Transactional(readOnly = true)
	public Long countAllActionItem(Date delta) {
		QueryBuilder<T> builder = createUserSecurityBuilder(entity);
		builder.addWhere(WhereParameter.Comparator.GE, "modified", "modified", delta);
		builder.addWhere(WhereParameter.Comparator.NOTNULL, "triggerEvent", "triggerEvent", "");
		return count(builder);
	}

	@Transactional(readOnly = true)
	public Long countAllActionItem() {
		QueryBuilder<T> builder = createUserSecurityBuilder(entity);
		builder.addWhere(WhereParameter.Comparator.NOTNULL, "triggerEvent", "triggerEvent", "");
		return count(builder);
	}

	@Transactional(readOnly = true)
	public Long count(Map<String, Object> optionalParameters) {
		QueryBuilder<T> builder = createUserSecurityBuilder(entity);
		addFindAllParameters(builder, optionalParameters);
		return count(builder);
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

	@Transactional
	public T deleteByPublicId(String id) {
		throw new UnsupportedOperationException(); // delete operation not supported for all entity types
	}

	@Transactional
	public T saveOrUpdate(T model) {
		return persistenceService.saveOrUpdate(model);
	}

	@Transactional
	public List<T> findByAssetId(String assetId, int page, int pageSize, boolean openInspections) {
		throw new UnsupportedOperationException(); // This operation is not supported for all entity types
	}

	@Transactional
	public List<T> findActionItemByAssetId(String assetId, int page, int pageSize, boolean openActionItems) {
		throw new UnsupportedOperationException(); // This operation is not supported for all entity types
	}

	/**
	 * May be extended by subclasses that need to add extra query parameters that apply only to specific entity types.
	 * The 'delta' query parameter is common to all entity types so extensions of this method should be sure to call
	 * super.addFileAllParameters
	 *
	 * @param builder
	 * @param optionalParameters
	 */
	protected void addFindAllParameters(QueryBuilder<T> builder, Map<String, Object> optionalParameters) {
		if (optionalParameters.containsKey("delta"))
			builder.addWhere(WhereParameter.Comparator.GE, "modified", "modified", optionalParameters.get("delta"));
	}
}
