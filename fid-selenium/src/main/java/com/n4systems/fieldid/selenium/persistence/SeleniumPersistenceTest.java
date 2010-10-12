package com.n4systems.fieldid.selenium.persistence;

import com.n4systems.model.Tenant;
import com.n4systems.persistence.PersistenceManager;
import com.n4systems.persistence.Transaction;

import javax.persistence.EntityManager;

public class SeleniumPersistenceTest {

    public void testPersistence() {
        Transaction tx = PersistenceManager.startTransaction();

        EntityManager em = tx.getEntityManager();

        Tenant t = em.find(Tenant.class, 15511513L);

        System.out.println("Found tenant: " + t);

        tx.commit();
    }

    public static void main(String[] args) {
        new SeleniumPersistenceTest().testPersistence();
    }
}
