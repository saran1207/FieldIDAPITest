package com.n4systems.testutils;

import javax.persistence.EntityManager;

import com.n4systems.exceptions.EntityStillReferencedException;
import com.n4systems.model.api.Saveable;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.savers.Saver;

public class DummySaver<T extends Saveable> extends Saver<T> {

	@Override
	public void remove(EntityManager em, T entity) {}

	@Override
	public void remove(T entity) throws EntityStillReferencedException {}

	@Override
	public void remove(Transaction transaction, T entity) {}

	@Override
	public void save(EntityManager em, T entity) {}

	@Override
	public void save(T entity) {}

	@Override
	public void save(Transaction transaction, T entity) {}

	@Override
	public T saveOrUpdate(T entity) {
		return entity;
	}

	@Override
	public T saveOrUpdate(Transaction transaction, T entity) {
		return entity;
	}

	@Override
	public T update(EntityManager em, T entity) {
		return entity;
	}

	@Override
	public T update(T entity) {
		return entity;
	}

	@Override
	public T update(Transaction transaction, T entity) {
		return entity;
	}

}
