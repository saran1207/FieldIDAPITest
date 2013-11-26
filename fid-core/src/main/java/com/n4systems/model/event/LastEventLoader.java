package com.n4systems.model.event;

import com.n4systems.model.Event;
import com.n4systems.model.ThingEvent;
import com.n4systems.model.WorkflowState;
import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.PassthruWhereClause;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Loads the latest Event for each Event Type for a given Asset
 */
@Deprecated
public class LastEventLoader extends ListLoader<ThingEvent> {
	private Long assetId;
	
	public LastEventLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<ThingEvent> load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<ThingEvent> builder = new QueryBuilder<ThingEvent>(ThingEvent.class, filter, "i");
		builder.addWhere(WhereClauseFactory.create("asset.id", assetId));

		PassthruWhereClause latestClause = new PassthruWhereClause("latest_event");
		String maxDateSelect = String.format("SELECT MAX(iSub.completedDate) FROM %s iSub WHERE iSub.state = :iSubState AND iSub.type.state = :iSubState AND iSub.asset.id = :iSubAssetId AND iSub.workflowState = :workflowState GROUP BY iSub.type", ThingEvent.class.getName());
		latestClause.setClause(String.format("i.completedDate IN (%s)", maxDateSelect));
		latestClause.getParams().put("iSubAssetId", assetId);
		latestClause.getParams().put("iSubState", EntityState.ACTIVE);
        latestClause.getParams().put("workflowState", WorkflowState.COMPLETED);
		builder.addWhere(latestClause);
		
		List<ThingEvent> lastEvents = builder.getResultList(em);
		return lastEvents;
	}

	public LastEventLoader setAssetId(Long assetId) {
		this.assetId = assetId;
		return this;
	}
}
