package com.n4systems.model.event;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.EntityManager;

import org.apache.commons.lang.time.DateUtils;

import com.n4systems.model.Event;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class EventCountLoader extends Loader<Long> {

	private Long tenantId;
	
	private boolean limit30Days = false;

	@Override
	public Long load(EntityManager em) {
		SecurityFilter filter = new TenantOnlySecurityFilter(tenantId);
		QueryBuilder<Long> builder = new QueryBuilder<Long>(Event.class, filter);
		
		if (limit30Days) {
			builder.addWhere(WhereClauseFactory.create(Comparator.GE, "created", get30DaysAgo()));
		}
		
		Long assetCount = builder.getCount(em);
		return assetCount;
	}
	
	public EventCountLoader setTenantId(Long tenantId) {
		this.tenantId = tenantId;
		return this;
	}

	public EventCountLoader setLimit30Days() {
		return setLimit30Days(true);
	}

	public EventCountLoader setLimit30Days(boolean limit30Days) {
		this.limit30Days = limit30Days;
		return this;
	}
	
	public Date get30DaysAgo() {
		Date date = DateUtils.addDays(DateUtils.truncate(new Date(), Calendar.DATE), -30);
		return date;
	}
}
