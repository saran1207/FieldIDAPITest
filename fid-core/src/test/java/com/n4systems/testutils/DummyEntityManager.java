package com.n4systems.testutils;

import java.util.List;
import java.util.Map;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.metamodel.Metamodel;

public class DummyEntityManager implements EntityManager {
	
	public DummyEntityManager() {}
	
	public void clear() {}
	
	public void close() {}
	
	public boolean contains(Object arg0) {
		return false;
	}
	
	public Query createNamedQuery(String arg0) {
		return null;
	}
	
	public Query createNativeQuery(String arg0) {
		return null;
	}
	
	
	@SuppressWarnings("unchecked")
	public Query createNativeQuery(String arg0, Class arg1) {
		return null;
	}
	
	public Query createNativeQuery(String arg0, String arg1) {
		return null;
	}

    @Override
    public StoredProcedureQuery createNamedStoredProcedureQuery(String s) {
        return null;
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String s) {
        return null;
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String s, Class... classes) {
        return null;
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String s, String... strings) {
        return null;
    }

    public Query createQuery(String arg0) {
		return null;
	}
	
	public <T> T find(Class<T> arg0, Object arg1) {
		return null;
	}
	
	public void flush() {}
	
	public Object getDelegate() {
		return this;
	}
	
	public FlushModeType getFlushMode() {
		return null;
	}
	
	public <T> T getReference(Class<T> arg0, Object arg1) {
		return null;
	}
	
	public EntityTransaction getTransaction() {
		return new DummyEntityTransaction();
	}
	
	public boolean isOpen() {
		return true;
	}
	
	public void joinTransaction() {}

    @Override
    public boolean isJoinedToTransaction() {
        return false;
    }

    public void lock(Object arg0, LockModeType arg1) {}
	
	public <T> T merge(T arg0) {
		return null;
	}
	
	public void persist(Object arg0) {}
	
	public void refresh(Object arg0) {}
	
	public void remove(Object arg0) {}
	
	public void setFlushMode(FlushModeType arg0) {}

	@Override
	public <T> TypedQuery<T> createNamedQuery(String name, Class<T> resultClass) {
		return null;
	}

	@Override
	public <T> TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery) {
		return null;
	}

    @Override
    public Query createQuery(CriteriaUpdate criteriaUpdate) {
        return null;
    }

    @Override
    public Query createQuery(CriteriaDelete criteriaDelete) {
        return null;
    }

    @Override
	public <T> TypedQuery<T> createQuery(String qlString, Class<T> resultClass) {
		return null;
	}

	@Override
	public void detach(Object entity) {
	}

	@Override
	public <T> T find(Class<T> entityClass, Object primaryKey, Map<String, Object> properties) {
		return null;
	}

	@Override
	public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode) {
		return null;
	}

	@Override
	public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode, Map<String, Object> properties) {
		return null;
	}

	@Override
	public CriteriaBuilder getCriteriaBuilder() {
		return null;
	}

	@Override
	public EntityManagerFactory getEntityManagerFactory() {
		return null;
	}

	@Override
	public LockModeType getLockMode(Object entity) {
		return null;
	}

	@Override
	public Metamodel getMetamodel() {
		return null;
	}

    @Override
    public <T> EntityGraph<T> createEntityGraph(Class<T> tClass) {
        return null;
    }

    @Override
    public EntityGraph<?> createEntityGraph(String s) {
        return null;
    }

    @Override
    public EntityGraph<?> getEntityGraph(String s) {
        return null;
    }

    @Override
    public <T> List<EntityGraph<? super T>> getEntityGraphs(Class<T> tClass) {
        return null;
    }

    @Override
	public Map<String, Object> getProperties() {
		return null;
	}

	@Override
	public void lock(Object entity, LockModeType lockMode, Map<String, Object> properties) {
	}

	@Override
	public void refresh(Object entity, Map<String, Object> properties) {	
	}

	@Override
	public void refresh(Object entity, LockModeType lockMode) {
	}

	@Override
	public void refresh(Object entity, LockModeType lockMode, Map<String, Object> properties) {	
	}

	@Override
	public void setProperty(String propertyName, Object value) {
	}

	@Override
	public <T> T unwrap(Class<T> cls) {
		return null;
	}
}

