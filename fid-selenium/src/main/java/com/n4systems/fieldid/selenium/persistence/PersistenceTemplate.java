package com.n4systems.fieldid.selenium.persistence;

import com.n4systems.persistence.PersistenceManager;
import com.n4systems.persistence.Transaction;

public class PersistenceTemplate {

    private PersistenceCallback callback;

    public PersistenceTemplate(PersistenceCallback callback) {
        this.callback = callback;
    }

    public void execute() throws Exception {
        Transaction transaction = PersistenceManager.startTransaction();
        try {
            callback.doInTransaction(transaction);
            transaction.commit();
        } catch(Exception e) {
            transaction.rollback();
            throw e;
        }
    }

}
