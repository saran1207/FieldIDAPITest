package com.n4systems.model.inspection;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.Event;
import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.PassthruWhereClause;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

/**
 * Loads the latest Inspection for each Inspection Type for a given Asset
 */
public class LastInspectionLoader extends ListLoader<Event> {
	private Long assetId;
	
	public LastInspectionLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<Event> load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<Event> builder = new QueryBuilder<Event>(Event.class, filter, "i");
		builder.addWhere(WhereClauseFactory.create("asset.id", assetId));
		
		PassthruWhereClause latestClause = new PassthruWhereClause("latest_inspection");
		String maxDateSelect = String.format("SELECT MAX(iSub.date) FROM %s iSub WHERE iSub.state = :iSubState AND iSub.type.state = :iSubState AND iSub.asset.id = :iSubAssetId GROUP BY iSub.type", Event.class.getName());
		latestClause.setClause(String.format("i.date IN (%s)", maxDateSelect));
		latestClause.getParams().put("iSubAssetId", assetId);
		latestClause.getParams().put("iSubState", EntityState.ACTIVE);
		builder.addWhere(latestClause);
		
		List<Event> lastEvents = builder.getResultList(em);
		return lastEvents;
	}

	public LastInspectionLoader setAssetId(Long assetId) {
		this.assetId = assetId;
		return this;
	}
}
