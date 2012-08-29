package com.n4systems.ejb.wrapper;

import com.n4systems.ejb.PredefinedLocationManager;
import com.n4systems.ejb.impl.PredefinedLocationManagerImpl;
import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.FieldIdTransactionManager;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.TransactionManager;

import javax.persistence.EntityManager;

public class PredefinedLocationManagerEJBContainer extends EJBTransactionEmulator<PredefinedLocationManager> implements PredefinedLocationManager {

    @Override
    public void updateChildrenOwner(SecurityFilter securityFilter, PredefinedLocation parentNode) {
        TransactionManager transactionManager = new FieldIdTransactionManager();
        Transaction transaction = transactionManager.startTransaction();
        try {
            createManager(transaction.getEntityManager()).updateChildrenOwner(securityFilter, parentNode);
        } catch (RuntimeException e) {
            transactionManager.rollbackTransaction(transaction);
            throw e;
        } finally {
            transactionManager.finishTransaction(transaction);
        }
    }

    @Override
    protected PredefinedLocationManager createManager(EntityManager em) {

        return new PredefinedLocationManagerImpl(em);
    }

}

