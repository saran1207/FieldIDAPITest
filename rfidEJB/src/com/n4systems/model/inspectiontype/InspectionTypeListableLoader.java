package com.n4systems.model.inspectiontype;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.InspectionType;
import com.n4systems.model.api.Listable;
import com.n4systems.persistence.loaders.legacy.ListableLoader;
import com.n4systems.util.SecurityFilter;
import com.n4systems.util.persistence.ListableSelect;
import com.n4systems.util.persistence.QueryBuilder;

//TODO: Update this class to extend com.n4systems.persistence.loaders.ListableLoader
public class InspectionTypeListableLoader extends ListableLoader {

	public InspectionTypeListableLoader(SecurityFilter filter) {
		super(filter);
	}
	
	public InspectionTypeListableLoader(PersistenceManager pm, SecurityFilter filter) {
		super(pm, filter);
	}

	@Override
	protected QueryBuilder<Listable<Long>> createBuilder(SecurityFilter filter) {
		QueryBuilder<Listable<Long>> builder = new QueryBuilder<Listable<Long>>(InspectionType.class, filter.prepareFor(InspectionType.class));
		builder.setSelectArgument(new ListableSelect());
		builder.addOrder("name");

		return builder;
	}

}
