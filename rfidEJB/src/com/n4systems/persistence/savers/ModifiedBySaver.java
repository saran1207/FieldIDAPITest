package com.n4systems.persistence.savers;

import rfid.ejb.entity.UserBean;

import com.n4systems.model.api.HasModifiedBy;
import com.n4systems.model.api.Saveable;
import com.n4systems.model.user.UserUnfilteredLoader;
import com.n4systems.persistence.Transaction;

public abstract class ModifiedBySaver<T extends Saveable & HasModifiedBy> extends Saver<T> {
	private final UserBean modifiedBy;
	
	public ModifiedBySaver() {
		this((UserBean)null);
	}
	
	public ModifiedBySaver(UserBean modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	
	public ModifiedBySaver(Long modifiedBy) {
		this(new UserUnfilteredLoader().setId(modifiedBy).load());
	}

	@Override
	public void save(Transaction transaction, T entity) {
		entity.setModifiedBy(modifiedBy);
		super.save(transaction, entity);
	}

	@Override
	public T update(Transaction transaction, T entity) {
		entity.setModifiedBy(modifiedBy);
		return super.update(transaction, entity);
	}

}
