package com.n4systems.fieldid.service.event;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.*;
import com.n4systems.model.builders.AssetStatusBuilder;
import com.n4systems.util.persistence.QueryBuilder;

import java.util.List;
import java.util.stream.Collectors;

public class EventTypeRulesService extends FieldIdPersistenceService {

    private static List<EventResult> results = Lists.newArrayList(EventResult.PASS, EventResult.FAIL, EventResult.NA);

    public List<EventTypeRule> getAllRules(EventType eventType) {
        return results.stream().map(result-> getRule(eventType, result)).collect(Collectors.toList());
    }

    public EventTypeRule getRule(EventType eventType, EventResult result) {
        EventTypeRule rule = persistenceService.find(getEventTypeRuleQueryBuilder(eventType, result));

        if (rule == null) {
            return new EventTypeRule(eventType, result);
        } else {
            return rule;
        }
    }

    public Boolean exists(EventType eventType, EventResult result) {
        return persistenceService.exists(getEventTypeRuleQueryBuilder(eventType, result));
    }

    private QueryBuilder<EventTypeRule> getEventTypeRuleQueryBuilder(EventType eventType, EventResult result) {
        QueryBuilder<EventTypeRule> query = createTenantSecurityBuilder(EventTypeRule.class);
        query.addSimpleWhere("eventType", eventType);
        query.addSimpleWhere("result", result);
        return query;
    }

    public EventTypeRule saveOrUpdateRule(EventTypeRule rule) {
        Preconditions.checkArgument(rule.getAssetStatus().getId() != -1);
        return persistenceService.saveOrUpdate(rule);
    }

    public void deleteRule(EventType eventType, EventResult result) {
        EventTypeRule rule = getRule(eventType, result);
        persistenceService.remove(rule);
    }

    public static AssetStatus getNoChangeStatus() {
        AssetStatus noChangeAssetStatus = AssetStatusBuilder.anAssetStatus().named("No Change").createObject();
        noChangeAssetStatus.setId(-1L);
        return noChangeAssetStatus;
    }

    public void deleteRules(AssetStatus assetStatus) {
        QueryBuilder<EventTypeRule> query = createTenantSecurityBuilder(EventTypeRule.class);
        query.addSimpleWhere("assetStatus", assetStatus);
        persistenceService.findAll(query).stream().forEach(rule -> persistenceService.remove(rule));
    }

}
