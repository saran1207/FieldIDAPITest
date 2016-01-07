package com.fieldid.data.service;

import com.n4systems.fieldid.context.ThreadLocalInteractionContext;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.Tenant;
import com.n4systems.model.api.NamedEntity;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import org.apache.log4j.Logger;

import javax.transaction.Transactional;
import java.util.Objects;

public abstract class DataMigrator<T extends EntityWithTenant & NamedEntity> extends FieldIdPersistenceService {
	private static Logger logger = Logger.getLogger(EventTypeGroupMigrator.class);

	private final Class<T> targetClass;

	protected DataMigrator(Class<T> targetClass) {
		this.targetClass = targetClass;
	}

	protected <U> U findByName(Class<U> clazz, String name, Tenant tenant) {
		U entity = persistenceService.find(
				new QueryBuilder<U>(clazz, new TenantOnlySecurityFilter(tenant))
						.addWhere(WhereClauseFactory.create("name", name))
		);
		return Objects.requireNonNull(entity, "Unable to find " + clazz.getSimpleName() + " for name: '" + name + "' under Tenant: " + tenant);
	}

	@Transactional
	public T copy(Long targetId, Long newTenantId, String newName) {
		// Since we have no user context, we will disable the interaction context.
		ThreadLocalInteractionContext.getInstance().disable();

		T target = persistenceService.findNonSecure(targetClass, targetId);
		Objects.requireNonNull(target, "Unable to find " + targetClass.getSimpleName() + " for id: " + targetId);

		Tenant newTenant = (newTenantId != null) ? persistenceService.findNonSecure(Tenant.class, newTenantId) : target.getTenant();
		Objects.requireNonNull(newTenant, "Unable to find Tenant for id: " + newTenantId);

		logger.info("Coping " + targetClass.getSimpleName() + " [" + target.getTenant() + ": " + target + "] to [" + newTenant + ": " + newName + "]");
		return copy(target, newTenant, (newName != null) ? newName : target.getName());
	}

	@Transactional
	protected abstract T copy(T target, Tenant newTenant, String newName);
}
