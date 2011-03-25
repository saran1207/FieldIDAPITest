package com.n4systems.export;

import javax.persistence.EntityManager;

public interface PersistenceTask {
	public void runTask(EntityManager em) throws Exception;
}
