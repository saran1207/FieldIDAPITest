package com.n4systems.fieldid.wicket.pages.org;

import com.n4systems.fieldid.wicket.components.navigation.MattBar;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.model.orgs.BaseOrg;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;

public class OrgConfigurePage extends FieldIDFrontEndPage {

    public enum PageState {
        RECURRING_EVENTS,EVENT_TYPES
    }

    PageState state = PageState.EVENT_TYPES;

    public OrgConfigurePage(IModel<BaseOrg> model, PageState state) {
        super();
        WebMarkupContainer container;
        container = new WebMarkupContainer("container");
        container.add(new MattBar("buttons") {
            @Override
            protected void onEnterState(AjaxRequestTarget target, Object state) {
                buttonClicked(target, state);
            }
        }
                .setCurrentState(this.state = state)
                .addLink(new FIDLabelModel("label.recurring_events"), PageState.RECURRING_EVENTS)
                .addLink(new FIDLabelModel("label.event_types"), PageState.EVENT_TYPES)
        );
        container.add(new ConfigureRecurringEventsPanel("recurring"));
        container.add(new ConfigureEventTypesPanel("eventTypes"));
        add(container.setOutputMarkupId(true));
    }

//    public OrgConfigurePage(BaseOrg org, PageState state) {
//        this(new EntityModel<BaseOrg>(BaseOrg.class,org),state);
//    }

    private void buttonClicked(AjaxRequestTarget target, Object state) {
        this.state = (PageState) state;
        target.add(get("container"));
    }


    class ConfigureRecurringEventsPanel extends Fragment {

        public ConfigureRecurringEventsPanel(String id) {
            super(id, "configureRecurringEvents", OrgConfigurePage.this);
            setOutputMarkupId(true);
        }

        @Override
        public boolean isVisible() {
            return PageState.RECURRING_EVENTS.equals(state);
        }
    }

    private class ConfigureEventTypesPanel extends Fragment {

        public ConfigureEventTypesPanel(String id) {
            super(id, "configureEventTypes", OrgConfigurePage.this);
            setOutputMarkupId(true);
        }

        @Override
        public boolean isVisible() {
            return PageState.EVENT_TYPES.equals(state);
        }
    }
}
