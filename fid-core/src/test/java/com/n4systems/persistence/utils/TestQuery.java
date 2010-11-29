package com.n4systems.persistence.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Parameter;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import com.n4systems.exceptions.NotImplementedException;

public class TestQuery implements Query {

	private Map<String, Object> parameters = new HashMap<String, Object>();;

	public int executeUpdate() {
		return 0;
	}

	@SuppressWarnings("unchecked")
	public List getResultList() {
		return null;
	}

	public Object getSingleResult() {
		return null;
	}

	public Query setFirstResult(int arg0) {
		return this;
	}

	public Query setFlushMode(FlushModeType arg0) {
		throw new NotImplementedException();
	}

	public Query setHint(String arg0, Object arg1) {
		throw new NotImplementedException();
	}

	public Query setMaxResults(int arg0) {
		return this;
	}

	public Query setParameter(String name, Object value) {
		parameters.put(name, value);
		return this;
	}

	public Query setParameter(int arg0, Object arg1) {
		throw new NotImplementedException();
	}

	public Query setParameter(String arg0, Date arg1, TemporalType arg2) {
		throw new NotImplementedException();
	}

	public Query setParameter(String arg0, Calendar arg1, TemporalType arg2) {
		throw new NotImplementedException();
	}

	public Query setParameter(int arg0, Date arg1, TemporalType arg2) {
		throw new NotImplementedException();
	}

	public Query setParameter(int arg0, Calendar arg1, TemporalType arg2) {
		throw new NotImplementedException();
	}

	@Override
	public int getFirstResult() {
		return 0;
	}

	@Override
	public FlushModeType getFlushMode() {
		return null;
	}

	@Override
	public Map<String, Object> getHints() {
		return null;
	}

	@Override
	public LockModeType getLockMode() {
		return null;
	}

	@Override
	public int getMaxResults() {
		return 0;
	}

	@Override
	public Parameter<?> getParameter(String name) {
		return null;
	}

	@Override
	public Parameter<?> getParameter(int position) {
		return null;
	}

	@Override
	public <T> Parameter<T> getParameter(String name, Class<T> type) {
		return null;
	}

	@Override
	public <T> Parameter<T> getParameter(int position, Class<T> type) {
		return null;
	}

	@Override
	public <T> T getParameterValue(Parameter<T> param) {
		return null;
	}

	@Override
	public Object getParameterValue(String name) {
		return null;
	}

	@Override
	public Object getParameterValue(int position) {
		return null;
	}

	@Override
	public Set<Parameter<?>> getParameters() {
		return null;
	}

	@Override
	public boolean isBound(Parameter<?> param) {
		return false;
	}

	@Override
	public Query setLockMode(LockModeType lockMode) {
		return null;
	}

	@Override
	public <T> Query setParameter(Parameter<T> param, T value) {
		return null;
	}

	@Override
	public Query setParameter(Parameter<Calendar> param, Calendar value, TemporalType temporalType) {
		return null;
	}

	@Override
	public Query setParameter(Parameter<Date> param, Date value, TemporalType temporalType) {
		return null;
	}

	@Override
	public <T> T unwrap(Class<T> cls) {
		return null;
	}

}
