package com.n4systems.model.builders;

import com.n4systems.model.InspectionBook;
import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.BaseOrg;

public class InspectionBookBuilder extends BaseBuilder<InspectionBook> {

    private Tenant tenant;
    private BaseOrg owner;
    private String name;
    private boolean open;

    private InspectionBookBuilder(Tenant tenant, BaseOrg owner, String name, boolean open) {
        this.tenant = tenant;
        this.owner = owner;
        this.name = name;
        this.open = open;
    }

    public InspectionBookBuilder forTenant(Tenant tenant) {
        return makeBuilder(new InspectionBookBuilder(tenant, owner, name, open));
    }

    public InspectionBookBuilder withOwner(BaseOrg owner) {
        return makeBuilder(new InspectionBookBuilder(tenant, owner, name, open));
    }

    public InspectionBookBuilder withName(String name) {
        return makeBuilder(new InspectionBookBuilder(tenant, owner, name, open));
    }

    public InspectionBookBuilder open(boolean open) {
        return makeBuilder(new InspectionBookBuilder(tenant, owner, name, open));
    }

    @Override
    public InspectionBook createObject() {
        InspectionBook book = new InspectionBook();
        book.setTenant(tenant);
        book.setOwner(owner);
        book.setName(name);
        book.setOpen(open);

        return book;
    }

    public static InspectionBookBuilder anInspectionBook() {
        return new InspectionBookBuilder(null, null, null, false);
    }
}
