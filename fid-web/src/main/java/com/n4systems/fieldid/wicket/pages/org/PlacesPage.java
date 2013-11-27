package com.n4systems.fieldid.wicket.pages.org;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.n4systems.fieldid.actions.users.UploadedImage;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.attachment.AttachmentService;
import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.fieldid.service.org.PlaceService;
import com.n4systems.fieldid.service.user.UserGroupService;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.service.uuid.UUIDService;
import com.n4systems.fieldid.wicket.components.ExternalImage;
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
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.setup.org.OrgViewPage;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.*;
import com.n4systems.model.Event;
import com.n4systems.model.attachment.PlaceAttachment;
import com.n4systems.model.attachment.S3Attachment;
import com.n4systems.model.builders.CustomerOrgBuilder;
import com.n4systems.model.builders.DivisionOrgBuilder;
import com.n4systems.model.builders.EventTypeBuilder;
import com.n4systems.model.orgs.*;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserGroup;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AbstractAjaxTimerBehavior;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.AbstractItem;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;

import java.awt.*;
import java.util.List;

import static ch.lambdaj.Lambda.on;


public class PlacesPage extends FieldIDFrontEndPage {

    enum Content { DETAILS, EVENTS, PEOPLE, ATTACHMENTS }

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
    private @SpringBean AttachmentService attachmentService;
    private @SpringBean S3Service s3Service;
    private @SpringBean UUIDService uuidService;

    private IModel<? extends BaseOrg> model;
    private MarkupContainer actions;
    private GoogleMap map;
    private RepeatingView tabs;
    private Component content,detailsPanel,eventsPanel,peoplePanel;
    private Component newUserPanel,archivePanel,editEventTypesPanel,editRecurringPanel,editDetailsPanel,attachmentsPanel;
    private ModalWindow modal;

    // ................TEST DATA...................
    private Address address = new Address("111 Queen St east, toronto");
    private String name="sue richardson",
            email="foo@bar.com",
            notes,
            phone="123 456 7890", fax;
    // ...........................................




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
            case ATTACHMENTS:
                return getAttachmentsPanel();
            default:
                throw new IllegalArgumentException("can't find content panel for " + contentState);
        }
    }

    private Component createHeader(String id) {
        List<BaseOrg> hierarchy = Lists.newArrayList(getOrg());
        BaseOrg parent = getOrg().getParent();
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
                item.add(createChildrenMenu("children",item));
            }
        });
        return container;
    }

    private Component createChildrenMenu(String id, ListItem<BaseOrg> item) {
        final BaseOrg parentOrg = item.getModelObject();
        final MarkupContainer container = new WebMarkupContainer("separator") {
            @Override public boolean isVisible() {
                // TODO : need to add permission checking for this.
                return !(parentOrg instanceof DivisionOrg);
            }
        };

        final RepeatingView repeat = new RepeatingView("children");
        repeat.setOutputMarkupId(true);
        final AbstractAjaxTimerBehavior timer = new AbstractAjaxTimerBehavior(Duration.milliseconds(300)) {
            @Override protected void onTimer(AjaxRequestTarget target) {
                for (final BaseOrg org:getOrgChildren(parentOrg)) {
                    AbstractItem item = new AbstractItem(repeat.newChildId());
                    item.add(new BookmarkablePageLink<Void>("link", PlacesPage.class, PageParametersBuilder.id(org.getId()))
                                    .add(new Label("name", Model.of(org.getDisplayName()))));
                    repeat.add(item);
                }
                stop();  // only do it once.
                target.add(container);
            }

        };
        container.setOutputMarkupId(true);
        container.add(timer);
        container.add(repeat);
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
                .add( new NonWicketLink("merge", "mergeCustomers.action?uniqueID="+ getOrg().getId()) {
                    @Override public boolean isVisible() {
                        return getOrg() instanceof CustomerOrg;
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

        List<ThingEventType> eventTypes = Lists.newArrayList(EventTypeBuilder.anEventType().named("Visual Inspection").build());
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

    private boolean updateContent(Content contentState, AjaxRequestTarget target) {
        if (updateContent(contentState)) {
            target.add(content);
            return true;
        }
        return false;
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

    private Component getAttachmentsPanel() {
        if (this.attachmentsPanel==null) {
            attachmentsPanel = new AttachmentsPanel();
        }
        return attachmentsPanel;
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
            updateContent(contentState, target);
        }
    }



    // --------------------- FRAGMENTS ---------------------------------


    class DetailsPanel extends Fragment {
        public DetailsPanel() {
            super(CONTENT_ID, "details", PlacesPage.this);

            //add(new GoogleMap("map",ProxyModel.of(model, on(BaseOrg.class).getGpsLocation())));
            add(map = new GoogleMap("map", Model.of(new GpsLocation(43.70263, -79.46654))));

            add(new AjaxLink("attachmentsLink") {
                @Override public void onClick(AjaxRequestTarget target) {
                    updateContent(Content.ATTACHMENTS, target);  // make this more of settings thang...?
                }
            }.add(new Label("label", "8 attachments")));

            add(new ContextImage("img", "images/add-photo-slate.png"));

            add(new InlineEditableForm("contact").withSaveCancelEditLinks()
                    .add(new TextField("name", new PropertyModel(PlacesPage.this, "name")))
                    .add(new TextField("email", new PropertyModel(PlacesPage.this, "email")))
                    .add(new AddressPanel("address", new PropertyModel(PlacesPage.this, "address")).withExternalMap(map.getJsVar()))
                    .add(new TextField("phone", new PropertyModel(PlacesPage.this, "phone")))
                    .add(new TextField("fax", new PropertyModel(PlacesPage.this, "fax"))));

            add(new InlineEditableForm("general").withSaveCancelEditLinks()
                    .add(new TextArea<String>("notes", new PropertyModel(PlacesPage.this, "notes")))
                    );
        }
    }


    class InlineEditableForm extends Form {

        private boolean editing;
        private String notEditingCss;
        private String editingCss;

        public InlineEditableForm(String id) {
            super(id);
            add(new AttributeAppender("class", new Model<String>() {
                @Override public String getObject() {
                    return editing ? "editable-inputs" : "";
                }
            }));
        }

        public InlineEditableForm toggleEdit() {
            if (editing) {
                stopEditing();
            } else {
                startEditing();
            }
            return this;
        }

        public InlineEditableForm startEditing() {
            this.editing  = true;
            visitChildren(FormComponent.class, new IVisitor<Component, Object>() {
                @Override public void component(Component component, IVisit<Object> visit) {
                    component.setVisible(true);
                }
            });
            return this;
        }

        public InlineEditableForm stopEditing() {
            this.editing = false;
            visitChildren(FormComponent.class, new IVisitor<Component, Object>() {
                @Override public void component(Component component, IVisit<Object> visit) {
                    boolean isVisible = hideEmptyFieldsWhenViewing() && !StringUtils.isBlank(component.getDefaultModelObjectAsString());
                    component.setVisible(isVisible);
                }
            });
            return this;
        }

        private final IModel<String> getCssClassForFields(final Component component) {
            return new Model<String>() {
                @Override public String getObject() {
                    return editing ? getEditingCss() : getNotEditingCss();
                }
            };
        }

        public InlineEditableForm withSaveCancelEditLinks() {
            addSaveLink("save");
            addCancelLink("cancel");
            addEditLink("edit");
            return this;
        }

        @Override
        public MarkupContainer add(Component... childs) {
            MarkupContainer container = super.add(childs);
            for (Component component:childs) {
                if (component instanceof FormComponent) {
                    component.setOutputMarkupPlaceholderTag(true);
                    component.add(new AttributeAppender("class",getCssClassForFields(component)));
                }
            }
            return container;
        }

        protected boolean hideEmptyFieldsWhenViewing() {
            return true;
        }

        protected String getNotEditingCss() {
            return "";
        }

        protected String getEditingCss() {
            return "editing";
        }

        protected String getHiddenCss() {
            return "hidden";
        }

        protected void save(AjaxRequestTarget target) {  }

        public void addSaveLink(String id) {
            add(new AjaxSubmitLink(id) {
                @Override protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    editing = false;
                    save(target);
                    target.add(InlineEditableForm.this);
                }
                @Override protected void onError(AjaxRequestTarget target, Form<?> form) {
                    // TODO : how to handle errors? are they practically possible?
                }
                @Override public boolean isVisible() {
                    return editing;
                }
            }.setOutputMarkupPlaceholderTag(true));
        }

        public void addEditLink(String id) {
            add(new AjaxLink(id) {
                @Override public void onClick(AjaxRequestTarget target) {
                    startEditing();
                    target.add(InlineEditableForm.this);
                }

                @Override public boolean isVisible() {
                    return !editing;
                }
            }.setOutputMarkupPlaceholderTag(true));
        }


        public void addCancelLink(String id) {
            add(new AjaxLink(id) {
                @Override public void onClick(AjaxRequestTarget target) {
                    stopEditing();
                    target.add(InlineEditableForm.this);   // TODO : add some javascript to refresh map.
                }

                @Override public boolean isVisible() {
                    return editing;
                }
            }.setOutputMarkupPlaceholderTag(true));
        }

    }


    class PeoplePanel extends Fragment {
        public PeoplePanel() {
            super(CONTENT_ID, "peoplePanel", PlacesPage.this);
            add(new ListView<User>("people", getUsersModel()) {
                @Override protected void populateItem(ListItem<User> item) {
                    User user = item.getModelObject();
                    item.add(new Label("id",user.getUserID()));
                    item.add(new Label("first",user.getFirstName()));
                    item.add(new Label("last",user.getLastName()));
                }
            });
            add(new AjaxLink("add") {
                @Override public void onClick(AjaxRequestTarget target) {
                    showEditDialog(getNewUserPanel(),target);
                }
            });
        }

        private IModel<? extends List<? extends User>> getUsersModel() {
            return new LoadableDetachableModel<List<? extends User>>() {
                @Override protected List<? extends User> load() {
                    return placeService.getUsersFor(getOrg());
                }
            };
        }
    }

    private BaseOrg getOrg() {
        return model.getObject();
    }


    class AttachmentsPanel extends Fragment {
        private FileUploadField uploadField;

        public AttachmentsPanel() {
            super(CONTENT_ID,"attachmentsPanel", PlacesPage.this);
            add(new ListView<S3Attachment>("attachments",getAttachments()) {
                @Override protected void populateItem(ListItem<S3Attachment> item) {
                    S3Attachment attachment = item.getModelObject();
                    WebMarkupContainer cell = new WebMarkupContainer("cell");
                    //String url = s3Service.getPlaceAttachment(getOrg(), attachment);
                    cell.add(new Label("comments", attachment.getComments()));
                    cell.add(new ExternalImage("image", s3Service.generateResourceUrl("/tenants/15511493/places/15511494/attachments/temp/").toString()));
                    item.add(cell);
                }
            });
            add(getAddAttachmentsForm());
        }

        private Form<Attachment> getAddAttachmentsForm() {
            Form form = new Form("form");
            form.setOutputMarkupId(true);
            form.add(uploadField = new FileUploadField("upload"));
            uploadField.add(new AjaxFormSubmitBehavior("onchange") {
                @Override protected void onSubmit(AjaxRequestTarget target) {
                    FileUpload fileUpload = uploadField.getFileUpload();

                    if (fileUpload != null) {
                        PlaceAttachment attachment = createNewPlaceAttachment(fileUpload);
                        s3Service.uploadTempAttachment(attachment);
                        // TODO DD : add them to the org.  org.addAttachment(attachment);
                        attachmentService.save(attachment);
                    }
                }

                @Override protected void onError(AjaxRequestTarget target) { }
            });
            return form;
        }

        private PlaceAttachment createNewPlaceAttachment(FileUpload fileUpload) {
            PlaceAttachment attachment = new PlaceAttachment(getOrg());
            attachment.withContent(fileUpload.getClientFileName(), fileUpload.getContentType(), fileUpload.getBytes())
                      .withTempFileName(uuidService.createUuid());
            return attachment;
        }

        private IModel<? extends List<? extends S3Attachment>> getAttachments() {
           // TODO : remove.  this is just bogus test data.  should be stored via join table associated to org.
            final List<S3Attachment> result = Lists.newArrayList();
            for (int i=0; i<10 ; i++) {
                final int finalI = i;
                result.add(new PlaceAttachment(823473f));
            }
            return new LoadableDetachableModel<List<S3Attachment>>() {
                @Override protected List<S3Attachment> load() {
                    return result;
                }
            };
        }

    }


    class EventsPanel extends Fragment {
        public EventsPanel() {
            super(CONTENT_ID, "eventsPanel", PlacesPage.this);
            add(new ListView<Event>("events", getEventsModel()) {
                @Override protected void populateItem(ListItem<Event> item) {
                    Event event = item.getModelObject();
                    item.add(new Label("type",event.getEventType().getDisplayName()));
                    item.add(new Label("status",event.getWorkflowState().getLabel()));
                    item.add(new Label("scheduled",event.getDueDate().toString()));
                    item.add(new Label("completed",event.getCompletedDate().toString()));
                }
            });
        }

        private IModel<? extends List<? extends Event>> getEventsModel() {
            return new LoadableDetachableModel<List<? extends Event>>() {
                @Override protected List<? extends Event> load() {
                    return placeService.getEventsFor(getOrg());
                }
            };
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
            return placeService.getEventTypesFor(getOrg());
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
                    .add(new Label("confirm", new FIDLabelModel("message.confirm_archive_place", getOrg().getName())))
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
