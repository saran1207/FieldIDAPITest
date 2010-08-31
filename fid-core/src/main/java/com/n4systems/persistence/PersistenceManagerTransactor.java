package com.n4systems.persistence;

import static com.n4systems.persistence.PersistenceManager.*;

public class PersistenceManagerTransactor implements Transactor {
	
	public <T> T execute(UnitOfWork<T> unit) {
		T result = null;
		Transaction transaction = startTransaction();
		try {
			result = unit.run(transaction);
		} catch(RuntimeException e) {
			rollbackTransaction(transaction);
			throw e;
		} finally {
			finishTransaction(transaction);
		}
		
		return result;
	}
}
