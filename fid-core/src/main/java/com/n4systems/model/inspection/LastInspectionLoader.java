package com.n4systems.model.inspection;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.Inspection;
import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.PassthruWhereClause;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

/**
 * Loads the latest Inspection for each Inspection Type for a given Asset
 */
public class LastInspectionLoader extends ListLoader<Inspection> {
	private Long productId;	
	
	public LastInspectionLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<Inspection> load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<Inspection> builder = new QueryBuilder<Inspection>(Inspection.class, filter, "i");
		builder.addWhere(WhereClauseFactory.create("asset.id", productId));
		
		PassthruWhereClause latestClause = new PassthruWhereClause("latest_inspection");
		String maxDateSelect = String.format("SELECT MAX(iSub.date) FROM %s iSub WHERE iSub.state = :iSubState AND iSub.type.state = :iSubState AND iSub.asset.id = :iSubProductId GROUP BY iSub.type", Inspection.class.getName());
		latestClause.setClause(String.format("i.date IN (%s)", maxDateSelect));
		latestClause.getParams().put("iSubProductId", productId);
		latestClause.getParams().put("iSubState", EntityState.ACTIVE);
		builder.addWhere(latestClause);
		
		List<Inspection> lastInspections = builder.getResultList(em);
		return lastInspections;
	}

	public LastInspectionLoader setProductId(Long productId) {
		this.productId = productId;
		return this;
	}
}
