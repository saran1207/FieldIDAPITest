package com.n4systems.fieldid.selenium.persistence.builder;

import com.n4systems.fieldid.selenium.persistence.Scenario;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetType;
import com.n4systems.model.Event;
import com.n4systems.model.builders.BaseBuilder;

public class SimpleEventBuilder extends BaseBuilder<Event> {

    private Scenario scenario;

    public static SimpleEventBuilder aSimpleEvent(Scenario scenario) {
        return new SimpleEventBuilder(scenario);
    }

    private SimpleEventBuilder(Scenario scenario) {
        this.scenario = scenario;
    }

    @Override
    public Event createObject() {
        AssetType type = scenario.anAssetType()
                .forTenant(scenario.defaultTenant())
                .named("Test Asset Type")
                .build();

        Asset asset = scenario.anAsset()
                .withOwner(scenario.primaryOrgFor("seafit"))
                .forTenant(scenario.defaultTenant())
                .withSerialNumber("9671111")
                .ofType(type)
                .build();

        return scenario.anEvent().on(asset)
                .withPerformedBy(scenario.defaultUser())
                .withOwner(scenario.primaryOrgFor("seafit"))
                .withTenant(scenario.defaultTenant()).build();
    }

}
