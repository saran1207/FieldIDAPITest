package com.n4systems.persistence.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.FlushModeType;
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

}
