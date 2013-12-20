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
import com.n4systems.model.orgs.InternalOrg;
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
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
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
    private AjaxLink removeImageLink;

    private WebMarkupContainer certImageContainer;
    private WebMarkupContainer certImageMsg;
    private WebComponent certImage;
    private AjaxLink removeCertImageLink;

    public PlaceSummaryPage(PageParameters params) {
        super(params);

        final BaseOrg org = getOrg();
        boolean canManageCustomers = FieldIDSession.get().getUserSecurityGuard().isAllowedManageEndUsers();

        add(map = new GoogleMap("map", ProxyModel.of(orgModel, on(BaseOrg.class).getAddressInfo().getGpsLocation())));

        add(futureEventsListContainer = new WebMarkupContainer("eventsListContainer") {
            @Override
            public boolean isVisible() {
                return futureEventsListView.getList().size() > 0;
            }
        });
        futureEventsListContainer.add(futureEventsListView = createFutureEventsListView());
        futureEventsListContainer.setOutputMarkupPlaceholderTag(true);

        PageParameters pageParameters = PageParametersBuilder.id(org.getId());
        pageParameters.add(PlaceEventsPage.OPEN_PARAM, true);
        add(new BookmarkablePageLink<PlaceEventsPage>("viewAll", PlaceEventsPage.class, pageParameters));

        add(imageContainer = new WebMarkupContainer("imageContainer"));
        imageContainer.setOutputMarkupId(true);

        imageContainer.add(imageMsg = new WebMarkupContainer("imgMsg"));
        imageMsg.setOutputMarkupPlaceholderTag(true);
        imageMsg.setVisible(!logoExists() && canManageCustomers);

        imageContainer.add(image = getImage());
        image.setOutputMarkupId(true);

        Form imageUploadForm = new Form<Void>("imageUploadForm");
        final FileUploadField fileUploadField = new FileUploadField("imageUploadField");
        imageUploadForm.setVisible(canManageCustomers);

        fileUploadField.add(new AjaxFormSubmitBehavior("onchange") {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                FileUpload uploadedFile = fileUploadField.getFileUpload();

                s3Service.uploadCustomerLogo(org.getId(), uploadedFile.getContentType(), uploadedFile.getBytes());

                imageContainer.replace(getImage());
                image.setOutputMarkupId(true);
                imageMsg.setVisible(!logoExists());
                removeImageLink.setVisible(true);

                target.add(image, imageMsg, removeImageLink, imageContainer);
            }

            @Override
            protected void onError(AjaxRequestTarget target) {
            }
        });

        imageUploadForm.add(fileUploadField);

        imageContainer.add(removeImageLink = new AjaxLink<Void>("removeImageLink") {

            @Override
            public void onClick(AjaxRequestTarget target) {
                s3Service.removeCustomerLogo(org.getId());
                imageContainer.replace(getImage());
                image.setOutputMarkupId(true);
                imageMsg.setVisible(!logoExists());
                removeImageLink.setVisible(false);

                target.add(image, imageMsg, removeImageLink, imageContainer);
            }
        });
        removeImageLink.setOutputMarkupPlaceholderTag(true);
        removeImageLink.setVisible(logoExists() && canManageCustomers);

        imageContainer.add(imageUploadForm);

        contact = new Contact(org.getContact());
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
                .add(new TextField<String>("name", ProxyModel.of(contact, on(Contact.class).getName())))
                .add(new TextField<String>("email", ProxyModel.of(contact, on(Contact.class).getEmail())))
                .add(new AddressPanel("address", ProxyModel.of(orgModel, on(BaseOrg.class).getAddressInfo())).withExternalMap(map.getJsVar()).hideIfChildrenHidden())
                .add(new TextField<String>("phone", ProxyModel.of(orgModel, on(BaseOrg.class).getAddressInfo().getPhone1())))
                .add(new TextField<String>("phone2", ProxyModel.of(orgModel, on(BaseOrg.class).getAddressInfo().getPhone2())))
                .add(new TextField<String>("fax", ProxyModel.of(orgModel, on(BaseOrg.class).getAddressInfo().getFax1()))));

        if(orgModel.getObject().isInternal()) {
            add(new InlineEditableForm("general").withSaveCancelEditLinks()
                    .add(new TextField<String>("name", ProxyModel.of(orgModel, on(BaseOrg.class).getName())).add(new LinkFieldsBehavior(".js-title-label").forTextField()))
                    .add(new TextField<String>("id", ProxyModel.of(orgModel, on(BaseOrg.class).getCode())))
                    .add(new TextArea<String>("notes", ProxyModel.of(orgModel, on(BaseOrg.class).getNotes())))
                    .add(new TextField<String>("certificateName", ProxyModel.of(orgModel, on(InternalOrg.class).getCertificateName())))
            );
        } else {
            add(new InlineEditableForm("general").withSaveCancelEditLinks()
                    .add(new TextField<String>("name", ProxyModel.of(orgModel, on(BaseOrg.class).getName())).add(new LinkFieldsBehavior(".js-title-label").forTextField()))
                    .add(new TextField<String>("id", ProxyModel.of(orgModel, on(BaseOrg.class).getCode())))
                    .add(new TextArea<String>("notes", ProxyModel.of(orgModel, on(BaseOrg.class).getNotes())))
                    .add(new TextField<String>("certificateName").setVisible(false))
            );
        }


        add(certImageContainer = new WebMarkupContainer("certImageContainer"));
        certImageContainer.setOutputMarkupId(true);
        certImageContainer.setVisible(org.isInternal());

        certImageContainer.add(certImageMsg = new WebMarkupContainer("certImgMsg"));
        certImageMsg.setOutputMarkupPlaceholderTag(true);
        certImageMsg.setVisible(!certificateImageExists() && canManageCustomers);

        certImageContainer.add(certImage = getCertificateImage());
        certImage.setOutputMarkupId(true);

        Form certImageUploadForm = new Form<Void>("certImageUploadForm");
        final FileUploadField certFileUploadField = new FileUploadField("certImageUploadField");
        certImageUploadForm.setVisible(canManageCustomers);

        certFileUploadField.add(new AjaxFormSubmitBehavior("onchange") {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                FileUpload uploadedFile = certFileUploadField.getFileUpload();

                if(org.isPrimary())
                    s3Service.uploadPrimaryOrgCertificateLogo(uploadedFile.getContentType(), uploadedFile.getBytes());
                else
                    s3Service.uploadSecondaryOrgCertificateLogo(org.getId(), uploadedFile.getContentType(), uploadedFile.getBytes());

                certImageContainer.replace(getCertificateImage());
                certImage.setOutputMarkupId(true);
                certImageMsg.setVisible(!certificateImageExists());
                removeCertImageLink.setVisible(true);

                target.add(certImage, certImageMsg,  removeCertImageLink, certImageContainer);
            }

            @Override
            protected void onError(AjaxRequestTarget target) {
            }
        });

        certImageUploadForm.add(certFileUploadField);

        certImageContainer.add(removeCertImageLink = new AjaxLink<Void>("removeCertImageLink") {

            @Override
            public void onClick(AjaxRequestTarget target) {
                if(org.isPrimary())
                    s3Service.removePrimaryOrgCertificateLogo();
                else
                    s3Service.removeSecondaryOrgCertificateLogo(org.getId());

                certImageContainer.replace(getCertificateImage());
                certImage.setOutputMarkupId(true);
                certImageMsg.setVisible(!certificateImageExists());
                removeCertImageLink.setVisible(false);

                target.add(certImage, certImageMsg, removeCertImageLink, certImageContainer);
            }
        });
        removeCertImageLink.setOutputMarkupPlaceholderTag(true);
        removeCertImageLink.setVisible(certificateImageExists() && canManageCustomers);

        certImageContainer.add(certImageUploadForm);

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

    private boolean certificateImageExists() {
        if(getOrg().isPrimary())
            return s3Service.primaryOrgCertificateLogoExists();
        else if (getOrg().isSecondary())
            return s3Service.secondaryOrgCertificateLogoExists(getOrg().getId());
        else
            return false;
    }

    public WebComponent getCertificateImage() {
        if (certificateImageExists()) {
            if(getOrg().isPrimary())
                return new ExternalImage("certImg", s3Service.getPrimaryOrgCertificateLogoURL());
            else
                return new ExternalImage("certImg", s3Service.getSecondaryOrgCertificateLogoURL(getOrg().getId()));
        } else {
           return new ContextImage("certImg", "images/add-photo-slate.png");
        }
    }

    class FutureEventsModel extends LoadableDetachableModel<List<PlaceEvent>> {

        @Override
        protected List<PlaceEvent> load() {
            return placeService.getOpenEventsFor(getOrg(), 7);
        }
    }
}
