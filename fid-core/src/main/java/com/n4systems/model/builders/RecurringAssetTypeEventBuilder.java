package com.n4systems.model.builders;

import com.n4systems.model.*;
import com.n4systems.model.orgs.BaseOrg;

public class RecurringAssetTypeEventBuilder extends BaseBuilder<RecurringAssetTypeEvent> {

    private BaseOrg owner;
    private Recurrence recurrence;
    private Tenant tenant;
    private EventType eventType;
    private AssetType assetType;
    private Boolean ownerAndDown;

    private RecurringAssetTypeEventBuilder(AssetType assetType, EventType eventType, BaseOrg owner, Recurrence recurrence, Tenant tenant, Boolean ownerAndDown) {
        super(null);
        this.assetType = assetType;
        this.eventType = eventType;
        this.owner = owner;
        this.recurrence = recurrence;
        this.tenant = tenant;
        this.ownerAndDown = ownerAndDown;
    }

    public static RecurringAssetTypeEventBuilder anAssetTypeEvent() {
        AssetType assetType = AssetTypeBuilder.anAssetType().build();
        EventType eventType = EventTypeBuilder.anEventType().build();
        BaseOrg owner = OrgBuilder.aPrimaryOrg().build();
        Recurrence recurrence = RecurrenceBuilder.aRecurrence().build();
        Tenant tenant = TenantBuilder.aTenant().build();
        return new RecurringAssetTypeEventBuilder(assetType, eventType, owner, recurrence, tenant, false);
    }

    public RecurringAssetTypeEventBuilder withAssetType(AssetType assetType) {
        return new RecurringAssetTypeEventBuilder(assetType, eventType, owner, recurrence, tenant, ownerAndDown);
    }

    public RecurringAssetTypeEventBuilder withEventType(EventType eventType) {
        return new RecurringAssetTypeEventBuilder(assetType, eventType, owner, recurrence, tenant, ownerAndDown);
    }

    public RecurringAssetTypeEventBuilder withOwner(BaseOrg owner) {
        return new RecurringAssetTypeEventBuilder(assetType, eventType, owner, recurrence, tenant, ownerAndDown);
    }

    public RecurringAssetTypeEventBuilder withRecurrence(Recurrence recurrence) {
        return new RecurringAssetTypeEventBuilder(assetType, eventType, owner, recurrence, tenant, ownerAndDown);
    }

    public RecurringAssetTypeEventBuilder withTenant(Tenant tenant) {
        return new RecurringAssetTypeEventBuilder(assetType, eventType, owner, recurrence, tenant, ownerAndDown);
    }


    @Override
    public RecurringAssetTypeEvent createObject() {
        RecurringAssetTypeEvent event;

        event = new RecurringAssetTypeEvent();
        event.setOwner(owner);
        event.setRecurrence(recurrence);
        event.setTenant(tenant);
        event.setEventType(eventType);
        event.setAssetType(assetType);
        event.setOwnerAndDown(ownerAndDown);

        return event;
    }

}
