package com.n4systems.model.builders;

import com.n4systems.model.parents.AbstractEntity;
import com.n4systems.model.user.User;

import java.util.Date;

public abstract class AbstractEntityBuilder<K extends AbstractEntity> extends BaseEntityBuilder<K> {
	protected Date created;
	protected Date modified;
	protected User modifiedBy;
	
	public AbstractEntityBuilder(Long id, Date created, Date modified, User modifiedBy) {
		super(id);
		this.created = created;
		this.modified = modified;
		this.modifiedBy = modifiedBy;
	}
	
	@Override
	protected K assignAbstractFields(K model) {
		super.assignAbstractFields(model);
		model.setCreated(created);
		model.setModified(modified);
		model.setModifiedBy(modifiedBy);
		return model;
	}

    public void modifiedBy(User user) {
        this.modifiedBy = user;
    }
	
}
