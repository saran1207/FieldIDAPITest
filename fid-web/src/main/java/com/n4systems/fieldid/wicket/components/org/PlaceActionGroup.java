package com.n4systems.fieldid.wicket.components.org;

import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.CustomerOrg;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class PlaceActionGroup extends Panel {

    private final IModel<BaseOrg> model;

    public PlaceActionGroup(String id, IModel<BaseOrg> model) {
        super(id);

        this.model = model;

        add(new Link<Void>("scheduleLink") {
            @Override public void onClick() {
            }
        });

        add( new NonWicketLink("mergeLink", "mergeCustomers.action?uniqueID="+ getOrg().getId()) {
            @Override public boolean isVisible() {
                return getOrg() instanceof CustomerOrg;
            }
        });

        add(new Link<Void>("archiveLink") {
            @Override public void onClick() {
            }
        });

        add(new Link<Void>("recurringSchedulesLink") {
            @Override public void onClick() {
            }
        });

        add(new Link<Void>("eventTypesLink") {
            @Override public void onClick() {
            }
        });
    }

    private BaseOrg getOrg() {
        return model.getObject();
    }
}
