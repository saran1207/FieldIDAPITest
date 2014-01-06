package com.n4systems.fieldid.wicket.pages.org;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.org.PlaceService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.ExternalImage;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.fieldid.wicket.components.GoogleMap;
import com.n4systems.fieldid.wicket.components.addressinfo.AddressPanel;
import com.n4systems.fieldid.wicket.components.form.InlineEditableForm;
import com.n4systems.fieldid.wicket.components.form.LinkFieldsBehavior;
import com.n4systems.fieldid.wicket.components.renderer.ListableChoiceRenderer;
import com.n4systems.fieldid.wicket.components.timezone.RegionListModel;
import com.n4systems.fieldid.wicket.components.timezone.RegionModel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.model.orgs.CountryFromAddressModel;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.Contact;
import com.n4systems.model.PlaceEvent;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.util.StringUtils;
import com.n4systems.util.timezone.Country;
import com.n4systems.util.timezone.Region;
import org.apache.wicket.MarkupContainer;
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
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Date;
import java.util.List;

import static ch.lambdaj.Lambda.on;

public class PlaceSummaryPage extends PlacePage {

    private @SpringBean PlaceService placeService;
    private @SpringBean S3Service s3Service;
    private @SpringBean PersistenceService persistenceService;


    private final GoogleMap map;
    private WebMarkupContainer futureEventsListContainer;
    private ListView<PlaceEvent> futureEventsListView;
    private Contact contact;

    private WebMarkupContainer imageContainer;
    private WebMarkupContainer imageMsg;
    private WebComponent image;
    private AjaxLink removeImageLink;
    private AjaxLink removeLink;
    private FidDropDownChoice<Region> timeZone;
    private AddressPanel address;
    private String defaultTimeZone;
    private String certificateName;

    private WebMarkupContainer certImageContainer;
    private WebMarkupContainer certImageMsg;
    private WebMarkupContainer schedulesBlankSlate;
    private WebComponent certImage;
    private AjaxLink removeCertImageLink;

    public PlaceSummaryPage(PageParameters params) {
        super(params);

        final BaseOrg org = getOrg();
        boolean canManageCustomers = FieldIDSession.get().getUserSecurityGuard().isAllowedManageEndUsers();

        add(map = new GoogleMap("map", ProxyModel.of(orgModel, on(BaseOrg.class).getAddressInfo().getGpsLocation())));


        add(futureEventsListContainer = new WebMarkupContainer("eventsListContainer"));
        futureEventsListContainer.add(futureEventsListView = createFutureEventsListView());
        futureEventsListContainer.setOutputMarkupPlaceholderTag(true);

        boolean hasSchedules = futureEventsListView.getList().size() > 0;

        futureEventsListContainer.setVisible(hasSchedules);

        add(schedulesBlankSlate = new WebMarkupContainer("schedulesBlankSlate"));
        schedulesBlankSlate.setOutputMarkupPlaceholderTag(true);
        schedulesBlankSlate.setVisible(!hasSchedules);

        PageParameters pageParameters = PageParametersBuilder.id(org.getId());
        pageParameters.add(PlaceEventsPage.OPEN_PARAM, true);
        futureEventsListContainer.add(new BookmarkablePageLink<PlaceEventsPage>("viewAll", PlaceEventsPage.class, pageParameters));

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

        if (getOrg() instanceof InternalOrg) {
            defaultTimeZone = ((InternalOrg)getOrg()).getDefaultTimeZone();
        }

        final IModel<String> timeZoneIdModel = new PropertyModel(this,"defaultTimeZone");
        final IModel<Country> countryModel = new CountryFromAddressModel(ProxyModel.of(orgModel,on(BaseOrg.class).getAddressInfo()));
        final IModel<Region> regionModel = new RegionModel(timeZoneIdModel,countryModel);

        contact = new Contact(org.getContact());
        MarkupContainer form = new InlineEditableForm("contact") {
            @Override protected void onSave(AjaxRequestTarget target) {
                if (getOrg() instanceof InternalOrg) {
                    ((InternalOrg)getOrg()).setDefaultTimeZone(defaultTimeZone);
                }
                persistenceService.save(getOrg());
                info(new FIDLabelModel("label.place_saved", getOrg().getName()).getObject());
                target.add(map,getTopFeedbackPanel());
            }

            @Override protected void error(AjaxRequestTarget target) {
                error(new FIDLabelModel("errors.place_save",getOrg().getName()));
                target.add(getTopFeedbackPanel());
            }
        }.withSaveCancelEditLinks();
        form.add(new TextField("name", ProxyModel.of(contact, on(Contact.class).getName())))
            .add(new TextField("email", ProxyModel.of(contact, on(Contact.class).getEmail())))
            .add(new TextField("phone", ProxyModel.of(orgModel, on(BaseOrg.class).getAddressInfo().getPhone1())))
            .add(new TextField("phone2", ProxyModel.of(orgModel, on(BaseOrg.class).getAddressInfo().getPhone2())))
            .add(new TextField("fax", ProxyModel.of(orgModel, on(BaseOrg.class).getAddressInfo().getFax1())))
            .add(address = new AddressPanel("address", ProxyModel.of(orgModel, on(BaseOrg.class).getAddressInfo())) {
                @Override protected void onCountryChange(AjaxRequestTarget target) {
                    setDefaultTimeZone();
                    target.add(timeZone);
                }
            }.withExternalMap(map.getJsVar()).hideIfChildrenHidden())
            .add(timeZone = new FidDropDownChoice<Region>("timeZone", regionModel, new RegionListModel(countryModel), new ListableChoiceRenderer<Region>()) {
                @Override public boolean isVisible() {
                    return getOrg().isInternal() && address.isVisible();
                }
            });
        add(form);

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

        InlineEditableForm generalForm = new InlineEditableForm("general") {
            @Override protected void onSave(AjaxRequestTarget target) {
                persistenceService.save(getOrg());
                info(new FIDLabelModel("label.place_saved", getOrg().getName()).getObject());
                target.add(getTopFeedbackPanel());
            }

            @Override protected void error(AjaxRequestTarget target) {
                target.add(getTopFeedbackPanel());
            }
        }.withSaveCancelEditLinks();

        generalForm
                .add(new TextField<String>("name", ProxyModel.of(orgModel, on(BaseOrg.class).getName())).add(new LinkFieldsBehavior(".js-title-label").forTextField()))
                .add(new TextField<String>("id", ProxyModel.of(orgModel, on(BaseOrg.class).getCode())))
                .add(new TextArea<String>("notes", ProxyModel.of(orgModel, on(BaseOrg.class).getNotes())));

        if(orgModel.getObject().isInternal()) {
            generalForm.add(new TextField<String>("certificateName", ProxyModel.of(orgModel, on(InternalOrg.class).getCertificateName())));
        } else {
            generalForm.add(new TextField<String>("certificateName", new PropertyModel(this,"certificateName")).setVisible(false));
        }
        add(generalForm);
    }

    private void setDefaultTimeZone() {
        //set defaultTimeZone to
        //   1: existing non-null value if valid (i.e. exists in regionModels list)
        //   2: the best one or first one in region models list.  (scan for similar city names).
        RegionModel regionModel = (RegionModel) timeZone.getModel();
        Region currentRegion = regionModel.getObject();

        List<? extends Region> regions = timeZone.getChoices();
        if (StringUtils.isNotEmpty(defaultTimeZone)) {
            for (Region region:regions) {
                if (region.equals(currentRegion)) {
                    return;
                }
            }
        }
        if (regions.size()>0) {
            regionModel.setObject(regions.get(0));
        } else {
            defaultTimeZone = null;
        }
    }

    @Override
    protected void refreshContent(AjaxRequestTarget target) {
        boolean hasSchedules = futureEventsListView.getList().size() > 0;
        futureEventsListContainer.setVisible(hasSchedules);
        schedulesBlankSlate.setVisible(!hasSchedules);
        target.add(futureEventsListContainer, schedulesBlankSlate);
    }

    @Override
    public String getMainCss() {
        return "place-summary";
    }

    private ListView<PlaceEvent> createFutureEventsListView() {
        final FutureEventsModel model = new FutureEventsModel();
        ListView<PlaceEvent> view = new ListView<PlaceEvent>("events", model ) {
            @Override protected void populateItem(ListItem<PlaceEvent> item) {
                IModel<PlaceEvent> event = item.getModel();
                item.add(new Label("due", new PropertyModel<Date>(event, "dueDate")));
                item.add(new Label("type", new PropertyModel<Date>(event, "type.displayName")));
                item.add(new Label("assignee", new PropertyModel<Date>(event, "assignedUserOrGroup.displayName")));
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
