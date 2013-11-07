package com.n4systems.fieldid.wicket.pages.org;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.org.PlaceService;
import com.n4systems.fieldid.wicket.components.MultiSelectDropDownChoice;
import com.n4systems.fieldid.wicket.components.navigation.MattBar;
import com.n4systems.fieldid.wicket.components.renderer.EventTypeChoiceRenderer;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.model.EventType;
import com.n4systems.model.orgs.BaseOrg;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class OrgConfigurePage extends FieldIDFrontEndPage {

    private final WebMarkupContainer container;

    public enum PageState {
        RECURRING_EVENTS,EVENT_TYPES
    }

    private @SpringBean PlaceService placeService;

    private PageState state = PageState.EVENT_TYPES;
    private final IModel<BaseOrg> model;


    public OrgConfigurePage(IModel<BaseOrg> model, PageState state) {
        super();
        this.model = model;
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
        target.add(container);
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

        List<EventType> types = Lists.newArrayList();

        public ConfigureEventTypesPanel(String id) {
            super(id, "configureEventTypes", OrgConfigurePage.this);
            setOutputMarkupId(true);
            add(new Form("form")
                    .add(new MultiSelectDropDownChoice<EventType>("types", new PropertyModel<List<EventType>>(this, "types"), getEventTypes(), new EventTypeChoiceRenderer()))
                    .add(new AjaxSubmitLink("submit") {
                        @Override
                        protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                            // do saving stuff here...
                            setResponsePage(new OrgSummaryPage(model.getObject()));
                        }

                        @Override
                        protected void onError(AjaxRequestTarget target, Form<?> form) {
                            // show messages here...?
                        }
                    })
                    .add(new Link("cancel") {
                        @Override
                        public void onClick() {
                            setResponsePage(new OrgSummaryPage(model.getObject()));
                        }
                    }));
        }

        private List<EventType> getEventTypes() {
            return placeService.getEventTypesFor(model.getObject());
        }

        @Override
        public boolean isVisible() {
            return PageState.EVENT_TYPES.equals(state);
        }
    }
}
