package com.n4systems.fieldid.selenium;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.n4systems.persistence.PersistenceManager;
import com.n4systems.persistence.Transaction;

public class PersistenceCheck {
	
	public static void main(String[] args) throws Exception {
		new PersistenceCheck().execute();
	}

	private void execute() throws Exception {
		Map<String, String> connectionProperties = new HashMap<String,String>();

//		Class<?> c = Class.forName("com.n4systems.persistence.listeners.SetupDataUpdateEventListener");
//		System.out.println("got: " + c);
//		whereDidThisClassComeFrom("org.hibernate.search.event.FullTextIndexEventListener");
//		whereDidThisClassComeFrom("org.hibernate.event.EventListeners");
		
//		PersistenceManager.createEntityManagerFactoryWithProperties("fieldidtest", connectionProperties);
		
		Transaction tx = PersistenceManager.startTransaction();
		
		EntityManager em = tx.getEntityManager();
		Query query = em.createQuery("from Tenant");
		List resultList = query.getResultList();
		System.out.println("Results: " + resultList.size());
		
		tx.commit();
		
//		SessionFactory sf = new Configuration().configure().buildSessionFactory();
//		
//		Map<String,ClassMetadata> res = sf.getAllClassMetadata();
//		Set<String> keys = res.keySet();
//		System.out.println("Size of keys: " + keys.size());
//		for (String key : keys) {
//			System.out.println("Map: " + key);
//		}
	}
	
	public static void whereDidThisClassComeFrom(String className) throws Exception {
		String location = Class.forName(className).getProtectionDomain().getCodeSource().getLocation().toString();
		System.out.println(className + " from: " + location);
	}
	
}
