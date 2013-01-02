package com.n4systems.model.builders;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.n4systems.api.conversion.event.CriteriaResultFactory;
import com.n4systems.model.*;

import java.util.List;

public class CriteriaResultBuilder extends BaseBuilder<CriteriaResult> {

    private CriteriaResultFactory criteriaResultFactory = new CriteriaResultFactory();
    private CriteriaType type;
    private Criteria criteria;
    private List<Recommendation> recommendations = Lists.newArrayList();
    private List<Deficiency> deficiencies = Lists.newArrayList();
    private AbstractEvent event;

    public static CriteriaResultBuilder aCriteriaResult() {
		return new CriteriaResultBuilder();
	}

    private CriteriaResultBuilder() {
        super(null);
    }

    public CriteriaResultBuilder(Criteria criteria, AbstractEvent event, CriteriaType type, List<Recommendation> recommendations, List<Deficiency> deficiencies) {
        super(null);
        this.criteria = criteria;
        this.event = event;
        this.type = type;
        this.recommendations = recommendations;
        this.deficiencies = deficiencies;
    }

    public CriteriaResultBuilder withCriteria(Criteria criteria) {
        return new CriteriaResultBuilder(criteria, event, type, recommendations, deficiencies);
    }

    public CriteriaResultBuilder withType(CriteriaType type) {
        return new CriteriaResultBuilder(criteria, event, type, recommendations, deficiencies);
    }

    public CriteriaResultBuilder withRecommendations(List<Recommendation> recommendations) {
        Preconditions.checkArgument(recommendations!=null);
        return new CriteriaResultBuilder(criteria, event, type, recommendations, deficiencies);
    }

    public CriteriaResultBuilder withDeficiencies(List<Deficiency> deficiencies) {
        Preconditions.checkArgument(deficiencies!=null);
        return new CriteriaResultBuilder(criteria, event, type, recommendations, deficiencies);
    }

    @Override
	public CriteriaResult createObject() {
		CriteriaResult result = criteriaResultFactory.createCriteriaResult(type);
        result.setCriteria(criteria);
        result.setRecommendations(recommendations);
        result.setDeficiencies(deficiencies);
		result.setId(getId());
        result.setEvent(event);
		return result;
	}

}
