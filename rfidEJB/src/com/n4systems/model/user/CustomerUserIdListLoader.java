package com.n4systems.model.user;

import java.util.List;

import javax.persistence.EntityManager;

import rfid.ejb.entity.UserBean;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class CustomerUserIdListLoader extends ListLoader<Long> {

	public CustomerUserIdListLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<Long> load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<Long> builder = new QueryBuilder<Long>(UserBean.class, filter);
		builder.addSimpleWhere("system", false);
		builder.addSimpleWhere("deleted", false);
		builder.addWhere(new WhereParameter<Long>(Comparator.NOTNULL, "owner.customerOrg"));
		builder.setSimpleSelect("id", true);
				
		return builder.getResultList(em);
	}
	

}
