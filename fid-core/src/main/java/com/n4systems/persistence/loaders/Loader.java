package com.n4systems.persistence.loaders;

import javax.persistence.EntityManager;

import com.n4systems.persistence.PersistenceManager;
import com.n4systems.persistence.Transaction;


abstract public class Loader<T> {
    private EntityManager entityManager;

	protected abstract T load(EntityManager em);

	public T load() {
        if (entityManager == null) {
            // Legacy call: not injected with a transactional entity manager so we use the old method
            return PersistenceManager.executeLoader(this);
        }
		return load(entityManager);
	}

	public T load(Transaction transaction) {
        if (entityManager == null) {
            // Legacy call: not injected with a transactional entity manager so we use the old method
            return load(transaction.getEntityManager());
        }
		return load(entityManager);
	}

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

}
