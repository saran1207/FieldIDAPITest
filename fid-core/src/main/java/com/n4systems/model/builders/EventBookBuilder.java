package com.n4systems.model.builders;

import com.n4systems.model.EventBook;
import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.BaseOrg;

public class EventBookBuilder extends BaseBuilder<EventBook> {

    private Tenant tenant;
    private BaseOrg owner;
    private String name;
    private boolean open;

    private EventBookBuilder(Tenant tenant, BaseOrg owner, String name, boolean open) {
        this.tenant = tenant;
        this.owner = owner;
        this.name = name;
        this.open = open;
    }

    public EventBookBuilder forTenant(Tenant tenant) {
        return makeBuilder(new EventBookBuilder(tenant, owner, name, open));
    }

    public EventBookBuilder withOwner(BaseOrg owner) {
        return makeBuilder(new EventBookBuilder(tenant, owner, name, open));
    }

    public EventBookBuilder withName(String name) {
        return makeBuilder(new EventBookBuilder(tenant, owner, name, open));
    }

    public EventBookBuilder open(boolean open) {
        return makeBuilder(new EventBookBuilder(tenant, owner, name, open));
    }

    @Override
    public EventBook createObject() {
        EventBook book = new EventBook();
        book.setTenant(tenant);
        book.setOwner(owner);
        book.setName(name);
        book.setOpen(open);

        return book;
    }

    public static EventBookBuilder anEventBook() {
        return new EventBookBuilder(null, null, null, false);
    }
}
