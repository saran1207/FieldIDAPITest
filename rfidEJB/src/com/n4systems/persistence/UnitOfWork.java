package com.n4systems.persistence;

public interface UnitOfWork<T> {

	T run(Transaction transaction);

}
