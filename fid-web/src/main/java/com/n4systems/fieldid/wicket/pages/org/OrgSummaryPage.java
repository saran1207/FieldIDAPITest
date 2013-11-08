package com.n4systems.fieldid.wicket.pages.org;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.org.PlaceService;
import com.n4systems.fieldid.wicket.components.GoogleMap;
import com.n4systems.fieldid.wicket.components.MultiSelectDropDownChoice;
import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.fieldid.wicket.components.addressinfo.AddressPanel;
import com.n4systems.fieldid.wicket.components.renderer.EventTypeChoiceRenderer;
import com.n4systems.fieldid.wicket.model.EntityModel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.setup.org.OrgViewPage;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.Address;
import com.n4systems.model.EventType;
import com.n4systems.model.GpsLocation;
import com.n4systems.model.builders.EventTypeBuilder;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.CustomerOrg;
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

    interface FormPanel {
        Component submit(AjaxRequestTarget target);
        Component error(AjaxRequestTarget target);
    }

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
    private Component left;
    private Component right;
    private GoogleMap map;
    private Component deletePanel;
    private Component archivePanel;

    public OrgSummaryPage(PageParameters params) {
        init(params.get("id").toLong());
    }

    public <T extends BaseOrg> OrgSummaryPage(T org) {
        super(new PageParameters().add("id",org.getId()));
    }


    private void init(Long id) {
        model = new EntityModel(BaseOrg.class, id);
        add(new Label("header", ProxyModel.of(model, on(BaseOrg.class).getName())));
        add(actions = createActions("actions"));
        add(left=getViewDetailsLeftPanel());
        add(right=getSummaryPanel());
    }

    private MarkupContainer createActions(String id) {
        WebMarkupContainer actions = new WebMarkupContainer(id) {
            @Override public boolean isVisible() {
                return right instanceof SummaryPanel;
            }
        };
        actions.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true);

        MarkupContainer editMenu = new WebMarkupContainer("edit")
                .add(new AjaxLink("details") {
                    @Override public void onClick(AjaxRequestTarget target) {
                        switchRightPanel(getEditPanel(), target);
                    }
                })
                .add(new AjaxLink("archive") {
                    @Override public void onClick(AjaxRequestTarget target) {
                        switchRightPanel(getArhcivePanel(), target);
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
                })
                .add( new NonWicketLink("merge", "mergeCustomers.action?uniqueID="+model.getObject().getId()) {
                    @Override public boolean isVisible() {
                        return model.getObject() instanceof CustomerOrg;
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

    private Component getArhcivePanel() {
        if (archivePanel==null) {
            archivePanel = new ArchivePanel();
        }
        return archivePanel;
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
        right.replaceWith(component);
        right = component;
        target.add(right,actions);
        return right;
    }

    private Component switchLeftPanel(Component component, AjaxRequestTarget target) {
        left.replaceWith(component);
        left = component;
        target.add(left);
        return left;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        // for starters, i am basing this page on asset summary page.  later on need to either refactor common css or create my own file.
        response.renderCSSReference("style/newCss/asset/asset.css");
    }

    private Component createSubmitCancelButtons(String id, final FormPanel formPanel) {
        return new Fragment(id,"saveCancelButtons",OrgSummaryPage.this)
                .add(new AjaxSubmitLink("submit") {
                    @Override protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                        formPanel.submit(target);
                    }
                    @Override protected void onError(AjaxRequestTarget target, Form<?> form) {
                        formPanel.submit(target);
                    }
                })
                .add(new AjaxLink("cancel") {
                    @Override public void onClick(AjaxRequestTarget target) {
                        switchRightPanel(getSummaryPanel(), target);
                    }
                });
    }


    // --------------------- FRAGMENTS ---------------------------------

    class ViewDetailsLeftPanel extends Fragment {

        ViewDetailsLeftPanel() {
            super(LEFT_PANEL_ID,"viewDetails",OrgSummaryPage.this);
            //add(new GoogleMap("map",ProxyModel.of(model, on(BaseOrg.class).getGpsLocation())));
            add(map = new GoogleMap("map", Model.of(new GpsLocation(43.70263, -79.46654))));
            // add name, email, phone, fax, etc... here..
            add(new TextArea("comments",Model.of("comments go here")));
        }
    }


    class EditPanel extends Fragment implements FormPanel {
        IModel<BaseOrg> model;
        Address address = new Address("111 queen st east, toronto");

        EditPanel() {
            super(RIGHT_PANEL_ID, "edit", OrgSummaryPage.this);
            add(new Form("form")
//            add(new TextField("address", ProxyModel.of(model,on(BaseOrg.getLocation().getAddress()))));
                .add(createSubmitCancelButtons("buttons",this))
                .add(new TextField("type", Model.of("commercial")))
                .add(new TextField("status", Model.of("sold")))
                .add(new TextField("name", Model.of("joe smith")))
                .add(new TextField("email", Model.of("jsmith@foo.com")))
                .add(new TextField("phone", Model.of("123 456 7894")))
                .add(new TextField("fax", Model.of("964 745 3528")))
                .add(new AddressPanel("address", new PropertyModel(this,"address")).withExternalMap(map.getJsVar())));
        }

        @Override public Component submit(AjaxRequestTarget target) {
            return this;
        }

        @Override public Component error(AjaxRequestTarget target) {
            return this;
        }
    }

    class SummaryPanel extends Fragment implements FormPanel {
        public SummaryPanel() {
            super(RIGHT_PANEL_ID, "summary", OrgSummaryPage.this);
            setOutputMarkupId(true);
        }

        @Override public Component submit(AjaxRequestTarget target) {
            return this;
        }

        @Override public Component error(AjaxRequestTarget target) {
            return this;
        }
    }

    class EditEventTypesPanel extends Fragment implements FormPanel {
        private List<EventType> types = Lists.newArrayList();

        public EditEventTypesPanel() {
            super(RIGHT_PANEL_ID, "editEventTypes", OrgSummaryPage.this);
            setOutputMarkupId(true);
            add(new Form("form")
// TODO DD                    .add(new MultiSelectDropDownChoice<EventType>("types", ProxyModel.of(model, on(BaseOrg.class).getEventTypes()), getEventTypes(), new EventTypeChoiceRenderer()))
                    .add(new MultiSelectDropDownChoice<EventType>("types", new PropertyModel<List<EventType>>(this, "types"), getEventTypes(), new EventTypeChoiceRenderer()))
                    .add(createSubmitCancelButtons("buttons",this)));
        }

        private List<EventType> getEventTypes() {
            return placeService.getEventTypesFor(model.getObject());
        }
        @Override public Component submit(AjaxRequestTarget target) {
            // TODO DD : save event types
            switchRightPanel(getSummaryPanel(),target);
            return this;
        }

        @Override public Component error(AjaxRequestTarget target) {
            return this;
        }
    }

    class EditRecurringPanel extends Fragment implements FormPanel {
        public EditRecurringPanel() {
            super(RIGHT_PANEL_ID, "editRecurringEvents", OrgSummaryPage.this);
            setOutputMarkupId(true);
            add(new Form("form")
                    .add(createSubmitCancelButtons("buttons",this)));
        }
        @Override public Component submit(AjaxRequestTarget target) {
            // TODO : save recurring events stuff
            switchRightPanel(getSummaryPanel(),target);
            return this;
        }

        @Override public Component error(AjaxRequestTarget target) {
            return this;
        }
    }

    class ArchivePanel extends Fragment implements FormPanel {
        public ArchivePanel() {
            super(RIGHT_PANEL_ID,"archivePanel",OrgSummaryPage.this);
            setOutputMarkupId(true);
            add(new Form("form")
                    .add(new Label("confirm", new FIDLabelModel("message.confirm_archive_place",model.getObject().getName())))
                    .add(createSubmitCancelButtons("buttons",this)));
        }
        @Override public Component submit(AjaxRequestTarget target) {
            // TODO DD: archive it.
            setResponsePage(OrgViewPage.class);
            return this;
        }

        @Override public Component error(AjaxRequestTarget target) {
            return this;
        }

    }

}
