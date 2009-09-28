package com.n4systems.testutils;

import javax.persistence.EntityTransaction;

public class DummyEntityTransaction implements EntityTransaction {

	public void begin() {}

	public void commit() {}

	public boolean getRollbackOnly() {
		return false;
	}

	public boolean isActive() {
		return false;
	}

	public void rollback() {}

	public void setRollbackOnly() {}

}
