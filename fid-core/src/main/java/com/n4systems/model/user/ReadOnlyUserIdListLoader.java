package com.n4systems.model.user;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.security.UserType;
import com.n4systems.util.persistence.QueryBuilder;

import javax.persistence.EntityManager;
import java.util.List;

public class ReadOnlyUserIdListLoader extends ListLoader<Long> {

	public ReadOnlyUserIdListLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<Long> load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<Long> builder = new QueryBuilder<Long>(User.class, filter);
		builder.addSimpleWhere("userType", UserType.READONLY);
		UserQueryHelper.applyFullyActiveFilter(builder);
		builder.setSimpleSelect("id", true);
				
		return builder.getResultList(em);
	}
	

}
