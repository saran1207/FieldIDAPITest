package com.n4systems.persistence;

public class ContinueExistingTransactionManager implements TransactionManager {

    private Transaction tx;

    public ContinueExistingTransactionManager(Transaction tx) {
        this.tx = tx;
    }

    @Override
    public Transaction startTransaction() {
        return tx;
    }

    @Override
    public void rollbackTransaction(Transaction transaction) {
        tx.getEntityTransaction().setRollbackOnly();
    }

    @Override
    public void finishTransaction(Transaction transaction) {
    }

}
