package com.n4systems.model.tenant;

import java.util.List;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.persistence.loaders.legacy.EntityLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class SetupDataLastModDatesListLoader extends EntityLoader<List<SetupDataLastModDates>> {

	public SetupDataLastModDatesListLoader() {
		super();
	}
	
	public SetupDataLastModDatesListLoader(PersistenceManager pm) {
		super(pm);
	}

	@Override
	protected List<SetupDataLastModDates> load(PersistenceManager pm) {
		QueryBuilder<SetupDataLastModDates> builder = new QueryBuilder<SetupDataLastModDates>(SetupDataLastModDates.class);
		
		List<SetupDataLastModDates> setupModDates = pm.findAll(builder);
		return setupModDates;
	}

}
