package com.n4systems.fieldid.wicket.pages.org;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.org.PlaceService;
import com.n4systems.fieldid.wicket.components.GoogleMap;
import com.n4systems.fieldid.wicket.components.MultiSelectDropDownChoice;
import com.n4systems.fieldid.wicket.components.renderer.EventTypeChoiceRenderer;
import com.n4systems.fieldid.wicket.model.EntityModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.EventType;
import com.n4systems.model.GpsLocation;
import com.n4systems.model.builders.EventTypeBuilder;
import com.n4systems.model.orgs.BaseOrg;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

import static ch.lambdaj.Lambda.on;

// terminology note : you might also think of this as "places" page.  we might want to refactor code to have common words at some point.
public class OrgSummaryPage extends FieldIDFrontEndPage {

    private static final String RIGHT_PANEL_ID = "right";
    private static final String LEFT_PANEL_ID = "left";

    private @SpringBean PlaceService placeService;

    private EntityModel<BaseOrg> model;
    private MarkupContainer actions;
    private Component editPanel;
    private Component editEventTypesPanel;
    private Component editRecurringPanel;
    private Component summaryPanel;
    private Component viewDetailsLeftPanel;

    public <T extends BaseOrg> OrgSummaryPage(T org) {
        this(org.getId());
    }

    public OrgSummaryPage(Long id) {
        super();
        model = new EntityModel(BaseOrg.class, id);
        add(new Label("header", ProxyModel.of(model, on(BaseOrg.class).getName())));
        add(actions = createActions("actions"));
        add(getViewDetailsLeftPanel());
        add(getSummaryPanel());
    }

    public OrgSummaryPage(PageParameters params) {
        this(params.get("id").toLong());
    }

    private MarkupContainer createActions(String id) {
        WebMarkupContainer actions = new WebMarkupContainer(id);
        actions.setOutputMarkupId(true);

        MarkupContainer editMenu = new WebMarkupContainer("edit")
                .add(new AjaxLink("details") {
                    @Override public void onClick(AjaxRequestTarget target) {
                        switchRightPanel(getEditPanel(), target);
                    }
                })
                .add(new AjaxLink("recurring") {
                    @Override public void onClick(AjaxRequestTarget target) {
                        switchRightPanel(getEditRecurringPanel(), target);
                    }
                })
                .add(new AjaxLink("eventTypes") {
                    @Override public void onClick(AjaxRequestTarget target) {
                        switchRightPanel(getEditEventTypesPanel(), target);
                    }
                });

        MarkupContainer scheduleMenu = new WebMarkupContainer("schedule")
                .add(new AjaxLink("oneTime") {
                    @Override public void onClick(AjaxRequestTarget target) {
                        //show schedule picker dialog.
                    }
                })
                .add(new AjaxLink("recurring") {
                    @Override public void onClick(AjaxRequestTarget target) {
                        // show create recurring action dialog.
                    }
                });

        List<EventType> eventTypes = Lists.newArrayList(EventTypeBuilder.anEventType().named("Visual Inspection").build());
        MarkupContainer startMenu = new WebMarkupContainer("start")
                .add(new ListView<EventType>("types", eventTypes) {
                    @Override
                    protected void populateItem(ListItem<EventType> item) {
                        EventType eventType = item.getModelObject();
                        item.add(new AjaxLink("type") {
                            @Override
                            public void onClick(AjaxRequestTarget target) {
                                // start event of this type....setResponsePage(new PerformEvent(this.place,eventType);
                            }
                        }.add(new Label("name", Model.of(eventType.getName()))));
                    }
                });

        return actions.add(startMenu.setRenderBodyOnly(true),editMenu.setRenderBodyOnly(true),scheduleMenu.setRenderBodyOnly(true));
    }

    private Component getSummaryPanel() {
        if (summaryPanel==null) {
            summaryPanel = new SummaryPanel();
        }
        return summaryPanel;
    }

    private Component getViewDetailsLeftPanel() {
        if (viewDetailsLeftPanel==null) {
            viewDetailsLeftPanel = new ViewDetailsLeftPanel();
        }
        return viewDetailsLeftPanel;
    }

    private Component getEditPanel() {
        if (editPanel==null) {
            editPanel = new EditPanel();
        }
        return editPanel;
    }

    private Component getEditEventTypesPanel() {
        if (editEventTypesPanel==null) {
            editEventTypesPanel = new EditEventTypesPanel();
        }
        return editEventTypesPanel;
    }

    private Component getEditRecurringPanel() {
        if (editRecurringPanel==null) {
            editRecurringPanel = new EditRecurringPanel();
        }
        return editRecurringPanel;
    }

    private Component switchRightPanel(Component component, AjaxRequestTarget target) {
        get(RIGHT_PANEL_ID).replaceWith(component);
        Component newRight = get(RIGHT_PANEL_ID);
        target.add(newRight);
        return newRight;
    }

    private Component switchLeftPanel(Component component, AjaxRequestTarget target) {
        get(LEFT_PANEL_ID).replaceWith(component);
        Component newLeft = get(LEFT_PANEL_ID);
        target.add(newLeft);
        return newLeft;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        // for starters, i am basing this page on asset summary page.  later on need to either refactor common css or create my own file.
        response.renderCSSReference("style/newCss/asset/asset.css");
    }


    class ViewDetailsLeftPanel extends Fragment {

        ViewDetailsLeftPanel() {
            super(LEFT_PANEL_ID,"viewDetails",OrgSummaryPage.this);
            //add(new GoogleMap("map",ProxyModel.of(model, on(BaseOrg.class).getGpsLocation())));
            add(new GoogleMap("map", Model.of(new GpsLocation(43.70263, -79.46654))));
            // add name, email, phone, fax, etc... here..
            add(new TextArea("comments",Model.of("comments go here")));
        }
    }


    class EditPanel extends Fragment {
        IModel<BaseOrg> model;

        EditPanel() {
            super(RIGHT_PANEL_ID, "edit", OrgSummaryPage.this);
//            add(new TextField("address", ProxyModel.of(model,on(BaseOrg.getLocation().getAddress()))));
            add(new TextField("address", Model.of("111 queen street east")));
            add(new TextField("type", Model.of("commercial")));
            add(new TextField("status", Model.of("sold")));
            add(new TextField("name", Model.of("joe smith")));
            add(new TextField("email", Model.of("jsmith@foo.com")));
            add(new TextField("phone", Model.of("123 456 7894")));
            add(new TextField("fax", Model.of("964 745 3528")));
            add(new AjaxLink("recurring") {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    // goto recurring events panel.
                    // switchRightPanel(new EditRecurringPanel(),target);
                }
            }.add(new Label("details", Model.of("4 recurring events scheduled"))));
            add(new AjaxLink("eventTypes") {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    // goto recurring events panel.
                    // switchRightPanel(new EditEventTypePanel(),target);
                }
            }.add(new Label("details", Model.of("13 event types can be performed on this place."))));
        }
    }

    class SummaryPanel extends Fragment {
        public SummaryPanel() {
            super(RIGHT_PANEL_ID, "summary", OrgSummaryPage.this);
            setOutputMarkupId(true);
        }
    }

    class EditEventTypesPanel extends Fragment {
        private List<EventType> types = Lists.newArrayList();

        public EditEventTypesPanel() {
            super(RIGHT_PANEL_ID, "editEventTypes", OrgSummaryPage.this);
            setOutputMarkupId(true);
            add(new Form("form")
//                    .add(new MultiSelectDropDownChoice<EventType>("types", new PropertyModel<List<EventType>>(this, "types"), getEventTypes(), new EventTypeChoiceRenderer()))
                    .add(new MultiSelectDropDownChoice<EventType>("types", new PropertyModel<List<EventType>>(this, "types"), getEventTypes(), new EventTypeChoiceRenderer()))
                    .add(new AjaxSubmitLink("submit") {
                        @Override protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                            switchRightPanel(getSummaryPanel(),target);
                        }
                        @Override protected void onError(AjaxRequestTarget target, Form<?> form) {
                            switchRightPanel(getSummaryPanel(),target);
                        }
                    })
                    .add(new AjaxLink("cancel") {
                        @Override public void onClick(AjaxRequestTarget target) {
                            switchRightPanel(getSummaryPanel(), target);
                        }
                    }));
        }

        private List<EventType> getEventTypes() {
            return placeService.getEventTypesFor(model.getObject());
        }
    }

    class EditRecurringPanel extends Fragment {
        public EditRecurringPanel() {
            super(RIGHT_PANEL_ID, "editRecurringEvents", OrgSummaryPage.this);
            setOutputMarkupId(true);
            add(new Form("form") {

            });
        }
    }

}
