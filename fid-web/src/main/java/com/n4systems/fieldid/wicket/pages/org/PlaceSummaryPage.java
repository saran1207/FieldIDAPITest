package com.n4systems.fieldid.wicket.pages.org;

import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.org.PlaceService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.ExternalImage;
import com.n4systems.fieldid.wicket.components.GoogleMap;
import com.n4systems.fieldid.wicket.components.addressinfo.AddressPanel;
import com.n4systems.fieldid.wicket.components.form.InlineEditableForm;
import com.n4systems.fieldid.wicket.components.form.LinkFieldsBehavior;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.Contact;
import com.n4systems.model.PlaceEvent;
import com.n4systems.model.orgs.BaseOrg;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

import static ch.lambdaj.Lambda.on;

public class PlaceSummaryPage extends PlacePage {

    private @SpringBean PlaceService placeService;
    private @SpringBean S3Service s3Service;


    private final GoogleMap map;
    private WebMarkupContainer futureEventsListContainer;
    private ListView<PlaceEvent> futureEventsListView;
    private Contact contact;

    private WebMarkupContainer imageContainer;
    private WebMarkupContainer imageMsg;
    private WebComponent image;
    private AjaxLink removeLink;

    public PlaceSummaryPage(PageParameters params) {
        super(params);

        add(map = new GoogleMap("map", ProxyModel.of(orgModel, on(BaseOrg.class).getAddressInfo().getGpsLocation())));

        add(futureEventsListContainer = new WebMarkupContainer("eventsListContainer") {
            @Override
            public boolean isVisible() {
                return futureEventsListView.getList().size() > 0;
            }
        });
        futureEventsListContainer.add(futureEventsListView = createFutureEventsListView());
        futureEventsListContainer.setOutputMarkupPlaceholderTag(true);

        add(new Link("viewAll") {
            @Override
            public void onClick() {
                setResponsePage(new PlaceEventsPage(PageParametersBuilder.id(getOrg().getId()).add(PlaceEventsPage.OPEN_PARAM, "true")));
            }
        });

        add(imageContainer = new WebMarkupContainer("imageContainer"));
        imageContainer.setOutputMarkupId(true);

        imageContainer.add(imageMsg = new WebMarkupContainer("imgMsg"));
        imageMsg.setOutputMarkupPlaceholderTag(true);
        imageMsg.setVisible(!logoExists());

        imageContainer.add(image = getImage());
        image.setOutputMarkupId(true);


        Form imageUploadForm = new Form<Void>("imageUploadForm");
        final FileUploadField fileUploadField = new FileUploadField("imageUploadField");
        fileUploadField.setVisible(FieldIDSession.get().getUserSecurityGuard().isAllowedManageEndUsers());

        fileUploadField.add(new AjaxFormSubmitBehavior("onchange") {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                FileUpload uploadedFile = fileUploadField.getFileUpload();

                BaseOrg org = getOrg();

                s3Service.uploadCustomerLogo(org.getId(), uploadedFile.getContentType(), uploadedFile.getBytes());

                imageContainer.replace(getImage());
                image.setOutputMarkupId(true);
                imageMsg.setVisible(s3Service.customerLogoExists(org.getId()));
                removeLink.setVisible(true);

                target.add(image, imageMsg, removeLink, imageContainer);
            }

            @Override
            protected void onError(AjaxRequestTarget target) {
            }
        });

        imageUploadForm.add(fileUploadField);

        imageContainer.add(removeLink = new AjaxLink<Void>("removeLink") {

            @Override
            public void onClick(AjaxRequestTarget target) {
                s3Service.removeCustomerLogo(getOrg().getId());
                imageContainer.replace(getImage());
                image.setOutputMarkupId(true);
                imageMsg.setVisible(!logoExists());
                removeLink.setVisible(false);

                target.add(image, imageMsg, removeLink, imageContainer);
            }
        });
        removeLink.setOutputMarkupPlaceholderTag(true);
        removeLink.setVisible(logoExists());

        imageContainer.add(imageUploadForm);

        contact = new Contact(getOrg().getContact());
        add(new InlineEditableForm("contact") {
            @Override
            protected void onSave(AjaxRequestTarget target) {
                super.onSave(target);
                target.add(map);
            }

            @Override
            protected void onCancel(AjaxRequestTarget target) {
                super.onCancel(target);
            }
        }.withSaveCancelEditLinks()
                .add(new TextField("name", ProxyModel.of(contact, on(Contact.class).getName())))
                .add(new TextField("email", ProxyModel.of(contact, on(Contact.class).getEmail())))
                .add(new AddressPanel("address", ProxyModel.of(orgModel, on(BaseOrg.class).getAddressInfo())).withExternalMap(map.getJsVar()).hideIfChildrenHidden())
                .add(new TextField("phone", ProxyModel.of(orgModel, on(BaseOrg.class).getAddressInfo().getPhone1())))
                .add(new TextField("phone2", ProxyModel.of(orgModel, on(BaseOrg.class).getAddressInfo().getPhone2())))
                .add(new TextField("fax", ProxyModel.of(orgModel, on(BaseOrg.class).getAddressInfo().getFax1()))));

        add(new InlineEditableForm("general").withSaveCancelEditLinks()
                .add(new TextField("name", ProxyModel.of(orgModel, on(BaseOrg.class).getName())).add(new LinkFieldsBehavior(".js-title-label").forTextField()))
                .add(new TextField("id", ProxyModel.of(orgModel, on(BaseOrg.class).getCode())))
                .add(new TextArea<String>("notes", ProxyModel.of(orgModel, on(BaseOrg.class).getNotes())))
        );

    }

    @Override
    protected void refreshContent(AjaxRequestTarget target) {
        futureEventsListContainer.setVisible(futureEventsListView.getList().size() > 0);
        target.add(futureEventsListContainer);
    }

    @Override
    public String getMainCss() {
        return "place-summary";
    }

    private ListView<PlaceEvent> createFutureEventsListView() {
        final FutureEventsModel model = new FutureEventsModel();
        ListView<PlaceEvent> view = new ListView<PlaceEvent>("events", model ) {
            @Override protected void populateItem(ListItem<PlaceEvent> item) {
                PlaceEvent event = item.getModelObject();
                item.add(new Label("due", Model.of(event.getDueDate())));
                // TODO : for debugging only. remove this when we know all data is valid.
                if ( event.getEventType()==null) {
                    item.add(new Label("type", "ERROR : event '" + event.getId() + "' has null type???"));
                } else {
                    item.add(new Label("type", Model.of(event.getEventType().getDisplayName())));
                }
                item.add(new Label("assignee", Model.of("joe smith")));
            }

            @Override public boolean isVisible() {
                return model.getObject().size()>0;
            }
        };
        return view;
    }

    private boolean logoExists() {
        return s3Service.customerLogoExists(getOrg().getId());
    }

    public WebComponent getImage() {
        if(logoExists()) {
            return new ExternalImage("img", s3Service.getCustomerLogoURL(getOrg().getId()));
        } else {
           return new ContextImage("img", "images/add-photo-slate.png");
        }
    }

    class FutureEventsModel extends LoadableDetachableModel<List<PlaceEvent>> {

        @Override
        protected List<PlaceEvent> load() {
            return placeService.getOpenEventsFor(getOrg(), 7);
        }
    }
}
