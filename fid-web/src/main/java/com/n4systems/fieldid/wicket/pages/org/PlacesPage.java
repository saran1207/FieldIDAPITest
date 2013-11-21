package com.n4systems.fieldid.wicket.pages.org;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.n4systems.fieldid.actions.users.UploadedImage;
import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.fieldid.service.org.PlaceService;
import com.n4systems.fieldid.service.user.UserGroupService;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.components.GoogleMap;
import com.n4systems.fieldid.wicket.components.MultiSelectDropDownChoice;
import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.fieldid.wicket.components.addressinfo.AddressPanel;
import com.n4systems.fieldid.wicket.components.modal.FIDModalWindow;
import com.n4systems.fieldid.wicket.components.renderer.EventTypeChoiceRenderer;
import com.n4systems.fieldid.wicket.components.text.LabelledRequiredTextField;
import com.n4systems.fieldid.wicket.components.text.LabelledTextField;
import com.n4systems.fieldid.wicket.components.user.UserFormIdentifiersPanel;
import com.n4systems.fieldid.wicket.model.EntityModel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.setup.org.OrgViewPage;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.Address;
import com.n4systems.model.EventType;
import com.n4systems.model.GpsLocation;
import com.n4systems.model.builders.CustomerOrgBuilder;
import com.n4systems.model.builders.DivisionOrgBuilder;
import com.n4systems.model.builders.EventTypeBuilder;
import com.n4systems.model.orgs.*;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserGroup;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.awt.*;
import java.util.List;

import static ch.lambdaj.Lambda.on;


public class PlacesPage extends FieldIDFrontEndPage {


    enum Content { DETAILS, EVENTS, PEOPLE }

    interface ModalPanel {
        Component submit(AjaxRequestTarget target);
        Component error(AjaxRequestTarget target);
        Dimension init();
    }

    private static final String CONTENT_ID = "content";
    private static final String TABS_ID = "tabs";
    private static final String ACTIONS_ID = "actions";
    private static final String HEADER_ID = "header";

    private @SpringBean PlaceService placeService;
    private @SpringBean UserGroupService userGroupService;
    private @SpringBean UserService userService;
    private @SpringBean OrgService orgService;

    private IModel<? extends BaseOrg> model;
    private MarkupContainer actions;
    private GoogleMap map;
    private RepeatingView tabs;
    private Component content,detailsPanel,eventsPanel,peoplePanel;
    private Component newUserPanel,archivePanel,editEventTypesPanel,editRecurringPanel,editDetailsPanel;
    private ModalWindow modal;

    public PlacesPage(PageParameters params) {
        this(new EntityModel(BaseOrg.class, params.get("id").toLong()));
    }

    public PlacesPage(IModel<? extends BaseOrg> model) {
        init(model);
    }

    private void init(IModel<? extends BaseOrg>  model) {
        this.model = model;
        add(createHeader(HEADER_ID));
        add(actions = createActions(ACTIONS_ID));
        add(content = new WebMarkupContainer(CONTENT_ID));
        add(tabs = createTabs(TABS_ID));
        add(modal = new FIDModalWindow("modal") {
            @Override
            public boolean isResizable() {
                return true;
            }
        });
        updateContent(getInitialContentState());
    }

    private Content getInitialContentState() {
        return Content.DETAILS;
    }

    private RepeatingView createTabs(String id) {
        RepeatingView repeat = new RepeatingView(id);
        repeat.add(new Label(repeat.newChildId(),new FIDLabelModel("label.details")).add(new TabBehavior(Content.DETAILS)));
        repeat.add(new Label(repeat.newChildId(),new FIDLabelModel("label.events")).add(new TabBehavior(Content.EVENTS)));
        repeat.add(new Label(repeat.newChildId(),new FIDLabelModel("label.people")).add(new TabBehavior(Content.PEOPLE)));
        return repeat;
    }

    private Component getContentForState(Content contentState) {
        switch (contentState) {
            case DETAILS:
                return getDetailsPanel();
            case EVENTS:
                return getEventsPanel();
            case PEOPLE:
                return getPeoplePanel();
            default:
                throw new IllegalArgumentException("can't find content panel for " + contentState);
        }
    }

    private Component createHeader(String id) {
        List<BaseOrg> hierarchy = Lists.newArrayList(model.getObject());
        BaseOrg parent = model.getObject().getParent();
        while (parent!=null) {
            hierarchy.add(0,parent);
            parent = parent.getParent();
        }

        WebMarkupContainer container = new WebMarkupContainer(id);
        container.add(new ListView<BaseOrg>("hierarchy", hierarchy) {
            @Override protected void populateItem(final ListItem<BaseOrg> item) {
                final BaseOrg org = item.getModelObject();
                item.add(new Link("org") {
                    @Override public void onClick() {
                        setResponsePage(PlacesPage.class,new PageParameters().add("id",org.getId()));
                    }
                }.add(new Label("name", ProxyModel.of(item.getModel(), on(BaseOrg.class).getName()))));
                item.add(createChildrenMenu("children",org));
            }
        });
        return container;
    }

    private Component createChildrenMenu(String id, final BaseOrg parentOrg) {
        List<? extends BaseOrg> children = getOrgChildren(parentOrg);
        children.add(null);

        MarkupContainer container = new WebMarkupContainer("separator") {
            @Override public boolean isVisible() {
                // TODO : need to add permission checking for this.
                return !(parentOrg instanceof DivisionOrg);
            }
        };

        container.add(new ListView<BaseOrg>(id, children) {
            @Override protected void populateItem(ListItem<BaseOrg> item) {
                final BaseOrg org = item.getModelObject();
                if (org == null) {
                    // TODO : make this dynamic = create new division/customer/secondary??? depending on org.
                    String key = getChildLabelKey(parentOrg);
                    FIDLabelModel createNewLabel = new FIDLabelModel("label.create_new", new FIDLabelModel(key).getObject());
                    item.add(new Label("name", createNewLabel).add(new AjaxEventBehavior("onclick") {
                        @Override protected void onEvent(AjaxRequestTarget target) {
                            setResponsePage(new PlacesPage(createNewOrg(parentOrg)));
                        }
                    }));
                } else {
                    item.add(new Label("name", Model.of(org.getDisplayName())).add(new AjaxEventBehavior("onclick") {
                        @Override protected void onEvent(AjaxRequestTarget target) {
                            setResponsePage(PlacesPage.class, new PageParameters().add("id", org.getId()));
                        }
                    }));
                }
            }

        });
        return container;
    }

    private String getChildLabelKey(BaseOrg parentOrg) {
        if (parentOrg instanceof InternalOrg) {
            return "label.customer_type";
        } else if (parentOrg instanceof CustomerOrg) {
            return "label.division_type";
        } else {
            throw new IllegalStateException("can't create child org for " + parentOrg.getClass().getSimpleName());
        }
    }

    private IModel<? extends BaseOrg> createNewOrg(BaseOrg parentOrg) {
        // TODO : set proper defaults for new children orgs.
        if (parentOrg instanceof PrimaryOrg) {
            return Model.of(CustomerOrgBuilder.aCustomerOrg().withParent((InternalOrg)parentOrg).withName("New Customer").build());
        } else if (parentOrg instanceof CustomerOrg) {
            return Model.of(DivisionOrgBuilder.aDivisionOrg().withCustomerOrg((CustomerOrg)parentOrg).withName("New Division").build());
        }
        return null;
    }

    private List<? extends BaseOrg> getOrgChildren(BaseOrg org) {
        // TODO DD : make this ajax so only updated when user clicks on it...not during initial render.
        // i.e. replace("children", new ChildrenMenu(org))???   OR set item's model to org.
        if (org instanceof PrimaryOrg || org instanceof SecondaryOrg) {
            return orgService.getCustomersUnder(org);
        } else if (org instanceof CustomerOrg) {
            return orgService.getDivisionsUnder(org);
        } else {
            return Lists.newArrayList();
        }
    }

    private MarkupContainer createActions(String id) {
        WebMarkupContainer actions = new WebMarkupContainer(id);
        actions.setOutputMarkupPlaceholderTag(true);

        MarkupContainer editMenu = new WebMarkupContainer("edit")
                .add(new AjaxLink("details") {
                    @Override public void onClick(AjaxRequestTarget target) {
                        showEditDialog(getEditDetailsPanel(), target);
                    }
                })
                .add(new AjaxLink("archive") {
                    @Override public void onClick(AjaxRequestTarget target) {
                        showEditDialog(getArhcivePanel(), target);
                    }
                })
                .add(new AjaxLink("recurring") {
                    @Override public void onClick(AjaxRequestTarget target) {
                        showEditDialog(getEditRecurringPanel(), target);
                    }
                })
                .add(new AjaxLink("eventTypes") {
                    @Override public void onClick(AjaxRequestTarget target) {
                        showEditDialog(getEditEventTypesPanel(), target);
                    }
                })
                .add(new AjaxLink("addUser") {
                    @Override public void onClick(AjaxRequestTarget target) {
                        showEditDialog(getNewUserPanel(), target);
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
                    @Override protected void populateItem(ListItem<EventType> item) {
                        EventType eventType = item.getModelObject();
                        item.add(new AjaxLink("type") {
                            @Override public void onClick(AjaxRequestTarget target) {
                                // start event of this type....setResponsePage(new PerformEvent(this.place,eventType);
                            }
                        }.add(new Label("name", Model.of(eventType.getName()))));
                    }
                });
        return actions.add(startMenu.setRenderBodyOnly(true),editMenu.setRenderBodyOnly(true),scheduleMenu.setRenderBodyOnly(true));
    }

    private boolean updateContent(Content contentState) {
        Component newContent = getContentForState(contentState).setOutputMarkupPlaceholderTag(true);
        if (!content.equals(newContent)) {
            content.replaceWith(newContent);
            content = newContent;
            return true;
        }
        return false;
    }

    private void showEditDialog(Component panel, AjaxRequestTarget target) {
        Preconditions.checkArgument(panel instanceof ModalPanel);
        Dimension dim = ((ModalPanel)panel).init();
        modal.setInitialHeight(dim.height);
        modal.setInitialWidth(dim.width);
        modal.setContent(panel);
        modal.show(target);
    }

    private Component getDetailsPanel() {
        if (detailsPanel==null) {
            detailsPanel = new DetailsPanel();
        }
        return detailsPanel;
    }

    private Component getEventsPanel() {
        if (eventsPanel==null) {
            eventsPanel = new EventsPanel();
        }
        return eventsPanel;
    }

    private Component getPeoplePanel() {
        if (peoplePanel==null) {
            peoplePanel = new PeoplePanel();
        }
        return peoplePanel;
    }

    private Component getEditDetailsPanel() {
        if (editDetailsPanel==null) {
            editDetailsPanel = new EditPanel();
        }
        return editDetailsPanel;
    }

    private Component getNewUserPanel() {
        if (newUserPanel==null) {
            newUserPanel = new NewUserPanel();
        }
        return newUserPanel;
    }

    private Component getArhcivePanel() {
        if (archivePanel==null) {
            archivePanel = new ArchivePanel();
        }
        return archivePanel;
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

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        // for starters, i am basing this page on asset summary page.  later on need to either refactor common css or create my own file.
        response.renderCSSReference("style/newCss/asset/asset.css");
    }

    private Component createSubmitCancelButtons(String id, final ModalPanel formPanel) {
        return new Fragment(id,"saveCancelButtons",PlacesPage.this)
                .add(new AjaxSubmitLink("submit") {
                    @Override protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                        formPanel.submit(target);
                        modal.close(target);
                    }
                    @Override protected void onError(AjaxRequestTarget target, Form<?> form) {
                        formPanel.error(target);
                        modal.close(target);
                    }
                })
                .add(new AjaxLink("cancel") {
                    @Override public void onClick(AjaxRequestTarget target) {
                        modal.close(target);
                    }
                });
    }


    class TabBehavior extends AjaxEventBehavior {

        private final Content contentState;

        public TabBehavior(Content contentState) {
            super("onclick");
            this.contentState = contentState;
        }

        @Override protected void onEvent(AjaxRequestTarget target) {
            if (updateContent(contentState)) {
                target.add(content);  // TODO : add tabs as well, update the selected css class.
            }
        }
    }



    // --------------------- FRAGMENTS ---------------------------------


    class DetailsPanel extends Fragment {
        public DetailsPanel() {
            super(CONTENT_ID, "details", PlacesPage.this);
            //add(new GoogleMap("map",ProxyModel.of(model, on(BaseOrg.class).getGpsLocation())));
            add(map = new GoogleMap("map", Model.of(new GpsLocation(43.70263, -79.46654))));
            // add name, email, phone, fax, etc... here...
            add(new ContextImage("img", "images/add-photo-slate.png"));
        }
    }

    class PeoplePanel extends Fragment {
        public PeoplePanel() {
            super(CONTENT_ID, "people", PlacesPage.this);
        }
    }

    class EventsPanel extends Fragment {
        public EventsPanel() {
            super(CONTENT_ID, "events", PlacesPage.this);
        }
    }

    class EditPanel extends Fragment implements ModalPanel {
        Address address = new Address("111 queen st east, toronto");

        EditPanel() {
            super(CONTENT_ID, "edit", PlacesPage.this);
            add(new Form("form")
//            add(new TextField("address", ProxyModel.of(model,on(BaseOrg.getLocation().getAddress()))));
                    .add(createSubmitCancelButtons("buttons", this))
                    .add(new TextField("type", Model.of("commercial")))
                    .add(new TextField("status", Model.of("sold")))
                    .add(new TextField("name", Model.of("joe smith")))
                    .add(new TextField("email", Model.of("jsmith@foo.com")))
                    .add(new TextField("phone", Model.of("123 456 7894")))
                    .add(new TextField("fax", Model.of("964 745 3528")))
                    .add(new AddressPanel("address", new PropertyModel(this, "address")).withExternalMap(map.getJsVar())));
        }

        @Override public Component submit(AjaxRequestTarget target) {
            return this;
        }

        @Override public Component error(AjaxRequestTarget target) {
            return this;
        }

        @Override public Dimension init() {
            return new Dimension(500,500);
        }
    }

    class EditEventTypesPanel extends Fragment implements ModalPanel {
        private List<EventType> types = Lists.newArrayList();

        public EditEventTypesPanel() {
            super(CONTENT_ID, "editEventTypes", PlacesPage.this);
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
            //switchRightPanel(getSummaryPanel(),target);
            return this;
        }

        @Override public Component error(AjaxRequestTarget target) {
            return this;
        }

        @Override public Dimension init() {
            return new Dimension(500,300);
        }
    }

    class EditRecurringPanel extends Fragment implements ModalPanel {
        public EditRecurringPanel() {
            super(CONTENT_ID, "editRecurringEvents", PlacesPage.this);
            add(new Form("form")
                    .add(new OrgRecurringEventPanel("recurring",model))
                    .add(createSubmitCancelButtons("buttons", this)));
        }
        @Override public Component submit(AjaxRequestTarget target) {
            // TODO : save recurring events stuff
            return this;
        }

        @Override public Component error(AjaxRequestTarget target) {
            return this;
        }

        @Override public Dimension init() {
            return new Dimension(500,400);
        }
    }

    class ArchivePanel extends Fragment implements ModalPanel {
        public ArchivePanel() {
            super(CONTENT_ID, "archivePanel", PlacesPage.this);
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

        @Override public Dimension init() {
            return new Dimension(400,250);
        }
    }

    class NewUserPanel extends Fragment implements ModalPanel {
        private User user;
        private List<UserGroup> groups = Lists.newArrayList();
        private String confirmPassword, password, rfidNumber;
        private boolean assignPassword = false;
        private Component passwordText;
        private Component confirmPasswordText;

        public NewUserPanel() {
            super(CONTENT_ID,"newUserPanel",PlacesPage.this);
            user = createNewUser(new User());
            add(new Form("form")
                    .add(new CheckBox("assignPassword", new PropertyModel(NewUserPanel.this,"assignPassword")).add(new AjaxFormComponentUpdatingBehavior("onchange") {
                        @Override protected void onUpdate(AjaxRequestTarget target) {
                            target.add(passwordText, confirmPasswordText);
                        }
                    }))
                    .add(passwordText = new LabelledRequiredTextField<String>("password", "label.password", new PropertyModel(NewUserPanel.this, "password")) {
                        @Override public boolean isVisible() {
                            return assignPassword;
                        }
                    }.setOutputMarkupPlaceholderTag(true))
                    .add(confirmPasswordText = new LabelledRequiredTextField<String>("confirmPassword", "label.confirmpassword", new PropertyModel(NewUserPanel.this, "confirmPassword")) {
                        @Override public boolean isVisible() { return assignPassword; }
                    }.setOutputMarkupPlaceholderTag(true))
                    .add(new LabelledTextField<String>("rfidNumber", "label.rfidnumber", new PropertyModel(NewUserPanel.this, "rfidNumber")))
                    .add(new UserFormIdentifiersPanel("user", new PropertyModel(this, "user"), new UploadedImage()) {
                        @Override protected Component createOrgPicker(String id, IModel<BaseOrg> org) {
                            return new WebMarkupContainer(id, org).setVisible(false);
                        }
                    })
                    .add(createSubmitCancelButtons("buttons", this)));
            // TODO DD : need to add password and rfid stuff so it can handle new users.
        }

        private User createNewUser(User user) {
            User newUser = new User();
            newUser.setTenant(getCurrentUser().getTenant());
            // pre-populate some fields for convenience.
            newUser.setOwner(user.getOwner());
            newUser.setEmailAddress(user.getEmailAddress());
            newUser.setGroups(user.getGroups());
            return newUser;
        }

        @Override public Component submit(AjaxRequestTarget target) {
            user.setGroups(Sets.newHashSet(groups));
            user.assignPassword(password);
            user.assignSecruityCardNumber(rfidNumber);
            userService.create(user);
            user = createNewUser(user);  // reset it so you are editing a new one each time.
            return this;
        }

        @Override public Component error(AjaxRequestTarget target) {
            return this;
        }

        @Override
        public Dimension init() {
            user = createNewUser(user);
            return new Dimension(500,800);
        }

        User getUser() {
            return user;
        }
    }

}
