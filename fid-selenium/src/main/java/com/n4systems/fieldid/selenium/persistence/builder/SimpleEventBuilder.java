package com.n4systems.fieldid.selenium.persistence.builder;

import com.n4systems.fieldid.selenium.persistence.Scenario;
import com.n4systems.model.*;
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
                .named("Test Asset Type")
                .build();

        Asset asset = scenario.anAsset()
                .withOwner(scenario.defaultPrimaryOrg())
                .withIdentifier("9671111")
                .ofType(type)
                .build();

        EventForm eventForm = scenario.anEventForm().build();

        EventType eventType = scenario.anEventType()
                .withEventForm(eventForm)
                .named("Test Event Type")
                .build();
        
        Event event = scenario.anEvent().on(asset)
                .ofType(eventType)
                .withPerformedBy(scenario.defaultUser())
                .withOwner(scenario.defaultPrimaryOrg())
                .withTenant(scenario.defaultTenant())
                .build();
        
//        scenario.save(group);
        
        return event;
    }

}
