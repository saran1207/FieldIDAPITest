package com.n4systems.persistence;


/**
 * this transaction manager allows start transaction to create a new transaction if the pervious one was closed.
 * @author aaitken
 *
 */
public interface MultiTransactionManager extends TransactionManager {

}
