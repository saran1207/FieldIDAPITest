package com.n4systems.fieldid.selenium.persistence;

import com.n4systems.persistence.Transaction;

public interface PersistenceCallback {

    public void doInTransaction(Transaction transaction) throws Exception;

}
