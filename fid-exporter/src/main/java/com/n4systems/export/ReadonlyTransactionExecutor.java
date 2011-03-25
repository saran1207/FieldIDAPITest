package com.n4systems.export;

import javax.persistence.EntityManager;

import com.n4systems.persistence.PersistenceManager;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.loaders.Loader;

public class ReadonlyTransactionExecutor {
	
	public static void execute(PersistenceTask task) throws Exception {
        Transaction transaction = PersistenceManager.startTransaction();
        try {
        	EntityManager em = transaction.getEntityManager();
        	PersistenceManager.setSessionReadOnly(em);
        	task.runTask(em);
        } finally {
        	transaction.rollback();
        }
    }
	
	public static <T> T load(Loader<T> loader) {
        Transaction transaction = PersistenceManager.startTransaction();
        try {
        	EntityManager em = transaction.getEntityManager();
        	PersistenceManager.setSessionReadOnly(em);
        	return loader.load(transaction);
        } finally {
        	transaction.rollback();
        }
    }
}
