package com.n4systems.persistence;

public interface Transactor {

	public <T> T execute(UnitOfWork<T> unit);

}